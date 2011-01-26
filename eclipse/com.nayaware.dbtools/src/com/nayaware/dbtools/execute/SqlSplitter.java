
package com.nayaware.dbtools.execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlSplitter {
	private static final int STATE_MEANINGFUL_TEXT = 0;
	private static final int STATE_MAYBE_LINE_COMMENT = 1;
	private static final int STATE_LINE_COMMENT = 2;
	private static final int STATE_MAYBE_BLOCK_COMMENT = 3;
	private static final int STATE_BLOCK_COMMENT = 4;
	private static final int STATE_MAYBE_END_BLOCK_COMMENT = 5;
	private static final int STATE_STRING = 6;

	private String sql;
	private int sqlLength;

	private StringBuffer statement = new StringBuffer();
	private List<StatementInfo> statements = new ArrayList<StatementInfo>();

	private int pos = 0;
	private int line = -1;
	private int column;
	private boolean wasEOL = true;

	private int rawStartOffset;
	private int startOffset;
	private int startLine;
	private int startColumn;
	private int endOffset;
	private int rawEndOffset;

	private int state = STATE_MEANINGFUL_TEXT;

	private String delimiter = ";"; 
	private static final String DELIMITER_TOKEN = "delimiter"; 

	/**
	 * @param sql
	 *            the SQL string to parse. If it contains multiple lines they
	 *            have to be delimited by ';' characters.
	 */
	public SqlSplitter(String sql) {
		assert sql != null;
		this.sql = sql;
		sqlLength = sql.length();
		parse();
	}

	private void parse() {
		checkDelimiterStatement();
		while (pos < sqlLength) {
			char ch = sql.charAt(pos);

			if (ch == '\r') { //$NON-NLS-1$
				// the string should not contain these
				System.out
						.println(Messages.getString("SqlSplitter.2")); //$NON-NLS-1$
				continue;
			}

			nextColumn();

			switch (state) {
			case STATE_MEANINGFUL_TEXT:
				if (ch == '-') {
					state = STATE_MAYBE_LINE_COMMENT;
				} else if (ch == '/') {
					state = STATE_MAYBE_BLOCK_COMMENT;
				} else if (ch == '#') {
					state = STATE_LINE_COMMENT;
				} else if (ch == '\'') {
					state = STATE_STRING;
				}
				break;

			case STATE_MAYBE_LINE_COMMENT:
				if (ch == '-') {
					state = STATE_LINE_COMMENT;
				} else {
					state = STATE_MEANINGFUL_TEXT;
					statement.append('-'); // previous char
					endOffset = pos;
				}
				break;

			case STATE_LINE_COMMENT:
				if (ch == '\n') {
					state = STATE_MEANINGFUL_TEXT;
					continue;
				}
				break;

			case STATE_MAYBE_BLOCK_COMMENT:
				if (ch == '*') {
					state = STATE_BLOCK_COMMENT;
				} else {
					statement.append('/'); // previous char
					endOffset = pos;
					if (ch != '/') {
						state = STATE_MEANINGFUL_TEXT;
					}
				}
				break;

			case STATE_BLOCK_COMMENT:
				if (ch == '*') {
					state = STATE_MAYBE_END_BLOCK_COMMENT;
				}
				break;

			case STATE_MAYBE_END_BLOCK_COMMENT:
				if (ch == '/') {
					state = STATE_MEANINGFUL_TEXT;
					// avoid writing the final / to the result
					pos++;
					continue;
				} else if (ch != '*') {
					state = STATE_BLOCK_COMMENT;
				}
				break;

			case STATE_STRING:
				if (ch == '\'') {
					state = STATE_MEANINGFUL_TEXT;
				}
				break;

			default:
				assert false;
			}

			if (state == STATE_MEANINGFUL_TEXT && isDelimiter()) {
				rawEndOffset = pos;
				addStatement();
				statement.setLength(0);
				rawStartOffset = pos + delimiter.length(); // skip the delimiter
				pos += delimiter.length();
			} else if (state == STATE_MEANINGFUL_TEXT || state == STATE_STRING) {
				// don't append leading whitespace
				if (statement.length() > 0 || !Character.isWhitespace(ch)) {
					// remember the position of the first appended char
					if (statement.length() == 0) {
						// See if the next statement changes the delimiter
						// Note how we skip over a 'delimiter' statement - it's
						// not
						// something we send to the server.
						if (checkDelimiterStatement()) {
							continue;
						}
						startOffset = pos;
						endOffset = pos;
						startLine = line;
						startColumn = column;
					}
					statement.append(ch);
					// the end offset is the character after the last
					// non-whitespace character
					if (state == STATE_STRING || !Character.isWhitespace(ch)) {
						endOffset = pos + 1;
					}
				}
				pos++;
			} else {
				pos++;
			}
		}

		rawEndOffset = pos;
		addStatement();
	}

	/**
	 * See if the user wants to use a different delimiter for splitting up
	 * statements. This is useful if, for example, their SQL contains stored
	 * procedures or triggers or other blocks that contain multiple statements
	 * but should be executed as a single unit.
	 * 
	 * If we see the delimiter token, we read in what the new delimiter should
	 * be, and then return the new character position past the delimiter
	 * statement, as this shouldn't be passed on to the database.
	 */
	private boolean checkDelimiterStatement() {
		skipWhitespace();

		if (pos == sqlLength) {
			return false;
		}

		if (!isToken(DELIMITER_TOKEN)) {
			return false;
		}

		// Skip past the delimiter token
		int tokenLength = DELIMITER_TOKEN.length();
		pos += tokenLength;

		skipWhitespace();

		int endPos = pos;
		while (endPos < sqlLength
				&& !Character.isWhitespace(sql.charAt(endPos))) {
			endPos++;
		}

		if (pos == endPos) {
			return false;
		}

		delimiter = sql.substring(pos, endPos);

		pos = endPos;
		statement.setLength(0);
		rawStartOffset = pos;

		return true;
	}

	private void skipWhitespace() {
		while (pos < sqlLength && Character.isWhitespace(sql.charAt(pos))) {
			nextColumn();
			pos++;
		}
	}

	private boolean isDelimiter() {
		int length = delimiter.length();

		if (pos + length > sqlLength) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			if (delimiter.charAt(i) != sql.charAt(pos + i)) {
				return false;
			}
			i++;
		}

		return true;
	}

	private void nextColumn() {
		if (wasEOL) {
			line++;
			column = 0;
			wasEOL = false;
		} else {
			column++;
		}

		if (sql.charAt(pos) == '\n') {
			wasEOL = true;
		}
	}

	/**
	 * See if the SQL text starting at the given position is a given token
	 * 
	 * @param sql
	 *            - the full SQL text
	 * @param ch
	 *            - the character at the current position
	 * @param pos
	 *            - the current position index for the SQL text
	 * @param token
	 *            - the token we are looking for
	 * 
	 * @return true if the token is found at the current position
	 */
	private boolean isToken(String token) {
		char ch = sql.charAt(pos);

		// Simple check to see if there's potential. In most cases this
		// will return false and we don't have to waste our time doing
		// any other processing. Move along, move along...
		if (Character.toUpperCase(ch) != Character.toUpperCase(token.charAt(0))) {
			return false;
		}

		// Don't want to recognize larger strings that contain the token
		if (pos > 0 && !Character.isWhitespace(sql.charAt(pos - 1))) {
			return false;
		}

		if (sql.length() > pos + token.length()
				&& Character.isLetterOrDigit(sql.charAt(pos + token.length()))) {
			return false;
		}

		// Create a substring that contains just the potential token
		// This way we don't have to uppercase the entire SQL string.
		String substr;
		try {
			substr = sql.substring(pos, pos + token.length()); //$NON-NLS-1$
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

		if (substr.toUpperCase().equals(token.toUpperCase())) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	private void addStatement() {
		// PENDING since startOffset is the first non-whitespace char and
		// endOffset is the offset after the last non-whitespace char,
		// the trim() call could be replaced with
		// statement.substring(startOffset, endOffset)
		String sql = statement.toString().trim();
		if (sql.length() <= 0) {
			return;
		}

		StatementInfo info = new StatementInfo(sql, rawStartOffset,
				startOffset, startLine, startColumn, endOffset, rawEndOffset);
		statements.add(info);
	}

	public List<StatementInfo> getStatements() {
		return Collections.unmodifiableList(statements);
	}
}

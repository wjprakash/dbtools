
package com.nayaware.dbtools.execute;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class StatementInfo {
	private final String sql;
	private final int rawStartOffset;
	private final int startOffset;
	private final int startLine;
	private final int startColumn;
	private final int rawEndOffset;
	private final int endOffset;

	public StatementInfo(String sql, int rawStartOffset, int startOffset,
			int startLine, int startColumn, int endOffset, int rawEndOffset) {
		this.sql = sql;
		this.rawStartOffset = rawStartOffset;
		this.startOffset = startOffset;
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endOffset = endOffset;
		this.rawEndOffset = rawEndOffset;
	}

	/**
	 * Returns the SQL text statement with comments and leading and trailing
	 * whitespace removed.
	 */
	public String getSQL() {
		return sql;
	}

	/**
	 * Returns the start offset of the raw SQL text (including comments and
	 * leading whitespace).
	 */
	public int getRawStartOffset() {
		return rawStartOffset;
	}

	/**
	 * Returns the start offset of the text returned by {@link #getSQL}.
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Returns the zero-based number of the line corresponding to
	 * {@link #getStartOffset}.
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Returns the zero-based number of the column corresponding to
	 * {@link #getStartOffset}.
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Returns the end offset of the text returned by {@link #getSQL}.
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * Returns the end offset of the raw SQL text (including comments and
	 * trailing whitespace).
	 */
	public int getRawEndOffset() {
		return rawEndOffset;
	}
}

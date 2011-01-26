
package com.nayaware.dbtools.editors.sql;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * Simple scanner to find SQL Keywords
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlKeywordScanner extends RuleBasedScanner {

	private SqlEditorColorManager colorManager;

	public SqlKeywordScanner(SqlEditorColorManager colorManager) {
		this.colorManager = colorManager;
		initialize();
	}

	public void initialize() {

		setDefaultReturnToken(new Token(new TextAttribute(colorManager
				.getColor(SqlEditorColorManager.COLOR_DEFAULT))));

		IRule[] rules = new IRule[2];
		IToken other = new Token(new TextAttribute(colorManager
				.getColor(SqlEditorColorManager.COLOR_DEFAULT)));
		IToken keyword = new Token(new TextAttribute(colorManager
				.getColor(SqlEditorColorManager.COLOR_KEYWORD), null, SWT.BOLD));
		IToken dataType = new Token(new TextAttribute(colorManager
				.getColor(SqlEditorColorManager.COLOR_DATA_TYPE), null,
				SWT.BOLD));
		IToken function = new Token(
				new TextAttribute(colorManager
						.getColor(SqlEditorColorManager.COLOR_FUNCTION), null,
						SWT.BOLD));

		WordRule wordRule = new WordRule(new WordDetector(), other);

		// Add SQL Keywords
		String[] keywords = SqlKeywordUtils.getKeywordNamess();
		for (int i = 0; i < keywords.length; i++) {
			String key = keywords[i];
			wordRule.addWord(key, keyword);
			wordRule.addWord(key.toLowerCase(), keyword);
		}

		// Add SQL Data Types
		String[] dataTYpes = SqlKeywordUtils.getDataTypeNames();
		for (int i = 0; i < dataTYpes.length; i++) {
			String name = dataTYpes[i];
			wordRule.addWord(name, dataType);
			wordRule.addWord(name.toLowerCase(), dataType);
		}

		// Add SQL functions
		String[] functions = SqlKeywordUtils.getFunctionNames();
		for (int i = 0; i < functions.length; i++) {
			String name = functions[i];
			wordRule.addWord(name, function);
			wordRule.addWord(name.toLowerCase(), function);
		}

		rules[0] = wordRule;
		rules[1] = new WhitespaceRule(new WhitespaceDetector());
		setRules(rules);
	}

	private static class WordDetector implements IWordDetector {
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c);
		}
	}

	private static class WhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}
}

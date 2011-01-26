
package com.nayaware.dbtools.editors.sql;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class SqlPartitionScanner extends RuleBasedPartitionScanner {

	public static final String SQL_COMMENT = "sql_comment"; //$NON-NLS-1$
	public static final String SQL_STRING = "sql_string"; //$NON-NLS-1$

	public SqlPartitionScanner() {
		IPredicateRule[] rules = new IPredicateRule[5];

		IToken comment = new Token(SQL_COMMENT);
		rules[0] = new MultiLineRule("/*", "*/", comment); //$NON-NLS-1$ //$NON-NLS-2$
		rules[1] = new EndOfLineRule("--", comment); // //$NON-NLS-1$
		rules[2] = new EndOfLineRule("#", comment); // //$NON-NLS-1$

		IToken string = new Token(SQL_STRING);
		rules[3] = new MultiLineRule("\"", "\"", string); //$NON-NLS-1$ //$NON-NLS-2$
		rules[4] = new MultiLineRule("\'", "\'", string); //$NON-NLS-1$ //$NON-NLS-2$

		setPredicateRules(rules);
	}
}

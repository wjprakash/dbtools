
package com.nayaware.dbtools.editors.sql.templates;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;

/**
 * Context type for the SQL Editor content assistant
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlTemplateContextType extends TemplateContextType {

	public static final String CONTEXT_TYPE_SQL = "com.nayaware.dbtools.editor.sql.template"; //$NON-NLS-1$

	public SqlTemplateContextType() {
		super();
		addGlobalResolvers();
	}

	/**
	 * Adds the global resolvers.
	 */
	private void addGlobalResolvers() {
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
		addResolver(new GlobalTemplateVariables.Dollar());
		addResolver(new GlobalTemplateVariables.Date());
		addResolver(new GlobalTemplateVariables.Year());
		addResolver(new GlobalTemplateVariables.Time());
		addResolver(new GlobalTemplateVariables.User());
	}
}

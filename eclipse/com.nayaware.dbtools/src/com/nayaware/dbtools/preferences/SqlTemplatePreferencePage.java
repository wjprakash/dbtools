
package com.nayaware.dbtools.preferences;

import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import com.nayaware.dbtools.DbExplorerPlugin;
import com.nayaware.dbtools.editors.sql.templates.SqlTemplateManager;

/**
 * Preference Page for the SQL Templates
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlTemplatePreferencePage extends TemplatePreferencePage {

	public SqlTemplatePreferencePage() {
		setPreferenceStore(DbExplorerPlugin.getDefault().getPreferenceStore());
		setTemplateStore(SqlTemplateManager.getInstance().getTemplateStore());
		setContextTypeRegistry(SqlTemplateManager.getInstance()
				.getContextTypeRegistry());
		setDescription(Messages.getString("SqlTemplatePreferencePage.0")); //$NON-NLS-1$
		setTitle(Messages.getString("SqlTemplatePreferencePage.1")); //$NON-NLS-1$
	}
}

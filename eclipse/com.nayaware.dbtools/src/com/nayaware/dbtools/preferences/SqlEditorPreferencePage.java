
package com.nayaware.dbtools.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.nayaware.dbtools.DbExplorerPlugin;

/**
 * UI to edit the SQL Editor preferences
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public SqlEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(DbExplorerPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.getString("SqlEditorPreferencePage.0")); //$NON-NLS-1$
	}

	@Override
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addColorOptions(parent);
	}

	private void addColorOptions(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 4;
		layout.marginWidth = 4;
		group.setLayout(layout);
		group.setText(Messages.getString("SqlEditorPreferencePage.1")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		Composite grp = new Composite(group, SWT.NONE);
		grp.setLayout(new GridLayout(2, false));

		addField(new ColorFieldEditor(PreferenceConstants.COLOR_DEFAULT,
				Messages.getString("SqlEditorPreferencePage.2"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_COMMENT,
				Messages.getString("SqlEditorPreferencePage.3"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_KEYWORD,
				Messages.getString("SqlEditorPreferencePage.4"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_STRING,
				Messages.getString("SqlEditorPreferencePage.5"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_DATA_TYPE,
				Messages.getString("SqlEditorPreferencePage.6"), grp)); //$NON-NLS-1$
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_FUNCTION,
				Messages.getString("SqlEditorPreferencePage.7"), grp)); //$NON-NLS-1$

	}

	public void init(IWorkbench workbench) {
	}

}
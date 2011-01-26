
package com.nayaware.dbtools.viewers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.model.TableData;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Viewer to view the SQL execution results. Provides three tabs 1. Error Tab 2.
 * DDL result (Create, Insert, Update etc ) result Tab 2. Table data tab
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlResultViewer {

	private CTabFolder tabFolder;
	private CTabItem errorTab;
	private CTabItem successTab;
	private CTabItem dataTab;
	Composite parent;

	public SqlResultViewer(Composite parent) {
		this.parent = parent;
	}

	public void showResults(ExecutionStatus execStatus) {
		if (execStatus.hasResults()) {
			addResultDataTab(execStatus);
		} else if (execStatus.hasExceptions()) {
			addErrorTab(execStatus);
		} else {
			addSuccessTab(execStatus);
		}
	}

	private void addSuccessTab(ExecutionStatus execStatus) {
		createResultTabFolder();
		if (successTab == null) {
			successTab = new CTabItem(tabFolder, SWT.CLOSE);
			successTab.setText(Messages.getString("SqlResultViewer.0")); //$NON-NLS-1$
			Text successText = new Text(tabFolder, SWT.MULTI
					| SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL
					| SWT.V_SCROLL);
			successText.setEditable(false);
			successTab.setControl(successText);
			successTab.setImage(ImageUtils.getIcon(ImageUtils.SUCCESS));
		}

		StringBuffer successBuffer = new StringBuffer();
		successBuffer.append(Messages.getString("SqlResultViewer.1")); //$NON-NLS-1$
		successBuffer.append("\n\n"); //$NON-NLS-1$

		if (execStatus.getUpdateCount() > 0) {
			successBuffer.append(execStatus.getUpdateCount());
			successBuffer.append(Messages.getString("SqlResultViewer.2")); //$NON-NLS-1$
			successBuffer.append("\n\n"); //$NON-NLS-1$
		}

		if (execStatus.hasWarnings()) {
			successBuffer.append(getWarningString(execStatus.getWarnings())); 
		}
		((Text) successTab.getControl()).setText(successBuffer.toString());

		parent.layout(true);
		successTab.getParent().setFocus();
		tabFolder.setSelection(successTab);
	}

	private void addErrorTab(ExecutionStatus execStatus) {
		createResultTabFolder();
		if (errorTab == null) {
			errorTab = new CTabItem(tabFolder, SWT.CLOSE);
			errorTab.setText(Messages.getString("SqlResultViewer.4")); //$NON-NLS-1$

			Text errorText = new Text(tabFolder, SWT.MULTI | SWT.FULL_SELECTION
					| SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			errorText.setEditable(false);
			errorTab.setControl(errorText);
			errorTab.setImage(ImageUtils.getIcon(ImageUtils.ERROR));
		}

		// Show the list of SQL errors
		List<Throwable> errors = execStatus.getExceptions();
		StringBuffer errorBuffer = new StringBuffer();
		errorBuffer.append(Messages.getString("SqlResultViewer.5")); //$NON-NLS-1$
		errorBuffer.append("\n\n"); //$NON-NLS-1$
		// errorBuffer.append(execStatus.getUpdateCount());
		//		errorBuffer.append(Messages.getString("SqlResultViewer.6")); //$NON-NLS-1$
		// errorBuffer.append("\n\n");
		for (Throwable error : errors) {
			errorBuffer.append(Messages.getString("SqlResultViewer.7")); //$NON-NLS-1$
			errorBuffer.append(error.getLocalizedMessage());
			errorBuffer.append("\n \n"); //$NON-NLS-1$
		}

		errorBuffer.append("\n \n"); //$NON-NLS-1$

		// Also show the list of SQL Warnings
		if (execStatus.hasWarnings()) {
			errorBuffer.append(getWarningString(execStatus.getWarnings()));
		}

		((Text) errorTab.getControl()).setText(errorBuffer.toString());

		parent.layout(true);
		errorTab.getParent().setFocus();
		tabFolder.setSelection(errorTab);
	}

	private String getWarningString(List<SQLWarning> sqlWarnings) {
		StringBuffer warningBuffer = new StringBuffer();
		for (SQLWarning warningChain : sqlWarnings) {
			for (SQLWarning warning = warningChain; warning != null; warning = warning
					.getNextWarning()) {
				warningBuffer.append(Messages.getString("SqlResultViewer.10")); //$NON-NLS-1$
				warningBuffer.append(warning.getLocalizedMessage());
				warningBuffer.append("\n \n"); //$NON-NLS-1$
			}
		}
		return warningBuffer.toString();
	}

	private void addResultDataTab(ExecutionStatus execStatus) {
		createResultTabFolder();

		if (dataTab == null) {
			dataTab = new CTabItem(tabFolder, SWT.CLOSE);
			dataTab.setText(Messages.getString("SqlResultViewer.12")); //$NON-NLS-1$
			dataTab.setImage(ImageUtils.getIcon(ImageUtils.TABLE));
		}

		ResultSet rs = execStatus.getResults().get(0);
		try {
			TableData tableData = new TableData(Messages.getString("SqlResultViewer.9"), rs); //$NON-NLS-1$
			DataTableViewer dataTableViewer = new DataTableViewer(tabFolder,
					tableData.getColumnNames());
			dataTableViewer.setInput(tableData.getTableData());
			dataTab.setControl(dataTableViewer.getTable());
		} catch (SQLException exc) {
			ErrorManager.showException(exc);
		}

		parent.layout(true);
		dataTab.getParent().setFocus();
		tabFolder.setSelection(dataTab);
	}

	private synchronized void createResultTabFolder() {
		if (tabFolder == null) {
			tabFolder = new CTabFolder(parent, SWT.BORDER);
			tabFolder.setSimple(false);
			tabFolder.setUnselectedImageVisible(false);
			tabFolder.setUnselectedCloseVisible(false);
			tabFolder.setMinimizeVisible(true);
			tabFolder.setMaximizeVisible(true);
			tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
				@Override
				public void close(CTabFolderEvent event) {
					if (tabFolder.getItemCount() > 1) {
						return;
					} else {
						tabFolder.setVisible(false);
						parent.layout(true);
					}
				}
			});
		} else {
			if (!tabFolder.isVisible()) {
				tabFolder.setVisible(true);
			}
		}
	}
}


package com.nayaware.dbtools.ui;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.viewers.SqlResultViewer;
import com.nayaware.dbtools.viewers.SqlSourceViewer;

/**
 * A simple SQL editor that popsup for commands like - Create Connection, Drop
 * database, Drop Table etc
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditorDialog extends TitleAreaDialog {

	private SqlSourceViewer sqlScriptViewer;
	private String sqlScript;
	private String title;
	private SqlResultViewer sqlResultViewer;
	private SqlExecutor sqlExecutor;

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public SqlEditorDialog(String title, Shell parentShell,
			IDatabaseInfo databaseInfo, String sqlScript) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.SHELL_TRIM);
		this.sqlScript = sqlScript;
		this.title = title;
		sqlExecutor = new SqlExecutor(databaseInfo, sqlScript);
	}

	public SqlEditorDialog(String title, Shell parentShell,
			IDatabaseInfo databaseInfo, SqlExecutor sqlExecutor) {
		super(parentShell);
		this.sqlScript = sqlExecutor.getScript();
		this.title = title;
		this.sqlExecutor = sqlExecutor;
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		setMessage(Messages.getString("SqlEditorDialog.0")); //$NON-NLS-1$
		setTitle(Messages.getString("SqlEditorDialog.1")); //$NON-NLS-1$

		SashForm sourceSashForm = new SashForm(container, SWT.BORDER
				| SWT.VERTICAL);
		sourceSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		sourceSashForm.setLayout(new FillLayout());
		// Create the SQL Result Viewer
		sqlScriptViewer = new SqlSourceViewer(sourceSashForm);
		sqlScriptViewer.getDocument().set(sqlScript);

		// Create the SQL Result Viewer
		sqlResultViewer = new SqlResultViewer(sourceSashForm);

		if ((sqlExecutor.getExecutionStatus() != null)
				&& (sqlExecutor.getExecutionStatus().hasExceptions())) {
			sqlResultViewer.showResults(sqlExecutor.getExecutionStatus());
		}

		return container;
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("SqlEditorDialog.2"), true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}
	
	@Override
	protected boolean isResizable() {
	   return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.CANCEL_ID) {
			close();
			return;
		}
		if (buttonId == IDialogConstants.OK_ID) {
			sqlExecutor.setScript(sqlScriptViewer.getDocument().get());
			Job job = sqlExecutor.asyncExecute();
			try {
				job.join();
				if (job.getResult() == Status.OK_STATUS) {
					close();
				} else {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							sqlResultViewer.showResults(sqlExecutor
									.getExecutionStatus());
						}
					});
				}
			} catch (InterruptedException exc) {
				ErrorManager.showException(exc);
			}
		}
	}
}

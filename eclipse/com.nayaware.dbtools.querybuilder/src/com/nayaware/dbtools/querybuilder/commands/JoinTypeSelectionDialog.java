
package com.nayaware.dbtools.querybuilder.commands;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.nayaware.dbtools.querybuilder.model.Join;

/**
 * Dialog that allows the user to select the type of Join
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class JoinTypeSelectionDialog extends TitleAreaDialog {

	private int joinType = Join.INNER;

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public JoinTypeSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setLayout(new TableWrapLayout());

		Button innerJoinButton;
		innerJoinButton = new Button(group, SWT.RADIO);
		innerJoinButton.setSelection(true);
		final TableWrapData twd_innerJoinButton = new TableWrapData(
				TableWrapData.LEFT, TableWrapData.TOP);
		twd_innerJoinButton.heightHint = 20;
		innerJoinButton.setLayoutData(twd_innerJoinButton);
		innerJoinButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				joinType = Join.INNER;
			}
		});
		innerJoinButton
				.setText(Messages.getString("JoinTypeSelectionDialog.0")); //$NON-NLS-1$

		Button outerJoinButton;
		outerJoinButton = new Button(group, SWT.RADIO);
		final TableWrapData twd_outerJoinButton = new TableWrapData(
				TableWrapData.LEFT, TableWrapData.TOP);
		twd_outerJoinButton.heightHint = 20;
		outerJoinButton.setLayoutData(twd_outerJoinButton);
		outerJoinButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				joinType = Join.LEFT;
			}
		});
		outerJoinButton
				.setText(Messages.getString("JoinTypeSelectionDialog.1")); //$NON-NLS-1$

		Button rightJoinButton;
		rightJoinButton = new Button(group, SWT.RADIO);
		final TableWrapData twd_rightJoinButton = new TableWrapData(
				TableWrapData.LEFT, TableWrapData.TOP);
		twd_rightJoinButton.heightHint = 20;
		rightJoinButton.setLayoutData(twd_rightJoinButton);
		rightJoinButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				joinType = Join.RIGHT;
			}
		});
		rightJoinButton
				.setText(Messages.getString("JoinTypeSelectionDialog.2")); //$NON-NLS-1$

		setTitle(Messages.getString("JoinTypeSelectionDialog.3")); //$NON-NLS-1$
		setMessage(Messages.getString("JoinTypeSelectionDialog.4")); //$NON-NLS-1$
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 250);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("JoinTypeSelectionDialog.5")); //$NON-NLS-1$
	}

	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}
}


package com.nayaware.dbtools.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class InputDialog extends Dialog {

	private Text inputText;
	private String message;
	private String title;
	private String input = ""; //$NON-NLS-1$

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	public InputDialog(Shell parentShell, String title, String message) {
		super(parentShell);
		this.message = message;
		this.title = title;
	}

	public InputDialog(Shell parentShell, String title, String message,
			String input) {
		super(parentShell);
		this.message = message;
		this.title = title;
		this.input = input;
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Label messageLabel = new Label(container, SWT.NONE);
		messageLabel.setText(message);

		inputText = new Text(container, SWT.BORDER);
		final GridData gd_inputText = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		inputText.setLayoutData(gd_inputText);
		inputText.setText(input);
		//
		return container;
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
		return new Point(250, 150);
	}

	public String getInput() {
		return input;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			input = inputText.getText();
			close();
			return;
		}
		super.buttonPressed(buttonId);
	}
}

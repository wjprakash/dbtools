
package com.nayaware.dbtools.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Error Manager that displays execptions and other error messages
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ErrorManager {

	private static Shell shell = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getShell();
	private static String INFORMATION = Messages
			.getString("ErrorManager.Information"); //$NON-NLS-1$
	private static String ERROR = Messages.getString("ErrorManager.Error"); //$NON-NLS-1$
	private static String WARNING = Messages.getString("ErrorManager.Warning"); //$NON-NLS-1$

	public static void showInformationMessage(String message) {
		MessageDialog.openInformation(shell, INFORMATION, message);
	}

	public static void showWarningMessage(String message) {
		MessageDialog.openWarning(shell, WARNING, message);
	}

	public static void showException(Throwable exc) {
		MessageDialog.openError(shell, ERROR, exc.getLocalizedMessage());
		exc.printStackTrace();
	}

	public static void showError(String errorMsg) {
		MessageDialog.openError(shell, ERROR, errorMsg);
	}

}

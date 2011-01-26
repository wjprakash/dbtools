
package com.nayaware.dbtools.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * An action to import SQL content from a file on to SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ImportSqlFromFileAction extends Action {

	public final static String ID = "com.nayaware.dbtools.actions.importSqlFromFileAction"; //$NON-NLS-1$
	private ISourceViewer sourceViewer;
	protected String defaultFileName;

	public ImportSqlFromFileAction(ISourceViewer sourceViewer2) {
		this.sourceViewer = sourceViewer2;
		setId(ID);
		setText(Messages.getString("ImportSqlFromFileAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ImportSqlFromFileAction.1")); //$NON-NLS-1$
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.IMPORT));
		defaultFileName = Messages.getString("ImportSqlFromFileAction.2"); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		FileDialog dlg = new FileDialog(shell, SWT.OPEN);
		dlg.setFilterPath(System.getProperty("user.home")); //$NON-NLS-1$
		dlg.setFileName(defaultFileName);
		dlg.setFilterNames(new String[] {
				Messages.getString("ImportSqlFromFileAction.4"), //$NON-NLS-1$
				Messages.getString("ImportSqlFromFileAction.5") }); //$NON-NLS-1$
		String fileLocation = dlg.open();
		if (fileLocation != null) {
			setDocumentContent(sourceViewer.getDocument(), fileLocation);
		}
	}

	private void setDocumentContent(IDocument document, String filePath) {
		Reader in = null;

		try {
			File file = new File(filePath);
			in = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer(512);
			char[] readBuffer = new char[512];
			int n = in.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n = in.read(readBuffer);
			}
			document.set(buffer.toString());

		} catch (IOException exc) {
			ErrorManager.showException(exc);
		} finally {
			try {
				in.close();
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			}
		}
	}
}

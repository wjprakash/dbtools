
package com.nayaware.dbtools.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
 * An action to export SQL content to a file from SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ExportSqlToFileAction extends Action {

	public final static String ID = "com.nayaware.dbtools.actions.exportSqlToFileAction"; //$NON-NLS-1$
	private ISourceViewer sourceViewer;
	protected String defaultFileName;

	public ExportSqlToFileAction(ISourceViewer sourceViewer2) {
		this.sourceViewer = sourceViewer2;
		setId(ID);
		setText(Messages.getString("ExportSqlToFileAction.0")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportSqlToFileAction.1")); //$NON-NLS-1$
		setImageDescriptor(ImageUtils.getImageDescriptor(ImageUtils.IMPORT));
		defaultFileName = Messages.getString("ExportSqlToFileAction.2"); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setFilterPath(System.getProperty("user.home")); //$NON-NLS-1$
		dlg.setFileName(defaultFileName);
		dlg.setFilterNames(new String[] {
				Messages.getString("ExportSqlToFileAction.4"), //$NON-NLS-1$
				Messages.getString("ExportSqlToFileAction.5") }); //$NON-NLS-1$
		String fileLocation = dlg.open();
		if (fileLocation != null) {
			writeDocumentContent(sourceViewer.getDocument(), fileLocation);
		}
	}

	private void writeDocumentContent(IDocument document, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			if (file.canWrite()) {
				Writer writer = new FileWriter(file);
				Writer out = new BufferedWriter(writer);
				try {
					out.write(document.get());
				} finally {
					out.close();
				}
			} else {
				ErrorManager.showError("File is read only");
			}
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
	}
}

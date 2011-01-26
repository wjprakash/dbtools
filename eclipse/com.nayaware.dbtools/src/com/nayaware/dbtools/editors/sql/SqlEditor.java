

package com.nayaware.dbtools.editors.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

import com.nayaware.dbtools.actions.ExportSqlToFileAction;
import com.nayaware.dbtools.actions.ImportSqlFromFileAction;
import com.nayaware.dbtools.api.IScript;
import com.nayaware.dbtools.editors.ISqlExecutionCapableEditor;
import com.nayaware.dbtools.editors.Messages;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.Script;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.ScriptNode;
import com.nayaware.dbtools.ui.InputDialog;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.util.PersistanceManager;
import com.nayaware.dbtools.viewers.SqlResultViewer;

/**
 * SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlEditor extends TextEditor implements ISqlExecutionCapableEditor {
	public static String ID = "com.nayaware.dbtools.sqlEditor"; //$NON-NLS-1$
	private static final String CONTENTASSIST_PROPOSAL_ID = "com.nayaware.dbtools.ContentAssistProposal"; //$NON-NLS-1$

	private SqlResultViewer sqlResultViewer;

	private SqlExecutor sqlExecutor;

	private SqlEditorColorManager colorManager;

	public SqlEditor() {
		super();
		this.colorManager = new SqlEditorColorManager();
		setSourceViewerConfiguration(new SqlSourceViewerConfiguration(
				colorManager));
		setDocumentProvider(new SqlSourceDocumentProvider());
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		setSite(site);
		setInput(editorInput);
		if (editorInput instanceof SqlEditorInput) {
			SqlEditorInput sqlEditorInput = (SqlEditorInput) editorInput;
			sqlExecutor = new SqlExecutor(sqlEditorInput.getDatabaseInfo(), ""); //$NON-NLS-1$
		}
		setPartName(editorInput.getName());
	}

	@Override
	public void createPartControl(Composite parent) {

		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		final ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.BORDER);
		final GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		gd_toolBar.horizontalIndent = 10;
		toolBar.setLayoutData(gd_toolBar);

		final ToolItem executeToolItem = new ToolItem(toolBar, SWT.PUSH);
		executeToolItem.setImage(ImageUtils.getIcon(ImageUtils.EXECUTE));
		executeToolItem.setToolTipText(Messages.getString("SqlEditor.2")); //$NON-NLS-1$
		executeToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				execute();
			}
		});

		final ToolItem importToolItem = new ToolItem(toolBar, SWT.PUSH);
		importToolItem.setImage(ImageUtils.getIcon(ImageUtils.IMPORT));
		importToolItem.setToolTipText(Messages.getString("SqlEditor.3")); //$NON-NLS-1$
		importToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				new ImportSqlFromFileAction(getSourceViewer()).run();
			}
		});

		final ToolItem exportToolItem = new ToolItem(toolBar, SWT.PUSH);
		exportToolItem.setImage(ImageUtils.getIcon(ImageUtils.EXPORT));
		exportToolItem.setToolTipText(Messages.getString("SqlEditor.4")); //$NON-NLS-1$
		exportToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				new ExportSqlToFileAction(getSourceViewer()).run();
			}
		});

		SashForm sashForm = new SashForm(parent, SWT.BORDER | SWT.VERTICAL);
		sashForm.setLayout(new FillLayout());
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the Source Viewer
		super.createPartControl(sashForm);
		getSourceViewer().getTextWidget().forceFocus();

		// Create the SQL Result Viewer
		sqlResultViewer = new SqlResultViewer(sashForm);
	}

	@Override
	protected void createActions() {
		super.createActions();

		// This action will fire a CONTENTASSIST_PROPOSALS operation
		// when executed
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("com.nayaware.dbtools.editors.sql.messages"); //$NON-NLS-1$
		IAction action = new TextOperationAction(resourceBundle,
				"ContentAssistProposal", this, //$NON-NLS-1$
				ISourceViewer.CONTENTASSIST_PROPOSALS);
		action.setActionDefinitionId(CONTENTASSIST_PROPOSAL_ID);

		// Tell the editor about this new action
		setAction(CONTENTASSIST_PROPOSAL_ID, action);

		// Tell the editor to execute this action
		// when Ctrl+Spacebar is pressed
		setActionActivationCode(CONTENTASSIST_PROPOSAL_ID, ' ', -1, SWT.CTRL);
	}

	public void execute() {
		IDocument document = getDocumentProvider()
				.getDocument(getEditorInput());
		final String sqlScript = document.get();
		Job dbJob = new Job("sqlEditorJob") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				sqlExecutor.setScript(sqlScript);
				final ExecutionStatus execStatus = sqlExecutor.execute();
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						sqlResultViewer.showResults(execStatus);
					}
				});
				return Status.OK_STATUS;
			}
		};
		dbJob.schedule();
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	public static class SqlSourceDocumentProvider extends FileDocumentProvider {

		private IScript currentScript;

		@Override
		protected IDocument createDocument(Object element) throws CoreException {
			IDocument document;
			if (element instanceof SqlEditorInput) {
				document = new Document();
				SqlEditorInput sqlEditorInput = (SqlEditorInput) element;
				AbstractNode node = sqlEditorInput.getNode();
				if (node instanceof ScriptNode) {
					currentScript = ((ScriptNode) node).getScript();
					setDocumentContent(document);
				} else {
					String script = (String) sqlEditorInput
							.getAdapter(String.class);
					document.set(script);
				}
			} else {
				document = super.createDocument(element);
			}
			if (document != null) {
				IDocumentPartitioner partitioner = new FastPartitioner(
						new SqlPartitionScanner(), new String[] {
								SqlPartitionScanner.SQL_COMMENT,
								SqlPartitionScanner.SQL_STRING });
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
			return document;
		}

		private void setDocumentContent(IDocument document) {
			Reader in = null;

			try {
				File file = new File(currentScript.getPath());
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

		@Override
		public boolean isReadOnly(Object element) {
			if (element instanceof SqlEditorInput) {
				return false;
			} else {
				return super.isReadOnly(element);
			}
		}

		@Override
		public boolean isModifiable(Object element) {
			if (element instanceof SqlEditorInput) {
				return true;
			} else {
				return super.isModifiable(element);
			}
		}

		@Override
		protected void doSaveDocument(IProgressMonitor monitor, Object element,
				IDocument document, boolean overwrite) throws CoreException {
			if (element instanceof SqlEditorInput) {

				SqlEditorInput sqlEditorInput = (SqlEditorInput) element;
				AbstractNode node = sqlEditorInput.getNode();

				if (currentScript != null) {
					writeDocumentContent(document);

				} else {
					Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
					InputDialog inputDialog = new InputDialog(shell, Messages
							.getString("SqlEditor.6"), //$NON-NLS-1$
							Messages.getString("SqlEditor.7")); //$NON-NLS-1$

					int ret = inputDialog.open();
					if (ret == IDialogConstants.OK_ID) {

						String scriptName = inputDialog.getInput();

						File scritptDir = PersistanceManager.getInstance()
								.getScriptDir(
										sqlEditorInput.getDatabaseInfo()
												.getConnectionConfig());

						File file = new File(scritptDir, scriptName + ".sql"); //$NON-NLS-1$

						if (node instanceof ConnectionNode) {
							ConnectionNode connectionNode = (ConnectionNode) node;
							currentScript = new Script(connectionNode
									.getDatbaseObject().getDatabaseInfo(),
									scriptName);
							currentScript.setPath(file.getAbsolutePath());
							connectionNode.addScriptNode(new ScriptNode(
									currentScript));
							writeDocumentContent(document);
						} else {
							// Should not happen
							ErrorManager.showException(new Throwable(Messages
									.getString("SqlEditor.9"))); //$NON-NLS-1$
						}
					}

					// ErrorManager.showInformationMessage("File saved to - "
					// + file.getAbsolutePath());
				}
			} else {
				super.doSaveDocument(monitor, element, document, overwrite);
			}
		}

		private void writeDocumentContent(IDocument document) {
			try {
				File file = new File(currentScript.getPath());
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
					ErrorManager.showError(Messages.getString("SqlEditor.10")); //$NON-NLS-1$
				}
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			}
		}
	}
}

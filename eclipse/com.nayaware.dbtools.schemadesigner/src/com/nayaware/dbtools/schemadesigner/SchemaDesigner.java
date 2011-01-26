
package com.nayaware.dbtools.schemadesigner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.api.ISchemaDiagram;
import com.nayaware.dbtools.editors.ISqlExecutionCapableEditor;
import com.nayaware.dbtools.editors.sql.SqlEditor;
import com.nayaware.dbtools.editors.sql.SqlEditorInput;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.SchemaDiagram;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.SchemaDiagramNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.schemadesigner.parts.SchemaDesignerEditPartFactory;
import com.nayaware.dbtools.ui.InputDialog;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.util.PersistanceManager;
import com.nayaware.dbtools.viewers.SqlResultViewer;
import com.nayaware.dbtools.viewers.SqlSourceViewer;

/**
 * Designer to design Connection Schema
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesigner extends GraphicalEditorWithPalette implements
		ISqlExecutionCapableEditor, PropertyChangeListener {

	public static final String ID = "com.nayaware.dbtools.schemaDesigner"; //$NON-NLS-1$

	private SchemaDesignerPalette schemaDesignerPalette = new SchemaDesignerPalette();

	private SqlSourceViewer sqlScriptViewer;

	private Schemata schemata;

	private SqlResultViewer sqlResultViewer;
	private SqlExecutor sqlExecutor;

	private DetailPanel detailPanel;
	private SashForm designerSashForm;
	private TabFolder tabFolder;
	private TabItem sqlTabItem;

	private ISchemaDiagram currentSchemaDiagram;

	private boolean dirty;

	public SchemaDesigner() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		schemata = (Schemata) getEditorInput().getAdapter(Schemata.class);
		schemata.addPropertyChangeListener(this);
		sqlExecutor = new SqlExecutor(schemata.getDatabaseInfo(), ""); //$NON-NLS-1$
		setPartName(input.getName());
		SchemaDesignerInput queryBuilderInput = (SchemaDesignerInput) input;
		AbstractNode node = queryBuilderInput.getNode();
		if (node instanceof SchemaDiagramNode) {
			currentSchemaDiagram = ((SchemaDiagramNode) node)
					.getSchemaDiagram();
			schemata.restore(new File(currentSchemaDiagram.getPath()));
		}
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(schemata);
		viewer.addDropTargetListener(createTransferDropTargetListener());
	}

	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			@Override
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class<?>) template);
			}
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = this.getGraphicalViewer();
		viewer.setEditPartFactory(new SchemaDesignerEditPartFactory(schemata,
				true));

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);

		viewer.setRootEditPart(root);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// Add Support for mouse wheel
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);

		ContextMenuProvider provider = new SchemaDesignerContextMenuProvider(
				viewer, getActionRegistry(), true);
		viewer.setContextMenu(provider);
		getSite().registerContextMenu(ID, provider, viewer);
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		Composite toolbarComposite = new Composite(parent, SWT.BORDER);
		final GridLayout toolbarCompositeGridLayout = new GridLayout();
		toolbarCompositeGridLayout.verticalSpacing = 0;
		toolbarCompositeGridLayout.marginWidth = 0;
		toolbarCompositeGridLayout.marginHeight = 0;
		toolbarCompositeGridLayout.horizontalSpacing = 0;
		toolbarComposite.setLayout(toolbarCompositeGridLayout);
		toolbarComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final ToolBar toolBar = new ToolBar(toolbarComposite, SWT.FLAT);
		final GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		gd_toolBar.horizontalIndent = 10;
		toolBar.setLayoutData(gd_toolBar);

		final ToolItem executeToolItem = new ToolItem(toolBar, SWT.PUSH);
		executeToolItem.setImage(ImageUtils.getIcon(ImageUtils.EXECUTE));
		executeToolItem.setToolTipText(Messages.getString("SchemaDesigner.2")); //$NON-NLS-1$
		executeToolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				execute();
			}
		});

		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL,
				ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
		ZoomComboContributionItem zoomComboContributionItem = new ZoomComboContributionItem(
				getSite().getPage(), zoomStrings);
		zoomComboContributionItem.fill(toolBar, 1);

		tabFolder = new TabFolder(parent, SWT.FILL);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				updateScript();
			}
		});

		// Create the Design tab
		final TabItem designTabItem = new TabItem(tabFolder, SWT.FILL);
		designTabItem.setText(Messages.getString("SchemaDesigner.3")); //$NON-NLS-1$

		final SashForm designTabSashForm = new SashForm(tabFolder,
				SWT.HORIZONTAL);
		designTabSashForm.setLayout(new FillLayout());

		final Composite composite = new Composite(designTabSashForm, SWT.BORDER);
		composite.setLayout(new FillLayout());
		createPaletteViewer(composite);

		designerSashForm = new SashForm(designTabSashForm, SWT.VERTICAL);
		designerSashForm.setLayout(new FillLayout());

		createGraphicalViewer(designerSashForm);
		getGraphicalViewer().getControl().setLayoutData(
				new GridData(GridData.FILL, GridData.FILL, true, true));

		detailPanel = new DetailPanel(designerSashForm, schemata);

		designTabSashForm.setWeights(new int[] { 1, 5 });
		designTabItem.setControl(designTabSashForm);

		// Create the SQL tab
		sqlTabItem = new TabItem(tabFolder, SWT.FILL);
		sqlTabItem.setText(Messages.getString("SchemaDesigner.4")); //$NON-NLS-1$

		SashForm sourceSashForm = new SashForm(tabFolder, SWT.VERTICAL);
		sourceSashForm.setLayout(new GridLayout());
		sqlTabItem.setControl(sourceSashForm);

		Composite sourceViewerComposite = new Composite(sourceSashForm,
				SWT.BORDER);
		final GridLayout sourceViewerCompositeGridLayout = new GridLayout();
		sourceViewerCompositeGridLayout.verticalSpacing = 0;
		sourceViewerCompositeGridLayout.marginWidth = 0;
		sourceViewerCompositeGridLayout.marginHeight = 0;
		sourceViewerCompositeGridLayout.horizontalSpacing = 0;
		sourceViewerComposite.setLayout(sourceViewerCompositeGridLayout);
		sourceViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));

		// sqlScriptViewer = new SqlSourceViewer(sourceSashForm);
		sqlScriptViewer = new SqlSourceViewer(sourceViewerComposite);
		sqlScriptViewer.setEditable(false);

		sqlScriptViewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		sqlScriptViewer.getTextWidget().setBackground(sourceViewerComposite.getBackground());

		Button editUsingSqlButton = new Button(sourceViewerComposite, SWT.NONE);
		editUsingSqlButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false));
		editUsingSqlButton.setText("Edit using SQL Editor");
		editUsingSqlButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				openInSqlEditor();
			}
		});

		// Create the SQL Result Viewer
		sqlResultViewer = new SqlResultViewer(sourceSashForm);
		setSql();
	}

	private void setSql() {
		if (currentSchemaDiagram == null) {
			return;
		}
		Reader in = null;

		try {
			File file = new File(currentSchemaDiagram.getPath());
			if (!file.exists()) {
				return;
			}
			in = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer(512);
			char[] readBuffer = new char[512];
			int n = in.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n = in.read(readBuffer);
			}
			sqlScriptViewer.getDocument().set(buffer.toString());
			try {
				in.close();
			} catch (IOException exc) {
				ErrorManager.showException(exc);
			}
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SchemaDesignerInput schemaDesignerInput = (SchemaDesignerInput) getEditorInput();
		AbstractNode node = schemaDesignerInput.getNode();

		if (currentSchemaDiagram != null) {
			schemata.persist(new File(currentSchemaDiagram.getPath()));
		} else {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			InputDialog inputDialog = new InputDialog(shell, Messages
					.getString("SchemaDesigner.5"), //$NON-NLS-1$
					Messages.getString("SchemaDesigner.6")); //$NON-NLS-1$

			int ret = inputDialog.open();
			if (ret == IDialogConstants.OK_ID) {

				String queryName = inputDialog.getInput();

				File scritptDir = PersistanceManager.getInstance()
						.getSchemaDiagramDir(
								schemaDesignerInput.getDatabaseInfo()
										.getConnectionConfig());

				File file = new File(scritptDir, queryName + ".xml"); //$NON-NLS-1$

				if (node instanceof ConnectionNode) {
					ConnectionNode connectionNode = (ConnectionNode) node;
					currentSchemaDiagram = new SchemaDiagram(connectionNode
							.getDatbaseObject().getDatabaseInfo(), queryName);
					currentSchemaDiagram.setPath(file.getAbsolutePath());
					connectionNode.addSchemaDiagramNode(new SchemaDiagramNode(
							currentSchemaDiagram));
					schemata.persist(new File(currentSchemaDiagram.getPath()));
				} else {
					// Should not happen
					ErrorManager.showException(new Throwable(Messages
							.getString("SchemaDesigner.8"))); //$NON-NLS-1$
				}
			}
		}
		setDirty(false);
	}

	private void writeDocumentContent() {
		try {
			File file = new File(currentSchemaDiagram.getPath());
			if (!file.exists()) {
				file.createNewFile();
			}

			if (file.canWrite()) {
				Writer writer = new FileWriter(file);
				Writer out = new BufferedWriter(writer);
				try {
					out.write(schemata.generateSql());
				} finally {
					out.close();
				}
			} else {
				ErrorManager.showError(Messages.getString("SchemaDesigner.9")); //$NON-NLS-1$
			}
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return schemaDesignerPalette;
	}

	private void updateScript() {
		if (sqlScriptViewer != null) {
			sqlScriptViewer.getDocument().set(schemata.generateSql());
		}
	}

	public void execute() {
		updateScript();
		tabFolder.setSelection(sqlTabItem);
		Job dbJob = new Job("Database Designer Job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				sqlExecutor.setScript(sqlScriptViewer.getDocument().get());
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

	private void openInSqlEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		String sqlScript =  sqlScriptViewer.getDocument().get();
		SqlEditorInput input = new SqlEditorInput(schemata
				.getDatabaseInfo(), sqlScript);
		try {
			page.openEditor(input, SqlEditor.ID, true);
		} catch (PartInitException exc) {
			ErrorManager.showException(exc);
		}
	}

	public synchronized void propertyChange(PropertyChangeEvent evt) {
		if (Schemata.SELECTED_COLUMN_NODE.equals(evt.getPropertyName())) {
			detailPanel.setColumnNode((SdColumnNode) evt.getNewValue());
		}

		if (Schemata.SELECTED_TABLE_NODE.equals(evt.getPropertyName())) {
			detailPanel.setTableNode((SdTableNode) evt.getNewValue());
		}

		if (Schemata.SELECTED_SCHEMATA.equals(evt.getPropertyName())) {
			detailPanel.setSchemata((Schemata) evt.getNewValue());
		}

		if (Schemata.PROP_DIRTY.equals(evt.getPropertyName())) {
			setDirty(true);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());
		}

		return super.getAdapter(type);
	}
}

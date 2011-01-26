
package com.nayaware.dbtools.querybuilder;

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
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
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

import com.nayaware.dbtools.api.IQuery;
import com.nayaware.dbtools.editors.ISqlExecutionCapableEditor;
import com.nayaware.dbtools.editors.sql.SqlEditor;
import com.nayaware.dbtools.editors.sql.SqlEditorInput;
import com.nayaware.dbtools.execute.ExecutionStatus;
import com.nayaware.dbtools.execute.SqlExecutor;
import com.nayaware.dbtools.model.Query;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.nodes.QueryNode;
import com.nayaware.dbtools.querybuilder.model.QbColumnNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.parts.QueryBuilderEditPartFactory;
import com.nayaware.dbtools.ui.InputDialog;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;
import com.nayaware.dbtools.util.PersistanceManager;
import com.nayaware.dbtools.viewers.SqlResultViewer;
import com.nayaware.dbtools.viewers.SqlSourceViewer;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilder extends GraphicalEditor implements
		PropertyChangeListener, ISqlExecutionCapableEditor {

	public static final String ID = "com.nayaware.dbtools.queryBuilder"; //$NON-NLS-1$

	private SqlSourceViewer sqlScriptViewer;
	private QueryData queryData;
	private QueryBuilderColumnTableViewer queryBuilderColumnTableViewer;

	private SqlResultViewer sqlResultViewer;
	private SqlExecutor sqlExecutor;

	private TabItem sqlTabItem;
	private TabFolder tabFolder;

	private IQuery currentQuery;

	private boolean dirty;

	public QueryBuilder() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		queryData = (QueryData) getEditorInput().getAdapter(QueryData.class);
		queryData.addPropertyChangeListener(this);
		sqlExecutor = new SqlExecutor(queryData.getDatabaseInfo(), ""); //$NON-NLS-1$
		setPartName(input.getName());
		QueryBuilderInput queryBuilderInput = (QueryBuilderInput) input;
		AbstractNode node = queryBuilderInput.getNode();
		if (node instanceof QueryNode) {
			currentQuery = ((QueryNode) node).getQuery();
			queryData.restore(new File(currentQuery.getPath()));
		}
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
		executeToolItem.setToolTipText(Messages.getString("QueryBuilder.2")); //$NON-NLS-1$
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

		tabFolder = new TabFolder(parent, SWT.BORDER);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (e.item == sqlTabItem) {
					updateScript();
				}
			}
		});
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Create the Design tab
		final TabItem designTabItem = new TabItem(tabFolder, SWT.FILL);
		designTabItem.setText(Messages.getString("QueryBuilder.3")); //$NON-NLS-1$

		SashForm designTabSashForm = new SashForm(tabFolder, SWT.VERTICAL);
		designTabSashForm.setLayout(new FillLayout());
		designTabItem.setControl(designTabSashForm);

		// Create the query graph view
		createGraphicalViewer(designTabSashForm);
		getGraphicalViewer().getControl().setLayoutData(
				new GridData(GridData.FILL, GridData.FILL, true, true));

		// Create the selected column table view
		queryBuilderColumnTableViewer = new QueryBuilderColumnTableViewer(
				designTabSashForm, queryData);
		queryBuilderColumnTableViewer.setInput(queryData);

		designTabSashForm.setWeights(new int[] { 4, 1 });

		// Create the SQL tab
		sqlTabItem = new TabItem(tabFolder, SWT.FILL);
		sqlTabItem.setText(Messages.getString("QueryBuilder.4")); //$NON-NLS-1$

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
		sqlScriptViewer.getTextWidget().setBackground(
				sourceViewerComposite.getBackground());

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
		if (currentQuery == null) {
			return;
		}
		Reader in = null;

		try {
			File file = new File(currentQuery.getPath());
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
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(queryData);
		viewer
				.addDropTargetListener(new QbDropTargetListener(viewer,
						queryData));
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new QueryBuilderEditPartFactory(queryData));

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

		ContextMenuProvider provider = new QueryBuilderContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu(ID, provider, viewer);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		QueryBuilderInput queryBuilderInput = (QueryBuilderInput) getEditorInput();
		AbstractNode node = queryBuilderInput.getNode();

		if (currentQuery != null) {
			queryData.persist(new File(currentQuery.getPath()));
		} else {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			InputDialog inputDialog = new InputDialog(shell, Messages
					.getString("QueryBuilder.5"), //$NON-NLS-1$
					Messages.getString("QueryBuilder.6")); //$NON-NLS-1$

			int ret = inputDialog.open();
			if (ret == IDialogConstants.OK_ID) {

				String queryName = inputDialog.getInput();

				File scritptDir = PersistanceManager.getInstance().getQueryDir(
						queryBuilderInput.getDatabaseInfo()
								.getConnectionConfig());

				File file = new File(scritptDir, queryName + ".xml"); //$NON-NLS-1$

				if (node instanceof ConnectionNode) {
					ConnectionNode connectionNode = (ConnectionNode) node;
					currentQuery = new Query(connectionNode.getDatbaseObject()
							.getDatabaseInfo(), queryName);
					currentQuery.setPath(file.getAbsolutePath());
					connectionNode.addQueryNode(new QueryNode(currentQuery));
					queryData.persist(file);
				} else {
					// Should not happen
					ErrorManager.showException(new Throwable(Messages
							.getString("QueryBuilder.8"))); //$NON-NLS-1$
				}
			}
		}
		setDirty(false);
	}

	private void writeDocumentContent() {
		try {
			File file = new File(currentQuery.getPath());
			if (!file.exists()) {
				file.createNewFile();
			}

			if (file.canWrite()) {
				Writer writer = new FileWriter(file);
				Writer out = new BufferedWriter(writer);
				try {
					out.write(queryData.getSelectStatement());
				} finally {
					out.close();
				}
			} else {
				ErrorManager.showError(Messages.getString("QueryBuilder.9")); //$NON-NLS-1$
			}
		} catch (IOException exc) {
			ErrorManager.showException(exc);
		}
	}

	private void updateScript() {
		if (sqlScriptViewer != null) {
			sqlScriptViewer.getDocument().set(queryData.getSelectStatement());
		}
	}

	public void execute() {
		updateScript();
		tabFolder.setSelection(sqlTabItem);
		Job dbJob = new Job(Messages.getString("QueryBuilder.10")) { //$NON-NLS-1$
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
		String sqlScript = sqlScriptViewer.getDocument().get();
		SqlEditorInput input = new SqlEditorInput(queryData.getDatabaseInfo(),
				sqlScript);
		try {
			page.openEditor(input, SqlEditor.ID, true);
		} catch (PartInitException exc) {
			ErrorManager.showException(exc);
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());

		return super.getAdapter(type);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean value) {
		dirty = value;
		firePropertyChange(PROP_DIRTY);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (QueryData.COLUMN_NODE_ADD.equals(property)) {
			QbColumnNode columnNode = (QbColumnNode) evt.getNewValue();
			if (queryBuilderColumnTableViewer != null) {
				queryBuilderColumnTableViewer.add(columnNode);
			}
		}
		if (QueryData.COLUMN_NODE_REMOVE.equals(property)) {
			QbColumnNode columnNode = (QbColumnNode) evt.getNewValue();
			if (queryBuilderColumnTableViewer != null) {
				queryBuilderColumnTableViewer.remove(columnNode);
			}
		}
		if (QueryData.PROP_DIRTY.equals(evt.getPropertyName())) {
			setDirty(true);
		}
	}
}

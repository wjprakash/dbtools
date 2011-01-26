
package com.nayaware.dbtools.nodes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.actions.CreateDatabaseAction;
import com.nayaware.dbtools.actions.EditDatabaseConnectionAction;
import com.nayaware.dbtools.actions.OpenQueryBuilderAction;
import com.nayaware.dbtools.actions.OpenSchemaDesignerAction;
import com.nayaware.dbtools.actions.OpenSqlEditorAction;
import com.nayaware.dbtools.actions.RefreshAction;
import com.nayaware.dbtools.actions.RemoveDatabaseConnectionAction;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.util.ErrorManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Connection Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ConnectionNode extends AbstractNode {

	public static final String DATABASE_REFRESH_JOB = Messages.getString("ConnectionNode.0"); //$NON-NLS-1$

	private WaitingNode waitingNode = new WaitingNode();

	public TableGroupNode tableGroupNode;
	public ViewGroupNode viewGroupNode;
	public ScriptGroupNode scriptGroupNode;
	public QueryGroupNode queryGroupNode;
	public SchemaDiagramGroupNode schemaDiagramGroupNode;

	public ConnectionNode(IConnection database) {
		super(database);
		setImageKey(ImageUtils.DATABASE);
	}

	/**
	 * Do lazy initialization of children
	 * 
	 * @throws SQLException
	 */
	@Override
	protected synchronized void initializeChildren() {
		if (initializing) {
			return;
		}
		initializing = true;
		super.initializeChildren();

		Job dbJob = new Job(DATABASE_REFRESH_JOB) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				IConnection connection = (IConnection) getDatbaseObject();
				try {
					if (connection.hasSchemaSupport()) {

						if (getDatbaseObject() instanceof IConnection) {
							IConnection database = (IConnection) getDatbaseObject();
							List<ISchema> schemaList = database
									.getSchemaList();
							List<AbstractNode> children = new ArrayList<AbstractNode>();
							for (ISchema schema : schemaList) {
								AbstractNode child = new SchemaNode(schema);
								child.setParent(ConnectionNode.this);
								children.add(child);
							}
							setChildren(children);
							addScriptGroupNode();
							addQueryGroupNode();
							addSchemaDiagramGroupNode();
						}
					} else {
						tableGroupNode = new TableGroupNode(getDatbaseObject());
						viewGroupNode = new ViewGroupNode(getDatbaseObject());
						addChild(tableGroupNode);
						addChild(viewGroupNode);
						addScriptGroupNode();
						addQueryGroupNode();
						addSchemaDiagramGroupNode();
					}
				} catch (final Exception exc) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ErrorManager.showException(exc);
						}
					});
					return Status.OK_STATUS;
				} finally {
					initializing = false;
					removeChild(waitingNode);
				}
				return Status.OK_STATUS;
			}
		};
		dbJob.setPriority(Job.SHORT);
		dbJob.setSystem(true);
		dbJob.schedule(refreshDelay);
		addChild(waitingNode);
	}

	private void addScriptGroupNode() {
		IConnection connection = (IConnection) getDatbaseObject();
		if (!connection.getScriptList().isEmpty()) {
			scriptGroupNode = new ScriptGroupNode(
					(IConnection) getDatbaseObject());
			addChild(scriptGroupNode);
		}
	}

	private void addQueryGroupNode() {
		IConnection connection = (IConnection) getDatbaseObject();
		if (!connection.getQueryList().isEmpty()) {
			queryGroupNode = new QueryGroupNode(
					(IConnection) getDatbaseObject());
			addChild(queryGroupNode);
		}
	}

	private void addSchemaDiagramGroupNode() {
		IConnection connection = (IConnection) getDatbaseObject();
		if (!connection.getSchemaDiagramList().isEmpty()) {
			schemaDiagramGroupNode = new SchemaDiagramGroupNode(
					(IConnection) getDatbaseObject());
			addChild(schemaDiagramGroupNode);
		}
	}

	@Override
	public Action[] getActions() {
		return new Action[] {
				new RefreshAction(this),
				null, // Separator
				new EditDatabaseConnectionAction(this),
				new RemoveDatabaseConnectionAction(this),
				null, // Separator
				new CreateDatabaseAction(this),
				// new LaunchMigrationToolAction(this),
				null, // Separator
				new OpenSqlEditorAction(this),
				new OpenQueryBuilderAction(this),
				new OpenSchemaDesignerAction(this) };
	}

	public synchronized List<AbstractNode> getSchemaNodeList() {
		if (children == null) {
			initializeChildren();
		}
		return children;
	}

	public SchemaNode findSchemaNodeByName(String schemaName) {
		if (children != null)
			for (AbstractNode node : children) {
				if (node instanceof SchemaNode) {
					if (schemaName.equals(node.getName())) {
						return (SchemaNode) node;
					}
				}
			}
		return null;
	}

	@Override
	public void refresh() {
		if (initializing) {
			return;
		}
		initializing = true;
		children = new ArrayList<AbstractNode>();
		Job dbJob = new Job(DATABASE_REFRESH_JOB) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				try {
					getDatbaseObject().getDatabaseInfo().refreshConnection();
					getDatbaseObject().refresh();
					removeChild(waitingNode);
					initializing = false;
					initializeChildren();
				} catch (final Exception exc) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ErrorManager.showException(exc);
						}
					});
					return Status.OK_STATUS;
				} finally {
					initializing = false;
					removeChild(waitingNode);
				}
				return Status.OK_STATUS;
			}
		};
		dbJob.setPriority(Job.SHORT);
		dbJob.setSystem(true);
		dbJob.schedule(refreshDelay);
		addChild(waitingNode);
		propertyChangeSupport.firePropertyChange(NODE_CHILDREN_MODIFIED, null,
				this);
	}

	public synchronized void addScriptNode(ScriptNode scriptNode) {
		if (scriptGroupNode == null) {
			scriptGroupNode = new ScriptGroupNode(
					(IConnection) getDatbaseObject());
			addChild(scriptGroupNode);
		} else {
			if (!children.contains(scriptGroupNode)) {
				addChild(scriptGroupNode);
			}
		}
		scriptGroupNode.addScriptNode(scriptNode);
	}

	public void addQueryNode(QueryNode queryNode) {
		if (queryGroupNode == null) {
			queryGroupNode = new QueryGroupNode(
					(IConnection) getDatbaseObject());
			addChild(queryGroupNode);
		} else {
			if (!children.contains(queryGroupNode)) {
				addChild(queryGroupNode);
			}
		}
		queryGroupNode.addQueryNode(queryNode);
	}

	public void addSchemaDiagramNode(SchemaDiagramNode schemaDiagramNode) {
		if (schemaDiagramGroupNode == null) {
			schemaDiagramGroupNode = new SchemaDiagramGroupNode(
					(IConnection) getDatbaseObject());
			addChild(schemaDiagramGroupNode);
		} else {
			if (!children.contains(schemaDiagramGroupNode)) {
				addChild(schemaDiagramGroupNode);
			}
		}
		schemaDiagramGroupNode.addSchemaDiagramNode(schemaDiagramNode);
	}
}

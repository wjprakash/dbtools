
package com.nayaware.dbtools.nodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.Action;

import com.nayaware.dbtools.actions.AddDatabaseConnectionAction;
import com.nayaware.dbtools.actions.CreateDerbyDatabaseAction;
import com.nayaware.dbtools.actions.CreateSqliteDatabaseAction;
import com.nayaware.dbtools.api.ConnectionManager;
import com.nayaware.dbtools.api.IConnection;
import com.nayaware.dbtools.api.IConnectionConfig;
import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ConnectionManager.ConnectionManagerListener;
import com.nayaware.dbtools.core.DatabaseInfo;
import com.nayaware.dbtools.model.AbstractDatabaseObject;
import com.nayaware.dbtools.model.Connection;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The root node of the Connection Explorer Tree
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class ConnectionGroupNode extends AbstractNode implements
		ConnectionManagerListener {

	private ConnectionManager connectionManager = ConnectionManager
			.getInstance();

	public ConnectionGroupNode() {
		super(new AbstractDatabaseObject(null, Messages.getString("ConnectionGroupNode.0"))); //$NON-NLS-1$
		setImageKey(ImageUtils.DATABASE_GROUP);
		connectionManager.addConnectionManagerListener(this);
	}

	/**
	 * Do lazy initialization of children
	 */
	@Override
	protected void initializeChildren() {
		super.initializeChildren();
		for (IConnectionConfig conInfo : connectionManager
				.getConnectionConfigList()) {
			IDatabaseInfo dbInfo = new DatabaseInfo(conInfo);
			addChild(new ConnectionNode(new Connection(dbInfo)));
		}
	}

	@Override
	public Action[] getActions() {
		return new Action[] { new AddDatabaseConnectionAction(this), null,
				new CreateDerbyDatabaseAction(this),
				new CreateSqliteDatabaseAction(this), null,
		// TODO: make it visible once the Migration tool implementation is done
		// new LaunchMigrationToolAction(this)
		};
	}

	public synchronized void connectionConfigAdded(IConnectionConfig config) {
		if (children == null) {
			super.initializeChildren();
		}
		IDatabaseInfo dbInfo = new DatabaseInfo(config);
		addChild(new ConnectionNode(new Connection(dbInfo)));
	}

	public void connectionConfigRemoved(IConnectionConfig connectionConfig) {
		ConnectionNode connectionNode = findConnectionNode(connectionConfig);
		if (connectionNode != null) {
			removeChild(connectionNode);
			refresh();
		}
	}

	public synchronized List<AbstractNode> getDatabaseList() {
		if (children == null) {
			initializeChildren();
		}
		return Collections.unmodifiableList(children);
	}

	public void connectionConfigModified(IConnectionConfig connectionConfig) {
		ConnectionNode connectionNode = findConnectionNode(connectionConfig);
		if (connectionNode != null) {
			connectionNode.getDatbaseObject().getDatabaseInfo()
					.setConnectionConfig(connectionConfig);
			connectionNode.setName(connectionConfig.getDisplayName());
		}
	}

	private ConnectionNode findConnectionNode(IConnectionConfig connectionConfig) {
		if (children == null) {
			return null;
		}
		for (AbstractNode node : children) {
			if (node instanceof ConnectionNode) {
				ConnectionNode dbNode = (ConnectionNode) node;
				IConnection database = (IConnection) dbNode.getDatbaseObject();
				if (connectionConfig.getName().equals(
						database.getDatabaseInfo().getConnectionConfig()
								.getName())) {
					return dbNode;
				}
			}
		}
		return null;
	}
}

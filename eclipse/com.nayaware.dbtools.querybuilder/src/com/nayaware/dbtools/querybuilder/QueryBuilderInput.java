
package com.nayaware.dbtools.querybuilder;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.editors.IQueryBuilderInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ConnectionNode;
import com.nayaware.dbtools.querybuilder.model.QueryData;

/**
 * Input for the Schema Diagram Viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderInput implements IQueryBuilderInput {

	private QueryData query;
	private IDatabaseInfo databaseInfo;
	private AbstractNode node;
	private AbstractNode connectionNode;

	public QueryBuilderInput(){
		
	}
	public QueryBuilderInput(IDatabaseInfo dbInfo, AbstractNode node,
			AbstractNode connectionNode) {
		this.connectionNode = connectionNode;
		this.query = new QueryData((ConnectionNode) connectionNode);
		databaseInfo = dbInfo;
		this.node = node;
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
	
	public void setConnectionNode(AbstractNode connectionNode) {
		this.connectionNode = connectionNode;
		this.query = new QueryData((ConnectionNode) connectionNode);
	}

	public AbstractNode getConnectionNode() {
		return connectionNode;
	}

	public AbstractNode getNode() {
		return node;
	}

	public void setNode(AbstractNode node) {
		this.node = node;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return Messages.getString("QueryBuilderInput.0") + databaseInfo.getName() + Messages.getString("QueryBuilderInput.1"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return Messages.getString("QueryBuilderInput.2") + databaseInfo.getName() + Messages.getString("QueryBuilderInput.3") //$NON-NLS-1$ //$NON-NLS-2$
				+ databaseInfo.getConnectionConfig().getUrl()
				+ Messages.getString("QueryBuilderInput.4"); //$NON-NLS-1$
	}

	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(QueryData.class)) {
			return query;
		}
		return null;
	}
}


package com.nayaware.dbtools.schemadesigner;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.editors.ISchemaDesignerInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * Input for the Schema Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerInput implements ISchemaDesignerInput {

	protected Schemata schemata;
	protected IDatabaseInfo databaseInfo;
	private AbstractNode node;
	private AbstractNode connectionNode;
	
	public SchemaDesignerInput(){
		
	}

	public IDatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
	
	public void setConnectionNode(AbstractNode connectionNode) {
		this.connectionNode = connectionNode;
		this.schemata = new Schemata("Schemata", connectionNode, true);
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
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return Messages.getString("SchemaDesignerInput.0") + databaseInfo.getName() + Messages.getString("SchemaDesignerInput.1"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return Messages.getString("SchemaDesignerInput.2") + databaseInfo.getName() + Messages.getString("SchemaDesignerInput.3") //$NON-NLS-1$ //$NON-NLS-2$
				+ databaseInfo.getConnectionConfig().getUrl() + Messages.getString("SchemaDesignerInput.4"); //$NON-NLS-1$
	}

	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(Schemata.class)) {
			return schemata;
		}
		return null;
	}
}

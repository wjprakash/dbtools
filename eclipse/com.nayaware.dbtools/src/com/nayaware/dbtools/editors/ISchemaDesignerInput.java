
package com.nayaware.dbtools.editors;

import org.eclipse.ui.IEditorInput;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.nodes.AbstractNode;

public interface ISchemaDesignerInput extends IEditorInput {

	public IDatabaseInfo getDatabaseInfo() ;

	public void setDatabaseInfo(IDatabaseInfo databaseInfo);
	
	public void setConnectionNode(AbstractNode connectionNode);

	public AbstractNode getConnectionNode() ;

	public AbstractNode getNode();

	public void setNode(AbstractNode node);
}

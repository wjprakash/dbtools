
package com.nayaware.dbtools.schemadesigner;

import com.nayaware.dbtools.editors.ISchemaDiagramInput;
import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.SchemaNode;
import com.nayaware.dbtools.schemadesigner.model.Schemata;

/**
 * Input for the Schema Diagram Viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDiagramInput extends SchemaDesignerInput implements ISchemaDiagramInput{
	
	private AbstractNode schemaNode;
	
	public SchemaDiagramInput(){
		
	}
	
	public void setSchemaNode(AbstractNode schemaNode) {
		this.schemaNode = schemaNode;
		this.schemata = new Schemata((SchemaNode) schemaNode, true);
	}

	public AbstractNode getSchemaNode() {
		return schemaNode;
	}

	@Override
	public String getName() {
		return Messages.getString("SchemaDiagramInput.0") + databaseInfo.getName() + Messages.getString("SchemaDiagramInput.1"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getToolTipText() {
		return Messages.getString("SchemaDiagramInput.2") + databaseInfo.getName() + Messages.getString("SchemaDiagramInput.3") //$NON-NLS-1$ //$NON-NLS-2$
				+ databaseInfo.getConnectionConfig().getUrl() + Messages.getString("SchemaDiagramInput.4"); //$NON-NLS-1$
	}

}

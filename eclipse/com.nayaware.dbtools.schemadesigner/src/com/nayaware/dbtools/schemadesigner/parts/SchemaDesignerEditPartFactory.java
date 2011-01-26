
package com.nayaware.dbtools.schemadesigner.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.nayaware.dbtools.nodes.WaitingNode;
import com.nayaware.dbtools.schemadesigner.model.Relationship;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;

/**
 * Schema diagram viewer edit part factory
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerEditPartFactory implements EditPartFactory {
	boolean isDesigner = false;
	private Schemata schemata;

	public SchemaDesignerEditPartFactory(Schemata schemata, boolean isDesigner) {
		this.isDesigner = isDesigner;
		this.schemata = schemata;
	}

	public EditPart createEditPart(EditPart parentPart, Object model) {
		EditPart editPart = null;
		if (model instanceof Schemata) {
			editPart = new SchemaDesignerEditPart((Schemata) model, isDesigner);
		}
		if (model instanceof SdTableNode) {
			editPart = new TableNodeEditPart(schemata, (SdTableNode) model,
					isDesigner);
		}
		if (model instanceof SdColumnNode) {
			editPart = new ColumnNodeEditPart(schemata, (SdColumnNode) model,
					isDesigner);
		}
		if (model instanceof WaitingNode) {
			editPart = new WaitingNodeEditPart((WaitingNode) model);
		}
		if (model instanceof Relationship) {
			editPart = new RelationshipEditPart(schemata, (Relationship) model);
		}
		if (editPart != null) {
			return editPart;
		} else {
			throw new IllegalArgumentException(model.toString());
		}
	}
}

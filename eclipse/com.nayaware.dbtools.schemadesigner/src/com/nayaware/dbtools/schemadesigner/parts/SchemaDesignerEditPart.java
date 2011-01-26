
package com.nayaware.dbtools.schemadesigner.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.WaitingNode;
import com.nayaware.dbtools.schemadesigner.figures.SchemaDesignerFigure;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.schemadesigner.policies.SchemaDesignerLayoutEditPolicy;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Content pane of the Schema Diagram
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SchemaDesignerEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private static final int X_INCREMENT = 50;
	private static final int Y_INCREMENT = 50;

	private boolean isDesigner;

	public SchemaDesignerEditPart(Schemata schemata, boolean isDesigner) {
		setModel(schemata);
		this.isDesigner = isDesigner;
		schemata.addPropertyChangeListener(this);
	}

	@Override
	protected IFigure createFigure() {
		Figure schemaDiagramFigure = new SchemaDesignerFigure();

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(
				schemaDiagramFigure));

		return schemaDiagramFigure;

	}

	@Override
	protected void createEditPolicies() {
		// Disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		// installEditPolicy(EditPolicy.CONTAINER_ROLE,
		// new SchemaDesignerContainerEditPolicy());
		// Handles constraint changes (e.g. moving and/or resizing) of model
		// elements and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new SchemaDesignerLayoutEditPolicy((Schemata) getModel()));
	}

	@Override
	protected List<AbstractNode> getModelChildren() {
		Schemata schemata = (Schemata) getModel();
		List<AbstractNode> tableNodeList = schemata.getTableNodes();
		if (!isDesigner) {
			int xLoc = 50;
			int yLoc = 50;
			for (AbstractNode node : tableNodeList) {
				if (node instanceof SdTableNode) {
					SdTableNode tableNode = (SdTableNode) node;
					tableNode.setLocation(new Point(xLoc, yLoc));
					int width = tableNode.getSize().width;
					int height = tableNode.getSize().height;
					xLoc += (width + X_INCREMENT);
					if (xLoc > 600) {
						xLoc = 50;
						yLoc += height + Y_INCREMENT;
					}
				} else if (node instanceof WaitingNode) {
					WaitingNode waitingNode = (WaitingNode) node;
					waitingNode.setLocation(new Point(100, 100));
				}

			}
		} else {
			if ((tableNodeList == null) || tableNodeList.isEmpty()) {
				StringBuffer messageBuffer = new StringBuffer();
				messageBuffer.append(Messages.getString("SchemaDesignerEditPart.0")); //$NON-NLS-1$
				messageBuffer
						.append(Messages.getString("SchemaDesignerEditPart.1")); //$NON-NLS-1$
				messageBuffer
						.append(Messages.getString("SchemaDesignerEditPart.2")); //$NON-NLS-1$
				messageBuffer
						.append(Messages.getString("SchemaDesignerEditPart.3")); //$NON-NLS-1$
				WaitingNode waitingNode = new WaitingNode();
				waitingNode.setDisplayName(messageBuffer.toString());
				waitingNode.setImageKey(ImageUtils.INFORMATION);
				waitingNode.setLocation(new Point(25, 100));
				return Arrays.asList(new AbstractNode[] { waitingNode });
			}
		}
		return tableNodeList;
	}

	// public void addNotify(){
	// super.addNotify();
	// Schemata schemata = (Schemata) getModel();
	// schemata.setSelected(true);
	// }

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		Schemata schemata = (Schemata) getModel();
		if (value != EditPart.SELECTED_NONE) {
			schemata.setSelected(true);
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (Schemata.TABLE_NODES_MODIFIED.equals(property)) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					refresh();
				}
			});
		}
	}
}

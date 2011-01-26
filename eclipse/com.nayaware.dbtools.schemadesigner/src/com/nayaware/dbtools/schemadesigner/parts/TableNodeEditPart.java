
package com.nayaware.dbtools.schemadesigner.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Display;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.schemadesigner.figures.TableNodeFigure;
import com.nayaware.dbtools.schemadesigner.model.Schemata;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.schemadesigner.policies.TableNodeComponentEditPolicy;
import com.nayaware.dbtools.schemadesigner.policies.TableNodeDirectEditPolicy;
import com.nayaware.dbtools.schemadesigner.policies.TableNodeLayoutEditPolicy;

/**
 * Edit part for the table node. It listens to the table node and refreshes
 * itself when a column node is added to the table node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class TableNodeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	private boolean isDesigner = false;

	private DirectEditManager directManager;
	private Schemata schemata;

	public TableNodeEditPart(Schemata schemata, SdTableNode sdTableNode,
			boolean isDesigner) {
		setModel(sdTableNode);
		sdTableNode.addPropertyChangeListener(this);
		this.isDesigner = isDesigner;
		this.schemata = schemata;
	}

	@Override
	protected IFigure createFigure() {
		return new TableNodeFigure((SdTableNode) getModel());
	}

	@Override
	protected void refreshVisuals() {
		TableNodeFigure tableFigure = (TableNodeFigure) getFigure();
		SchemaDesignerEditPart parent = (SchemaDesignerEditPart) getParent();
		SdTableNode tableNode = (SdTableNode) getModel();
		Point location = tableFigure.getLocation();
		Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
		if (tableNode.getLocation() != null) {
			constraint.setLocation(tableNode.getLocation());
		}
		if (tableNode.getSize() != null) {
			constraint.setSize(tableNode.getSize());
		}
		parent.setLayoutConstraint(this, tableFigure, constraint);
	}

	@Override
	protected List<AbstractNode> getModelChildren() {
		SdTableNode tableNode = (SdTableNode) getModel();
		List<AbstractNode> children = tableNode
				.getSdColumnNodeList(!isDesigner);
		if (children == null) {
			children = new ArrayList<AbstractNode>();
		}
		return children;
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	@Override
	public IFigure getContentPane() {
		TableNodeFigure figure = (TableNodeFigure) getFigure();
		return figure.getContentPane();
	}

	@Override
	protected void createEditPolicies() {
		if (isDesigner) {
			installEditPolicy(EditPolicy.LAYOUT_ROLE,
					new TableNodeLayoutEditPolicy());
			installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
					new TableNodeDirectEditPolicy());
		}

		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new TableNodeComponentEditPolicy());
		// installEditPolicy(EditPolicy.CONTAINER_ROLE, new
		// TableNodeContainerEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				TableNodeFigure tableFigure = (TableNodeFigure) getFigure();
				tableFigure.refreshTitle();
				refresh();
			}
		});
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		TableNodeFigure tableFigure = (TableNodeFigure) getFigure();
		if (value != EditPart.SELECTED_NONE) {
			tableFigure.setSelected(true);
		} else {
			tableFigure.setSelected(false);
		}
		schemata.setSelectedTableNode((SdTableNode) getModel());
	}

	@Override
	public void performRequest(Request req) {
		if (isDesigner) {
			if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)
					|| req.getType().equals(RequestConstants.REQ_OPEN)) {
				performDirectEdit();
				return;
			}
		}
		super.performRequest(req);
	}

	private void performDirectEdit() {
		if (directManager == null) {
			directManager = new TableNodeDirectEditManager(this);
		}
		directManager.show();
	}
}

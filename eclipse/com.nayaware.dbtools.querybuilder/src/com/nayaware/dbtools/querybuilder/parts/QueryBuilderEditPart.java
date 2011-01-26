
package com.nayaware.dbtools.querybuilder.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.WaitingNode;
import com.nayaware.dbtools.querybuilder.figures.QueryBuilderFigure;
import com.nayaware.dbtools.querybuilder.model.QueryData;
import com.nayaware.dbtools.querybuilder.policies.QueryBuilderLayoutEditPolicy;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * The Content pane of the Schema Diagram
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QueryBuilderEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	public QueryBuilderEditPart(QueryData query) {
		setModel(query);
		query.addPropertyChangeListener(this);
	}

	@Override
	protected IFigure createFigure() {
		Figure queryBuilderFigure = new QueryBuilderFigure();

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(
				queryBuilderFigure));

		return queryBuilderFigure;

	}

	@Override
	protected void createEditPolicies() {
		// Disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		// Handles constraint changes (e.g. moving and/or resizing) of model
		// elements and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new QueryBuilderLayoutEditPolicy((QueryData) getModel()));
	}

	@Override
	protected List<AbstractNode> getModelChildren() {
		QueryData query = (QueryData) getModel();
		List<AbstractNode> tableNodes = query.getTableNodes();
		if ((tableNodes == null) || (tableNodes.isEmpty())) {
			WaitingNode waitingNode = new WaitingNode();
			waitingNode
					.setDisplayName(Messages.getString("QueryBuilderEditPart.0")); //$NON-NLS-1$
			waitingNode.setImageKey(ImageUtils.INFORMATION);
			return Arrays.asList(new AbstractNode[] { waitingNode });
		}
		return tableNodes;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if (QueryData.TABLE_NODE_ADD.equals(property)
				|| QueryData.TABLE_NODE_REMOVE.equals(property)) {
			refresh();
		}
	}
}

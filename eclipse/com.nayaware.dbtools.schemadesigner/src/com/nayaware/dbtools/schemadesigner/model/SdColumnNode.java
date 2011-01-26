
package com.nayaware.dbtools.schemadesigner.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ColumnNode;

/**
 * Column Node wrapper for Schema Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SdColumnNode extends AbstractNode {

	public static final String COLUMN_MODIFIED = "columnModified"; //$NON-NLS-1$
	public static final String RELATIONSHIP_ADDED = "relationshipAdded"; //$NON-NLS-1$
	public static final String RELATIONSHIP_REMOVED = "relationshipRemoved"; //$NON-NLS-1$

	protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	private List<Relationship> sourceRelationships = new ArrayList<Relationship>();
	private List<Relationship> targetRelationships = new ArrayList<Relationship>();

	private ColumnNode columnNode;

	private SdTableNode parent;

	public SdColumnNode(ColumnNode columnNode) {
		super(columnNode.getDatbaseObject());
		this.columnNode = columnNode;
	}

	public ColumnNode getColumnNode() {
		return columnNode;
	}

	public void setColumnNode(ColumnNode columnNode) {
		this.columnNode = columnNode;
	}

	@Override
	public SdTableNode getParent() {
		return parent;
	}

	public void setParent(SdTableNode parent) {
		this.parent = parent;
	}

	public List<Relationship> getSourceRelationships() {
		return sourceRelationships;
	}

	public void setSourceRelationships(List<Relationship> sourceRelationships) {
		this.sourceRelationships = sourceRelationships;
	}

	public List<Relationship> getTargetRelationships() {
		return targetRelationships;
	}

	public void setTargetRelationships(List<Relationship> targetJoins) {
		this.targetRelationships = targetJoins;
	}

	public void addRelationship(Relationship relationship) {
		(getParent()).addRelationshipColumnNode(this);
		if (relationship.getSource() == this) {
			sourceRelationships.add(relationship);
			firePropertyChange(RELATIONSHIP_ADDED, null, relationship);
		} else if (relationship.getTarget() == this) {
			targetRelationships.add(relationship);
			firePropertyChange(RELATIONSHIP_ADDED, null, relationship);
		}
	}

	public void removeRelationship(Relationship relationship) {
		(getParent()).removeRelationshipColumnNode(this);
		if (relationship.getSource() == this) {
			this.getColumnNode().getColumn().removeForeignKeyReference(
					relationship.getForeignKeyReference());
			sourceRelationships.remove(relationship);
			firePropertyChange(RELATIONSHIP_REMOVED, null, relationship);
		} else if (relationship.getTarget() == this) {
			targetRelationships.remove(relationship);
			firePropertyChange(RELATIONSHIP_REMOVED, null, relationship);
		}
	}

	public void fireColumnModifiedEvent() {
		firePropertyChange(COLUMN_MODIFIED, null, this);
	}

}

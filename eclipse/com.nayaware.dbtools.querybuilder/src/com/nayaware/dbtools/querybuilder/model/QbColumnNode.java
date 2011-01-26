
package com.nayaware.dbtools.querybuilder.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.nayaware.dbtools.nodes.AbstractNode;
import com.nayaware.dbtools.nodes.ColumnNode;

/**
 * Column Node wrapper for query builder
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbColumnNode extends AbstractNode {

	public final static String SOURCE_JOIN = "sourceJoin"; //$NON-NLS-1$
	public final static String TARGET_JOIN = "targetJoin"; //$NON-NLS-1$

	protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	private boolean selected = false;

	private List<Join> sourceJoins = new ArrayList<Join>();
	private List<Join> targetJoins = new ArrayList<Join>();

	private ColumnNode columnNode;

	private QbTableNode parent;

	private String sortCriteria = "NONE"; //$NON-NLS-1$
	private String aggregateFunctionType = "NONE"; //$NON-NLS-1$
	private String criteria = ""; //$NON-NLS-1$
	private String orFlag="";
	private int sortOrder = 0;
	private boolean usedForGroupBy = false;
	
	private String  whereClauseText = "<NONE>";

	public QbColumnNode(ColumnNode columnNode) {
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
	public QbTableNode getParent() {
		return parent;
	}

	public void setParent(QbTableNode parent) {
		this.parent = parent;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			// Will be added only if not added already
			(getParent()).addSelectedColumnNode(this);
		} else {
			// Will be removed only if added already
			(getParent()).removeSelectedColumnNode(this);
		}
		this.firePropertyChange(AbstractNode.NODE_MODIFIED, null, this);
	}

	public String getSortCriteria() {
		return sortCriteria;
	}

	public void setSortCriteria(String sortCriteria) {
		this.sortCriteria = sortCriteria;
	}

	public String getAggregateFunctionType() {
		return aggregateFunctionType;
	}

	public void setAggregateFunctionType(String aggregateFunctionType) {
		this.aggregateFunctionType = aggregateFunctionType;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getOrFlag() {
		return orFlag;
	}

	public void setOrFlag(String orFlag) {
		this.orFlag = orFlag;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isUsedForGroupBy() {
		return usedForGroupBy;
	}

	public void setUsedForGroupBy(boolean usedForGroupBy) {
		this.usedForGroupBy = usedForGroupBy;
	}

	public List<Join> getSourceJoins() {
		return sourceJoins;
	}

	public void setSourceJoins(List<Join> sourceJoins) {
		this.sourceJoins = sourceJoins;
	}

	public List<Join> getTargetJoins() {
		return targetJoins;
	}

	public void setTargetJoins(List<Join> targetJoins) {
		this.targetJoins = targetJoins;
	}

	public void addJoin(Join join) {
		(getParent()).addJoinedColumnNode(this);
		if (join.getSource() == this) {
			sourceJoins.add(join);
			firePropertyChange(SOURCE_JOIN, null, join);
		} else if (join.getTarget() == this) {
			targetJoins.add(join);
			firePropertyChange(TARGET_JOIN, null, join);
		}
	}

	public void removeJoin(Join join) {
		(getParent()).removeJoinedColumnNode(this);
		if (join.getSource() == this) {
			sourceJoins.remove(join);
			firePropertyChange(SOURCE_JOIN, null, join);
		} else if (join.getTarget() == this) {
			targetJoins.remove(join);
			firePropertyChange(TARGET_JOIN, null, join);
		}
	}

	@Override
	public String toString() {
		return Messages.getString("QbColumnNode.5") + getName() + Messages.getString("QbColumnNode.6"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void setWhereClauseText(String whereClauseText) {
		this.whereClauseText = whereClauseText;
	}

	public String getWhereClauseText() {
		return whereClauseText;
	}
}

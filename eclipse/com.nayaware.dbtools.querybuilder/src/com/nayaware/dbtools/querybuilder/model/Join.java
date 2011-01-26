
package com.nayaware.dbtools.querybuilder.model;

/**
 * Model to hold the join related data
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class Join {

	public static final int NONE = -1;
	public static final int INNER = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	private QbColumnNode source;
	private QbColumnNode target;
	private int joinType; // INNER, LEFT, RIGHT

	public Join(QbColumnNode source, QbColumnNode target, int joinType) {
		this.source = source;
		this.target = target;
		this.joinType = joinType;
	}

	public QbColumnNode getSource() {
		return source;
	}

	public void setSource(QbColumnNode source) {
		this.source = source;
	}

	public QbColumnNode getTarget() {
		return target;
	}

	public void setTarget(QbColumnNode target) {
		this.target = target;
	}

	public int getType() {
		return joinType;
	}

	public void setType(int type) {
		this.joinType = type;
	}
}

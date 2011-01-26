
package com.nayaware.dbtools.schemadesigner.model;

import com.nayaware.dbtools.api.IColumn.IForeignKeyReference;

/**
 * @author Winston Prakash
 * @version 1.0
 */
public class Relationship {
	private SdColumnNode source;
	private SdColumnNode target;

	IForeignKeyReference foreignKeyReference;

	public IForeignKeyReference getForeignKeyReference() {
		return foreignKeyReference;
	}

	public void setForeignKeyReference(IForeignKeyReference foreignKeyReference) {
		this.foreignKeyReference = foreignKeyReference;
	}

	public Relationship(SdColumnNode source, SdColumnNode target) {
		this.source = source;
		this.target = target;
	}

	public SdColumnNode getSource() {
		return source;
	}

	public void setSource(SdColumnNode source) {
		this.source = source;
	}

	public SdColumnNode getTarget() {
		return target;
	}

	public void setTarget(SdColumnNode target) {
		this.target = target;
	}
}

package com.jfxtools.database.model;

import com.jfxtools.database.api.IDatabaseInfo;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.api.IView;

/**
 * Model for DB Explorer View Node
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class View extends Table implements IView {

	public View(IDatabaseInfo dbMetadata, ISchema schema, String name) {
		super(dbMetadata, schema, name);
	}
}

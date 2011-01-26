
package com.nayaware.dbtools.model;

import com.nayaware.dbtools.api.IDatabaseInfo;
import com.nayaware.dbtools.api.ISchema;
import com.nayaware.dbtools.api.IView;

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

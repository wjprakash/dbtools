
package com.nayaware.dbtools.nodes;

import com.nayaware.dbtools.model.AbstractDatabaseObject;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * This Node acts as a place holder until the actual children are fetched
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public final class WaitingNode extends AbstractNode {

	public WaitingNode() {
		super(new AbstractDatabaseObject(null, "waitingNode"), true); //$NON-NLS-1$
		setImageKey(ImageUtils.BUSY);
		this.setDisplayName(Messages.getString("WaitingNode.1")); //$NON-NLS-1$
	}
}

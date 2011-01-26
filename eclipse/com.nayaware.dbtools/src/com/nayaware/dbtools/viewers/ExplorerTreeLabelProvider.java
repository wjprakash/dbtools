
package com.nayaware.dbtools.viewers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.nayaware.dbtools.nodes.AbstractNode;

/**
 * Provides Labeling information for the explorer tree viewer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
final class ExplorerTreeLabelProvider extends LabelProvider {
	@Override
	public String getText(Object obj) {
		if (obj instanceof AbstractNode) {
			return ((AbstractNode) obj).getDisplayName();
		} else {
			return obj.toString();
		}
	}

	@Override
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof AbstractNode) {
			return ((AbstractNode) obj).getIcon();
		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					imageKey);
		}
	}
}


package com.nayaware.dbtools.querybuilder.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Source anchor for the Query builder Join connection
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class QbJoinSourceAnchor extends AbstractConnectionAnchor {

	public QbJoinSourceAnchor() {
		super();
	}

	public QbJoinSourceAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point reference) {

		IFigure parentFigure = getOwner().getParent();

		while (!(parentFigure instanceof QbTableNodeFigure)) {
			parentFigure = parentFigure.getParent();
		}

		Rectangle parentRect = parentFigure.getBounds().getCopy();

		Rectangle ownerRect = getOwner().getBounds().getCopy();
		getOwner().translateToAbsolute(ownerRect);

		int off = ownerRect.height / 2;
		Point anchorPoint;
		if (ownerRect.contains(reference) || ownerRect.x > reference.x) {
			anchorPoint = ownerRect.getTopLeft().translate(0, off);
			if (anchorPoint.y < parentRect.getTopLeft().y) {
				anchorPoint.y = parentRect.getTopLeft().y;
			} else if (anchorPoint.y > parentRect.getBottomLeft().y) {
				anchorPoint.y = parentRect.getBottomLeft().y;
			}
			anchorPoint.x = parentRect.getBottomLeft().x;
		} else {
			anchorPoint = ownerRect.getTopRight().translate(0, off);
			if (anchorPoint.y > parentRect.getBottomRight().y) {
				anchorPoint.y = parentRect.getBottomRight().y;
			} else if (anchorPoint.y < parentRect.getTopRight().y) {
				anchorPoint.y = parentRect.getTopRight().y;
			}
			anchorPoint.x = parentRect.getBottomRight().x;
		}

		return anchorPoint;
	}

}

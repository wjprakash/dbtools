/*
 * ColumnTreeNode.fx
 *
 * Created on Jul 31, 2009, 8:39:09 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.resources.Images;
import com.jfxtools.database.model.DatabaseObject;

/**
 * Node represents the Table Column
 * @author Winston Prakash
 */

public class WaitTreeNode extends AbstractTreeNode{
    package override var leafNode = true;
    postinit{
        leaf = true;
        icon = Images.wait;
        dataObject = new DatabaseObject(null, "Loading...");
    }
}

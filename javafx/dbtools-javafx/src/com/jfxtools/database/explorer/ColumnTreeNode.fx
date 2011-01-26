/*
 * ColumnTreeNode.fx
 *
 * Created on Jul 31, 2009, 8:39:09 PM
 */

package com.jfxtools.database.explorer;


import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;

/**
 * Node represents the Table Column
 * @author Winston Prakash
 */

public class ColumnTreeNode extends AbstractTreeNode{
    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Modify Column.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Delete Column.."
                action: function(){
                    showTbdMessage();
                }
            }
        ]
    };
    package override var leafNode = true;
    postinit{
        leaf = true;
        var column = dataObject as IColumn;
        if (column.isPrimaryKey()){
            icon = Images.columnPrimaryKey;
        }else if (column.isForeignKey()){
            icon = Images.columnForeignKey;
        }else if (column.isIndexKey()){
            icon = Images.columnIndexKey;
        }else{
            icon = Images.column;
        }
    }
}

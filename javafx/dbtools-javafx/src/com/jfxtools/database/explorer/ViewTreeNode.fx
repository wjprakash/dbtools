/*
 * ViewTreeNode.fx
 *
 * Created on Jul 31, 2009, 7:54:22 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.api.ITable;
import com.jfxtools.database.editors.table.TableDataView;
import com.jfxtools.database.explorer.ColumnTreeNode;
import com.jfxtools.database.util.AsyncTask;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;

/**
 * Table Tree Node
 * @author Winston Prakash
 */
public class ViewTreeNode extends AbstractTreeNode{

    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Refresh"
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "View Data.."
                action: function(){
                    onDoubleClick()
                }
            }
            PopupMenuItem {
                text: "Modify View.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Drop View.."
                action: function(){
                    showTbdMessage();
                }
            }
        ]
    };

    postinit{
        icon = Images.table;
    }

    public override function onDoubleClick():Void{
         var table = dataObject as ITable;
         var tableData = table.getData();
         var tableDataView = TableDataView{
            tableData: tableData
         }
         appContext.viewPanel.addTab(tableDataView);
    }

    public override function refresh():Void{
       var table = dataObject as ITable;
       var async = AsyncTask {
            action: function():Void {
                table.refresh();
            }
            onFinished: function() {
               childNodes = for (column in table.getColumnList()){
                  ColumnTreeNode{
                    appContext: appContext
                    dataObject: column
                  }
               }
               refreshed = true;
            }
        };
        async.start();
    }
}

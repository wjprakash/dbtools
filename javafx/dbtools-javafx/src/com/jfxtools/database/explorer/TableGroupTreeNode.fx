/*
 * TableGroupTreeNode.fx
 *
 * Created on Jul 31, 2009, 1:45:44 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.util.AsyncTask;

import com.jfxtools.database.schemadesigner.SchemaDiagramViewer;

/**
 * The Table Group Node
 * @author Winston Prakash
 */

public class TableGroupTreeNode extends AbstractTreeNode{

    public var connectionManger:ConnectionManager;

    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Create Table.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "View Entity Relationship Diagram.."
                action: function(){
                    onDoubleClick();
                }
            }
            PopupMenuItem {
                text: "Open Schema Designer.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Open SQL Editor.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Open Query Builder.."
                action: function(){
                    showTbdMessage();
                }
            }
        ]
    };

    postinit{
        displayName = "Tables";
        icon = Images.tableGroup;
    }

    public override function onDoubleClick():Void{
         var schema = dataObject as ISchema;
         var schemaDiagramViewer = SchemaDiagramViewer {
            schema: schema
         }
        appContext.viewPanel.addTab(schemaDiagramViewer);
    }

    public override function refresh():Void{
       var schema = dataObject as ISchema;
       var async = AsyncTask {
            action: function():Void {
                schema.refresh();
            }
            onFinished: function() {
               childNodes = for (table in schema.getTableList()){
                    TableTreeNode{
                        appContext: appContext
                        dataObject: table
                    }
                }
                refreshed = true;
            }
        };
        async.start();
    }
}

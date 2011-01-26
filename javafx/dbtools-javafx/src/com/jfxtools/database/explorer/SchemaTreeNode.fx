/*
 * SchemaTreeNode.fx
 *
 * Created on Jul 31, 2009, 7:54:22 PM
 */

package com.jfxtools.database.explorer;


import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.schemadesigner.SchemaDiagramViewer;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;

/**
 * Schema Tree Node
 * @author Winston Prakash
 */
public class SchemaTreeNode extends AbstractTreeNode{
    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Create Table.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Create View.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Drop Database.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "Migrate Database.."
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
        icon = Images.schema;
    }

    public override function onDoubleClick():Void{
         var schema = dataObject as ISchema;
         var schemaDiagramViewer = SchemaDiagramViewer {
            schema: schema
         }
        appContext.viewPanel.addTab(schemaDiagramViewer);
    }

    public override function refresh():Void{

       childNodes = [
            TableGroupTreeNode{
                appContext: appContext
                dataObject: dataObject
            }
            ViewGroupTreeNode{
                appContext: appContext
                dataObject: dataObject
            }
       ];

       refreshed = true;
    }
}

/*
 * ViewGroupTreeNode.fx
 *
 * Created on Jul 31, 2009, 1:45:44 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.model.DatabaseObject;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;
import com.jfxtools.database.api.ISchema;
import com.jfxtools.database.util.AsyncTask;


/**
 * The View Group Node
 * @author Winston Prakash
 */

public class ViewGroupTreeNode extends AbstractTreeNode{

    public var connectionManger:ConnectionManager;

    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Create View.."
                action: function(){
                    showTbdMessage();
                }
            }
            PopupMenuItem {
                text: "View Entity Relationship Diagram.."
                action: function(){
                    showTbdMessage();
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
        displayName = "Views";
        icon = Images.viewGroup;
    }

    public override function refresh():Void{
       var schema = dataObject as ISchema;
       var async = AsyncTask {
            action: function():Void {
                schema.refresh();
            }
            onFinished: function() {
               childNodes = for (view in schema.getViewList()){
                    ViewTreeNode{
                        appContext: appContext
                        dataObject: view
                    }
                }
                refreshed = true;
            }
        };
        async.start();
    }
}

/*
 * RootTreeNode.fx
 *
 * Created on Jul 31, 2009, 1:45:44 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.model.DatabaseObject;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.resources.Images;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;
import com.jfxtools.database.dialogs.AddConnectionDialog;


/**
 * The root node of the explorer
 * @author Winston Prakash
 */

public class RootTreeNode extends AbstractTreeNode{

    public override var popupMenu = PopupMenu {
        menuItems: [
            PopupMenuItem {
                text: "Add Connection.."
                action: function(){
                    var addConnectionDialog:AddConnectionDialog = AddConnectionDialog {
                                appContext: appContext
                                callbackFunction: function () {
                                    if (addConnectionDialog.result){
                                        var databaseInfo = new DatabaseInfo(addConnectionDialog.connectionConfig);
                                        var newConnection = ConnectionTreeNode{
                                            appContext: appContext
                                            dataObject: new Connection(databaseInfo)
                                        }
                                        insert newConnection into childNodes;
                                    }
                                }
                            };
                    addConnectionDialog.show();
                }
            }
            PopupMenuItem {
                text: "Migrate Database.."
                action: function(){
                    showTbdMessage();
                }
            }
        ]
    };

    public var connectionManger:ConnectionManager;

    postinit{
        dataObject = new DatabaseObject(null, "Databases Connections");
        icon = Images.databaseGroup;
        refresh();
        
    }

    public override function refresh():Void{
        childNodes = for (connectionConf in connectionManger.getConnectionConfigList()){
            var databaseInfo = new DatabaseInfo(connectionConf);
            var connection = new Connection(databaseInfo);
            ConnectionTreeNode{
                appContext: appContext
                dataObject: connection
            }
        }
        refreshed = true;
    }
}

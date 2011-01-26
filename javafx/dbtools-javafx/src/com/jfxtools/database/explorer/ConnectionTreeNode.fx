/*
 * ConnectionTreeNode.fx
 *
 * Created on Jul 31, 2009, 7:19:01 PM
 */
package com.jfxtools.database.explorer;

import com.jfxtools.database.api.IConnection;

import com.jfxtools.database.resources.Images;
import com.jfxtools.database.util.AsyncTask;
import com.jfxtools.database.controls.PopupMenu;
import com.jfxtools.database.controls.PopupMenuItem;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.dialogs.AddConnectionDialog;
import java.lang.Exception;

/**
 * Connection Tree node
 * @author Winston Prakash
 */
public class ConnectionTreeNode extends AbstractTreeNode {

    public override var popupMenu = PopupMenu {
                menuItems: [
                    PopupMenuItem {
                        text: "Refresh"
                        action: function () {
                            refresh();
                        }
                    }
                    PopupMenuItem {
                        text: "Edit Connection.."
                        action: function () {
                            var connection = dataObject as IConnection;
                            var addConnectionDialog: AddConnectionDialog = AddConnectionDialog {
                                        appContext: appContext
                                        editing: true
                                        connectionConfig: connection.getDatabaseInfo().getConnectionConfig()
                                        callbackFunction: function () {
                                            if (addConnectionDialog.result) {
                                                var modifiedConnectionConfig = addConnectionDialog.connectionConfig;
                                                ConnectionManager.getInstance().updateConnectionConfig(modifiedConnectionConfig);
                                                displayName = modifiedConnectionConfig.getName();
                                                var connectionManager = ConnectionManager.getInstance();
                                                try{
                                                    var dbConnection = connectionManager.createConnection(modifiedConnectionConfig);
                                                    connection.getDatabaseInfo().setConnection(dbConnection);
                                                    refresh();
                                                }catch (ex:Exception){
                                                    ex.printStackTrace();
                                                    return;
                                                }
                                            }
                                        }
                                    };
                            addConnectionDialog.show();
                        }
                    }
                    PopupMenuItem {
                        text: "Remove Connection.."
                        action: function () {
                            var connection = dataObject as IConnection;
                            ConnectionManager.getInstance().removeConnectionConfig(connection.getDatabaseInfo().getConnectionConfig());
                            delete this from parentNode.childNodes;
                        }
                    }
                    PopupMenuItem {
                        text: "Create Database.."
                        action: function () {
                            showTbdMessage();
                        }
                    }
                ]
            };

    postinit {
        icon = Images.database;
    }

    public override function refresh(): Void {
        var connection = dataObject as IConnection;
        var async = AsyncTask {
                    action: function (): Void {
                        connection.refresh();
                    }
                    onFinished: function () {
                        childNodes = for (schema in connection.getSchemaList()) {
                            SchemaTreeNode {
                                appContext: appContext
                                dataObject: schema
                            }
                        }
                        refreshed = true;
                    }
                };
        async.start();
    }
}

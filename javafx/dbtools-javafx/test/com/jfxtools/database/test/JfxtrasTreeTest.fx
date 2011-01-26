/*
 * JfxtrasTreeTest.fx
 *
 * Created on Sep 28, 2009, 9:58:13 AM
 */

package com.jfxtools.database.test;

import javafx.stage.Stage;
import javafx.scene.Scene;

import org.jfxtras.scene.control.Tree;
import org.jfxtras.scene.control.TreeNode;
import org.jfxtras.scene.control.data.DataProvider;
import org.jfxtras.scene.control.data.DataRow;

import javafx.scene.Group;

import javafx.reflect.FXLocal;

import com.jfxtools.database.api.IDatabaseObject;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.model.DatabaseObject;
import com.jfxtools.database.tests.ConnectionUtils;

import com.jfxtools.database.api.IConnection;

import com.jfxtools.database.api.ISchema;

import com.jfxtools.database.api.ITable;

/**
 * @author Winston Prakash
 */

public abstract class AbstractTreeNode extends TreeNode{

      public-read var displayName;
      public var content:IDatabaseObject on replace{
          if (content != null){
              item = content.getName();
              children = getChildren();
          }
      }

      public abstract function getChildren():TreeNode[];
}

public class RootTreeNode extends AbstractTreeNode{
    public var connectionManger:ConnectionManager;
    postinit{
        content = new DatabaseObject(null, "Database Explorer");
    }

    public override function getChildren():TreeNode[]{
        for (connectionConf in connectionManger.getConnectionConfigList()){
            var databaseInfo = new DatabaseInfo(connectionConf);
            var connection = new Connection(databaseInfo);
            ConnectionTreeNode{
                content: connection
            }
        }
    }
}

public class ConnectionTreeNode extends AbstractTreeNode{
    public override function getChildren():TreeNode[]{
        var connection = content as IConnection;
        connection.refresh();
        for (schema in connection.getSchemaList()){
            SchemaTreeNode{
                content: schema
            }
        }
    }
}

public class SchemaTreeNode extends AbstractTreeNode{
    public override function getChildren():TreeNode[]{
        var schema = content as ISchema;
        schema.refresh();
        children = for (table in schema.getTableList()){
            TableTreeNode{
                content: table
            }
        }
    }
}

public class TableTreeNode extends AbstractTreeNode{
    public override function getChildren():TreeNode[]{
       var table = content as ITable;
       table.refresh();
       children = for (column in table.getColumnList()){
            ColumnTreeNode{
                content: column
            }
        }
    }
}

public class ColumnTreeNode extends AbstractTreeNode{
    public override function getChildren():TreeNode[]{
        return null;
    }
}

class TreeDataProvider extends DataProvider {

    override bound function getRange(rowStart:Integer, rowCount:Integer, cols:String[]):DataRow[] {
        createData();
    }
    override var types = FXLocal.getContext().makeClassRef(String.class);;
    override var rowCount = 1;
    override var columns = "";
    function createData(){
        DataRow {
            override function getData():Object[] {
                var connectionManger = ConnectionManager.getInstance();
                connectionManger.addConnectionConfig(ConnectionUtils.createMySqlConnectionConfig());

                RootTreeNode {
                    connectionManger: connectionManger
                };
            }
        }
    }
}

public function run(){


    var fxTree = Tree {
        rootVisible: true
        width: 460
        height: 460
        onSelection: function(item:TreeNode) {
            println("Selection: {item}");
        }
        onExpand: function(item:TreeNode) {
            println("Expand: {item}");
        }
        onCollapse: function(item:TreeNode) {
            println("Colapse: {item}");
        }
        dataProvider:  TreeDataProvider{}
    }

    Stage {
        title: "Application title"
        width: 500
        height: 500
        scene: Scene {
            content: [
                Group{
                  layoutX: 20
                  layoutY: 20
                  content: fxTree
                }
            ]
        }
    }
}
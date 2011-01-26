/*
 * TableDataView.fx
 *
 * Created on Jul 31, 2009, 2:26:01 PM
 */

package com.jfxtools.database.editors.table;


import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.tests.ConnectionUtils;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.model.Schema;
import com.jfxtools.database.model.TableColumnData;
import com.jfxtools.database.api.ITableData;
import javafx.scene.layout.Panel;

import com.jfxtools.database.controls.Table;
import com.sun.javafx.scene.control.ScrollView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * Table Data View
 * @author winstonp
 */

public class TableDataView extends Panel{

    public-init var tableData:ITableData;

    var fxTable:Table = Table {
        width: bind width
        height: bind height
        tableData: tableData
    }

    var background = Rectangle {
        width: bind width
        height: bind height
        fill: Color.CORAL
        arcHeight: 12
        arcWidth: 12
    }

    var scrollView = ScrollView {
        layoutX: 2
        layoutY: 2
        width: bind width - 4
        height: bind height - 4
        node: fxTable
    }

    postinit {
       content =  [
           background,
           scrollView
       ];
   }
}

public function run() {
    var connectionConfig = ConnectionUtils
    					.createMySqlConnectionConfig();
    var databaseInfo = new DatabaseInfo(connectionConfig);
    var connection = new Connection(databaseInfo);
    connection.refresh();
    var schemas = connection.getSchemaList();
    var schema:Schema;

    for (obj in schemas) {
        schema = obj as Schema;
        if (schema.getName().equals("sakila")){
            break;
        }
    }

    schema.refresh();
    var table:com.jfxtools.database.model.Table;

    var tables = schema.getTableList();
    for (obj in tables) {
        table = obj as com.jfxtools.database.model.Table;
        if (table.getName().equals("customer")){
                println("Found customer");
            break;
        }
    }

    var tableData = table.getData();
    var rowData = tableData.getPageData();

    for (i in [0 .. sizeof rowData - 1]) {
        var colCount = rowData[i].getColumnCount();
        for (j in [0 .. colCount - 1]) {
            var colData = rowData[i].getTableColumnData().get(j) as TableColumnData;
            print("{colData.getName()}:{colData.toString()}  ");
        }
        println("\n");
    }

    Stage {
        title : "Database Tools Test"
        scene: Scene {
            width: 900
            height: 500
            content: [
                 TableDataView{
                    tableData: tableData
                    layoutX: 10
                    layoutY: 10
                    width: 880
                    height: 480
                 }
            ]
        }
    }

}


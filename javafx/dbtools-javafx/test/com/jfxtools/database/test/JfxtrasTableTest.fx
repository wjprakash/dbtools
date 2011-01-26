/*
 * JfxtrasTableTest.fx
 *
 * Created on Sep 25, 2009, 11:08:58 AM
 */

package com.jfxtools.database.test;

import javafx.stage.Stage;
import javafx.scene.Scene;
import org.jfxtras.scene.control.Table;
import org.jfxtras.scene.control.data.ObjectDataProvider;


import javafx.scene.Group;

import org.jfxtras.scene.control.data.DataProvider;

import org.jfxtras.scene.control.data.DataRow;

import javafx.reflect.FXLocal;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.model.Schema;
import com.jfxtools.database.tests.ConnectionUtils;


import com.jfxtools.database.api.ITableData;

import com.jfxtools.database.api.ITableColumnData;

/**
 * @author Winston Prakash
 */
public class TableDataProvider extends DataProvider{
    public-init var tableData:ITableData on replace{
        if (tableData != null){
            rowCount = tableData.getPageSize();
            columns = tableData.getColumnNames();
            types = for (clazz in tableData.getColumnTypes()){
                println(clazz);
                //FXLocal.getContext().makeClassRef(clazz);
                FXLocal.getContext().makeClassRef(String.class);
            }
        }
    }

    override bound function getRange(rowStart:Integer, rowCount:Integer, cols:String[]):DataRow[] {
        createDataRow(rowStart, rowCount, cols);
    }

    function createDataRow(rowStart:Integer, rowCount:Integer, cols:String[]){
        var rowData = tableData.getPageData();
        for (i in [rowStart .. rowStart +  rowCount - 1]) {
            DataRow {
                 override function getData():Object[]{
                    var colCount = rowData[i].getColumnCount();
                    for (j in [0 .. colCount - 1]) {
                        var colData = rowData[i].getTableColumnData().get(j) as ITableColumnData;
                        //colData.getValue();
                        colData.getValueAsString();
                    }
                 }
            }
        }

    }
}

public function run(){

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

    var fxTable:Table = Table {
        width: 1000
        height: 300
        dataProvider:  TableDataProvider{
            tableData: tableData
        }

        rowHeight: 60
        onMouseClicked: function(e) {
            if (e.clickCount == 2) {
                println("selected = {(fxTable.dataProvider as ObjectDataProvider).getItem(fxTable.selectedRow)}");
            }
        }
    }

    Stage {
        title: "Application title"
        width: 1100
        height: 500
        scene: Scene {
            content: [
                Group{
                  layoutX: 20
                  layoutY: 20
                  content: fxTable
                }
            ]
        }
    }

}


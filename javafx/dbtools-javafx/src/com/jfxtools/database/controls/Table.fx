/*
 * Table.fx
 *
 * Created on Feb 3, 2010, 4:32:36 PM
 */
package com.jfxtools.database.controls;

import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;

import com.jfxtools.database.api.ITableData;
import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.editors.table.TableDataView;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.model.Schema;
import com.jfxtools.database.tests.ConnectionUtils;
import javafx.util.Math;

/**
 * Simple Table control
 * @author winstonp
 */
public class Table extends Control{

    public var headerHeight = 20;
    public var rowHeight = 30;

    public-init var tableData:ITableData on replace{
        if (tableData != null){
             var headers:TableColumnHeader[];
             for (columnName in tableData.getColumnNames()){
                var columnHeader = TableColumnHeader{
                    index: indexof columnName
                    title: columnName
                }
                insert columnHeader into headers;
             }
             columnHeaders = headers;
             var rows:TableRow[];
             for (rowData in tableData.getPageData()){
                var row = TableRow {
                    rowData: rowData
                }

                insert row into rows
             }
             tableRows = rows;
        }
    }

    public var selectedRow:TableRow on replace oldSelectedRow{
        oldSelectedRow.selected = false;
        selectedRow.selected = true;
    }

    public var selectedColumn:TableColumn on replace oldSelectedColumn{
        oldSelectedColumn.selected = false;
        selectedColumn.selected = true;
    }

    public var tableRows:TableRow[] on replace oldChildren{
        for (child in oldChildren){
           child.table = null;
        }

        for (child in tableRows){
           child.table = this;
        }
    }

    public-init var columnHeaders:TableColumnHeader[];

    public var onMouseClickedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseDraggedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseEnteredCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseExitedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseMovedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMousePressedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseReleasedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onMouseWheelMovedCell: function(row:TableRow, column:TableColumn, event: MouseEvent):Void;
    public var onKeyReleasedCell: function(row:TableRow, column:TableColumn, event: KeyEvent):Void;
    public var onKeyPressedCell: function(row:TableRow, column:TableColumn, event: KeyEvent):Void;
    public var onKeyTypedCell: function(row:TableRow, column:TableColumn, event: KeyEvent):Void;

    postinit {
        skin = TableSkin{};
    }

    override function getPrefHeight(height) {
        var prefH = 0.;
        for (row in tableRows) {
            prefH += row.getPrefHeight(-1);
        }
        return Math.max(prefH, this.height);
    }

    override function getPrefWidth(width) {
        var prefW = 0.;
        for (row in tableRows) {
            prefW = Math.max(prefW, row.getPrefWidth(-1));
        }
        return Math.max(prefW, this.width);
    }
}

function run() {
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
            break;
        }
    }

    var tableData = table.getData();
    var rowData = tableData.getPageData();

    Stage {
        title : "Table Data view test"
        scene: Scene {
            width: 600
            height: 500
            content: [
                 TableDataView{
                    tableData: tableData
                    layoutX: 10
                    layoutY: 10
                    width: 980
                    height: 480
                 }
            ]
        }
    }
}
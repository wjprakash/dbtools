/*
 * TableColumn.fx
 *
 * Created on Feb 9, 2010, 12:52:09 PM
 */

package com.jfxtools.database.controls;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfxtools.database.api.ITableColumnData;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author winstonp
 */

public class TableColumn extends CustomNode, Resizable{
    override public function getPrefWidth (arg0 : Number) : Number {
        return 20;
    }

    override public function getPrefHeight (arg0 : Number) : Number {
        return 75;
    }

    var label:Label = Label { }

     var background = Rectangle {
        opacity: .1
    }
    
    override public var width on replace{
        background.width = width;
        selectionForeground.width = width;
        label.width = width
    }

    override public var height on replace{
        background.height = height;
        selectionForeground.height = height;
        label.layoutY = (height - label.layoutBounds.height)/2
    }

    public var columnData:ITableColumnData on replace{
        if (columnData != null){
            data = columnData.getValueAsString();
        }
    }

    public var index = 1;

    public var tableRow:TableRow;

    public var selected = false on replace{
        background.fill = if (selected) Color.BLUE else Color.TRANSPARENT
    }

    public var data:Object on replace {
        label.text = data.toString();
    }

    public var renderer:TableCellRenderer;
    

    var selectionForeground = Rectangle {
                fill: Color.TRANSPARENT
                onMouseClicked: function (e: MouseEvent) {
                    tableRow.table.onMouseClickedCell(tableRow, this, e);
                    }
                onMouseDragged: function (e: MouseEvent) {
                    tableRow.table.onMouseDraggedCell(tableRow, this, e);
                    }
                onMouseEntered: function (e: MouseEvent) {
                    tableRow.table.onMouseDraggedCell(tableRow, this, e);
                    }
                onMouseExited: function (e: MouseEvent) {
                    tableRow.table.onMouseEnteredCell(tableRow, this, e);
                    }
                onMouseMoved: function (e: MouseEvent) {
                    tableRow.table.onMouseMovedCell(tableRow, this, e);
                    }
                onMousePressed: function (e: MouseEvent) {
                    tableRow.table.onMousePressedCell(tableRow, this, e);
                    tableRow.table.selectedRow = tableRow;
                    tableRow.table.selectedColumn = this;
                }
                onMouseReleased: function (e: MouseEvent) {
                    tableRow.table.onMouseReleasedCell(tableRow, this, e);
                    }
                onMouseWheelMoved: function (e: MouseEvent) {
                    tableRow.table.onMouseWheelMovedCell(tableRow, this, e);
                    }
                onKeyReleased: function (e: KeyEvent) {
                    tableRow.table.onKeyReleasedCell(tableRow, this, e);
                    }
                onKeyPressed: function (e: KeyEvent) {
                    tableRow.table.onKeyPressedCell(tableRow, this, e);
                    }
                onKeyTyped: function (e: KeyEvent) {
                    tableRow.table.onKeyTypedCell(tableRow, this, e);
                    }
            }

    override protected function create () : Node {
        return Group{ content: [background, label, selectionForeground]}
    }
}

function run() {
    Stage {
        scene: Scene {
            width: 300 height: 300
            content:  TableColumn{ width: 75 height: 20 data: "column11"}
        }
    }
}
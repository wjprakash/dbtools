/*
 * TableSkin.fx
 *
 * Created on Feb 3, 2010, 4:47:12 PM
 */
package com.jfxtools.database.controls;

import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Math;
import javafx.scene.layout.Container;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

/**
 * Skin for the Tree
 * @author winstonp
 */
public class TableSkin extends Skin {

    var table = bind control as Table;

    override function contains(localX: Number, localY: Number): Boolean {
        return node.contains(localX, localY);
    }

    override function intersects(localX: Number, localY: Number, localWidth: Number, localHeight: Number): Boolean {
        return node.intersects(localX, localY, localWidth, localHeight);
    }

    var background = Rectangle {
        fill: Color.BLANCHEDALMOND
        //opacity: .1
    }

    override var node = Container {
        content: bind [background, table.columnHeaders, table.tableRows,]
        override function doLayout() {
            var hx = 5;
            for (node in getManaged(table.columnHeaders)){
                var columnHeader = node as TableColumnHeader;
                layoutNode(node, hx, 0, columnHeader.columnWidth, table.headerHeight, HPos.CENTER, VPos.CENTER);
                hx += columnHeader.columnWidth + 5
            }
            var ry = table.headerHeight + 10;
            for (node in getManaged(table.tableRows)) {
                var row = node as TableRow;
                layoutNode(node, 5, ry, hx, table.rowHeight, HPos.CENTER, VPos.CENTER);
                ry += table.rowHeight;
            }
            
            background.width = hx;
            background.height = ry;
        }
    }
}

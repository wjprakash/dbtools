/*
 * TableRow.fx
 *
 * Created on Feb 9, 2010, 12:52:09 PM
 */
package com.jfxtools.database.controls;

import com.jfxtools.database.api.ITableRowData;
import javafx.scene.layout.Container;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author winstonp
 */
public class TableRow extends Container {

    public var rowData: ITableRowData on replace {
                if (rowData != null) {
                    var columns: TableColumn[];
                    for (columnData in rowData.getTableColumnData()) {
                        var column = TableColumn {
                                    columnData: columnData
                                }

                        insert column into columns;
                    }
                    tableColumns = columns;
                }
            }
    public var table: Table;
    public var tableColumns: TableColumn[] on replace oldChildren {
                for (child in oldChildren) {
                    child.tableRow = null;
                }

                for (child in tableColumns) {
                    child.index = indexof child;
                    child.tableRow = this;
                }
            }
    var selectionBackground = Rectangle {
                opacity: .1
            }
    public var selected = false on replace {
                selectionBackground.fill = if (selected) Color.BLUE else Color.TRANSPARENT
            }

    postinit {
        content = [selectionBackground, tableColumns]
    }

    override function doLayout() {
        var hx = 0;
        for (node in getManaged(tableColumns)) {
            var columnHeader = table.columnHeaders[indexof node];
            layoutNode(node, hx, 0, columnHeader.columnWidth, table.rowHeight, HPos.CENTER, VPos.BOTTOM);
            hx += columnHeader.columnWidth + 5
        }
        selectionBackground.width = hx;
        selectionBackground.height = table.rowHeight;
   }

}

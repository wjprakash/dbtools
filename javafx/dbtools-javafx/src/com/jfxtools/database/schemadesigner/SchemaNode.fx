/*
 * TableNode.fx
 *
 * Created on Apr 14, 2009, 11:12:37 PM
 */

package com.jfxtools.database.schemadesigner;
import com.jfxtools.database.model.Table;

import com.jfxtools.database.api.ISchema;

import javafx.scene.layout.Panel;

/**
 * @author Winston Prakash
 */
public class SchemaNode extends Panel{

    var canvas:Canvas = Canvas{
        width:  bind width
        height: bind height
    }

    public var schema:ISchema;

    postinit{
        width = width;
        height = height;
        insert canvas into content;
        schema.refresh();
        var tables = schema.getTableList();
        var tableNodes:TableNode[];
        for (obj in tables) {
            var table = obj as Table;
            var tableNode = TableNode{
                canvas: canvas
                cache: true
                table: table
            }
            insert tableNode into tableNodes
        }
        canvas.tableNodes = tableNodes;
        var x = Canvas.gridWidth;
        var y = Canvas.gridHeight;

        for(node in getManaged(canvas.tableNodes)) {
          canvas.placeNode(node as TableNode, x, y);
          x += TableNode.WIDTH;
          if ( (x + TableNode.WIDTH) > 800){
            x = Canvas.gridWidth;
            y += TableNode.HEIGHT;
          }
        }
    }
}

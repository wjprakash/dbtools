/*
 * TableNode.fx
 *
 * Created on Apr 14, 2009, 11:12:37 PM
 */

package com.jfxtools.database.schemadesigner;
import com.jfxtools.database.api.IColumn;
import com.jfxtools.database.api.ITable;
import com.jfxtools.database.schemadesigner.ColumnNode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.jfxtools.database.resources.Images;

import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Container;
import javafx.util.Math;
import javafx.scene.control.Label;

/**
 * @author Winston Prakash
 */

public def WIDTH = 150.;
public def HEIGHT = 200.;

public class TableNode extends Container{

    public-init var canvas:Canvas;

    var headerHeight = 30.;
    var columnHeight = ColumnNode.HEIGHT;

    var startDragX:Number;
    var startDragY:Number;

    var background = Rectangle {
        fill: Color.web("#ce5c00"),
        arcWidth: 8
        arcHeight: 8
        width: bind width
        height: bind height
    }

    var header = Rectangle {
        height: headerHeight
        width: bind background.width
        fill: Color.web("#a40000")
        blocksMouse: true
        arcWidth: 8
        arcHeight: 8
        onMousePressed: function(e:MouseEvent) {
            bringToFront();
            startDragX = e.screenX - layoutX;
            startDragY = e.screenY - layoutY;
        }

        onMouseDragged: function(e:MouseEvent) {
            var moveX = e.screenX - startDragX;
            var moveY = e.screenY - startDragY;
            canvas.placeNode(this, moveX, moveY);
        }
    }

    var headerClip:Rectangle = Rectangle {
            height: 25,
            width: bind background.width
            layoutY: headerHeight - 5
            stroke: null
            fill: Color.web("#ce5c00")
            effect: InnerShadow {
                    //choke: 0.5
                    //offsetX: 1
                    offsetY: 1
                    radius: 1
                    color: Color.web("#ce5c00")
            }
   }

   var border = Rectangle {
        fill: Color.TRANSPARENT
        arcWidth: 8
        arcHeight: 8
        stroke: Color.WHITESMOKE
        height: bind height
        width: bind width
        //strokeWidth: .6
    }

    var icon = ImageView {
        x: 3;
        y: 5;
        image: Images.table
    }

    var title = Label {
        width: bind width - 25
        cache: true
        layoutX: 25
        textFill: Color.WHITE;
        font: Font {
            size: 16
        };
        textAlignment: TextAlignment.CENTER;
    }

    var columnNodes:ColumnNode[] = [];

    public var table:ITable on replace{
        if (table != null){
            table.refresh();
            var columns = table.getColumnList();
            for (obj in columns) {
                var column = obj as IColumn;
                var columnNode = ColumnNode{
                    cache: true;
                    column: column;
                    text: column.getName();
                }
                insert columnNode into columnNodes;
            }
            title.text = table.getName();
        }
    }

    function bringToFront(){
        //toFront();
    }

    override var content = bind [background, header, headerClip, border, icon, title, columnNodes ];

    public override function doLayout():Void {

       var x = 0;
       var y = headerHeight - 5;

       for(node in getManaged(columnNodes)) {
          positionNode(node, x, y);
          y += getNodePrefHeight(node);
       }
    }

    override function getPrefHeight(height) {
        var prefH = headerHeight;
        for (node in getManaged(columnNodes)) {
            prefH += getNodePrefHeight(node);
        }
        return prefH + 15;
    }

    override function getPrefWidth(width) {
        var prefW = 0.;
        for (node in getManaged(columnNodes)) {
            prefW = Math.max(prefW, getNodePrefWidth(node));
        }
        return prefW + 20;
    }
}

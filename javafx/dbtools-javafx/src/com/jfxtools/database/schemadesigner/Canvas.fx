/*
 * Canvas.fx
 *
 * Created on Nov 16, 2009, 7:21:07 PM
 */

package com.jfxtools.database.schemadesigner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javafx.scene.layout.Panel;

import javafx.scene.input.MouseEvent;

import javafx.scene.Node;
import javafx.util.Math;
import javafx.util.Sequences;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Container;


/**
 * Canvas for placing the table nodes
 * @author Winston Prakash
 */

 public def gridWidth = 25.;
 public def gridHeight = 25.;

public class Canvas extends Container{

    public var tableNodes:TableNode[];

    var dragLine:ConnectionLine = ConnectionLine {
        visible: false
    };

    var background = Rectangle {
        fill: Color.web("#2c0b0b");
    }

    var verticalLines:Line[];

    var horizontalLines:Line[];

    var grid = Group{
        content: bind [
            background,
            verticalLines,
            horizontalLines,
            dragLine,
            tableNodes
        ]
    }

    postinit {
        blocksMouse = true;
        content = [grid];
    }

    public override function getPrefWidth(reqWidth:Number):Number {
        var prefW = 0.;
        for (node in getManaged(tableNodes)) {
            var tableNode = node as TableNode;
            prefW = Math.max(prefW, tableNode.layoutX + tableNode.getPrefWidth(-1));
        }
        return Math.max(prefW, width);
    }
    public override function getPrefHeight(reqHeight:Number):Number {
        var prefH = 0.;
        for (node in getManaged(tableNodes)) {
            var tableNode = node as TableNode;
            prefH = Math.max(prefH, tableNode.layoutY + tableNode.getPrefHeight(-1));
        }
        return Math.max(prefH, height);
    }

    public override function doLayout():Void {
        if ((width > 0) and (height > 0)){
            //resizeContent();
            background.width = getPrefWidth(-1);
            background.height = getPrefHeight(-1);

            refreshGridLines();

            for (node in getManaged(horizontalLines)) {
                var hLine = node as Line;
                hLine.endX = getPrefWidth(-1);
            }

            for (node in getManaged(verticalLines)) {
                var vLine = node as Line;
                vLine.endY = getPrefHeight(-1);
            }
        }
    }

    function refreshGridLines(){
        var reqHorizLines = (getPrefHeight(-1) / gridHeight) as Integer;
        var currentHorizLines = horizontalLines.size();
        if (reqHorizLines < currentHorizLines){
            for (hLine in horizontalLines[reqHorizLines .. currentHorizLines]){
                delete hLine from horizontalLines;
            }
        }else{
            for (i in [currentHorizLines .. reqHorizLines]){
                var hLine = Line {
                    startY: i * gridHeight
                    endY: i * gridHeight
                    strokeWidth: 0.4
                    stroke: Color.GRAY
                }
                insert hLine into horizontalLines;
            }
        }

        var reqVertLines = (getPrefWidth(-1) / gridWidth) as Integer;
        var currentVertLines = verticalLines.size();
        if (reqVertLines < currentVertLines){
            for (vLine in verticalLines[reqVertLines .. currentVertLines]){
                delete vLine from verticalLines;
            }
        }else{
            for (i in [currentVertLines .. reqVertLines]){
                var vLine = Line {
                    startX: i * gridWidth
                    endX: i * gridWidth
                    strokeWidth: 0.4
                    stroke: Color.GRAY
                }
                insert vLine into verticalLines;
            }
        }
    }

    override public var onMousePressed = function(e:MouseEvent):Void {
        dragLine.sx = e.x;
        dragLine.sy = e.y;
    }

    override public var onMouseDragged = function(e:MouseEvent):Void {
        dragLine.ex = e.x;
        dragLine.ey = e.y;
        dragLine.visible = true;
    }

    package function placeNode(node:TableNode, x:Number, y:Number){
        if (Sequences.indexOf(tableNodes, node) == -1){
            insert node into tableNodes;
        }
        var xOffset = Math.floor(x / gridWidth) * gridWidth;
        var yOffset = Math.floor(y / gridHeight) * gridHeight;
        positionNode(node, xOffset, yOffset);
    }
}

public function run() {

    var canvas:Canvas = Canvas {
        layoutX: 20
        layoutY: 20
        width: bind scene.width - 40
        height: bind scene.height - 40
    }

    var scene:Scene = Scene {
        content: canvas
    }

    Stage {
        width: 400
        height: 400
        title: "Canvas Test"
        scene: scene
    }
}

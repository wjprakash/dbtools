/*
 * TableColumnHeader.fx
 *
 * Created on Feb 9, 2010, 12:52:09 PM
 */

package com.jfxtools.database.controls;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Resizable;
import javafx.scene.Group;
import javafx.scene.control.Label;

/**
 * @author winstonp
 */

public class TableColumnHeader extends CustomNode, Resizable{

    override public function getPrefHeight (height : Number) : Number {
         return 20;
    }

    override public function getPrefWidth (height : Number) : Number {

         return columnWidth;
    }

    var label:Label = Label { }

     var background = Rectangle {
        fill: Color.web("#FF0404")
        opacity: .4
    }

     override public var width on replace{
        background.width = width;
        label.width = width
    }

    override public var height on replace{
        background.height = height;
        label.layoutY = (height - label.layoutBounds.height)/2
    }

    public var table:Table;
    
    public-init var index = 1;
    
    public var title = "" on replace{
       label.text = title;
    }

    public var columnWidth = 75;

    override protected function create () : Node {
        return Group{ content: [background, label]}
    }

    postinit {
        width = columnWidth;
        if ((title == null) or ("".equals(title))){
            title = "Column{index}"
        }

    }
}

function run() {
    Stage {
        scene: Scene {
            width: 300 height: 300
            content:  TableColumnHeader{}
        }
    }
}
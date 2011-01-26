/*
 * PopupMenuItem.fx
 *
 * Created on Jan 27, 2010, 9:25:25 AM
 */
package com.jfxtools.database.controls;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Resizable;
import javafx.scene.paint.Color;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Container;
import com.jfxtools.database.AppContext;

/**
 * Popup Menu Item
 * @author Winston Prakash
 */
public class PopupMenuItem extends CustomNode, Resizable, Labeled {

    public var popupMenu:PopupMenu;

    public var action:function();

    public var paddingLeft: Number = 10;
    public var paddingTop: Number = 1;
    public var paddingBottom: Number = 1;
    public var paddingRight: Number = 10;

    public var appContext: AppContext;

    override public var hover on replace{
        if (hover){
            background.fill = Color.web("#ffffff");
        }else{
            background.fill = Color.TRANSPARENT;
        }
    }
   
    var background:Rectangle = Rectangle {
        width: bind width,
        height: bind height,
        opacity: .3
        arcWidth: 9
        arcHeight: 9
        onMousePressed: function (e) {
            popupMenu.hide();
            action();
        }
        fill: Color.TRANSPARENT
    }

    var label = Label {
        layoutX:  paddingLeft
        text: bind text
        width: bind width
        height: bind height
        textFill: Color.web("#ffffff")
    }

    var node = Group{
        layoutX:0
        layoutY:0
        translateX:0
        translateY:0
        content: bind [
           background,
           label
        ]
    };

    postinit {
        if (not isInitialized(width)) then width = getPrefWidth(-1);
        if (not isInitialized(height)) then height = getPrefHeight(-1);
    }

    override protected function create(): Node {
        return node;
    }

    protected override function getMinWidth(): Number {
        (paddingLeft + (label as Resizable).getMinWidth() + paddingRight) as Integer
    }

    protected override function getMinHeight(): Number {
        (paddingTop + (label as Resizable).getMinHeight() + paddingBottom) as Integer
    }

    protected override function getPrefWidth(height: Number): Number {
        (paddingLeft + label.getPrefWidth(-1) + paddingRight) as Integer
    }

    protected override function getPrefHeight(width: Number): Number {
        (paddingTop + label.getPrefHeight(-1) + paddingBottom) as Integer
    }
}

public function run() {
    var width = 450;
    var height = 250;

    var appContext: AppContext = AppContext {
        width: width
        height: height
        stage: bind stage
    };

    var menuBox = Container {
        content:[
            PopupMenuItem {
                text: "Simple Menu 1"
            }
            PopupMenuItem {
                text: "Simple Menu 2"
            }
            PopupMenuItem {
                text: "Simple Menu 3"
            }
            PopupMenuItem {
                text: "Simple Menu 4"
            }
        ]
        override function doLayout():Void {
            def pw = getPrefWidth(-1);
            def x:Number = 5;
            var y:Number = -8;

            for (node in getManaged(content)) {
                resizeNode(node, getNodePrefWidth(node), getNodePrefHeight(node));
                positionNode(node, x, y);
                y += getNodePrefHeight(node) + 5;
            }
        }
   }

   var stage: Stage = Stage {
        title: "Popup menu item test"
        width: width
        height: height
        scene: Scene {
            content: [
                Rectangle{
                    width: width
                    height: height
                    fill: Color.web("#220022")

                }
                menuBox
            ]
        }
    }
}

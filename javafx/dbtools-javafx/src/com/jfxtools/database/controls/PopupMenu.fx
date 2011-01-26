/*
 * PopupMenu.fx
 *
 * Created on Jan 17, 2010, 6:19:32 PM
 */

package com.jfxtools.database.controls;

import javafx.scene.CustomNode;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;

import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.scene.layout.Resizable;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import com.jfxtools.database.AppContext;

/**
 * Popup Menu
 * @author winstonp
 */

public class PopupMenu extends CustomNode, Resizable{

   public var paddingLeft: Number = 10;
   public var paddingTop: Number = 0;
   public var paddingBottom: Number = 2;
   public var paddingRight: Number = 2;
    
   public var appContext:AppContext;
   protected var mainPanel:Group;

   var glassPane: Rectangle;

   public var menuItems:PopupMenuItem[] on replace{
       for (item in menuItems){
          item.popupMenu = this;
       }
   }


   public override var onKeyPressed = function(e) {
        if(e.code == KeyCode.VK_ESCAPE) {
            hide();
        }
   }

   var background:Rectangle = Rectangle {
        width: bind width,
        height: bind height,
        opacity: .8
        arcWidth: 9
        arcHeight: 9
        effect: DropShadow {
            offsetX: 2
            offsetY: 2
            spread: .1
            //radius: 2
            color: Color.color(0., 0., 0., .5)
        }
        onMousePressed: function(e) {
            background.requestFocus();
        }
        onKeyPressed: function(e) {
            if(e.code == KeyCode.VK_ESCAPE) {
                hide();
            }
        }
        fill: Color.web("#220022")
   }

   var backgroundBorder:Rectangle = Rectangle {
        layoutX: 1
        layoutY: 1
        width: bind width - 3,
        height: bind height - 3,
        stroke: Color.web("#ffffff")
        strokeWidth: 1.8
        fill: Color.TRANSPARENT
        arcWidth: 6
        arcHeight: 6
        opacity: .6
   }

   var menuBox = VBox {
        layoutX: 1
        layoutY: 1
        spacing: 2

        //nodeHPos: HPos.RIGHT
        hpos: HPos.CENTER
        vpos: VPos.CENTER
        content: bind menuItems
        override function doLayout():Void {
            def pw = getPrefWidth(-1);
            def x:Number = 5;
            var y:Number = -8;

            for (node in getManaged(content)) {
                resizeNode(node, pw, getNodePrefHeight(node));
                positionNode(node, x, y);
                y += getNodePrefHeight(node) + 3;
            }
        }
   }

   override function create():Node {
        mainPanel = Group {
            content: bind [
                background,
                backgroundBorder,
                menuBox
            ]
            blocksMouse: true;
        }
        return mainPanel;
   }

   postinit {
        if (not isInitialized(width)) then width = getPrefWidth(-1);
        if (not isInitialized(height)) then height = getPrefHeight(-1);
    }

   protected override function getMinWidth(): Number {
        (paddingLeft + menuBox.getMinWidth() + paddingRight) as Integer
   }

   protected override function getMinHeight(): Number {
        (paddingTop + menuBox.getMinHeight() + paddingBottom) as Integer
   }

   protected override function getPrefWidth(width: Number): Number {

        return paddingLeft + menuBox.getPrefWidth(-1) + paddingRight;
   }

   protected override function getPrefHeight(height: Number): Number {
        var prefHeight:Number = 5;
        for (popupMenuItem in menuItems){
            prefHeight += (popupMenuItem as Resizable).getPrefHeight(-1) + 3;
        }
        return paddingTop + prefHeight + paddingBottom;
   }

   public function show(e:MouseEvent){
     glassPane = Rectangle {
        fill: Color.TRANSPARENT
        width: bind appContext.width
        height: bind appContext.height
        blocksMouse: true;
        onMousePressed: function(e) {
            hide()
        }
     }
     insert glassPane into appContext.stage.scene.content;
     glassPane.toFront();

     translateX = e.sceneX;
     translateY = e.sceneY;

     insert this into appContext.stage.scene.content;
     this.cursor = Cursor.DEFAULT;
     this.toFront();
     requestFocus();
   }

   public function hide(){
      delete this from appContext.stage.scene.content;
      delete glassPane from appContext.stage.scene.content;
      //return focus to the default focus
      appContext.defaultFocus.requestFocus();
   }
}

public function run() {
    var width = 450;
    var height = 250;

    var stage: Stage = Stage {
        title: "Popup menu test"
        width: width
        height: height
        scene: Scene {
            content: Rectangle{
                width: width
                height: height
                fill: Color.BLANCHEDALMOND
                onMousePressed: function (e:MouseEvent) {
                    popupMenu.show(e);
                }
            }
        }
    }

    var appContext: AppContext = AppContext {
        width: width
        height: height
        stage: bind stage
    };

    var popupMenu = PopupMenu {
        appContext: appContext
        menuItems: [
            PopupMenuItem {
                text: "Simple Menu 1"
                action: function(){
                    println("Menu 1 pressed");
                }
            },
            PopupMenuItem {
                text: "Simple Menu 2"
            },
            PopupMenuItem {
                text: "Simple 3"
            },
            PopupMenuItem {
                text: "Simple Menu 4"
            },
            PopupMenuItem {
                text: "Simple 5"
            },
            PopupMenuItem {
                text: "Simple Menu 6"
            }
        ]
    };

    popupMenu.show(MouseEvent{});

}


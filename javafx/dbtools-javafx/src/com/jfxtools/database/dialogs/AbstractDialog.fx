/*
 * AbstractDialog.fx
 *
 * Created on Aug 14, 2009, 7:32:33 PM
 */

package com.jfxtools.database.dialogs;

import javafx.scene.CustomNode;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;
import javafx.scene.effect.InnerShadow;

import com.jfxtools.database.Constants;
import com.jfxtools.database.controls.ClickOverlay;
import com.jfxtools.database.AppContext;

/**
 * An abstract dialog class for implementing dialogs
 * @author winstonp
 */

public abstract class AbstractDialog extends CustomNode{

   protected var initialFocusNode:Node;

   var stageDragInitialX:Number;
   var stageDragInitialY:Number;
   public var appContext:AppContext;

   public-init var onClose:function():Void;

   public var title:String="Dialog Title";

   public var width = 500;
   public var height = 300;

   protected var mainPanel:Group;

   var busyOverlay: Rectangle;

   var dlgBusyOverlay:Rectangle = Rectangle {
        fill: Color.rgb(127,163,187,.6)
        width: bind width
        height: bind height
        blocksMouse: true;
        cursor: Cursor.WAIT
   }

   var closeButtonImage = ImageView{
       layoutX: bind width - 21
       layoutY: 7
       image: Image{
           url: "{__DIR__}close.png"
       }
   }

   var closeButton = ClickOverlay {
        target: closeButtonImage
        action:function() {
            hide();
        }
   }

   public override var onKeyPressed = function(e) {
        if(e.code == KeyCode.VK_ESCAPE) {
            hide();
        }

   }

   var background:Rectangle = Rectangle {
        width: width,
        height: height,
        arcWidth: 6
        arcHeight: 6
        effect: DropShadow {
            offsetY: 2
            offsetX: 2
            spread: .2
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
        fill: Color.web("#eeeeee")
   }

   var header:Rectangle = Rectangle {
        width: width,
        height: 30
        stroke: null
        arcWidth: 6
        arcHeight: 6
        fill: LinearGradient {
            startX : 0.0
            startY : 0.0
            endX : 0
            endY : 1.0
            stops: [
                Stop {
                    color : Color.web("#0f77b2")
                    offset: 0.0
                },
                Stop {
                    color : Color.web("#5ba5d7")
                    offset: 0.6
                },

                Stop {
                    color : Color.web("#5ba5d7")
                    offset: 1.0
                }
            ]
        }
   }

   var headerClip:Rectangle = Rectangle {
            width: width,
            height: 25,
            layoutY: 25
            stroke: null
            fill: Color.web("#eeeeee")
            effect: InnerShadow {
                    //choke: 0.5
                    //offsetX: 1
                    offsetY: 1
                    radius: 1
                    color: Color.web("#35556a")
            }
   }


   var backgroundBorder:Rectangle = Rectangle {
            width: width,
            height: height,
            stroke: Color.web("#666666")
            strokeWidth: 0
            fill: Color.TRANSPARENT
            arcWidth: 6
            arcHeight: 6
   }

   override function create():Node {
        mainPanel = Group {
            content: [
                background,
                header,
                headerClip,
                backgroundBorder,
                Label {
                    layoutX: 20
                    width: width,
                    height: 30
                    text: bind title
                    vpos: VPos.CENTER
                    hpos: HPos.LEFT
                    font: Constants.fontBoldExtraLarge2
                    textFill: Color.WHITE
                }
                closeButtonImage,
                Group{
                    layoutY: 31
                    content: createContent()
                }
            ]
            blocksMouse: true;
        }
        return mainPanel;
   }

   abstract public function createContent():Node;

   public function showBusyCursor(show:Boolean){
        if (show){
            insert dlgBusyOverlay into mainPanel.content;
            dlgBusyOverlay.toFront();
        }else{
            delete dlgBusyOverlay from mainPanel.content;
        }
   }


   public function show(){
     busyOverlay = Rectangle {
        fill: Color.rgb(127,163,187,.6)
        width: bind appContext.width
        height: bind appContext.height
        blocksMouse: true;
//        onMousePressed: function(e) {
//            stageDragInitialX = e.screenX - appContext.x;
//            stageDragInitialY = e.screenY - appContext.y;
//        }
//        onMouseDragged: function(e) {
//            appContext.stage.x = e.screenX - stageDragInitialX;
//            appContext.stage.y = e.screenY - stageDragInitialY;
//        }
        arcWidth: 15
        arcHeight: 15
     }
     insert busyOverlay into appContext.stage.scene.content;
     busyOverlay.toFront();

     translateX = (appContext.width - width)/2;
     translateY = (appContext.height - height)/2;
     insert this into appContext.stage.scene.content;
     this.cursor = Cursor.DEFAULT;
     this.toFront();
     if (initialFocusNode != null){
         initialFocusNode.requestFocus();
     }else{
        requestFocus();
     }
   }

   public function hide(){
      delete this from appContext.stage.scene.content;
      delete busyOverlay from appContext.stage.scene.content;
      //return focus to the default focus
      appContext.defaultFocus.requestFocus();
      onClose();
   }

}

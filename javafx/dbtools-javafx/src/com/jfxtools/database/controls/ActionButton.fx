/*
 * CustomHyperlink.fx
 *
 * Created on Jan 03, 2010, 11:21:06 AM
 */

package com.jfxtools.database.controls;

import javafx.scene.control.Label;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import com.jfxtools.database.Constants;

/**
 * Action Button
 * @author Winston Prakash
 */
 
def gap = 8.0;
def cornerRadius = 4.0*2;
def strokeColor = Color.web("#468cc0");
def textColor = Color.web("#ffffff");
def glowColor = Constants.highlightColor;
def focusColor = Constants.keyboardFocusColor;

public class ActionButton extends CustomNode {

    public var onEscKey:function();
    public var onEnterKey:function();

    public var text = "";
    public var hoverText:String;

    public var height = 14.0;
    public var width = 50;

    public var font = Constants.fontBold;

    public override var focusTraversable = true;
 
    public var enabled = true;

    public var default = false;

    var textNode:Label = Label {
        width: bind width
        height: bind height
        text: bind getText(hover, text, hoverText)
        textFill: bind getTextColor(default, enabled, hover, pressed)
        font: bind font
        vpos: VPos.CENTER
        hpos: HPos.CENTER
    }

    var backgroundSolid:Rectangle = Rectangle {
        width: bind if(width == -1) { textNode.boundsInLocal.width + gap * 2} else { width }
        height: height
        arcWidth: cornerRadius
        arcHeight: cornerRadius
        fill: bind getBackground(default, enabled, hover, pressed)
    }

    var overlay:Rectangle = Rectangle {
        width: bind if(width == -1) { textNode.boundsInLocal.width + gap * 2} else { width }
        height: height
        arcWidth: cornerRadius
        arcHeight: cornerRadius
        opacity: bind getOverlayOpacity(default, enabled, hover, pressed)
        strokeWidth:1
        stroke: bind getStroke(default, enabled, hover, pressed)
        fill: bind overlayGetGradient(default, enabled, hover, pressed)
    }

    function getOverlayOpacity(default:Boolean, enabled:Boolean, hover:Boolean, pressed:Boolean):Number{
        if (enabled) {
            if (default and not pressed){
                1.0
            }else if (pressed){
                1.0
            }else{
                .5
            }
        } else {
            1.0
        }
    }

    function getBackground(default:Boolean, enabled:Boolean, hover:Boolean, pressed:Boolean):Color{
        if (enabled) {
            if (default and not pressed){
                Color.web("#0f77b2")
            }else if (pressed){
                Color.web("#659bc4")
            }else{
                Color.web("#659bc4")
            }
        } else {
            Color.web("#ffffff")
        }
    }

    function getStroke(default:Boolean, enabled:Boolean, hover:Boolean, pressed:Boolean):Color{
        if (enabled) {
            if (default and not pressed){
                Color.web("#0f77b2")
            }else if (pressed){
                Color.web("#659bc4")
            }else{
                Color.web("#659bc4")
            }
        } else {
            Color.web("#8c98a0")
        }
    }

    function overlayGetGradient(default:Boolean, enabled:Boolean, hover:Boolean, pressed:Boolean):LinearGradient{
         if (enabled){
             if (default and not pressed){
                 return  LinearGradient {
                    startX : 0.0
                    startY : 0.0
                    endX : 0
                    endY : 1.0
                    stops: [
                        Stop {
                            color : Color.web("#4cade4")
                            offset: 0.0
                        },

                        Stop {
                            color : Color.web("#4cade4")
                            offset: 0.20
                        },
                        Stop {
                            color : Color.web("#0f77b2")
                            offset: 1.0
                        }
                    ]
                 }
             }else if (pressed){
                 return  LinearGradient {
                    startX : 0.0
                    startY : 0.0
                    endX : 0
                    endY : 1.0
                    stops: [
                        Stop {
                            color : Color.web("#4cade4", 0)
                            offset: 0.0
                        },

                        Stop {
                            color : Color.web("#4cade4")
                            offset: 0.20
                        },
                        Stop {
                            color : Color.web("#4cade4", 0)
                            offset: 1.0
                        }
                    ]
                 }
             }else{
                 return  LinearGradient {
                    startX : 0.0
                    startY : 0.0
                    endX : 0
                    endY : 1.0
                    stops: [
                        Stop {
                            color : Color.web("#cce5f8", 1)
                            offset: 0.0
                        },
                        Stop {
                            color : Color.web("#cce5f8", 1)
                            offset: .12
                        },
                        Stop {
                            color : Color.web("#ffffff", .05)
                            offset: .46
                        },
                         Stop {
                            color : Color.web("d2e8fd", 0)
                            offset: 0.83
                        },
                        Stop {
                            color : Color.web("d2e8fd", 0)
                            offset: 0.88
                        },
                        Stop {
                            color : Color.web("#cce5f8", 1)
                            offset: 1.0
                        }
                    ]
                 }
             }


         } else {
              return  LinearGradient {
                startX : 0.0
                startY : 0.0
                endX : 0
                endY : 1.0
                stops: [
                    Stop {
                        color : Color.web("#ffffff")
                        offset: 0.0
                    },
                    Stop {
                        color : Color.web("#ffffff")
                        offset: 0.35
                    },

                    Stop {
                        color : Color.web("#9eafb8")
                        offset: 0.92
                    },
                    Stop {
                        color : Color.web("#9eafb8")
                        offset: 1.0
                    }
                ]
             }
          }
    }

    function getText(hover:Boolean, text:String, hoverText:String):String{
        if(hover and (hoverText != null) and enabled){
            return hoverText;
        }else{
            return text;
        }
    }

    function getTextColor(default:Boolean, enabled:Boolean, hover:Boolean, pressed:Boolean):Color {
        if(enabled) {
            if(pressed and hover) {
                Color.rgb(255,255,255,0.60)
            } else {
                Color.WHITE
            }
        } else {
            Color.web("#8c98a0")
        }
    }

    public var action:function();

    override var effect = bind if((hover or focused) and not pressed and enabled) {
        DropShadow {
            color: if (hover) glowColor else focusColor
            offsetX: 0
            offsetY: 0
            radius: 7
            spread: 0.5
        }
    } else { null }

    override var onKeyPressed = function(e) {
        if(e.code == KeyCode.VK_ESCAPE) {
            onEscKey();
        }else if(e.code == KeyCode.VK_ENTER) {
            onEnterKey();
        }else if(e.code == KeyCode.VK_SPACE) {
            if (focused){
                action();
            }
        }
    }
    override var blocksMouse = true;

    override public function create():Node {
        Group {
            onMouseReleased: function(e) {
                if(hover and action != null and enabled) {
                    action();
                }
            }

            content: Group {
                content: Group {
                        content:[
                            backgroundSolid,
                            overlay,
                            textNode
                        ]
                    }
            }
        }
    }
}

function run() {
    Stage {
        scene: Scene {
            width: 400 height: 300
            content: [
                ActionButton {
                    layoutX: 10
                    layoutY: 10
                    width: 70
                    height: 22
                    text: "Install"
                    translateX: 30
                    translateY: 30
                    enabled: true
                }
                ActionButton {
                    layoutX: 10
                    layoutY: 40
                    width: 70
                    height: 22
                    text: "Install"
                    translateX: 30
                    translateY: 30
                    enabled: false
                }
                ActionButton {
                    layoutX: 10
                    layoutY: 80
                    width: 70
                    height: 22
                    text: "Install"
                    translateX: 30
                    translateY: 30
                    default: true
                }
                ActionButton {
                    layoutX: 10
                    layoutY: 120
                    width: 70
                    height: 22
                    text: "Install"
                    translateX: 30
                    translateY: 30
                    pressed: true
                }
            ]
        }

    }

}


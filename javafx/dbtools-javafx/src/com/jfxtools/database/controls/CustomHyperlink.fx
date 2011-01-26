/*
 * CustomHyperlink.fx
 *
 * Created on Jan 03, 2010, 11:21:06 AM
 */

package com.jfxtools.database.controls;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import com.jfxtools.database.Constants;

/**
 * Hyperlink conrol
 * @author Winston Prakash
 */

public class CustomHyperlink extends CustomNode{

    public var onEscKey:function();
    public var onEnterKey:function();
    
    public var text:String;
    public var font:Font = Constants.fontBoldLarge;
    public var textColor:Color = Constants.linkColor;
    public var linkText:Text;
    public var rectangle:Rectangle;
    public var action:function();
    public var width = -1;
    public var height = 14;

    public override var focusTraversable = true;

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

    override var effect = bind if(hover or focused) {
        Glow {
          level: .7
        }
    } else { null }

    override function create():Node {
        return Group{
            content:[
                linkText = Text {
                    fill: textColor
                    content: bind text
                    font: font
                }
                rectangle = Rectangle {
                    x: bind linkText.boundsInLocal.minX -1
                    y: bind linkText.boundsInLocal.minY-1
                    width: bind linkText.boundsInLocal.width + 2
                    height: bind linkText.boundsInLocal.height + 2
                    fill: Color.TRANSPARENT
                    onMouseEntered: function(e) {
                        rectangle.cursor = Cursor.HAND;
                    }
                    onMouseExited: function(e) {
                        rectangle.cursor = Cursor.DEFAULT;
                    }
                    onMouseReleased: function(e) {
                        action();
                    }
                }
            ]
            clip: bind if (width > 0) then Rectangle {
                x: 0, y: bind -font.size
                width: bind width, height: bind font.size + 5
            } else null
        }
    }
}

function run() {
    Stage {
        scene: Scene {
            width: 400 height: 300
            content: [
                    CustomHyperlink {
                        width: 200
                        layoutX: 10;
                        layoutY: 20;
                        text: "Custom Hyper Link"
                    }
            ]
        }

    }
}
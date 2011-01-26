/*
 * ErrorDialog.fx
 *
 * Created on Aug 13, 2009, 1:15:51 PM
 */

package com.jfxtools.database.dialogs;

import javafx.scene.Group;
import javafx.scene.Node;


import javafx.scene.text.Text;

import javafx.scene.Scene;
import javafx.stage.Stage;

import com.jfxtools.database.Constants;
import com.jfxtools.database.AppContext;
import com.jfxtools.database.controls.ActionButton;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


/**
 * Error Dialog
 * @author Winston Prakash
 */
 public class ErrorDialog extends AbstractDialog{

    public-init var messageTitle = "";
    public-init var message = "";

    override public var width = 400;
    override public var height = 150;
    
    def buttonHeight = Constants.buttonHeight;

    var closeButton = ActionButton {
        layoutX: bind width - 90
        layoutY: bind height - 70
        text: "Close"
        width: 60
        height: buttonHeight
        default: true
        action: function() {;
            hide();
        }
        onEscKey: function(){
           hide();
        }
        onEnterKey: function(){
           hide();
        }
     }

    override function createContent():Node {
        initialFocusNode = closeButton;
        if (title.equals("Dialog Title")){
            title = "Error:";
        }
        var contentGroup = Group {
            content: [
                Text {
                    layoutX: 20
                    layoutY: 20
                    content: messageTitle
                    wrappingWidth: bind width - 64
                    font: Constants.fontBoldExtraLarge2
                    fill: Constants.textColor
                }
                Text {
                    layoutX: 20
                    layoutY: 40
                    content: message
                    wrappingWidth: bind width - 64
                    font: Constants.fontRegularExtraLarge
                    fill: Constants.textColor
                }
                closeButton
            ]
        }
        return contentGroup;
    }
}

public function run(){

    var width = 500;
    var height = 430;

    var stage: Stage = Stage {
                title: "Connection Creation Dialog"
                width: width
                height: height
                scene: Scene {
                    content: Rectangle{
                        width: width
                        height: height
                        fill: Color.AZURE
                    }
                }
            }

    var appContext: AppContext = AppContext {
                width: width
                height: height
                stage: stage
            };

    var errorDialog = ErrorDialog{
       //message: "Error message"
       appContext: bind appContext
       onClose: function(){
           FX.exit();
       }
    };
    errorDialog.show();
}

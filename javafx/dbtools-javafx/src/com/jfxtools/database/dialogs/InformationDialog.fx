/*
 * InformationDialog.fx
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
 public class InformationDialog extends AbstractDialog{

    public-init var messageTitle = "";
    public-init var message = "";

    override public var width = 225;
    override public var height = 125;
    
    def buttonHeight = Constants.buttonHeight;

    var closeButton = ActionButton {
        layoutX: bind width - 70
        layoutY: bind height - 60
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
            title = "Information";
        }
        var contentGroup = Group {
            content: [
                Text {
                    layoutX: 20
                    layoutY: 20
                    content: messageTitle
                    wrappingWidth: bind width - 40
                    font: Constants.fontBoldExtraLarge2
                    fill: Constants.textColor
                }
                Text {
                    layoutX: 20
                    layoutY: 40
                    content: message
                    wrappingWidth: bind width - 40
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

    var width = 400;
    var height = 330;

    var stage: Stage = Stage {
                title: "Information Dialog"
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

    var informationDialog = InformationDialog{
       //message: "Error message"
       appContext: bind appContext
       message: "Action not yet implemented."
       onClose: function(){
           FX.exit();
       }
    };
    informationDialog.show();
}

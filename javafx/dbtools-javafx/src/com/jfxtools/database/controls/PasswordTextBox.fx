/*
 * PasswordTextBox2.fx
 *
 * Created on Jan 03, 2009, 11:33:52 AM
 */
package com.jfxtools.database.controls;

import javafx.util.Math;
import com.jfxtools.database.controls.CustomTextBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Original code from: http://blog.alutam.com/2009/09/12/javafx-password-field/
 */

public class PasswordTextBox extends CustomTextBox {
    public-read var passwordString = "";

    override function replaceSelection(arg) {
        var pos1 = Math.min(dot, mark);
        var pos2 = Math.max(dot, mark);
        passwordString = "{passwordString.substring(0, pos1)}{arg}{passwordString.substring(pos2)}";
        super.replaceSelection(getStars(arg.length()));
    }

    override function deleteNextChar() {
        if ((mark == dot) and (dot < passwordString.length())) {
            passwordString = "{passwordString.substring(0, dot)}{passwordString.substring(dot + 1)}";
        }
        super.deleteNextChar();
    }

    override function deletePreviousChar() {
        if ((mark == dot) and (dot > 0)) {
            passwordString = "{passwordString.substring(0, dot - 1)}{passwordString.substring(dot)}";
        }
        super.deletePreviousChar();
    }

    function getStars(len: Integer): String {
        var result: String = "";
        for (i in [1..len]) {
            result = "{result}*";
        }
        result;
    }
}

function run() {
    Stage {
        scene: Scene {
            width: 400 height: 300
            content: [
                    PasswordTextBox {
                        width: 200
                        layoutX: 10;
                        layoutY: 20;
                    }
            ]
        }

    }
}


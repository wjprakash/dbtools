/*
 * AddConnectionDialog.fx
 *
 * Created on Jan 4, 2010, 5:36:43 PM
 */
package com.jfxtools.database.dialogs;

import javafx.scene.Node;
import com.jfxtools.database.Constants;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import com.jfxtools.database.controls.CustomTextBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfxtools.database.controls.ActionButton;
import com.jfxtools.database.AppContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import com.sun.javafx.runtime.Entry;
import javafx.scene.text.Text;
import com.jfxtools.database.controls.PasswordTextBox;
import com.jfxtools.database.ConnectionConfig;
import com.jfxtools.database.api.IConnectionType;
import com.jfxtools.database.api.ConnectionManager;
import java.lang.Exception;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.LayoutInfo;
import com.jfxtools.database.api.IConnectionConfig;

/**
 * Database connection dialog
 * @author Winston Prakash
 */
public class AddConnectionDialog extends AbstractDialog {

    public-init var callbackFunction:function ():  Void;
    public-read var result: Boolean;
    public override var width = 460;
    public override var height = 350;

    public-init var connectionConfig:IConnectionConfig;

    def controlHeight = 20;
    def vGap = 10;
    def hGap = 10;
    def labelWidth = 115;
    def fieldWidth = 310;
    def buttonHeight = Constants.buttonHeight;

    public var editing = false;

    var defaultButton: ActionButton;

    var connectionSuccess = false;

    var connectionNameLabel = Label {
            text: "Connection Name:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontBoldLarge
        }

    var connectionNameField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            selectOnFocus: true
            text: "New MySql Connection"
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var serverNameLabel = Label {
            text: "Database Server:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var serverNameField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            text: "MySql"
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var hostNameLabel = Label {
            text: "Host Name:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var hostNameField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            text: "localhost"
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var portLabel = Label {
            text: "Port:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var portField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            text: "3306" 
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var databaseLabel = Label {
            text: "Database:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var databaseField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var userNameLabel = Label {
            text: "Username:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var userNameField = CustomTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            text: "root"
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var passwordLabel = Label {
            text: "Password:"
            hpos: HPos.RIGHT
            vpos: VPos.CENTER
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
            textFill: Constants.textColor
            font: Constants.fontRegularLarge
        }

    var passwordField = PasswordTextBox {
            layoutInfo: LayoutInfo {width: fieldWidth, height: controlHeight }
            selectOnFocus: true
            onEscKey: function () {
                hide();
                }
            onEnterKey: bind defaultButton.action
        }

    var dummyLabel = Label {
            layoutInfo: LayoutInfo {width: labelWidth, height: controlHeight }
        }

    var testConnectionButton = ActionButton {
            text: "Test Connection"
            width: 110
            height: buttonHeight
            font: Constants.fontBoldMedium
            default: true
            action: testConnection
            onEnterKey: testConnection
        }


    var connectionNameBox = HBox {
            spacing: 5    
            content: [
                connectionNameLabel,
                connectionNameField
            ]
        }

    var labelBox = VBox {
        spacing: 5
        content: [
                serverNameLabel,
                hostNameLabel,
                portLabel,
                databaseLabel,
                userNameLabel,
                passwordLabel,
            ]
    }

    var FieldBox = VBox {
        spacing: 5
        content: [
                serverNameField,
                hostNameField,
                portField,
                databaseField,
                userNameField,
                passwordField
            ]
    }

    var connectionPropertiesBox = HBox{
        spacing: 5
        content: [
            labelBox,
            FieldBox
        ]
    }

    var connectionPropertiesBackground = Rectangle{
        x: bind connectionPropertiesBox.boundsInParent.minX - 10
        y: bind connectionPropertiesBox.boundsInParent. minY - 10
        width: bind connectionPropertiesBox.layoutBounds. width + 5
        height: bind connectionPropertiesBox.layoutBounds. height + 20
        arcWidth: 6
        arcHeight: 6
        stroke: Color.DARKSLATEBLUE
        strokeWidth: .1
        fill: Color.DARKKHAKI
    }

    var testConnectionBox = HBox {
            spacing: 5
            content: [
                dummyLabel,
                testConnectionButton
            ]
        }

     var messageText = Text {
            visible: false
            fill: Color.RED
            font: Constants.fontBoldMedium
            wrappingWidth: bind width - 40
            content: "This is a message"
        }

    var cancelButton = ActionButton {
            text: "Cancel"
            height: buttonHeight
            font: Constants.fontBoldMedium
            action: function () {
                result = false;
                showBusyCursor(false);
                hide();
            }
            onEnterKey: bind defaultButton.action
        }

    var createConnectionButton = ActionButton {
            text: if (editing) "Modify" else "Create"
            height: buttonHeight
            font: Constants.fontBoldMedium
            default: true
            action: createConnection
            onEnterKey: createConnection
        }

    var buttonGroup = HBox {
            layoutInfo: LayoutInfo {hpos: HPos.RIGHT}
            spacing: 5
            content: [
                cancelButton,
                createConnectionButton
            ]
        }

    var mainBox = VBox{
        spacing: 15
        content: [
            connectionNameBox,
            connectionPropertiesBox,
            testConnectionBox,
            messageText,
            buttonGroup
        ]
    }

    override public function createContent(): Node {
        initialFocusNode = connectionNameField;
        defaultButton = createConnectionButton;
        if (editing) {
            title = "Edit Connection";
        }else{
            title = "Create Connection";
        }

        return Group {
            layoutX: 10
            layoutY: 10
            content: [
                connectionPropertiesBackground,
                mainBox,
            ]
        }
    }
    
    postinit{
        if (connectionConfig != null){
            connectionNameField.text = connectionConfig.getName();
            //serverNameField.text = connectionConfig.
            hostNameField.text = connectionConfig.getHostname();
            portField.text = connectionConfig.getPort();
            databaseField.text = connectionConfig.getDatabase();
            userNameField.text = connectionConfig.getUsername();
            passwordField.replaceSelection(connectionConfig.getPassword());
        }
    }

    function postMessage(msg: String) {
        Entry.deferAction(function (): Void {
            messageText.visible = true;
            //helpLink.visible = false;
            messageText.fill = Constants.textColor;
            messageText.content = msg;
        });
    }

    function postErrorMessage(msg: String) {
        Entry.deferAction(function (): Void {
            messageText.visible = true;
            //helpLink.visible = false;
            messageText.fill = Color.RED;
            messageText.content = msg;
        });
    }

    function postSuccessMessage(msg: String) {
        Entry.deferAction(function (): Void {
            messageText.visible = true;
            //helpLink.visible = false;
            messageText.fill = Color.GREEN;
            messageText.content = msg;
        });
    }

    function createConnection():Void{
         testConnection();
         if (connectionSuccess){
            result = true;
            showBusyCursor(false);
            hide();
            callbackFunction();
         }
    }

    function testConnection():Void{
        connectionSuccess = false;
        connectionNameField.commit();
        hostNameField.commit();
        portField.commit();
        databaseField.commit();
        userNameField.commit();
        passwordField.commit();

        var name = connectionNameField.text;

        var hostName = hostNameField.text;
        if (hostName == null or "".equals(hostName)){
           hostNameField.requestFocus();
           postErrorMessage("Username required.");
           return;
        }

        var port = portField.text;

        var database = databaseField.text;

        var userName = userNameField.text;
        if (userName == null or "".equals(userName)){
           userNameField.requestFocus();
           postErrorMessage("Username required.");
           return;
        }

        var password = passwordField.passwordString;

        println("password: {password}");

        if (connectionConfig == null){
            connectionConfig = new ConnectionConfig(name, hostName, port, database, IConnectionType.MYSQL, userName,
                                    password);
        }else{
            connectionConfig.setName(connectionNameField.text);
            //serverNameField.text = connectionConfig.
            connectionConfig.setHostname(hostNameField.text);
            connectionConfig.setPort(portField.text);
            connectionConfig.setDatabase(databaseField.text);
            connectionConfig.setUsername(userNameField.text);
            connectionConfig.setPassword(passwordField.passwordString);
        }

        var connectionManager = ConnectionManager.getInstance();
        try{
            connectionManager.createConnection(connectionConfig);
        }catch (ex:Exception){
            ex.printStackTrace();
            postErrorMessage(ex.getLocalizedMessage());
            return;
        }
        
        postSuccessMessage("Connection Successful!");
        connectionSuccess = true;
    }
}

public function run() {
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

    var addConnectionDialog = AddConnectionDialog {
                appContext: appContext
                callbackFunction: function () {
                    FX.exit();
                }
            };
    addConnectionDialog.show();
}

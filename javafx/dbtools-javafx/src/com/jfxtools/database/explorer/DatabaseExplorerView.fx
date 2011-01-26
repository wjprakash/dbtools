/*
 * DatabaseExplorerView.fx
 *
 * Created on Jul 31, 2009, 2:26:01 PM
 */

package com.jfxtools.database.explorer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Panel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.jfxtools.database.AppContext;

import com.jfxtools.database.controls.Tree;
import com.jfxtools.database.api.ConnectionManager;
import com.jfxtools.database.controls.TreeNode;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import com.jfxtools.database.Constants;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.LayoutInfo;
import com.jfxtools.database.resources.Images;
import javafx.scene.input.MouseEvent;
import com.sun.javafx.scene.control.ScrollView;

/**
 * Database Explorer View
 * @author winstonp
 */

public class DatabaseExplorerView extends Panel{

    public var appContext:AppContext;

    var background = Rectangle {
        width: bind width
        height: bind height
        fill: Color.WHITESMOKE//Color {red: 0.941, green: 0.906, blue: 0.843}
//        effect: Lighting {
//            light: DistantLight {azimuth: -135, elevation: 75}
//            surfaceScale: 3
//            bumpInput: GaussianBlur {radius: 5}
//        }
        arcHeight: 12
        arcWidth: 12
    }

     var header: Rectangle = Rectangle {
                width: bind width,
                height: 30
                stroke: null
                arcWidth: 10
                arcHeight: 10
                fill: LinearGradient {
                    startX: 0.0
                    startY: 0.0
                    endX: 0
                    endY: 1.0
                    stops: [
                        Stop {
                            color: Color.web("#993333")
                            offset: 0.0
                        },
                        Stop {
                            color: Color.web("#AD593A")
                            offset: 0.6
                        },
                        Stop {
                            color: Color.web("#993333")
                            offset: 1.0
                        }
                    ]
                }
            }
    var headerClip: Rectangle = Rectangle {
                width: bind width,
                height: 25,
                layoutY: 25
                stroke: null
                fill: Color.WHITESMOKE
                effect: InnerShadow {
                    //choke: 0.5
                    //offsetX: 1
                    offsetY: 1
                    radius: 1
                    color: Color.web("#35556a")
                }
            }
    var iconView = ImageView {
                layoutX: 5
                layoutY: 5
                fitWidth: 16
                fitHeight: 16
                image: Images.databaseGroup
                layoutInfo: LayoutInfo {
                    vpos: VPos.CENTER
                }
            }
    var title = Label {
                layoutY: 5
                layoutX: 25
                width: bind width,
                //height: 60
                text: "Database Explorer"
                //vpos: VPos.CENTER
                //hpos: HPos.CENTER
                font: Constants.fontBoldExtraLarge
                textFill: Color.WHITE
            }

    var body = Rectangle {
        layoutY: 25
        width: bind width
        height: bind height - 30
        fill: Color.web("#665566")
        opacity: .05
        arcHeight: 12
        arcWidth: 12
    }

    var explorerTree:Tree =  Tree {

        //rootVisible: false
//        layoutX: 10
//        layoutY: 30
//        width: bind width - 20
//        height: bind height - 20
         
        onMouseClickedNode: function(treeNode:TreeNode, event:MouseEvent) {
            if (event.clickCount == 2){
                (treeNode as AbstractTreeNode).onDoubleClick();
            }
        }
        onMousePressedNode:function(treeNode:TreeNode, event:MouseEvent) {
            if (event.popupTrigger){
                 println(appContext);
                 treeNode.popupMenu.appContext = appContext;
                 treeNode.popupMenu.show(event);
            }
        }
        onExpand: function(treeNode:TreeNode) {
            (treeNode as AbstractTreeNode).onExpand();
        }
    }

    var scrollView = ScrollView{
        layoutX: 10
        layoutY: 30
        width: bind width - 15
        height: bind height - 35
        node: explorerTree
    }


    postinit{
        var connectionManger = ConnectionManager.getInstance();
        explorerTree.rootNode = RootTreeNode {
            appContext: appContext
            connectionManger: connectionManger
            expanded: true
        };
        content = [
           background,
           header,
           headerClip,
           iconView,
           title,
           body,
           scrollView
        ]
    }
}

public function run() {
    var width = 450;
    var height = 650;


    var appContext: AppContext = AppContext {
        width: width
        height: height
        stage: bind stage
    };

    var scene:Scene = Scene {
        width: width
        height: height
        content: [
            DatabaseExplorerView{
                    appContext: appContext
                    layoutX: 10
                    layoutY: 10
                    width: bind scene.width - 20
                    height: bind scene.height - 20
                 }
        ];
        fill: Color.rgb(105,4,0,1.0)
    };

    var stage:Stage = Stage {
        width: 200
        height: 300
        title: "Tree dynamic bound test"
        scene: scene
    }
}


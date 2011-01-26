/*
 * Tree.fx
 *
 * Created on Feb 3, 2010, 4:32:36 PM
 */
package com.jfxtools.database.controls;

import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import com.jfxtools.database.AppContext;

/**
 * Tree control
 * @author winstonp
 */
public class Tree extends Control{

    public var rootNode:TreeNode on replace oldRootNode{
        oldRootNode.tree = null;
        rootNode.tree = this;
    }

    public var selectedNode:TreeNode on replace oldSelectedNode{
        oldSelectedNode.selected = false;
        selectedNode.selected = true;
    }

    public var onExpand: function(treeNode:TreeNode);
    public var onCollapse: function(treeNode:TreeNode);

    public var rootVisible = true;

    public var onMouseClickedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseDraggedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseEnteredNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseExitedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseMovedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMousePressedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseReleasedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onMouseWheelMovedNode: function(treeNode:TreeNode, event: MouseEvent): Void;
    public var onKeyReleasedNode: function(treeNode:TreeNode, event: KeyEvent ) : Void;
    public var onKeyPressedNode: function(treeNode:TreeNode, event: KeyEvent ) : Void;
    public var onKeyTypedNode: function(treeNode:TreeNode, event: KeyEvent ) : Void;

    postinit {
        skin = TreeSkin{};
        rootNode.tree = this;
    }
}

function run() {

    var width = 400;
    var height = 300;


    var appContext: AppContext = AppContext {
        width: width
        height: height
        stage: bind stage
    };

    var popupMenu = PopupMenu {
        appContext: appContext
        menuItems: [
            PopupMenuItem {
                text: "Delete"
                action: function(){

                }
            }
        ]
    };

    var treeNode = TreeNode{
        data: "Node0"
        popupMenu: popupMenu
    }

    var stage = Stage {
        scene: Scene {
            width: width height: height
            content: [
                 Tree{
                    rootVisible: false
                    width: width
                    height: height
                    layoutX: 20
                    layoutY: 20
                    rootNode: TreeNode{
                        data: "Root Node"
                        expanded: true
                        childNodes: [
                            treeNode,
                            TreeNode{
                                expanded: true
                                data: "Node1"
                                childNodes: [
                                    TreeNode{
                                        data: "Node1.1"
                                        childNodes: [
                                            TreeNode{data: "Node1.1.2"}
                                        ]
                                    }
                                ]
                            }
                            TreeNode{
                                data: "Node2"
                                childNodes: [
                                    TreeNode{data: "Node2.1"}
                                    TreeNode{data: "Node2.2"}
                                ]
                            }
                            TreeNode{
                                expanded: true
                                data: "Node3"
                                childNodes: [
                                    TreeNode{
                                        expanded: true
                                        data: "Node3.1"
                                        childNodes: [
                                            TreeNode{data: "Node3.1.1"}
                                            TreeNode{data: "Node3.1.2"}
                                        ]
                                    }
                                ]
                            }
                        ]
                    }

                    onMousePressedNode:function(treeNode:TreeNode, event:MouseEvent) {
                        if (event.popupTrigger){
                             treeNode.popupMenu.appContext = appContext;
                             treeNode.popupMenu.show(event);
                        }
                    }

                 }
            ]
        }
    }
}
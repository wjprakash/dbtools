/*
 * TreeNode.fx
 *
 * Created on Feb 3, 2010, 5:47:56 PM
 */

package com.jfxtools.database.controls;

import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfxtools.database.AppContext;
import javafx.scene.layout.Container;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Tree Node
 * @author winstonp
 */

public class TreeNode extends Container{

    public var tree:Tree on replace{
        for (child in childNodes){
           child.tree = tree;
        }
    }

    public var data:Object = "Tree Node";


    public var expanded = false on replace{
        if (expanded){
            nodeArrow.expanded = true;
            childrenGroup = childNodes;
            tree.onExpand(this);
        }else{
            childrenGroup = null;
            tree.onCollapse(this);
        }
    }

    public var selected = false;


    public var renderer:NodeRenderer;

    public var parentNode:TreeNode;

    public var childNodes:TreeNode[] on replace oldChildren{
        for (child in oldChildren){
           child.tree = null;
           child.parentNode = null;
        }

        for (child in childNodes){
           child.tree = tree;
           child.parentNode = this;
        }

        if ((childNodes != null) and (childNodes.size() > 0)){
            leaf = false
        }else{
            leaf = true
        }
        if (expanded){
            childrenGroup = childNodes;
        } 
    }

    public var leaf = true;

    public var popupMenu:PopupMenu;

    def nodeArrow: NodeArrow = NodeArrow {
        width: 8
        height: 8
        action: function () {
            expanded = nodeArrow.expanded;
        }
        visible: bind not leaf
    }

    var defaultRenderer = NodeRenderer {
        override public function createNode(data: Object): Node {
            return Label {
                      text: bind data.toString()
                   }
        }
    }

    var renderedNode: Node = bind if (renderer != null)
                renderer.createNode(data) else
                defaultRenderer.createNode(data);

    var selectionBackground = Rectangle {
        layoutX: bind renderedNode.layoutX
        layoutY: bind renderedNode.layoutY
        width: bind renderedNode.layoutBounds.width
        height: bind renderedNode.layoutBounds.height
        fill: bind if (selected) Color.web("##E6E6E6") else Color.TRANSPARENT
        onMouseClicked: function (e: MouseEvent) {
            tree.onMouseClickedNode(this, e);
            }
        onMouseDragged: function (e: MouseEvent) {
            tree.onMouseDraggedNode(this, e);
            }
        onMouseEntered: function (e: MouseEvent) {
            tree.onMouseDraggedNode(this, e);
            }
        onMouseExited: function (e: MouseEvent) {
            tree.onMouseEnteredNode(this, e);
            }
        onMouseMoved: function (e: MouseEvent) {
            tree.onMouseMovedNode(this, e);
            }
        onMousePressed: function (e: MouseEvent) {
            tree.onMousePressedNode(this, e);
            tree.selectedNode = this;
        }
        onMouseReleased: function (e: MouseEvent) {
            tree.onMouseReleasedNode(this, e);
            }
        onMouseWheelMoved: function (e: MouseEvent) {
            tree.onMouseWheelMovedNode(this, e);
            }
        onKeyReleased: function (e: KeyEvent) {
            tree.onKeyReleasedNode(this, e);
            }
        onKeyPressed: function (e: KeyEvent) {
            tree.onKeyPressedNode(this, e);
            }
        onKeyTyped: function (e: KeyEvent) {
            tree.onKeyTypedNode(this, e);
            }
        opacity: .1
    }

    var childrenGroup:Node[];

    postinit {
        for (child in childNodes){
           child.tree = tree;
           child.parentNode = this;
        }
        if (expanded){
            nodeArrow.expanded = true;
            childrenGroup = childNodes;
        }else{
            childrenGroup = null;
        }
    }

    public override var content = bind [selectionBackground, nodeArrow, renderedNode, childrenGroup];

    override function doLayout() {
        var offset = 15;
        var nodeHeight = getNodePrefHeight(renderedNode);
        layoutNode(nodeArrow, 0, 0, 10, nodeHeight, HPos.CENTER, VPos.CENTER);
        layoutNode(renderedNode, offset, 0, getNodePrefWidth(renderedNode), nodeHeight, HPos.CENTER, VPos.CENTER);
        var hx = 0;
        var y = nodeHeight + 2;
        for (node in getManaged(childNodes)) {
            positionNode(node, offset, y);
            y += getNodePrefHeight(node) + 2;
        }
   }
   override function getPrefHeight(height) {
        var prefH: Number = getNodePrefHeight(renderedNode);
        if (expanded) {
            prefH += 2;
            for (node in childNodes) {
                prefH += getNodePrefHeight(node) + 2;
            }
        }
        return prefH;
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

    var popupMenu: PopupMenu = PopupMenu {
        appContext: appContext
        menuItems: [
            PopupMenuItem {
                text: "Delete"
                action: function(){
                    delete treeNode from treeNode.parentNode.childNodes;
                }
            }
        ]
    };

    var treeNode:TreeNode = TreeNode{
        data: "Node1.1.1"
        popupMenu: popupMenu
    }

    var stage = Stage {
        scene: Scene {
            width: 400 height: 300
            content: [
                 TreeNode{
                    expanded: true
                    layoutX: 20
                    layoutY: 20
                    childNodes: [treeNode, TreeNode{
                                childNodes: [TreeNode{}, TreeNode{}]
                            }]
                 }
            ]
        }
    }
}
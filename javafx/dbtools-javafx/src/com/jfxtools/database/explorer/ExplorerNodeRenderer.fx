/*
 * ExplorerNodeRenderer.fx
 *
 * Created on Nov 1, 2009, 6:36:59 PM
 */
package com.jfxtools.database.explorer;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.LayoutInfo;

import com.jfxtools.database.AppContext;
import com.jfxtools.database.resources.Images;

import com.jfxtools.database.controls.NodeRenderer;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author winstonp
 */
def defaultIcon = Images.default;

public class ExplorerNodeRenderer extends NodeRenderer {

    public-init var appContext: AppContext    ;

    public-init var treeNode: AbstractTreeNode;
    var iconView: ImageView = ImageView {
                fitWidth: 16
                fitHeight: 16
                image: bind treeNode.icon
                layoutInfo: LayoutInfo {
                    vpos: VPos.CENTER
                }
            }
    var label: Label = Label {
                layoutX: 20
                height: 16
                width: 200
                text: bind treeNode.displayName
            }
    var group = Group {
                content: [
                    iconView,
                    label
                ]
            }

    override public function createNode(data: Object): Node {
//        if (data instanceof IDatabaseObject) {
//            var dataObject = data as IDatabaseObject;
//            label.text = dataObject.getName();
//        }
        return group;
    }
}

public function run()  {

    var scene: Scene = Scene {
                width: 200
                height: 300
                content: [
                    ImageView {
                        fitWidth: 16
                        fitHeight: 16
                        image: defaultIcon
                        layoutInfo: LayoutInfo {
                            vpos: VPos.CENTER
                        }
                    }
                ];
            };

    var stage: Stage = Stage {
                width: 200
                height: 300
                title: "Image Resources"
                scene: scene
            }
}

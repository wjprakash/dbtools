/*
 * NodeRenderer.fx
 *
 * Created on Feb 3, 2010, 6:05:02 PM
 */

package com.jfxtools.database.controls;
import javafx.scene.Node;

/**
 * Renderer for the Tree Node
 * @author winstonp
 */

public abstract class NodeRenderer {
    public abstract function createNode(data:Object):Node;
}
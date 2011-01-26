/*
 * AppContext.fx
 *
 * Created on Oct 24, 2009, 2:44:57 PM
 */

package com.jfxtools.database;

import javafx.stage.Stage;
import javafx.scene.Node;

import com.jfxtools.database.panels.ViewPanel;

/**
 * Holds information about the Application context
 * @author Winston Prakash
 */

public class AppContext {
        
    public var x = bind stage.x;
    public var y = bind stage.y;
    public var width = bind stage.width;
    public var height = bind stage.height;
    
    public var stage:Stage;
    public var defaultFocus:Node;

    public var viewPanel:ViewPanel;
}

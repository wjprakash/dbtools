/*
 * MainPanel.fx
 *
 * Created on Sep 18, 2009, 10:46:19 PM
 */

package com.jfxtools.database.panels;

import javafx.scene.layout.Panel;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.jfxtools.database.explorer.DatabaseExplorerView;

import com.jfxtools.database.Constants;
import com.jfxtools.database.AppContext;

import javafx.scene.Group;

/**
 * The main panel of the database tool
 * @author Winston Prakash
 */

public class MainPanel extends Panel{

    public-init var appContext:AppContext;

    var explorerViewX = Constants.windowLeftIndent;
    var explorerViewY = Constants.windowTopIndent;

    var explorerViewW = Constants.explorerWidth;
    var explorerViewH = bind height - Constants.windowTopIndent - Constants.windowBottomIndent;

    var backgroundOverlay = Rectangle {
        width: bind width
        height: bind height
        fill: Color.rgb(105,4,0,1.0)
//        fill: LinearGradient {
//            startX : 0.0 startY : 0.0 endX : 0 endY : 1.0 proportional: true
//            stops: [
//                Stop{ offset: 0.00 color: Color.rgb(105,4,0,1.0) }
//                Stop{ offset: 0.10 color: Color.rgb(116,18,0,1.0) }
//                Stop{ offset: 0.60 color: Color.rgb(110,18,0,0.8) }
//                Stop{ offset: 0.98 color: Color.rgb(102,16,0,0.85) }
//                Stop{ offset: 1.00 color: Color.rgb(102,16,0,1.0) }
//            ]
//        }
    }
    
    var explorerView = DatabaseExplorerView{
        appContext: bind appContext
        layoutX: explorerViewX
        layoutY: explorerViewY
        width: bind explorerViewW
        height: bind explorerViewH
    }

    var centerPanelX = explorerViewX + explorerViewW + Constants.windowGap;
    var centerPanelY = Constants.windowTopIndent;

    var viewPanel = ViewPanel{
        layoutX: centerPanelX
        layoutY: centerPanelY
        width: bind width - explorerViewW - 3 * Constants.windowGap
        height: bind explorerViewH
    }

    postinit{
        appContext.viewPanel = viewPanel;
        content = [
            backgroundOverlay,
            explorerView,
            viewPanel
        ]
    }
}

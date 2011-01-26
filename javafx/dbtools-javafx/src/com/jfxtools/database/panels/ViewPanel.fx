/*
 * MainPanel.fx
 *
 * Created on Sep 18, 2009, 10:46:19 PM
 */

package com.jfxtools.database.panels;

import javafx.scene.layout.Panel;

import com.jfxtools.database.AppContext;
import javafx.scene.layout.Container;


/**
 * The main panel of the database tool
 * @author Winston Prakash
 */

public class ViewPanel extends Panel{

    public-init var appContext:AppContext;

    public function addTab(panel:Container){
        var newTab = ViewPanelTab{
            panelContent: panel
            appContext: appContext
        }
        insert newTab into content;
    }

    public override function doLayout():Void {
        if ((width > 0) and (height > 0)){
            for (node in getManaged(content)) {
                var panel = node as Container;
                resizeNode(panel, width, height);
            }
        }
    }
}

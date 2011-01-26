/*
 * ViewPanelTab.fx
 *
 * Created on Aug 14, 2009, 7:32:33 PM
 */

package com.jfxtools.database.panels;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import com.jfxtools.database.controls.ClickOverlay;
import com.jfxtools.database.AppContext;

import javafx.scene.layout.Container;

/**
 * A Tab class for the view Panel
 * @author Winston Prakash
 */

public class ViewPanelTab extends Container{

   protected var initialFocusNode:Node;

   public var appContext:AppContext;

   public-init var onClose:function():Void;
   public-init var panelContent:Container;

   public-init var viewPanel:ViewPanel;

   var busyOverlay: Rectangle;

   var closeButtonImage = ImageView{
       layoutX: bind width - 21
       layoutY: 4
       image: Image{
           url: "{__DIR__}close.png"
       }
   }

   var closeButton = ClickOverlay {
        target: closeButtonImage
        action:function() {
            hide();
        }
   }

   var background:Rectangle = Rectangle {
        arcWidth: 6
        arcHeight: 6
        effect: DropShadow {
            offsetY: 2
            offsetX: 2
            spread: .2
            color: Color.color(0., 0., 0., .5)
        }
        onMousePressed: function(e) {
            background.requestFocus();
        }
        fill: Color.web("#eeeeee")
   }

   postinit {
       blocksMouse = true;

       content = [
            background,
            closeButtonImage,
            panelContent
       ];
   }

   public override function doLayout():Void {
        if ((width > 0) and (height > 0)){
            background.width = width;
            background.height = height;
            layoutNode(panelContent, 5, 20, width - 10, height - 25);
        }
   }

   public function hide() {
      delete this from (this.parent as Container).content;
      appContext.viewPanel.requestLayout();
      appContext.defaultFocus.requestFocus();
      onClose();
   }
}

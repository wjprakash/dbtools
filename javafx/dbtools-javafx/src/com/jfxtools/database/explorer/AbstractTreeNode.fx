/*
 * AbstractTreeNode.fx
 *
 * Created on Jul 31, 2009, 12:18:37 PM
 */

package com.jfxtools.database.explorer;

import com.jfxtools.database.api.IDatabaseObject;
import com.jfxtools.database.AppContext;

import javafx.scene.image.Image;
import com.jfxtools.database.controls.TreeNode;
import com.jfxtools.database.dialogs.InformationDialog;


/**
 * An abstract tree node for displaying database objects
 * @author Winston Prakash
 */

public abstract class AbstractTreeNode extends TreeNode{

      protected var refreshed:Boolean = false;
      package var leafNode:Boolean = false;

      public-init var appContext:AppContext;

      public var displayName: String;
      protected var icon:Image;

      public var dataObject:IDatabaseObject on replace{
          if (dataObject != null){
              if (not leafNode){
                childNodes = WaitTreeNode{};
              }
              if (dataObject instanceof IDatabaseObject){
                 displayName = (dataObject as IDatabaseObject).getName();
                 data = displayName;
              }
          }
      }

      public function refresh():Void{}

      public function onDoubleClick():Void{
          if (expanded){
            expanded = false;
          }else{
            expanded = true;
          }
      }

      public function onExpand():Void{
           if (not refreshed){
               refresh();
           }
      }

      postinit{
          renderer = ExplorerNodeRenderer{
                 appContext: appContext
                 treeNode: this
              };
      }

      protected function showTbdMessage(){
          var informationDialog = InformationDialog{
               //message: "Error message"
               appContext: bind appContext
               message: "Action not yet implemented."
          };
          informationDialog.show();
      }
}

/*
 * TableNode.fx
 *
 * Created on Apr 14, 2009, 11:12:37 PM
 */

package com.jfxtools.database.schemadesigner;

import com.jfxtools.database.api.IColumn;
import javafx.scene.CustomNode;
import javafx.scene.effect.Reflection;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.jfxtools.database.resources.Images;

/**
 * @author Winston Prakash
 */

public def WIDTH = 150;
public def HEIGHT = 20;

public class ColumnNode extends CustomNode{

    public var column:IColumn;
    public var x = 10;
    public var y = 10;
    public var text = "";
    
    var group:Group;
    var columnFigure:Node;
    var startDragX:Number;
    var startDragY:Number;

    protected override function create():Node {
        translateX = x;
        translateY = y;
        cache = true;
        columnFigure = createFigure(120, text);
        group = Group {
            cache: true
            blocksMouse: true
            content: [
                columnFigure
            ]
            effect: Reflection { fraction: 0.05 };

//            onMousePressed: function(e:MouseEvent):Void {
//                    toFront();
//                    startDragX = translateX;
//                    startDragY = translateY;
//
//            }
//            onMouseDragged: function(e:MouseEvent):Void {
//                    translateX = startDragX + e.dragX;
//                    translateY = startDragY + e.dragY;
//            }

       }
        return group;
    }
     
    function getImage():Image{
        if (column.isPrimaryKey()){
            return Images.columnPrimaryKey;
        }else if (column.isForeignKey()){
            return Images.columnForeignKey;
        }else if (column.isIndexKey()){
            return Images.columnIndexKey;
        }else if (not column.isNullAllowed()){
            return Images.columnNotNull;
        }else{
            Images.column;
        }
    }

    var imageView = ImageView {
        x: 3;
        y: 3;
        image: getImage();
    }
    function createFigure(width: Integer, text:String):Node{
        var columnHeight = 20;
        return Group {
            cache: true;
            content: [
                imageView,
                Text {
                    x: 20;
                    y: 14
                    fill: Color.WHITE;
                    font: Font {
                        size: 14
                        embolden:true;
                    };
                    textAlignment: TextAlignment.CENTER;
                    content: text;
               }
            ]
        }
    }

}

/*
 * Connection.fx
 *
 * Created on Nov 16, 2009, 8:09:48 PM
 */

package com.jfxtools.database.schemadesigner;

import javafx.geometry.Bounds;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 * Quadratic line connecting two vertex
 * @author Winston Prakash
 */

public class ConnectionLine extends CustomNode {

    public var startCenter:Bounds;
    public var endCenter:Bounds;

    public var sx = 10.;
    public var sy = 10.;
    public var ex = 200.;
    public var ey = 200.;
    var dist = bind calcDistance(sx,ex);

    function curve(color:Color,width:Float):CubicCurve {
        CubicCurve { stroke:color strokeWidth: width fill: null
            startX: bind sx + 3 startY: bind  sy + 3 controlX1: bind sx + dist controlY1: bind sy + 3
            controlX2: bind ex - dist controlY2: bind ey + 3  endX: bind ex + 3 endY: bind ey + 3  }
    }


    override public function create():Node {

        return Group {
            content: [
                CubicCurve {
                    stroke:Color.LIGHTBLUE
                    strokeWidth: 5
                    fill: null
                    startX: bind sx + 3
                    startY: bind sy + 3
                    controlX1: bind sx + dist
                    controlY1: bind sy + 3

                    controlX2: bind ex - dist
                    controlY2: bind ey + 3
                    endX: bind ex + 3
                    endY: bind ey + 3
                }

            ]
        }
    }

    function calcDistance(sx,ex) {
        var dist = ex-sx;
        if(dist > 0 and dist < 100) {
            return dist;
        }
        if(dist < 0 and dist > -100) {
            return -dist;
        }

        return 100;
    }
}
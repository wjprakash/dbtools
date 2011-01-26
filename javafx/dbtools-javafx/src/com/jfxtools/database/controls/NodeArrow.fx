/*
 * Triangle.fx
 *
 * Created on Feb 3, 2010, 4:35:48 PM
 */
package com.jfxtools.database.controls;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.paint.Color;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.transition.RotateTransition;

/**
 * Triangle Shape
 * @author winstonp
 */

public class NodeArrow extends CustomNode {

    public var expanded = false on replace {
        if (expanded) {
            rotate = 90;
        } else {
            rotate = 0;
        }
    }

    public-init var shadowRadius = 2;
    public-init var shadowSpread = .2;
    public-init var action: function();

    public var width: Number = 20;
    public var height: Number = 20;
    var boundingRect = Rectangle {
                width: bind width
                height: bind height
                fill: Color.TRANSPARENT
            }
    var path = Path {
                elements: bind getPathElements()
                stroke: Color.BLACK
                strokeWidth: 1
                fill: LinearGradient {
                    startX: 0
                    startY: 0.5
                    endX: 1
                    endY: 0.5
                    proportional: true
                    stops: [Stop {
                            offset: 0
                            color: Color {red: 0.251, green: 0.106, blue: 0.004}
                        }, Stop {
                            offset: 1
                            color: Color {red: 0.475, green: 0.133, blue: 0.024}
                        }]
                }
            }
    var defaultEffect: Effect;
    public override var onMouseEntered = function (e) {
                defaultEffect = effect;
                effect = DropShadow {
                    color: Color.BROWN
                    spread: bind shadowSpread
                    radius: bind shadowRadius
                }
            }
    public override var onMouseExited = function (e) {
                effect = defaultEffect;
            }
    public override var onMouseReleased = function (e) {
                animate();
            }
    var content = Group {
                content: [boundingRect, path]
            }
    public override var effect = DropShadow {
                spread: bind shadowSpread
                radius: bind shadowRadius
                color: Color {red: 0.286, green: 0.133, blue: 0.02}
            }

    override protected function create(): Node {
        return content;
    }

    function animate() {
        var rotTransition = RotateTransition {
                    duration: .2s node: this
                    byAngle: if (expanded) -90 else 90
                    action: function(){
                       expanded = not expanded;
                       action();
                    }
                }
        rotTransition.play();
    }

    function getPathElements(): PathElement[] {
        return [
                    MoveTo {
                        x: bind boundingRect.x
                        y: bind boundingRect.y
                    },
                    LineTo {
                        x: bind boundingRect.x + boundingRect.width
                        y: bind boundingRect.y + boundingRect.height / 2
                    }
                    LineTo {
                        x: bind boundingRect.x
                        y: bind boundingRect.y + boundingRect.height
                    }
                    ClosePath {}
                ]
        }
}

function run()   {
    Stage {
        scene: Scene {
            width: 400 height: 300
            content: [
                NodeArrow {
                    layoutX: 20
                    layoutY: 20
                    width: 10
                    height: 10
                }
                NodeArrow {
                    layoutX: 20
                    layoutY: 50
                    expanded: true
                    width: 10
                    height: 10
                }
            ]
        }
    }
}

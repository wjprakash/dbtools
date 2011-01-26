/*
 * ArrowButton.fx
 *
 * Created on Dec 23, 2009, 12:57:35 PM
 */
package com.jfxtools.database.controls;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.Group;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.PathElement;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.effect.Effect;
import com.jfxtools.database.Constants;

/**
 * Arrow Button
 * @author Winston Prakash
 */
public def LEFT = 1;
public def RIGHT = 2;

public class ArrowButton extends CustomNode {

    public-init  var direction = RIGHT   ;

    public-init var shadowRadius = 5;
    public-init var shadowSpread = .5;
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
                action();
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

    function getPathElements(): PathElement[] {
        if (direction == RIGHT) {
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
        } else {
            return [
                        MoveTo {
                            x: bind boundingRect.x + boundingRect.width
                            y: bind boundingRect.y
                        },
                        LineTo {
                            x: bind boundingRect.x
                            y: bind boundingRect.y + boundingRect.height / 2
                        }
                        LineTo {
                            x: bind boundingRect.x + boundingRect.width
                            y: bind boundingRect.y + boundingRect.height
                        }
                        ClosePath {}
                    ]
        }
    }
}

function run() {
    var monthNameLabel: Label = Label {
                width: bind 150
                height: 20
                hpos: HPos.CENTER
                vpos: VPos.CENTER
                text: bind "December 2009"
                font: Constants.fontBoldExtraLarge3
            }
    var leftNavButton = ArrowButton {
                direction: ArrowButton.LEFT
                width: 20
                height: 15
                action: function () {

                    }
            }
    var rightNavButton = ArrowButton {
                direction: ArrowButton.RIGHT
                width: 20
                height: 15
                action: function () {

                    }
            }
    var header = HBox {
                width: 400
                height: 100
                vpos: VPos.CENTER
                hpos: HPos.CENTER
                spacing: 3
                content: [leftNavButton, monthNameLabel, rightNavButton]
            }
    Stage {
        title: "Application title"
        scene: Scene {
            width: 400
            height: 100
            content: [
                header
            ]
        }
    }
    }

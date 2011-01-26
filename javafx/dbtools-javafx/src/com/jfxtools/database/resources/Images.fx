/*
 * Images.fx
 *
 * Created on Apr 18, 2009, 12:10:56 AM
 */

package com.jfxtools.database.resources;

import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.LayoutInfo;

/**
 * Factory class to get the images
 * @author Winston Prakash
 */

public def default = Image {url: "{__DIR__}default.png"}

public def wait = Image {url: "{__DIR__}wait.png"}

public def database = Image {url: "{__DIR__}database.png"}

public def databaseGroup = Image {url: "{__DIR__}databaseGroup.png"}

public def schema = Image {url: "{__DIR__}schema.gif"}

public def table = Image {url: "{__DIR__}table.gif"}

public def tableGroup = Image {url: "{__DIR__}tableGroup.png"}

public def view = Image {url: "{__DIR__}view.gif"}

public def viewGroup = Image {url: "{__DIR__}viewGroup.png"}

public def column = Image {url: "{__DIR__}column.gif"}

public def columnPrimaryKey = Image {url: "{__DIR__}columnPrimary.png"}

public def columnForeignKey = Image {url: "{__DIR__}columnForeign.png"}

public def columnIndexKey = Image {url: "{__DIR__}columnIndex.png"}

public def columnNotNull = Image {url: "{__DIR__}columnNotNull.gif"}

public function run() {

    var scene:Scene = Scene {
        width: 200
        height: 300
        content: [
            ImageView {
                 fitWidth: 16
                 fitHeight: 16
                 image: databaseGroup
                 layoutInfo: LayoutInfo {
                     vpos: VPos.CENTER
                 }
             }
        ];
    };

    var stage:Stage = Stage {
        width: 200
        height: 300
        title: "Image Resources"
        scene: scene
    }
}
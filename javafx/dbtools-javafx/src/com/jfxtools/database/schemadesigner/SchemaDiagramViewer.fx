/*
 * SchemaDiagramViewer.fx
 *
 * Created on Apr 14, 2009, 9:32:20 PM
 */

package com.jfxtools.database.schemadesigner;

import com.jfxtools.database.DatabaseInfo;
import com.jfxtools.database.model.Connection;
import com.jfxtools.database.model.Schema;
import com.jfxtools.database.schemadesigner.SchemaNode;
import com.jfxtools.database.tests.ConnectionUtils;
import com.jfxtools.database.AppContext;
import com.jfxtools.database.api.ISchema;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Panel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import com.sun.javafx.scene.control.ScrollView;
import javafx.scene.layout.LayoutInfo;

/**
 * @author Winston Prakash
 */
public class SchemaDiagramViewer extends Panel{
    public-init var schema:ISchema;

    public override var width on replace{
        schemaNode.width = width;
    }

    public override var height on replace{
        schemaNode.height = height;
    }

    public-init var appContext:AppContext;

    var background = Rectangle {
        width: bind width
        height: bind height
        fill: Color.BLACK
        arcHeight: 12
        arcWidth: 12
    }
    
    var schemaNode:SchemaNode = SchemaNode {
        height: height
        width:  width
        schema: schema;
    }

    var scrollView = ScrollView {
        layoutX: 2
        layoutY: 2
        width: bind width - 4
        height: bind height - 4
        node: schemaNode
    }

    postinit{
        blocksMouse = true;
        content = [
           background,
           scrollView
        ];
    }
}

public function run() {
    var connectionConfig = ConnectionUtils
    					.createMySqlConnectionConfig();
    var databaseInfo = new DatabaseInfo(connectionConfig);
    var connection = new Connection(databaseInfo);
    connection.refresh();
    connection.refresh();
    var schemas = connection.getSchemaList();
    var schema:Schema;

    for (obj in schemas) {
        schema = obj as Schema;
        if (schema.getName().equals("sakila")){
            break;
        }
    }
    
    var schemaDesigner:SchemaDiagramViewer = SchemaDiagramViewer {
        width: bind scene.width
        height: bind scene.height
        schema: schema;
    }
    
    var scene:Scene = Scene {
        content: schemaDesigner
    }

    Stage {
        width: 900
        height: 900
        title: "Database Schema Viewer"
        scene: scene
    }
}
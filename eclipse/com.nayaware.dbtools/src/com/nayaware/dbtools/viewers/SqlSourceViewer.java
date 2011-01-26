
package com.nayaware.dbtools.viewers;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.nayaware.dbtools.editors.sql.SqlEditorColorManager;
import com.nayaware.dbtools.editors.sql.SqlPartitionScanner;
import com.nayaware.dbtools.editors.sql.SqlSourceViewerConfiguration;

/**
 * SQL source viewer for Query Builder and Schema Designer
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlSourceViewer extends SourceViewer {

	private static final int VERTICAL_RULER_WIDTH = 12;

	private SqlEditorColorManager colorManager;

	public SqlSourceViewer(Composite parent) {
		super(parent, null, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		//super(parent, null, SWT.NONE);
		this.colorManager = new SqlEditorColorManager();
		IDocument document = new Document();
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(
					new SqlPartitionScanner(), new String[] {
							SqlPartitionScanner.SQL_COMMENT,
							SqlPartitionScanner.SQL_STRING });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		this.setDocument(document);
		configure(new SqlSourceViewerConfiguration(colorManager));
	}
}


package com.nayaware.dbtools.editors.sql;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * Source Configuration for the SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlSourceViewerConfiguration extends TextSourceViewerConfiguration {

	private SqlEditorColorManager colorManager;

	public SqlSourceViewerConfiguration(SqlEditorColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer commentDamagerRepairer = new DefaultDamagerRepairer(
				new SingleTokenScanner(new TextAttribute(colorManager
						.getColor(SqlEditorColorManager.COLOR_COMMENT))));
		reconciler.setDamager(commentDamagerRepairer,
				SqlPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(commentDamagerRepairer,
				SqlPartitionScanner.SQL_COMMENT);

		DefaultDamagerRepairer stringDamagerRepairer = new DefaultDamagerRepairer(
				new SingleTokenScanner(new TextAttribute(colorManager
						.getColor(SqlEditorColorManager.COLOR_STRING))));
		reconciler.setDamager(stringDamagerRepairer,
				SqlPartitionScanner.SQL_STRING);
		reconciler.setRepairer(stringDamagerRepairer,
				SqlPartitionScanner.SQL_STRING);

		DefaultDamagerRepairer keywordDamagerRepairer = new DefaultDamagerRepairer(
				new SqlKeywordScanner(colorManager));
		reconciler.setDamager(keywordDamagerRepairer,
				IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keywordDamagerRepairer,
				IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	/**
	 * Setup processor for content assist
	 */
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		SqlContentAssistantProcessor processor = new SqlContentAssistantProcessor();
		assistant.setContentAssistProcessor(processor,
				IDocument.DEFAULT_CONTENT_TYPE);
		assistant
				.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(100);
		assistant.enableAutoInsert(true);
		return assistant;
	}

	private static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	};

}

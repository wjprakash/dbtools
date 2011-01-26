
package com.nayaware.dbtools.editors.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationPresenter;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;

import com.nayaware.dbtools.editors.sql.templates.SqlTemplateContextType;
import com.nayaware.dbtools.editors.sql.templates.SqlTemplateManager;
import com.nayaware.dbtools.util.ImageUtils;

/**
 * Content assistant processor for SQL Editor
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class SqlContentAssistantProcessor extends TemplateCompletionProcessor {

	protected IContextInformationValidator fValidator = new Validator();
	protected ITextViewer viewer;
	protected Set<String> proposals = new TreeSet<String>();

	@Override
	protected Image getImage(Template template) {
		return ImageUtils.getIcon(ImageUtils.SQL);
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		return SqlTemplateManager.getInstance().getTemplateStore()
				.getTemplates();
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '.' };
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int documentOffset) {
		this.viewer = viewer;
		WordPartDetector wordPart = new WordPartDetector(viewer, documentOffset);

		String[] sqlKeywords = SqlKeywordUtils.getAll();
		// iterate over all the different categories

		for (String keyword : sqlKeywords) {
			if (keyword.startsWith(wordPart.getString().toUpperCase())) {
				proposals.add(keyword);
			}
		}

		return turnProposalVectorIntoAdaptedArray(wordPart, viewer,
				documentOffset);
	}

	/*
	 * Turns the vector into an Array of ICompletionProposal objects
	 */
	protected ICompletionProposal[] turnProposalVectorIntoAdaptedArray(
			WordPartDetector word, ITextViewer viewer, int documentOffset) {
		ICompletionProposal[] templates = determineTemplateProposals(viewer,
				documentOffset);
		ICompletionProposal[] result = new ICompletionProposal[proposals.size()
				+ templates.length];
		int index = 0;
		for (int i = 0; i < templates.length; i++) {
			result[i] = templates[i];
			index++;
		}
		for (String keyWord : proposals) {
			IContextInformation info = new ContextInformation(keyWord,
					getContentInfoString(keyWord));
			// Creates a new completion proposal.
			result[index] = new CompletionProposal(keyWord, word.getOffset(),
					word.getString().length(), keyWord.length(), ImageUtils
							.getIcon(ImageUtils.KEYWORD), keyWord, info,
					getContentInfoString(keyWord));
			index++;
		}
		proposals.clear();
		return result;
	}

	private ICompletionProposal[] determineTemplateProposals(
			ITextViewer refViewer, int documentOffset) {
		String prefix = getCurrentPrefix(viewer.getDocument().get(),
				documentOffset);
		ICompletionProposal[] matchingTemplateProposals;
		if (prefix.length() == 0) {
			matchingTemplateProposals = super.computeCompletionProposals(
					refViewer, documentOffset);
		} else {
			ICompletionProposal[] templateProposals = super
					.computeCompletionProposals(refViewer, documentOffset);
			List<ICompletionProposal> templateProposalList = new ArrayList<ICompletionProposal>(
					templateProposals.length);
			for (int i = 0; i < templateProposals.length; i++) {
				if (templateProposals[i].getDisplayString().toLowerCase()
						.startsWith(prefix)) {
					templateProposalList.add(templateProposals[i]);
				}
			}
			matchingTemplateProposals = templateProposalList
					.toArray(new ICompletionProposal[templateProposalList
							.size()]);
		}
		return matchingTemplateProposals;
	}

	protected String getCurrentPrefix(String documentString, int documentOffset) {
		int tokenLength = 0;
		while ((documentOffset - tokenLength > 0)
				&& !Character.isWhitespace(documentString.charAt(documentOffset
						- tokenLength - 1))) {
			tokenLength++;
		}
		return documentString.substring((documentOffset - tokenLength),
				documentOffset);
	}

	/**
	 * Method getContentInfoString.
	 * 
	 * @param keyWord
	 */
	private String getContentInfoString(String keyWord) {
		String resourceString = ""; //$NON-NLS-1$
		// String resourceKey = "ContextString." + keyWord;
		// resourceString = DbUIPlugin.getResourceString(resourceKey);
		// if (resourceString.equals(keyWord)) {
		// resourceString = "No Context Info String";
		// }
		return resourceString;
	}

	/**
	 * This method is incomplete in that it does not implement logic to produce
	 * some context help relevant to SQL. It just hard codes two strings to
	 * demonstrate the action
	 * 
	 */
	// public IContextInformation[] computeContextInformation(ITextViewer
	// viewer,
	// int documentOffset) {
	//
	// IContextInformation[] result = new IContextInformation[2];
	// result[0] = new ContextInformation("contextDisplayString",
	// "informationDisplayString");
	// result[1] = new ContextInformation("contextDisplayString2",
	// "informationDisplayString2");
	//
	// return result;
	// }
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return fValidator;
	}

	/**
	 * Simple content assist tip closer. The tip is valid in a range of 5
	 * characters around its popup location.
	 */
	protected static class Validator implements IContextInformationValidator,
			IContextInformationPresenter {

		protected int fInstallOffset;

		public boolean isContextInformationValid(int offset) {
			return Math.abs(fInstallOffset - offset) < 5;
		}

		public void install(IContextInformation info, ITextViewer viewer,
				int offset) {
			fInstallOffset = offset;
		}

		public boolean updatePresentation(int documentPosition,
				TextPresentation presentation) {
			return false;
		}
	}

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		return SqlTemplateManager.getInstance().getContextTypeRegistry()
				.getContextType(SqlTemplateContextType.CONTEXT_TYPE_SQL);
	}

	private static class WordPartDetector {
		String wordPart = ""; //$NON-NLS-1$
		int docOffset;

		public WordPartDetector(ITextViewer viewer, int documentOffset) {
			docOffset = documentOffset - 1;
			try {
				while (((docOffset) >= viewer.getTopIndexStartOffset())
						&& Character.isLetterOrDigit(viewer.getDocument()
								.getChar(docOffset))) {
					docOffset--;
				}
				// we've been one step too far : increase the offset
				docOffset++;
				wordPart = viewer.getDocument().get(docOffset,
						documentOffset - docOffset);
			} catch (BadLocationException e) {
				// do nothing
			}
		}

		public String getString() {
			return wordPart;
		}

		public int getOffset() {
			return docOffset;
		}

	}
}

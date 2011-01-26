/**
 * This file Copyright (c) 2005-2008 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.nayaware.dbtools.schemadesigner.commands;

import org.eclipse.gef.commands.Command;

import com.nayaware.dbtools.schemadesigner.model.SdColumnNode;
import com.nayaware.dbtools.schemadesigner.model.SdTableNode;
import com.nayaware.dbtools.util.ErrorManager;

/**
 * Direct edit command that allows to edit and set the Table Node name
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class ColumnNodeDirectEditCommand extends Command {
	private String oldName;
	private String newName;

	private SdColumnNode columnNode;

	public ColumnNodeDirectEditCommand(SdColumnNode columnNode2, String newName) {
		this.columnNode = columnNode2;
		this.newName = newName;
	}

	@Override
	public boolean canExecute() {
		if ((newName == null) || newName.trim().equals("")) { //$NON-NLS-1$
			ErrorManager.showError(Messages.getString("ColumnNodeDirectEditCommand.1")); //$NON-NLS-1$
			return false;
		}
		SdTableNode tableNode = columnNode.getParent();
		if (tableNode.findColumnNodeByName(newName) != null) {
			ErrorManager.showError(Messages.getString("ColumnNodeDirectEditCommand.2")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		oldName = columnNode.getName();
		columnNode.setName(newName);
	}

	public void setName(String name) {
		newName = name;
	}

	@Override
	public void undo() {
		columnNode.setName(oldName);
	}
}

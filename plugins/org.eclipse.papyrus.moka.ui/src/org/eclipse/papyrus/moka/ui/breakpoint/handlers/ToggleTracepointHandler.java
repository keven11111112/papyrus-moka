/*****************************************************************************
 * Copyright (c) 2012 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Ansgar Radermacher (CEA LIST) - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.moka.ui.breakpoint.handlers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.moka.ui.breakpoint.commands.ToggleTracepointCommand;

/**
 * Handler for toggling a tracepoint. Delegates to associated command
 *
 * @author Ansgar Radermacher (CEA LIST)
 */
public class ToggleTracepointHandler extends AbstractTraceAndDebugCommandHandler {

	@Override
	protected Command getCommand() {
		// not useful to cache command, since selected element may change
		return new GMFtoEMFCommandWrapper(new ToggleTracepointCommand(getSelectedElement()));
	}
}

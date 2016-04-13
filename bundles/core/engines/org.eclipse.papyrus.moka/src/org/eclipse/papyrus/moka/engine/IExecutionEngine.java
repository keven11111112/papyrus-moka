/*****************************************************************************
 * Copyright (c) 2013 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  CEA LIST - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.papyrus.moka.engine;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.emf.ecore.EObject;

public interface IExecutionEngine extends IStreamsProxy {

	public void init(ILaunch launch, final EObject executionEntryPoint, String[] executionArgs);

	public void start(IProgressMonitor monitor);

	public void stop(IProgressMonitor monitor);

}
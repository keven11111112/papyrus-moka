/*****************************************************************************
 * Copyright (c) 2016 CEA LIST and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *   
 *****************************************************************************/

package org.eclipse.papyrus.moka.fuml.actions;

import java.util.List;

import org.eclipse.papyrus.moka.fuml.simpleclassifiers.IBooleanValue;

public interface IClauseActivation {

	public void receiveControl();

	public Boolean isReady();

	public void runTest();

	public void selectBody();

	public IBooleanValue getDecision();

	public List<IClauseActivation> getPredecessors();

	public List<IClauseActivation> getSuccessors();
}

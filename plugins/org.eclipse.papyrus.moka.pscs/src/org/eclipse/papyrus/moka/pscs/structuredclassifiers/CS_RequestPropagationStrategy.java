/*****************************************************************************
 * Copyright (c) 2013 CEA LIST.
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
 *  CEA LIST - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.papyrus.moka.pscs.structuredclassifiers;

// Imports
import java.util.List;

import org.eclipse.papyrus.moka.fuml.loci.ISemanticVisitor;
import org.eclipse.papyrus.moka.fuml.loci.SemanticStrategy;
import org.eclipse.papyrus.moka.fuml.structuredclassifiers.IReference;


public abstract class CS_RequestPropagationStrategy extends SemanticStrategy {

	@Override
	public String getName() {
		// a CS_RequestPropagationStrategy are always named "requestPropagation"
		return "requestPropagation";
	}

	public abstract List<IReference> select(List<IReference> potentialTargets, ISemanticVisitor context);
}

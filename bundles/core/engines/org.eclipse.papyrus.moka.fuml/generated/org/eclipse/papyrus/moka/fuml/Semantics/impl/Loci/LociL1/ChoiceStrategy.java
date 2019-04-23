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
 *  CEA LIST - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.papyrus.moka.fuml.Semantics.impl.Loci.LociL1;

import org.eclipse.papyrus.moka.fuml.Semantics.Loci.LociL1.IChoiceStrategy;

public abstract class ChoiceStrategy extends SemanticStrategy implements IChoiceStrategy{

	@Override
	public String getName() {
		// The name of a choice strategy is always "choice".
		return "choice";
	}

	public abstract Integer choose(Integer size);
}

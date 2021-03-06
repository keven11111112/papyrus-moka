/*****************************************************************************
 * Copyright (c) 2014 CEA LIST.
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
 *****************************************************************************/
package org.eclipse.papyrus.moka.fuml.actions;

import org.eclipse.papyrus.moka.fuml.loci.ILocus;
import org.eclipse.papyrus.moka.fuml.loci.SemanticStrategy;
import org.eclipse.papyrus.moka.fuml.structuredclassifiers.IObject_;
import org.eclipse.uml2.uml.Class;

public abstract class CreateObjectActionStrategy extends SemanticStrategy {

	public abstract IObject_ instantiate(Class type, ILocus locus);

	@Override
	public String getName() {
		return "CreateObjectActionStrategy";
	}
}

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

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StructuralFeature;

public class DefaultGetAssociationStrategy extends GetAssociationStrategy {

	@Override
	public Association getAssociation(StructuralFeature feature) {
		// If the structural feature for the action of this activation is an
		// association end,
		// then get the associated association.
		Association association = null;
		if (feature instanceof Property) {
			association = ((Property) feature).getAssociation();
		}
		return association;
	}
}

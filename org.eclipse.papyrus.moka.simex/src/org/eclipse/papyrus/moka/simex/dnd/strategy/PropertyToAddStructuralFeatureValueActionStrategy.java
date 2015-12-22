/*****************************************************************************
 * Copyright (c) 2015 CEA LIST and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *   
 *****************************************************************************/

package org.eclipse.papyrus.moka.simex.dnd.strategy;

import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Drop strategy to create a AddStructuralFeatureAction from a Property drop and to update
 * the "structuralFeature" reference.
 * 
 * @author SR246418
 *
 */
public class PropertyToAddStructuralFeatureValueActionStrategy extends AbstractDropInActivityStrategy {


	public PropertyToAddStructuralFeatureValueActionStrategy() {
		super(UMLPackage.eINSTANCE.getProperty(), UMLElementTypes.ADD_STRUCTURAL_FEATURE_VALUE_ACTION, UMLPackage.eINSTANCE.getStructuralFeatureAction_StructuralFeature());
		setNamePrefix("set");
	}

}

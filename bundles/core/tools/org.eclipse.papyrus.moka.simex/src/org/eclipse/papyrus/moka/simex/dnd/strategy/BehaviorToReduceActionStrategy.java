/*****************************************************************************
 * Copyright (c) 2015 CEA LIST and others.
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

package org.eclipse.papyrus.moka.simex.dnd.strategy;

import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Drop strategy to create a ReduceAction from a Behavior drop and to update
 * the "reducer" reference.
 * 
 * @author SR246418
 *
 */
public class BehaviorToReduceActionStrategy extends AbstractDropInActivityStrategy {

	public BehaviorToReduceActionStrategy() {
		super(UMLPackage.eINSTANCE.getBehavior(), UMLElementTypes.REDUCE_ACTION, UMLPackage.eINSTANCE.getReduceAction_Reducer());
		setNamePrefix("reduceWith");
	}

}

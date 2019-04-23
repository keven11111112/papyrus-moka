/*****************************************************************************
 * 
 * Copyright (c) 2016 CEA LIST.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  CEA LIST Initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.papyrus.moka.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;



public class ResourceSetUtils {

	private static ResourceSet internalResSet;
	
	public static ResourceSet getResourceSet(EObject context){
		if (context != null && context.eResource() != null && context.eResource().getResourceSet() != null){
			return context.eResource().getResourceSet();
		}else {
			return null;
		}
	}

	public static ResourceSet getDefaultResourceSet() {
		if (internalResSet == null){
			internalResSet = new ResourceSetImpl();
		}
		return internalResSet;
	}
}

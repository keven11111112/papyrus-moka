/*****************************************************************************
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
 *****************************************************************************/

package org.eclipse.papyrus.moka.fuml.profiling.loci;

import org.eclipse.papyrus.moka.fuml.loci.ILocus;
import org.eclipse.papyrus.moka.fuml.structuredclassifiers.IExtensionalValue;
import org.eclipse.papyrus.moka.fuml.structuredclassifiers.IObject_;
import org.eclipse.uml2.uml.Class;

public aspect LocusProfiler extends ValueLifecycleObservable{
	
	pointcut instantiate(ILocus locus, Class type): 
		target(locus) &&
		args(type) &&
		call(* ILocus.instantiate(Class));

	after(ILocus locus, Class type) returning(IObject_ instance) : instantiate(locus, type){
		this.fireValueCreated(instance);
	}
	
	pointcut destroy(ILocus locus, IExtensionalValue value): 
		target(locus) &&
		args(value) &&
		call(* ILocus.remove(IExtensionalValue));
	
	after(ILocus locus, IExtensionalValue value): destroy(locus, value){
		this.fireValueDestroyed(value);
	}
	
}
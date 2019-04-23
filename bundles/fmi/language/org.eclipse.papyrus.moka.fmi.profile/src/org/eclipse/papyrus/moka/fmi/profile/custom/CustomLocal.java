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
package org.eclipse.papyrus.moka.fmi.profile.custom;

import org.eclipse.papyrus.moka.fmi.fmiprofile.FMIProfileFactory;
import org.eclipse.papyrus.moka.fmi.fmiprofile.FMIProfilePackage;
import org.eclipse.papyrus.moka.fmi.fmiprofile.InitialKind;
import org.eclipse.papyrus.moka.fmi.fmiprofile.VariabilityKind;
import org.eclipse.papyrus.moka.fmi.fmiprofile.impl.LocalImpl;
import org.eclipse.papyrus.moka.fmi.modeldescription.Fmi2ScalarVariable;
import org.eclipse.papyrus.moka.fmi.profile.util.FMIProfileUtil;

public class CustomLocal extends LocalImpl {

	@Override
	public Fmi2ScalarVariable getFmiVariable() {
		if (fmiVariable != null){
			return fmiVariable;
		}else {
			return FMIProfileUtil.getFMIVariable(base_Property);
		}
	}
	@Override
	public String getDescription() {
		if (getFmiVariable() != null){
			return getFmiVariable().getDescription();
		}
		return super.getDescription();
	}

	@Override
	public VariabilityKind getVariability() {
		if (getFmiVariable() != null){
			return (VariabilityKind) FMIProfileFactory.eINSTANCE.createFromString(FMIProfilePackage.eINSTANCE.getVariabilityKind(), getFmiVariable().getVariability().getLiteral());
		}
		return super.getVariability();
	}

	@Override
	public InitialKind getInitial() {
		if (getFmiVariable() != null){
			return (InitialKind) FMIProfileFactory.eINSTANCE.createFromString(FMIProfilePackage.eINSTANCE.getInitialKind(), getFmiVariable().getInitial().getLiteral());
		}
		return super.getInitial();
	}

	@Override
	public long getValueReference() {
		if (getFmiVariable() != null){
			return getFmiVariable().getValueReference();
		}
		return super.getValueReference();
	}


}

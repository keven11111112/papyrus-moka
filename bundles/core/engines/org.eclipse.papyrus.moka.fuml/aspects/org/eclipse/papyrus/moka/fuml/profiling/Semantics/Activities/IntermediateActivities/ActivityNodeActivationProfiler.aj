/*****************************************************************************
 * Copyright (c) 2016 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  CEA LIST - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.moka.fuml.profiling.Semantics.Activities.IntermediateActivities;

import java.util.List;

import org.eclipse.papyrus.moka.fuml.Semantics.impl.Activities.IntermediateActivities.ObjectNodeActivation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.papyrus.moka.engine.MokaExecutionEngineJob;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IActivityNodeActivation;
import org.eclipse.papyrus.moka.fuml.profiling.Semantics.Loci.SemanticVisitorProfiler;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IToken;

public aspect ActivityNodeActivationProfiler extends SemanticVisitorProfiler{

	public ActivityNodeActivationProfiler() {
		super();
	}
	
	pointcut fire(IActivityNodeActivation activation, List<IToken> tokens):
		!within(ObjectNodeActivation+) &&
		target(activation) && 
		args(tokens) && 
		call(* IActivityNodeActivation.fire(List<IToken>));
	
	before(IActivityNodeActivation activation, List<IToken> tokens): fire(activation, tokens) {
		IProgressMonitor monitor = MokaExecutionEngineJob.getInstance().getMonitor();
		if(monitor!=null && monitor.isCanceled()){
			throw new OperationCanceledException();
		}
		this.fireNodeVisited(activation);
	}
	
}
/*****************************************************************************
 * Copyright (c) 2016 CEA LIST.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  CEA LIST Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.moka.animation.engine.rendering;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IObject_;

public interface IRenderingEngine {

	public boolean isRenderingRuleApplied(EObject modelElement, AnimationKind kind);

	public void removeRenderingRules(EObject modelElement);

	public void startRendering(EObject modelElement, IObject_ animator, AnimationKind kind);

	public void stopRendering(EObject modelElement, IObject_ animator, AnimationKind kind);

}
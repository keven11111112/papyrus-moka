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
 *   Jeremie Tatibouet (CEA) - Apply Fix fUML12-35 Initial execution of an activity is not run to completion
 *   
 *****************************************************************************/

package org.eclipse.papyrus.moka.fuml.commonbehavior;

import java.util.List;

import org.eclipse.papyrus.moka.fuml.structuredclassifiers.IReference;
import org.eclipse.papyrus.moka.fuml.tasks.IUMLEventSendingExecution;
import org.eclipse.papyrus.moka.fuml.tasks.IUMLTaskExecutionFactory;
import org.eclipse.uml2.uml.Trigger;

/**
 * An event occurrence represents a single occurrence of a specific kind of
 * event.
 */
public abstract class EventOccurrence implements IEventOccurrence {

	public IReference target;

	public abstract boolean match(Trigger trigger);

	public boolean matchAny(List<Trigger> triggers) {
		// Check that at least one of the given triggers is matched by this
		// event occurrence.
		boolean matches = false;
		int i = 1;
		while (!matches && i <= triggers.size()) {
			if (this.match(triggers.get(i - 1))) {
				matches = true;
			}
			i++;
		}
		return matches;
	}

	public abstract List<IParameterValue> getParameterValues();

	public void sendTo(IReference target) {
		// Set the target reference and start the SendingBehavior, which
		// will send this event occurrence to the target.
		this.target = target;
		this._startObjectBehavior();
	}

	public void doSend() {
		// Send this event occurrence to the target reference.
		this.target.send(this);
	}

	public void _startObjectBehavior() {
		// When the sending behavior starts, the current event
		// occurrence is is forwarded to the target object.
		IUMLTaskExecutionFactory taskFactory = target.getReferent().getLocus().getFactory().getTaskFactory();
		if (taskFactory != null) {
			IUMLEventSendingExecution execution = taskFactory.createEventSendingExecution();
			execution.setEvent(this);
			execution.schedule();
		}
	}

	public void setTarget(IReference target) {
		this.target = target;
	}

	public IReference getTarget() {
		return this.target;
	}
}

package org.eclipse.papyrus.fuml.statemachines.interfaces.Semantics.StateMachines;

public enum TransitionMetadata {
	
	// NONE: The transition exists, however its source state was is not yet reached 
	NONE,
	
	// REACHED: The source vertex of the transition is reached
	REACHED, 
	
	// TRAVERSED: The transition was executed
	TRAVERSED
	
}

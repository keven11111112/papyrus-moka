package org.eclipse.papyrus.fuml.statemachines.interfaces.Semantics.StateMachines;

import java.util.List;

import org.eclipse.papyrus.moka.fuml.Semantics.CommonBehaviors.Communications.IEventOccurrence;
import org.eclipse.uml2.uml.TransitionKind;
import org.eclipse.uml2.uml.Vertex;

public interface IVertexActivation extends IStateMachineSemanticVisitor{
		
	public StateMetadata getStatus();
	
	public void setStatus(StateMetadata status);
	
	public IVertexActivation getParentVertexActivation();
	
	public IRegionActivation getOwningRegionActivation();

	public void addIncomingTransition(ITransitionActivation activation);
	
	public void addOutgoingTransition(ITransitionActivation activation);
	
	public List<ITransitionActivation> getOutgoingTransitions();
	
	public List<ITransitionActivation> getIncomingTransitions();
	
	public IVertexActivation getVertexActivation(Vertex vertex);
	
	public List<IVertexActivation> getAscendingHierarchy();
	
	public void enter(ITransitionActivation enteringTransition, IEventOccurrence eventOccurrence,  IRegionActivation leastCommonAncestor);
	
	public void exit(ITransitionActivation exitingTransition, IEventOccurrence eventOccurrence, IRegionActivation leastCommonAncestor);
	
	public boolean isActive();
	
	public IRegionActivation getLeastCommonAncestor(IVertexActivation targetVertexActivation, TransitionKind transitionKind);
	
	public boolean isEnterable(ITransitionActivation enteringTransition, boolean staticCheck);
	
	public boolean isExitable(ITransitionActivation exitingTransition, boolean staticCheck);
	
	public void terminate();
	
	public boolean canPropagateExecution(ITransitionActivation enteringTransition, IEventOccurrence eventOccurrence , IRegionActivation leastCommonAncestor);
	
}

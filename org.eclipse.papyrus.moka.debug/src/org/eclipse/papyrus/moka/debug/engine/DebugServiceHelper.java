package org.eclipse.papyrus.moka.debug.engine;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.moka.animation.engine.IAnimation;
import org.eclipse.papyrus.moka.debug.breakpoint.MokaBreakpoint;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IActivityEdgeInstance;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IActivityNodeActivation;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IObject_;
import org.eclipse.papyrus.moka.fuml.Semantics.Loci.LociL1.ISemanticVisitor;
import org.eclipse.papyrus.moka.service.IMokaService;
import org.eclipse.papyrus.moka.service.MokaServiceRegistry;
import org.eclipse.papyrus.moka.utils.constants.MokaConstants;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;

public class DebugServiceHelper {
	
	private DebugServiceHelper(){}
	
	public static DebugServiceHelper INSTANCE = new DebugServiceHelper(); 
	
	public boolean isActive(IObject_ value){
		boolean active = false;
		Iterator<Classifier> typesIterator = value.getTypes().iterator(); 
		while(!active){
			Classifier type = typesIterator.next();
			if(type instanceof Class){
				active = ((Class)type).isActive() && !((Class)type).isAbstract(); 
			}
		}
		return active;
	}
	
	public boolean hasBreakpoint(ISemanticVisitor visitor){
		boolean breakpointExist = false;
		IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint[] breakpoints = breakpointManager.getBreakpoints(MokaConstants.MOKA_DEBUG_MODEL_ID);
		EObject visitedModelElement = null;
		if(visitor instanceof IActivityNodeActivation){
			visitedModelElement = ((IActivityNodeActivation)visitor).getNode();
		}else if(visitor instanceof IActivityEdgeInstance){
			visitedModelElement = ((IActivityEdgeInstance)visitor).getEdge();
		}
		if(visitedModelElement!=null){
			int i = 0;
			while(!breakpointExist && i < breakpoints.length){
				MokaBreakpoint breakpoint = (MokaBreakpoint) breakpoints[i]; 
				boolean isEnabled = false;
				try {
					isEnabled = breakpoint.isEnabled();
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if(isEnabled && visitedModelElement == breakpoint.getModelElement()){
					breakpointExist = true;
				}
				i++;
			}
		}
		return breakpointExist;
	}
	
	
	public IAnimation getAnimationService(){
		IAnimation animationService = null;
		List<IMokaService> services = MokaServiceRegistry.getInstance().getService(IAnimation.class);
		if(!services.isEmpty()){
			if(services.size() == 1){
				animationService = (IAnimation) services.iterator().next();
			}else{
				// TODO: handle via a strategy (e.g., the animation service with the highest priority)
			}
		}
		return animationService;
	}
	
}
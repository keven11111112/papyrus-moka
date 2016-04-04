package org.eclipse.papyrus.moka.debug.model.data.mapping.values;

import org.eclipse.papyrus.moka.debug.engine.MokaDebugTarget;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IForkedToken;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IObjectToken;
import org.eclipse.papyrus.moka.fuml.Semantics.Activities.IntermediateActivities.IToken;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IObject_;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IPrimitiveValue;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IStructuredValue;
import org.eclipse.papyrus.moka.fuml.Semantics.Classes.Kernel.IValue;
import org.eclipse.papyrus.moka.fuml.Semantics.CommonBehaviors.Communications.IEventOccurrence;

public class MokaValueAdapterFactory {

	protected static MokaValueAdapterFactory INSTANCE;
	
	private MokaValueAdapterFactory(){
	}
	
	public static MokaValueAdapterFactory getInstance(){
		if(INSTANCE == null){
			INSTANCE = new MokaValueAdapterFactory();
		}
		return INSTANCE;
	}
	
	public MokaValueAdapter instantiate(Object value, MokaDebugTarget debugTarget){
		MokaValueAdapter adapter = null;
		if(value != null){
			if(value instanceof IEventOccurrence){
				adapter = new EventOccurrenceValueAdapter(debugTarget, (IEventOccurrence)value);
			}else if(value instanceof IToken){
				if(value instanceof IObjectToken){
					adapter = new ObjectTokenValueAdapter(debugTarget, (IObjectToken)value);
				}else if(value instanceof IForkedToken){
					adapter = this.instantiate(((IForkedToken)value).getBaseToken(), debugTarget);
				}else{
					adapter = new TokenValueAdapter(debugTarget, (IToken)value);
				}
			}else if(value instanceof IValue){
				if(value instanceof IObject_){
					adapter = new ObjectValueAdapter(debugTarget, (IObject_)value);
				}else if(value instanceof IStructuredValue){
					adapter = new StructuredValueAdapter(debugTarget, (IStructuredValue)value);
				}else if(value instanceof IPrimitiveValue){
					adapter = new PrimitiveValueAdapter(debugTarget, (IPrimitiveValue)value);
				}
			}
		}else{
			adapter = new NullValueAdapter(debugTarget);
		}
		return adapter;
	}
}

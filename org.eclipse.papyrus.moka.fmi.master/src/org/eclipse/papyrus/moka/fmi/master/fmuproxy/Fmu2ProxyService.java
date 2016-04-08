package org.eclipse.papyrus.moka.fmi.master.fmuproxy;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.papyrus.moka.composites.Semantics.impl.CompositeStructures.StructuredClasses.CS_Object;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2CausalityType;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2Parameters;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2Port;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2ScalarVariable;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2Status;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2StatusKind;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2Type;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2VariabilityType;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.Fmi2VariableType;
import org.eclipse.papyrus.moka.fmi.master.fmilibrary.NativeSizeT;
import org.eclipse.papyrus.moka.fmi.master.fmulibrary.Fmu2Library;
import org.eclipse.papyrus.moka.fmi.master.fmulibrary.Fmu2Status;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralReal;
import org.eclipse.uml2.uml.LiteralSpecification;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.PointerByReference;
/**
 * A class implementing the proxy between the master model call and the FMI procedures 
 * Contains calls to JNA invocations with corresponding arguments
 * arguments are retrieved from the co-simulation graph
 * @author sahar.guermazi@cea.fr
 *
 */
public class Fmu2ProxyService extends CS_Object{
	
	public Fmi2Parameters parameters;

	public ArrayList<Fmi2Port> inputPorts = new ArrayList<Fmi2Port>();
	public ArrayList<Fmi2Port> outputPorts = new ArrayList<Fmi2Port>();
	public ArrayList<Fmi2ScalarVariable> variables = new ArrayList<Fmi2ScalarVariable>();
	
	public NativeLibrary dll_lib; 		//has its location and dll path has its location and dll path
	public Pointer component = Pointer.NULL; 	//has to be charged in memory and referenced buy a pointer
	public Pointer fmuState = Pointer.NULL;		//has a statuts --> used to register and restore an fmu status
	Fmu2Library fmuApi;		//uses an fmu2Library which contains the callback functions and JNA invocations
	public int fmi2Status = Fmi2Status.fmi2OK;
	public int fmu2Status =0;
	
	
	private ArrayList<Fmi2ScalarVariable> integerGetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> integerSetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> realGetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> realSetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> booleanGetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> booleanSetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> stringGetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	private ArrayList<Fmi2ScalarVariable> stringSetCachedVariables = new ArrayList<Fmi2ScalarVariable>();
	
	

	public Fmu2ProxyService(Class service) {

		fmuApi = new Fmu2Library();	
		initialize();
	}
	
	
	

	private void initialize() {
		for (Property p: types.get(0).getOwnedAttributes()){
			Stereotype st = p.getAppliedStereotypes().get(0);
			if (st!=null){
				//String variableType = p.getType().getName();
				Fmi2ScalarVariable variable = new Fmi2ScalarVariable(this, p, st);
				variables.add(variable);
				
				if (variable.getCausality().equals(Fmi2CausalityType.fmi2Input)){
					
					switch (variable.getType()) {
					case Fmi2VariableType.fmi2Boolean:
						booleanSetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2Integer:
						integerSetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2Real :
						realSetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2String :
						stringSetCachedVariables.add(variable);
					default:
						break;
					}
					
				}else if (variable.getCausality().equals(Fmi2CausalityType.fmi2Output) ||
						variable.getCausality().equals(Fmi2CausalityType.fmi2Parameter) && !variable.getVariability().equals(Fmi2VariabilityType.fmi2Constant)){
				
					switch (variable.getType()) {
					case Fmi2VariableType.fmi2Boolean:
						booleanGetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2Integer:
						integerGetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2Real :
						realGetCachedVariables.add(variable);
						break;
					case Fmi2VariableType.fmi2String :
						stringGetCachedVariables.add(variable);
					default:
						break;
					}
				}

				
			
			}
		}
				
		
	}


	public void fetchGetCache(){
		fmi2GetReal(realGetCachedVariables);
		fmi2GetInteger(integerGetCachedVariables);
		fmi2GetBoolean(booleanGetCachedVariables);
		fmi2GetString(stringGetCachedVariables);
	}
	
	public void flushSetCache(){
		ArrayList<Double> values = new ArrayList<Double>();
		
//		fmi2SetReal(realSetCachedVariables );
//		fmi2SetInteger(integerSetCachedVariables);
//		fmi2SetBoolean(booleanSetCachedVariables);
//		fmi2SetString(stringSetCachedVariables);
	}


	public Fmi2Parameters getParameters() {
	
		return parameters;

	}
	

	
	public void setParameters(Fmi2Parameters parameters) {
	
		this.parameters = parameters;
		this.dll_lib = NativeLibrary.getInstance(parameters.getDllPath());
	
	}

	
	/**
	 * fmi2Instantiate   
	 * 
	 * @param fmi2String instanceName (modelIdentifier) --> String
	 * @param fmi2Type  fmuType --> fmi2Type.cosimulation
	 * @param fmi2String fmuGUID --> string
	 * @param fmi2String fmuResourceLocation --> String
	 * @param fmi2CallbackFunctions* functions --> (Fmu2Library.cbf)
	 * @param fmi2Boolean visible --> boolean
	 * @param fmi2Boolean loggingOn --> boolean
	 * @return fmi2Component -->Pointer (fmi2Component)
	 */
	public int fmi2Instantiate(){
		
		component = fmuApi.invokePointer("fmi2Instantiate", dll_lib, 
				new Object[]{parameters.getModelIdentifier(), //instanceName
						Fmi2Type.fmi2CoSimulation, //fmuType
						parameters.getGuid(), //guid
						parameters.getFmuLocation(), //fmulocation
						Fmu2Library.cbf, //callback functions
						false,   //visible 
						false}, ""); //loggingOn
		if (component == null || component.equals(Pointer.NULL)) {
			fmi2Status = Fmi2Status.fmi2Error;
			//result.value = fmiStatus;
			throw new RuntimeException("Could not instantiate model.");
		}else{
			fmi2Status = Fmi2Status.fmi2OK;
		}
		fmu2Status = Fmu2Status.instantiated;
		return fmi2Status;

	}
	
	
	/**
	 * fmi2SetupExperiment  
	 *
	 * @param fmi2Component --> Pointer 
	 * @param fmi2Boolean toleranceDefined --> boolean
	 * @param fmi2Real tolerance --> double
	 * @param fmi2Real startTime --> double
	 * @param fmi2Boolean stopTimeDefined
	 * @param fmi2Real stopTime
	 * @return fmi2Status --> int
	 */
	public int fmi2SetupExperiments(double startTime, double stopTime, double tolerance){
		
		//this operation receives 5 parameters from the master model
		fmi2Status = fmuApi.invokeInteger("fmi2SetupExperiment", dll_lib, 
										new Object[]{component, true, tolerance, 
										startTime, true, stopTime}, "");
		return fmi2Status;
	}

	
	/**
	 * fmi2EnterInitializationMode 
	 * 
	 * @param fmi2Component  --> Pointer
	 * @return fmi2Status  --> int
	 */
	public int fmi2EnterInitializationMode() {
		
		fmi2Status= fmuApi.invokeInteger("fmi2EnterInitializationMode", dll_lib, new Object[]{component}, "");
		//result.value =fmiStatus;
		//resultParamater.values.add(result);
		//resultParamater.parameter = this.parameterValues.get(0).parameter;
		//this.setParameterValue(resultParamater);
		fmu2Status = Fmu2Status.underInitializationMode;
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2ExitInitializationMode   
	 * 
	 * @param fmi2Component  --> Pointer
	 * @return fmi2Status  --> int 
	 */
	public int fmi2ExitInitializationMode() {
		
		fmi2Status = fmuApi.invokeInteger("fmi2ExitInitializationMode", dll_lib, new Object[]{component}, "");
		//result.value=fmiStatus;
		//resultParamater.values.add(result);
		//resultParamater.parameter = this.parameterValues.get(0).parameter;
		//this.setParameterValue(resultParamater);
		fmu2Status = Fmu2Status.initialized;
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2DoStep  
	 *
	 * @param fmi2Component --> Pointer  //local
	 * @param fmi2Real currentCommunicationPoint --> double //from master model
	 * @param fmi2Real communicationStepSize --> double// from fmu.parameters
	 * @param fmi2Boolean noSetFMUStatePriorToCurrentPoint --> boolean //from master model
	 * @return  fmi2Status --> int//to the model
	 */
	public int fmi2DoStep(double currentTime, double stepSize, boolean noSetPrior){
			
		fmi2Status = fmuApi.invokeInteger("fmi2DoStep", dll_lib, 
				new Object[] {component, currentTime, stepSize, noSetPrior}, "");
		fmu2Status = Fmu2Status.stepComplete;
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2Terminate      
	 * 
	 * @param fmi2Component  --> Pointer
	 * @return fmi2Status  --> int 
	 */
	public int fmi2Terminate() {
			
		fmi2Status = fmuApi.invokeInteger("fmi2Terminate", dll_lib, new Object[]{component}, "");
		fmu2Status = Fmu2Status.terminated;
		return fmi2Status;
		
	}
	
	
	//get and set a list of variables
	/**
	 * fmi2GetReal    
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @param fmi2Real value[] --> doubleBuffer FIXME(double[])
	 * @return fmi2Status --> int
	 *
	 */
	public double[] fmi2GetReal(ArrayList<Fmi2ScalarVariable> variables) {
			
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		DoubleBuffer value = DoubleBuffer.allocate(bufferLength);
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetReal", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value.array();
	
	}
	
	
	/**
	 * fmi2GetInteger  
	 * 
	 * @param fmi2Component --> Pointer
	 * @param fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr  --> NativeSizeT
	 * @param fmi2Integer value[]  --> int[]
	 * @return fmi2Status --> int
	 */
	public int[] fmi2GetInteger(ArrayList<Fmi2ScalarVariable> variables) {
		
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		IntBuffer value = IntBuffer.allocate(bufferLength);
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetInteger", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value.array();

	}
	
	
	/**
	 * fmi2GetBoolean   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @param fmi2boolean value[] --> boolean[]
	 * @return fmi2Status --> int
	 */
	public boolean[] fmi2GetBoolean(ArrayList<Fmi2ScalarVariable> variables){
			
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		boolean[] value = new boolean[bufferLength];
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetBoolean", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value;

	}
	
	
	/**
	 * fmi2GetString  
	 * 
	 * @param Pointer (fmi2Component)
	 * @param fmi2ValueReference vr[]
	 * @param size_t nvr
	 * @param string value[]
	 * @return fmi2Status 
	 */
	public String[] fmi2GetString(ArrayList<Fmi2ScalarVariable> variables) {
			
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		String[] value = new String[bufferLength];
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetString", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value;
		
	}
	
	
	/**
	 * fmi2SetReal    
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2Real value[] --> doubleBuffer FIXME(double[]) 
	 */
	public int fmi2SetReal(ArrayList<Fmi2ScalarVariable> inputs2, double[] values) {
			
		int bufferLength = inputs2.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		DoubleBuffer value = DoubleBuffer.allocate(bufferLength);
		for(int i=0; i<inputs2.size();i++){
			vr.put(i,((Long) inputs2.get(i).getValueReference()));
			value.put(i,values[i]);
		}
		
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetReal", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;

	}
	
	
	/**
	 * fmi2SetInteger   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2integer value[] --> IntegerBuffer FIXME(int[]) 
	 */
	public int fmi2SetInteger(ArrayList<Fmi2ScalarVariable>  variables, int[] values){
			
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		IntBuffer value = IntBuffer.allocate(bufferLength);
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
			value.put(i,values[i]);
		}
		
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetInteger", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2SetBoolean  
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2Boolean value[] --> boolean[]
	 */
	public int fmi2SetBoolean(ArrayList<Fmi2ScalarVariable>  variables, boolean[] value){
			
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetBoolean", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2SetString   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2String value[] --> String[]
	 */
	public int fmi2SetString(ArrayList<Fmi2ScalarVariable>  variables, String[] value){
		
		int bufferLength = variables.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		for(int i=0; i<variables.size();i++){
			vr.put(i,((Long) variables.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetString", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;		
	
	}
	
	
	//get and set a single variable
	/**
	 * fmi2GetReal    
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @param fmi2Real value[] --> doubleBuffer FIXME(double[])
	 * @return fmi2Status --> int
	 *
	 */
	public double fmi2GetReal(Fmi2ScalarVariable variable) {
			
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		DoubleBuffer value = DoubleBuffer.allocate(1);
		vr.put(0,((Long) variable.getValueReference()));
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetReal", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value.get(0);
	
	}
	
	
	/**
	 * fmi2GetInteger  
	 * 
	 * @param fmi2Component --> Pointer
	 * @param fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr  --> NativeSizeT
	 * @param fmi2Integer value[]  --> int[]
	 * @return fmi2Status --> int
	 */
	public int fmi2GetInteger(Fmi2ScalarVariable variable) {
		
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		IntBuffer value = IntBuffer.allocate(1);
		vr.put(0,((Long) variable.getValueReference()));
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetInteger", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value.get(0);

	}
	
	
	/**
	 * fmi2GetBoolean   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @param fmi2boolean value[] --> boolean[]
	 * @return fmi2Status --> int
	 */
	public boolean fmi2GetBoolean(Fmi2ScalarVariable variable){
			
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		boolean[] value = new boolean[1];
		vr.put(0,((Long) variable.getValueReference()));
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetBoolean", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value[0];

	}
	
		
	/**
	 * fmi2GetString  
	 * 
	 * @param Pointer (fmi2Component)
	 * @param fmi2ValueReference vr[]
	 * @param size_t nvr
	 * @param string value[]
	 * @return fmi2Status 
	 */
	public String fmi2GetString(Fmi2ScalarVariable variable) {
			
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		String[] value = new String[1];
		vr.put(0,((Long) variable.getValueReference()));
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetString", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value[0];
		
	}
	
	
	/**
	 * fmi2SetReal    
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2Real value[] --> doubleBuffer FIXME(double[]) 
	 */
	public int fmi2SetReal(Fmi2ScalarVariable variable, double v) {
			
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		DoubleBuffer value = DoubleBuffer.allocate(1);
		vr.put(0,((Long) variable.getValueReference()));
		value.put(0,v);
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetReal", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;

	}
	
	
	/**
	 * fmi2SetInteger   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2integer value[] --> IntegerBuffer FIXME(int[]) 
	 */
	public int fmi2SetInteger(Fmi2ScalarVariable  variable, int v){
			
		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		IntBuffer value = IntBuffer.allocate(1);
		vr.put(0,((Long) variable.getValueReference()));
		value.put(0,v);
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetInteger", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2SetBoolean  
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2Boolean value[] --> boolean[]
	 */
	public int fmi2SetBoolean(Fmi2ScalarVariable variable, boolean v){

		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		boolean[] value = new boolean[1];
		vr.put(0,((Long) variable.getValueReference()));
		value[0] = v;
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetBoolean", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2SetString   
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @return fmi2String value[] --> String[]
	 */
	public int fmi2SetString(Fmi2ScalarVariable variable, String v){

		LongBuffer vr = LongBuffer.allocate(1);
		NativeSizeT nvr = new NativeSizeT(1);
		String[] value = new String[1];
		vr.put(0,((Long) variable.getValueReference()));
		value[0] = v;
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetString", dll_lib, new Object[] {component, vr, nvr, value},"");
		return fmi2Status;		
	
	}
	
	////////////////////////////////////////////////////////
	
	/**
	 * fmi2GetFMUstate  
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param fmi2FMUstate* FMUstate --> Pointer FIXME pointer by reference
	 * @return fmi2Status --> int
	 * FIXME check the fmuState argument type
	 */
	public int fmi2GetFMUstate(){
		PointerByReference fmuStateReference = new PointerByReference(fmuState);
		fmi2Status = fmuApi.invokeInteger("fmi2GetFMUstate", dll_lib, new Object[]{component,fmuStateReference}, "");
		fmuState = fmuStateReference.getValue();
		return fmi2Status;
	}

	
	/**
	 * fmi2SetFMUstate   
	 * 
	 * @return fmi2Status 
	 * @param fmi2Component --> Pointer
	 * @param fmi2FMUstate FMUstate  --> Pointer 
	 * FIXME check the fmustate argument type
	 */
	public int fmi2SetFMUstate(Pointer preFmuState) {
	       
		fmi2Status = fmuApi.invokeInteger("fmi2SetFMUstate", dll_lib, new Object[]{component,preFmuState}, "");
		//fmuState = preFmuState;
		return fmi2Status;
		
	}
	
	
	/**
	 * fmi2FreeFMUstate   
	 * 
	 * @param  fmi2Component -->  Pointer 
	 * @param fmi2FMUstate* --> Pointer FIXME pointer by byreference
	 * @return fmi2Status --> int
	 */
	public int fmi2FreeFMUstate(){
		
		fmi2Status = fmuApi.invokeInteger("fmi2FreeFMUstate", dll_lib, new Object[]{component,fmuState}, "");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2SerializedFMUstateSize 
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param fmi2FMUstate --> Pointer
	 * @return size_t *size --> NativeSizeT FIXME by reference
	 */
	public void fmi2SerializedFMUstateSize() {
		//TODO
		 
	}
	
	
	/**
	 * fmi2SerializeFMUstate 
	 * 
	 * @param fmi2Component --> Pointer
	 * @param fmi2FMUstate --> Pointer
	 * @param fmi2Byte[] --> byte[]
	 * @param size_t size) --> NativeSieT
	 * @return fmi2Status --> int
	 */
	public int fmi2SerializeFMUstate() {
		//TODO
		return 0;
	
	}
	
	
	/**
	 * fmi2DeSerializeFMUstate 
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2Byte serializedState[] --> byte[]
	 * @param size_t size --> NativeSieT
	 * @param fmi2FMUstate* FMUstate --> Pointer FIXME by reference
	 * @return fmi2Status
	 */
	public int fmi2DeSerializeFMUstate() {
		//TODO
		return 0;
	
	}
	
	
	/**
	 * fmi2GetDirectionalDerivative 
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2ValueReference vUnknown_ref[] --> long[]
	 * @param size_t nUnknown --> NativeSizeT
	 * @param const fmi2ValueReference vKnown_ref[] --> long[]
	 * @param size_t nKnown --> NativeSizeT
	 * @param const fmi2Real dvKnown[] --> double[]
	 * @param fmi2Real dvUnknown[]  --> double[]  FIXME return??
	 */
	public void fmi2GetDirectionalDerivative() {
		//TODO
	}
	
	
	/**
	 * fmi2SetRealInputDerivatives 
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr --> NativeSizeT
	 * @param const fmi2Integer order[] --> int[]
	 * @param const fmi2Real value[] --> double[]
	 * @return fmi2Status  --> int
	 */
	public int fmi2SetRealInputDerivatives(ArrayList<Fmi2ScalarVariable> derivatives, ArrayList<Integer> orders, double[] values) {
			
		int bufferLength = derivatives.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		IntBuffer order = IntBuffer.allocate(bufferLength);
		DoubleBuffer value = DoubleBuffer.allocate(bufferLength);
		for (int i=0; i< bufferLength; i++){
			vr.put(i,((Long) derivatives.get(i).getValueReference()));
			order.put(i,orders.get(i));
			value.put(i,values[i]);
		}	
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2SetRealInputDerivatives", dll_lib, new Object[] {component, vr, nvr, order , value},"");
		return fmi2Status;

	}
	
	
	/**
	 * fmi2GetRealOutputDerivatives    
	 * 
	 * @param fmi2Component --> Pointer 
	 * @param const fmi2ValueReference vr[] --> long[]
	 * @param size_t nvr  --> NativeSizeT
	 * @param const fmi2Integer order[] --> int[]
	 * @return fmi2Status --> int
	 */
	public double[] fmi2GetRealOutputDerivatives(ArrayList<Fmi2ScalarVariable> derivatives) {
		
		int bufferLength = derivatives.size();
		LongBuffer vr = LongBuffer.allocate(bufferLength);
		NativeSizeT nvr = new NativeSizeT(bufferLength);
		DoubleBuffer value = DoubleBuffer.allocate(bufferLength);
		for(int i=0; i<derivatives.size();i++){
			vr.put(i,((Long) derivatives.get(i).getValueReference()));
		}
			
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetRealOutputDerivatives", dll_lib, new Object[] {component, vr, nvr, value},"");
		return value.array();
	
	}
	
	
	/**
	 * fmi2CancelStep       
	 * Can be called if fmi2DoStep returned fmi2Pending in order to stop the current asynchronous execution
	 * The master calls this function if for example the co-simulation run is stopped by the user or one of the slaves
	 * Afterwards it is only allowed to call fmi2Reset or fmi2FreeInstance
	 * 
	 * @param component --> Pointer
	 * @return fmi2Status --> int
	 */
	public int fmi2CancelStep() {
		
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2CancelStep", dll_lib, new Object[] {component},"");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2GetStatus  
	 * Informs the master about the actual status of the simulation run     
	 * Can be called when the fmi2DoStep function returned fmi2Pending
	 * The function delivers fmi2Pending if the computation is not finished
	 * Otherwise the function returns the result of the asynchronously executed fmi2DoStep call
	 * 
	 * @param fmi2Component -->Pointer
	 * @param const fmi2StatusKind  --> int 
	 * @return fmi2Status* value  --> int FIXME by reference
	 */
	public int fmi2GetStatus(){
		
		int statusKind = Fmi2StatusKind.fmi2DoStepStatus;
		int value = 0; 
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetStatus", dll_lib, new Object[] {component, statusKind, value},"");
		return value;

	}
	
	
	/**
	 * fmi2GetRealStatus 
	 * Informs the master about the actual status of the simulation run
	 * Returns the end time of the last successfully completed communication step
	 * Can be called after fmi2DoStep(...) returned fmi2Discard
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2StatusKind --> int
	 * @return fmi2Real* value --> double FIXME by reference
	 */
	public double fmi2GetRealStatus(int statusKind){
		
		//int statusKind = Fmi2StatusKind.fmi2LastSuccessfulTime;
		DoubleByReference value = new DoubleByReference();
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetRealStatus", dll_lib, new Object[] {component, statusKind, value},"");
		return value.getValue();
	
	}
	
	
	/**
	 * fmi2GetIntegerStatus   
	 * Informs the master about the actual status of the simulation run
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2StatusKind  --> int 
	 * @result fmi2Integer* value  --> int  FIXME byreference
	 * FIXME it is not defined in the spec when to use it
	 */
	public int fmi2GetIntegerStatus(){
		
		int statusKind = 0;
		int value = 0; 
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetIntegerStatus", dll_lib, new Object[] {component, statusKind, value},"");
		return value;

	}
	
	
	/**
	 * fmi2GetBooleanStatus     
	 * Informs the master about the actual status of the simulation run
	 * Returns true, if the slave wants to terminate the simulation
	 * Can be called after fmi2DoStep(...) returned fmi2Discard
	 * Use fmi2LastSuccessfulTime to determine the time instant at which the slave terminated.
	 * 
	 * @param fmi2Component --> Pointer
	 * @param fmi2StatusKind --> int
	 * @return fmi2Boolean* value --> boolean FIXME by reference
	 */
	public boolean fmi2GetBooleanStatus(){
	
		int statusKind = Fmi2StatusKind.fmi2Terminated;
		boolean value = false; 
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetBooleanStatus", dll_lib, new Object[] {component, statusKind, value},"");
		return value;
		
	}
	
	
	/**
	 * fmi2GetStringStatus 
	 * Informs the master about the actual status of the simulation run
	 * Can be called when the fmi2DoStep function returned fmi2Pending
	 * The function delivers a string which informs about 
	 * the status of the currently running asynchronous fmi2DoStep computation.
	 * 
	 * @param fmi2Component --> Pointer
	 * @param const fmi2StatusKind --> int
	 * @return fmi2String* value --> String FIXME bureference
	 */
	public String fmi2GetStringStatus(){
		
		int statusKind = Fmi2StatusKind.fmi2PendingStatus;
		String value = ""; 
		fmi2Status = (Integer)fmuApi.invokeInteger("fmi2GetStringStatus", dll_lib, new Object[] {component, statusKind, value},"");
		return value;
		
	}
	
	
	/**
	 * fmi2GetTypesPlatform
	 *
	 * @param no parameters
	 * @return const char* --> String
	 */
	public String fmi2GetTypesPlatform() {
			
		return fmuApi.invokeString("fmi2GetTypesPlatform", dll_lib, null, "");
	
	}
	   
	
	/**
	 * fmi2GetVersion
	 * 
	 * @param no parameters
	 * @return const char* --> String
	 */
	public String fmi2GetVersion() {
		
		return fmuApi.invokeString("fmi2GetVersion", dll_lib, null, "");

	}
	
	
	/**
	 * fmi2SetDebugLogging  
	 * If loggingOn=fmi2True, debug logging is enabled, 
	 * otherwise it is switched off.
	 * The allowed values of “category” are defined by the modeling environment that generated the FMU.
	 * Depending on the generating modeling environment, none, some or all allowed values for “categories” 
	 * for this FMU are defined in the modelDescription.xml file via element “fmiModelDescription
	 * 
	 * @param fmi2Component c --> Pointer
	 * @param fmi2Boolean loggingOn --> boolean
	 * @paramn size_t nCategories --> NativeSizeT
	 * @param const fmi2String categories[] --> String[]
	 * @return fmi2Status
	 */
	public int fmi2SetDebugLogging(boolean loggingOn, ArrayList<String> categories) {
		
		fmi2Status = fmuApi.invokeInteger("fmi2SetDebugLogging", dll_lib, new Object[]{component,loggingOn,new NativeSizeT(categories.size()), (String[])categories.toArray()}, "");
		return fmi2Status;
	
	}
	
	
	/**
	 * fmi2FreeInstance 
	 * Disposes the given instance, unloads the loaded model, and frees all the allocated memory
	 * and other resources that have been allocated by the functions of the FMU interface.
	 * If a null * pointer is provided for “c”, the function call is ignored   
	 * 
	 * @param fmi2Component c --> Pointer
	 * @return void
	 */
	public void fmi2FreeInstance() {
			
		fmuApi.invoke("fmi2FreeInstance", dll_lib, new Object[]{component}, "");
	
	}
	
	
	/**
	 * fmi2Reset   
	 * Is called by the environment to reset the FMU after a simulation run. 
	 * The FMU goes into the same state as if fmi2Instantiate would have been called. 
	 * All variables have their default values. 
	 * Before starting a new run, fmi2SetupExperiment and fmi2EnterInitializationMode have to be called 
	 *  
	 * @param Pointer (fmi2Component)
	 * @return fmi2Status 
	 */
	public int fmi2Reset() {
		
		fmi2Status = fmuApi.invokeInteger("fmi2Reset", dll_lib, new Object[]{component}, "");
		return fmi2Status;
		
	}


	
	protected void registerServiceOperationImplementation() {
		
		
	}

	public Object fmi2Get(Fmi2ScalarVariable variable) {
		// TODO Auto-generated method stub
		Object value = null;
		if (variable.getType().equals(Fmi2VariableType.fmi2Real)){
			 value = fmi2GetReal(variable);
		}else if (variable.getType().equals(Fmi2VariableType.fmi2Integer)){
			value = fmi2GetInteger(variable);
		}else if (variable.getType().equals(Fmi2VariableType.fmi2Boolean)){
			value = fmi2GetBoolean(variable);
		}else if (variable.getType().equals(Fmi2VariableType.fmi2String)){
			value = fmi2GetString(variable);
		}
	
		return value;
	}

	public int fmi2Set(Fmi2ScalarVariable variable, Object value) {
		
		LiteralSpecification literalValue = getLiteral(value);
		
		if (variable.getType().equals(Fmi2VariableType.fmi2Real)){
			fmi2Status = fmi2SetReal(variable, ((LiteralReal)literalValue).getValue());
		}else if (variable.getType().equals(Fmi2VariableType.fmi2Integer)){
			fmi2Status = fmi2SetInteger(variable, ((LiteralInteger)literalValue).getValue());
		}else if (variable.getType().equals(Fmi2VariableType.fmi2Boolean)){
			fmi2Status = fmi2SetBoolean(variable, ((LiteralBoolean)literalValue).isValue());
		}else if (variable.getType().equals(Fmi2VariableType.fmi2String)){
			fmi2Status = fmi2SetString(variable,((LiteralString) literalValue).getValue());
		}
		return fmi2Status;
	}
	
	public LiteralSpecification getLiteral(Object fromValue){
		LiteralSpecification result = null;
		if (fromValue instanceof Double){
			LiteralReal real = UMLFactory.eINSTANCE.createLiteralReal();
			real.setValue((Double)fromValue);
			result = real;
		} else if (fromValue instanceof Integer){
			LiteralInteger integer = UMLFactory.eINSTANCE.createLiteralInteger();
			integer.setValue((Integer)fromValue);
			result = integer;
		} else if (fromValue instanceof String){
			LiteralString str = UMLFactory.eINSTANCE.createLiteralString();
			str.setValue((String)fromValue);
			result = str;
		} else if (fromValue instanceof Boolean) {
			LiteralBoolean bool = UMLFactory.eINSTANCE.createLiteralBoolean();
			bool.setValue((Boolean)fromValue);
			result = bool;
		} else if (fromValue instanceof LiteralReal) {
			result = (LiteralSpecification)fromValue;
		} else if (fromValue instanceof LiteralInteger) {
			result = (LiteralSpecification)fromValue;
		} else if (fromValue instanceof LiteralString) {
			result = (LiteralSpecification)fromValue;
		} else if (fromValue instanceof LiteralBoolean) {
			result = (LiteralSpecification)fromValue;
		} 
		return result;
	}
	
	
	
	//TODO
	public int fmi2UpdateVariables(){

		if (fmu2Status == Fmu2Status.instantiated){
			//1 for a variable with variability ≠ "constant" 
			//that has initial = "exact" or "approx"
		}else if (fmu2Status == Fmu2Status.underInitializationMode){
			//3 for a variable with variability≠"constant" that has initial="exact",
			//or causality="input"
			for (Fmi2Port input : inputPorts){
				fmi2Set(input, input.getValue());
			}

			
		}else if (fmu2Status == Fmu2Status.initialized){
			
		}else if (fmu2Status == Fmu2Status.stepComplete){
			//after a do step (causality = "parameter" and variability = "tunable")
			
		}	
		return fmi2Status;
	}
	
	

	
}
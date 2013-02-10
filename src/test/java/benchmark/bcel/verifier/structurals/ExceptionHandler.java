/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */ 
package benchmark.bcel.verifier.structurals;


import benchmark.bcel.generic.InstructionHandle;
import benchmark.bcel.generic.ObjectType;

/**
 * This class represents an exception handler; that is, an ObjectType
 * representing a subclass of java.lang.Throwable and the instruction
 * the handler starts off (represented by an InstructionContext).
 * 
 * @version $Id: ExceptionHandler.java 371539 2006-01-23 14:08:00Z tcurdt $
 * @author Enver Haase
 */
public class ExceptionHandler{
	/** The type of the exception to catch. NULL means ANY. */
	private ObjectType catchtype;
	
	/** The InstructionHandle where the handling begins. */
	private InstructionHandle handlerpc;

	/** Leave instance creation to JustIce. */
	ExceptionHandler(ObjectType catch_type, InstructionHandle handler_pc){
		catchtype = catch_type;
		handlerpc = handler_pc;
	}

	/**
	 * Returns the type of the exception that's handled. <B>'null' means 'ANY'.</B>
	 */
	public ObjectType getExceptionType(){
		return catchtype;
	}

	/**
	 * Returns the InstructionHandle where the handler starts off.
	 */
	public InstructionHandle getHandlerStart(){
		return handlerpc;
	}
}

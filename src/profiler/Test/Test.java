package profiler.Test;

import profiler.AgentLoader.AgentLoader;

public class Test {

    public static void main(String[] args) {

	try {

	    AgentLoader agent = new AgentLoader();

	    agent.addToLibPath("C:\\Documents and Settings\\Tudor\\Desktop");
	    agent.loadAgentLibrary();

	    agent.attachAgentToJVM(Agent.class,
		    new Class<?>[] { AgentLoader.class }, agent.getCurrentPID());
	} catch (Exception e) {
	    e.printStackTrace();
	}

	test();

    }

    public static void test() {
	
    }

}

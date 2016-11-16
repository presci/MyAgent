package org.presci.agent;


import java.lang.instrument.Instrumentation;

public class ThrottlingAgent {
    public static void premain(String args, Instrumentation instrumentation){
    	instrumentation.addTransformer(new ThrottlingFilterTransformer());
    }	
}

package org.presci.agent.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Lucifer {
	
	public static void main(String[] args) throws Exception {
		
		
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get("org.presci.agent.test.TestClass");
		CtMethod ctMethod = ctClass.getDeclaredMethod("testmethod");
		ctMethod.setBody("$3.discovery1($2,$1); ");
		ctClass.toClass();
		ctClass.detach();
		

		TestClass c = new TestClass();
		c.testmethod("hello", "world", new TestCls2());
		
		
		
		
	}

}

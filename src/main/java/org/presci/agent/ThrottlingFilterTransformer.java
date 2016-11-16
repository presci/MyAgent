package org.presci.agent;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ThrottlingFilterTransformer implements ClassFileTransformer {
	
	
	
	
	

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		
		ClassPool classPool = (new GetValue<ClassPool>(){
			@Override
			public ClassPool getvalue() {
				ClassPool $pool = ClassPool.getDefault();
				try {
					$pool.insertClassPath("/apache-tomcat-7.0.50/lib/servlet-api.jar");
				} catch (NotFoundException e) {
					throw new RuntimeException(e);
				}
				return $pool;
			}
		}).getvalue();
		
		
		byte[] byteCode = classfileBuffer;
		if (className.equals("ThrottlingFilter")) {
			System.out.println("ThrottlingFilterTransformer -- Instrumenting ThrottlingFilter");
			try {
				CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(
						classfileBuffer));
				CtMethod[] methods = ctClass.getDeclaredMethods();
				for (CtMethod method : methods) {
					System.out.println(method.getName());
					if ("doFilter".equals(method.getName())) {
						method.setBody(getBody());
					}
				}
				byteCode = ctClass.toBytecode();
				ctClass.detach();
				System.out.println("ThrottlingFilterTransformer -- Instrumentation complete.");
			} catch (Throwable ex) {
				System.out.println("Exception: " + ex);
				ex.printStackTrace();
			}

		}
		return byteCode;

	}
	
	public String getBody(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{System.out.println(\"Modified ThrottlingFilter\");");
		buffer.append("$3.doFilter($1, $2);}");
		return buffer.toString();
	}
	
	
	public static interface GetValue<T>{
		T getvalue();
	}

}

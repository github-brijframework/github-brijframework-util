package org.brijframework.util.factories;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectionFactory {
	private static final String INTERNAL_CLASS="INTERNAL_CLASS";
	private static final String EXTERNAL_CLASS="EXTERNAL_CLASS";
	private ConcurrentHashMap<String, Set<Class<?>>> cache=new ConcurrentHashMap<>();
	
	private static  ReflectionFactory factory;
	
	public static ReflectionFactory getFactory() {
		if(factory==null) {
			factory=new ReflectionFactory();
			factory.loadFactory();
		}
		return factory;
	}
	
	private ReflectionFactory loadFactory() {
		getCache().put(INTERNAL_CLASS,new HashSet<>());
		getCache().put(EXTERNAL_CLASS,new HashSet<>());
		try {
			getInternalClassList().addAll(ClassFileResourceUtil.getClassList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getExternalClassList().addAll(JarFileResourceUtil.getClassList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("getCache()="+getCache());
		return this;
	}

	public ConcurrentHashMap<String, Set<Class<?>>> getCache() {
		return cache;
	}
	
	public Set<Class<?>> getInternalClassList(){
		return getCache().get(INTERNAL_CLASS);
	}
	
	public Set<Class<?>> getExternalClassList(){
		return getCache().get(EXTERNAL_CLASS);
	}

	
	public static void main(String[] args) throws Exception {
		System.out.println("File "+ReflectionFactory.getFactory().loadFactory().getCache());
	}

	public boolean isProjectClass(Class<? extends Object> cls) {
		return getInternalClassList().contains(cls);
	}

	public boolean isJarClass(Class<? extends Object> cls) {
		return getExternalClassList().contains(cls);
	}

}

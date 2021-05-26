package org.brijframework.util.factories;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectionFactory {
	private static final String INTERNAL_CLASS="INTERNAL_CLASS";
	private static final String EXTERNAL_CLASS="EXTERNAL_CLASS";
	private ConcurrentHashMap<String, Set<Class<?>>> cache=new ConcurrentHashMap<>();
	{
		getCache().put(INTERNAL_CLASS,new HashSet<>());
		getCache().put(EXTERNAL_CLASS,new HashSet<>());
	}
	
	private static  ReflectionFactory factory;
	
	public static ReflectionFactory getFactory() {
		if(factory==null) {
			factory=new ReflectionFactory();
			factory.loadFactory();
		}
		return factory;
	}
	
	private void loadFactory() {
		try {
			ClassFileResourceUtil.getClassList().forEach(cls->{
				getCache().get(INTERNAL_CLASS).add(cls);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			JarFileResourceUtil.getClassList().forEach(cls->{
				getCache().get(EXTERNAL_CLASS).add(cls);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println(ReflectionFactory.getFactory().getExternalClassList());
	}

	public boolean isProjectClass(Class<? extends Object> cls) {
		return getInternalClassList().contains(cls);
	}

	public boolean isJarClass(Class<? extends Object> cls) {
		return getExternalClassList().contains(cls);
	}

}

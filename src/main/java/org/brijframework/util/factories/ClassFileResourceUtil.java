package org.brijframework.util.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassFileResourceUtil extends FileResourceUtils {

	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static List<File> getClassFiles() {
		return getFiles(System.getProperty(JAVA_CLASS_PATH)).stream().filter(file -> file.getPath().endsWith(CLASS_EXT))
				.collect(Collectors.toList());
	}

	public static List<String> getClassNames() {
		List<String> classes = new ArrayList<>();
		for (File file : getClassFiles()) {
			String classPath = file.getPath().split("classes")[1];
			classes.add(getClassPaths(classPath));
		}
		return classes;
	}

	public static List<Class<?>> getClassList() {
		List<Class<?>> classes = new ArrayList<>();
		for (String className : getClassNames()) {
			getSafeClass(className, getContextClassLoader()).ifPresent(cls->{
				classes.add(cls);
			});
		}
		return classes;
	}

	public static Optional<Class<?>> getSafeClass(String className, ClassLoader classLoader) {
		try {
			return Optional.ofNullable(Class.forName(className, false, classLoader));
		} catch (Throwable e) {
			return Optional.ofNullable(null);
		}
	}
	
	public static String getClassPaths(String classPath) {
		return classPath.replaceFirst(REGEX_PATH_SPRTOR, "").replace(CLASS_EXT, "").replaceAll(REGEX_PATH_SPRTOR, ".");
	}


	public static void main(String args[]) throws Exception {
		getClassList().forEach(System.out::println);
		
	}

}

package org.brijframework.util.factories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarFileResourceUtil extends FileResourceUtils{
       
	 
	  private static final String JAVA_CLASS_PATH = "java.class.path";
	  private static final String CLASS_EXT = ".class";

	  public static ClassLoader getContextClassLoader() {
		  return Thread.currentThread().getContextClassLoader();
	  }

	/**
	   * List the content of the given jar
	   * @param jarPath
	   * @return
	   * @throws IOException
	   */
	  public static List<String> getJarContent(String jarPath) throws IOException{
	    List<String> content = new ArrayList<String>();
	    @SuppressWarnings("resource")
		JarFile jarFile = new JarFile(jarPath);
	    Enumeration<JarEntry> e = jarFile.entries();
	    while (e.hasMoreElements()) {
	      JarEntry entry = (JarEntry)e.nextElement();
	      content.add(entry.getName());
	    }
	    return content;
	  }
	  
	  public static List<String> getJarClassNames(String jarPath) throws IOException{
		  return getJarContent(jarPath).stream().filter(file->file.endsWith(CLASS_EXT)).map(file->getClassPaths(file)).collect(Collectors.toList());
	  }
	  
	  public static String getClassPaths(String classPath) {
			return classPath.replace(CLASS_EXT, "").replaceAll(REGEX_PATH_SPRTOR, ".");
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

		private static List<String> getClassNames() {
		    List<String> list = new ArrayList<>();
		    for (File file: getJarFiles()) {
		    	try {
					list.addAll(getJarClassNames(file.getPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
			return list;
		}

		public static Optional<Class<?>> getSafeClass(String className, ClassLoader classLoader) {
			try {
				return Optional.ofNullable(Class.forName(className, false, classLoader));
			} catch (Throwable e) {
				return Optional.ofNullable(null);
			}
		}
	  
	  public static List<File> getJarFiles() {
		return  getFiles(System.getProperty(JAVA_CLASS_PATH)).stream().filter(file->file.getPath().endsWith(".jar")).collect(Collectors.toList());
	  }  

	  
	  public static void main(String args[]) throws Exception {
	   getClassList().forEach(System.out::println);
	 
	  }
	  
}

package org.brijframework.util.factories;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class FileResourceUtils {
	public static final String REGEX_PATH_SPRTOR = "[/\\\\]";
	public static final String CLASS_EXT = ".class";
	public static final String JAVA_CLASS_PATH = "java.class.path";
	
	
  /**
   * list files in the given directory and subdirs (with recursion)
   * @param paths
   * @return
   */
  public static List<File> getFiles(String paths) {
    List<File> filesList = new ArrayList<File>();
    for (final String path : paths.split(File.pathSeparator)) {
      final File file = new File(path);
      if( file.isDirectory()) {
         recurse(filesList, file);
      }
      else {
        filesList.add(file);
      }
    }
    return filesList;
  }

  private static void recurse(List<File> filesList, File f) { 
    File list[] = f.listFiles();
    for (File file : list) {
      if (file.isDirectory()) {
         recurse(filesList, file);
      }
      else {
        filesList.add(file);
      }
    }
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
      String name = entry.getName();
      content.add(name);
    }
    return content;
  }
  
  
  public static void main(String args[]) throws Exception {
    List<File> list = FileResourceUtils.getFiles(System.getProperty("java.class.path"));
    for (File file: list) {
      System.out.println(file.getPath());
    }
    list=new ArrayList<>();
	
    List<String> content = FileResourceUtils.getJarContent("C:\\Users\\Restart\\.m2\\repository\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.2.2\\jackson-databind-2.2.2.jar");
    for (String file: content) {
      System.out.println(file);
    }
    
  }  
}
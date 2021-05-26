package org.brijframework.util.printer;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;

import org.brijframework.util.resouces.ResourcesUtil;

public class LoggerConsole {
	static boolean isLoaded = false;

	public static void banner() {
		if (isLoaded) {
			return;
		}
		isLoaded = true;
		try {
			URL resource = ResourcesUtil.getResource("banner.txt");
			if(resource!=null) {
			String file = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
			PrintStream out;
			out = new PrintStream(System.err, true, "UTF-8");
			out.println(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void screen(String header, String message) {
		int space = 60 - header.length();
		System.out.printf(header + "%" + space + "s | %s", "", message + "\n");
	}

	public static void screen(String message) {
		int space = 25 - message.length();
		System.err.printf("%" + space + "s | %s | %" + space + "s", "===================================", message,
				"===================================" + "\n");
	}

	public static void file(String header, String message) {
		int space = 80 - header.length();
		System.err.printf(header + "%" + space + "s", message + "\n");
	}
}

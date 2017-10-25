package io.marangoni.java.cli;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.shell.Bootstrap;

/**
 * Driver class.
 * 
 * @author Nicola Marangoni
 *
 */
public class Main {

	static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Main class that delegates to Spring Shell's Bootstrap class in order to simplify debugging inside an IDE
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Bootstrap.main(args);

	}

}
package org.digijava.module.aim.fmtool;

import org.digijava.kernel.util.DummyServletContext;

public class Tester {

	private static String rootPath = "C:\\java\\workspace\\amp_head\\"; 
	private DummyServletContext context = null;


	public Tester(){
		context = new DummyServletContext(rootPath);
	}
	
	public void execute(){
	
	}
	
	public static void main(String[] args) {
		try {
			Tester tester = new Tester();

			tester.execute();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	
	
}

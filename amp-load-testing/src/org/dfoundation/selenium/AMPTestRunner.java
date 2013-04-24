package org.dfoundation.selenium;

import java.util.ArrayList;

import org.dfoundation.selenium.tests.amp24.ethiopia.EthiopiaTestReports;
import org.junit.runner.Result;

/**
 * This class spawns multhple Threads to perform the junit tests separately. Please start this class as a regular java app
 * Before starting the tests Start selenium server with a command like
 * $ java -jar selenium-server-standalone-2.32.0.jar -singleWindow -maxSession 25
 * 
 * @author mihai
 *
 */
public class AMPTestRunner {
	
	/**
	 * Simulated number of clients (browser sessions) to start
	 */
	protected static int NUMBER_OF_CLIENTS=20;
	
	/**
	 * The test to be run, change this to another junit
	 */
	protected static Class testClass=EthiopiaTestReports.class;
	
	
	final static ArrayList<Result> resultRefs = new ArrayList<Result>();
	protected static int threadIdSequenceNumber;
	
	public static void main(String[] args) throws InterruptedException {
		
					
		 Thread[] threads = new Thread[NUMBER_OF_CLIENTS];
		 for( int i = 0; i < threads.length; i++ ) {
		 	threads[i] = new Thread( new Runnable() {
		 		private final int ID = threadIdSequenceNumber;
		 		
		 		public synchronized void threadedIdSequenceNumber() {
		 			threadIdSequenceNumber++;
		 		}
		 		
		 		public synchronized void addResult(Result result) {
		 			resultRefs.add(result);
		 		}
		 		
		 		
		 		public void run() {
		 			try {
		 				
		 					
		 					addResult(org.junit.runner.JUnitCore.runClasses(testClass));
		 			}
		 			catch( Throwable t ) { 
		 				final String message = "error testing thread with id => "
		 							+ ID;
		 				System.out.println(message);
		 				t.printStackTrace();		 				
		 			}
		 		}
		 	} );	
	   }
		 for( int i = 0; i < threads.length; i++ ) threads[i].start();
		 
		 for( int i = 0; i < threads.length; i++ ) threads[i].join();
		 
		 for (Result result : resultRefs)  System.out.println(result.wasSuccessful());  
		 
	}

}

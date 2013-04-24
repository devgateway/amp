package org.dfoundation.selenium;

import org.junit.Before;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public abstract class AMPTest {
	protected Selenium selenium;
	protected String host="localhost";
	protected int port=4444;
	protected String URL=null;
	protected String speed="400";
	protected String browser="*firefox";


	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium(host, port, browser, URL);		
		selenium.setSpeed(speed);
		selenium.start();
	}
}

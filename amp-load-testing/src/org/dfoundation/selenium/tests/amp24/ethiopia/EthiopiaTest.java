package org.dfoundation.selenium.tests.amp24.ethiopia;

import org.dfoundation.selenium.AMPTest;


public abstract class EthiopiaTest extends AMPTest {
	
	public EthiopiaTest() {
		super();
		browser="*firefox";
		speed="300";		
		URL="http://ethiopia.staging.ampdev.net";
	}
}

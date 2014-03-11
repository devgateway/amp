package org.dgfoundation.amp.testutils;

import junit.framework.TestCase;

/**
 * intermediary class for holding various utility methods for testcases
 * @author simple
 *
 */
public abstract class AmpTestCase extends TestCase
{
	public AmpTestCase(String name) {
		super(name);
	}
	
	public void shouldFail(AmpRunnable runnable)
	{
		try
		{
			runnable.run();
			throw new RuntimeException("did not throw exception");
		}
		catch(Throwable thr)
		{
			// it is ok, we want an exception
		}
	}

}

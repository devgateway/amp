package org.dgfoundation.amp.testutils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.digijava.module.fundingpledges.action.DisableableKeyValue;

import junit.framework.TestCase;

/**
 * intermediary class for holding various utility methods for testcases
 * @author Dolghier Constantin
 *
 */
public abstract class AmpTestCase extends TestCase
{
	public AmpTestCase(String name) {
		super(name);
	}
	
	public void shouldFail(AmpRunnable runnable){
		shouldFail(runnable, null);
	}
	
	public void shouldFail(AmpRunnable runnable, Exception correctException) {
		try{
			runnable.run();
			throw new RuntimeException("did not throw exception");
		}
		catch(Throwable thr){
			if ("did not throw exception".equals(thr.getMessage()))
				throw new RuntimeException("code which should have failed did not throw an exception!");
			
			boolean shouldCheckException = correctException != null;
			
			if ((!shouldCheckException) || (shouldCheckException && thr.getClass() == correctException.getClass() && thr.getMessage().equals(correctException.getMessage()))) {
				// it is ok, we want an exception
				//System.out.println("caught an ok exception: " + thr.getMessage());
				return;
			}
			throw new RuntimeException(String.format("code which should have failed with (%s %s) has instead failed with (%s %s)", correctException.getClass(), correctException.getMessage(), thr.getClass(), thr.getMessage()));
		}
	}
	
	/**
	 * compares 2 BigDecimal values by value, not by value/scale (as equals() would do)
	 * @param a
	 * @param b
	 */
	public void assertBigDecimalEquals(BigDecimal a, BigDecimal b) {
		assertEquals(0, a.compareTo(b));
	}
	
	/**
	 * sorts the elements of a collection ascendingly and then compares its toString() result with a given cor
	 * @param cor
	 * @param col
	 */
	public void assertColEquals(String cor, Collection<?> col) {
		assertEquals(cor, new TreeSet<>(col).toString());
	}
	
	public void testDisableableKeyValues(Collection<DisableableKeyValue> res, DisableableKeyValue... cor){
		List<DisableableKeyValue> corList = new ArrayList<>(Arrays.asList(cor));
		List<DisableableKeyValue> resList = new ArrayList<>(res);
		Collections.sort(corList);
		Collections.sort(resList);
		assertEquals(corList.size(), resList.size());
		for(int i = 0; i < corList.size(); i++){
			assertEquals(corList.get(i), resList.get(i));
		}
	}
	
	/**
	 * sorts the input list in a copy
	 * @param in
	 * @return
	 */
	public static<K> List<K> sorted(Collection<K> in) {
		ArrayList<K> res = new ArrayList<>(in);
		res.sort(null);
		return res;
	}
	
	/**
	 * returns a toString() of a sorted list of the elements in the input 
	 * @param in
	 * @return
	 */
	public static String sortedString(Collection<?> in) {
		return sorted(in).toString();
	}

}

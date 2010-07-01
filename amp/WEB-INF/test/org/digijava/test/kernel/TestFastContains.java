package org.digijava.test.kernel;

import org.digijava.test.util.DigiTestBase;
import org.digijava.kernel.util.DgUtil;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TestFastContains
     extends DigiTestBase {

       public TestFastContains(String name) {
	 super(name, "org/digijava/test/kernel/conf/fastContainsTest.properties");
       }

       public void testFastContains() throws Exception{
	   String source = getTestProperty("fastContainsTest.source");
	   String other = getTestProperty("fastContainsTest.other");
	   boolean ignoreCase = getTestProperty("fastContainsTest.ignoreCase").equals("true") ? true : false;


	   boolean contains1 = DgUtil.fastContains(source,other,ignoreCase);
	   boolean contains2 = false;

	   if (ignoreCase) {
	       source = source.toLowerCase();
	       other = other.toLowerCase();
	}

	int index = 0;
	while (!contains2 &&
	       (index = source.indexOf(other.charAt(0),index)) >= 0) {

	    if ((index + other.length()) > source.length()) {
		break;
	    }

	    if (source.substring(index,(other.length()+1)).equals(other)) {
		contains2 = true;
	    } else {
		++index;
	    }
	}

	assertEquals(contains1,contains2);

    }
}
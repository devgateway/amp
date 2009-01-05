package org.dgfoundation.amp.test.jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import org.digijava.module.aim.util.AmpDateUtils;
import org.apache.log4j.Logger;

public class VerifyUserDateJobTest extends TestCase {

    private static Logger logger = Logger.getLogger(VerifyUserDateJobTest.class);
    Calendar testCal;
    Calendar anotherTestCal;
    final int DAYS = 30;
    Date expectedDate;
    Date actualDate;

    protected void setUp() throws Exception {
        super.setUp();
        testCal = Calendar.getInstance();
        anotherTestCal = Calendar.getInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        testCal = null;
        anotherTestCal = null;
        expectedDate = null;
        actualDate = null;
    }

    public void testDateBeforeDays() {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        testCal.set(Calendar.MONTH, Calendar.DECEMBER);
        testCal.set(Calendar.DAY_OF_MONTH, 14);
        anotherTestCal.set(Calendar.MONTH, Calendar.NOVEMBER);
        anotherTestCal.set(Calendar.DAY_OF_MONTH, 14);
        expectedDate = anotherTestCal.getTime();
        actualDate = AmpDateUtils.getDateBeforeDays(testCal.getTime(), DAYS);
       

        logger.info("Expected Date " + sdf.format(expectedDate));
        logger.info("Actual Date " + sdf.format(actualDate));
        assertEquals(sdf.format(expectedDate), sdf.format(actualDate));

        testCal.set(Calendar.MONTH, Calendar.JANUARY);
        testCal.set(Calendar.DAY_OF_MONTH, 14);
        testCal.set(Calendar.YEAR, 2008);
        anotherTestCal.set(Calendar.MONTH, Calendar.DECEMBER);
        anotherTestCal.set(Calendar.DAY_OF_MONTH, 15);
        anotherTestCal.set(Calendar.YEAR, 2007);
        expectedDate = anotherTestCal.getTime();
        actualDate = AmpDateUtils.getDateBeforeDays(testCal.getTime(), DAYS);

        logger.info("Expected Date " + sdf.format(expectedDate));
        logger.info("Actual Date " + sdf.format(actualDate));
        assertEquals(sdf.format(expectedDate), sdf.format(actualDate));

        // leap year
        testCal.set(Calendar.MONTH, Calendar.MARCH);
        testCal.set(Calendar.DAY_OF_MONTH, 14);
        testCal.set(Calendar.YEAR, 2008);
        anotherTestCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        anotherTestCal.set(Calendar.DAY_OF_MONTH, 13);
        anotherTestCal.set(Calendar.YEAR, 2008);
        expectedDate = anotherTestCal.getTime();
        actualDate = AmpDateUtils.getDateBeforeDays(testCal.getTime(), DAYS);

        logger.info("Expected Date " + sdf.format(expectedDate));
        logger.info("Actual Date " + sdf.format(actualDate));
        assertEquals(sdf.format(expectedDate), sdf.format(actualDate));

        // test february non leap year
        testCal.set(Calendar.MONTH, Calendar.MARCH);
        testCal.set(Calendar.DAY_OF_MONTH, 14);
        testCal.set(Calendar.YEAR, 2009);
        anotherTestCal.set(Calendar.MONTH, Calendar.FEBRUARY);
        anotherTestCal.set(Calendar.DAY_OF_MONTH, 12);
        anotherTestCal.set(Calendar.YEAR, 2009);
        expectedDate = anotherTestCal.getTime();
        actualDate = AmpDateUtils.getDateBeforeDays(testCal.getTime(), DAYS);

        logger.info("Expected Date " + sdf.format(expectedDate));
        logger.info("Actual Date " + sdf.format(actualDate));
        assertEquals(sdf.format(expectedDate), sdf.format(actualDate));



    }
}

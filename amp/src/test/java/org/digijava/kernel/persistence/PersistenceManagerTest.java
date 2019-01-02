package org.digijava.kernel.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicReference;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author Octavian Ciubotaru
 */
@Category(DatabaseTests.class)
public class PersistenceManagerTest {

    private static final String TEST_REPORT_NAME = "test report name 1234567890";

    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();

        PersistenceManager.inTransaction(() -> {
            for (Object report : testReportCriteria().list()) {
                PersistenceManager.getSession().delete(report);
            }
        });
    }

    @Test
    public void testSuccessfulTransaction() {
        try {
            PersistenceManager.inTransaction(this::saveTestReport);
            PersistenceManager.inTransaction(() -> assertTrue(testReportExists()));
        } finally {
            PersistenceManager.inTransaction(this::deleteTestReport);
        }
    }

    @Test
    public void testRollback() {
        try {
            PersistenceManager.inTransaction(() -> {
                saveTestReport();
                throw new RuntimeException("trigger rollback");
            });

            fail("Exception was swallowed!");
        } catch (RuntimeException e) {
            assertEquals("trigger rollback", e.getMessage());
        }

        PersistenceManager.inTransaction(() -> assertFalse(testReportExists()));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSessionNotAllowedOutsideInTransactionMethod() {
        PersistenceManager.getSession();
    }

    @Test
    public void testRecursiveTransactions() {
        try {
            PersistenceManager.inTransaction(() -> {
                saveTestReport();
                PersistenceManager.inTransaction(() -> {
                    assertTrue(testReportExists());
                });
                assertTrue(testReportExists());
            });

            PersistenceManager.inTransaction(() -> assertTrue(testReportExists()));
        } finally {
            PersistenceManager.inTransaction(this::deleteTestReport);
        }
    }

    @Test
    public void testSessionIsClosedAndDisconnected() {
        try {
            AtomicReference<Session> sessionRef = new AtomicReference<>();

            PersistenceManager.inTransaction(() -> {
                saveTestReport();

                Session session = PersistenceManager.getSession();
                sessionRef.set(session);

                assertTrue(session.isOpen());
                assertTrue(session.isConnected());
                assertTrue(session.isDirty());
            });

            Session session = sessionRef.get();
            assertFalse(session.isOpen());
            assertFalse(session.isConnected());
        } finally {
            PersistenceManager.inTransaction(this::deleteTestReport);
        }
    }

    @Test
    public void testDifferentSessionOnEachCall() {
        AtomicReference<Session> sessionRef1 = new AtomicReference<>();
        AtomicReference<Session> sessionRef2 = new AtomicReference<>();
        PersistenceManager.inTransaction(() -> sessionRef1.set(PersistenceManager.getSession()));
        PersistenceManager.inTransaction(() -> sessionRef2.set(PersistenceManager.getSession()));
        assertNotEquals(sessionRef1.get(), sessionRef2.get());
    }

    private boolean testReportExists() {
        Integer count = (Integer) testReportCriteria()
                .setProjection(Projections.rowCount())
                .uniqueResult();
        return count > 0;
    }

    private void saveTestReport() {
        AmpReports report = new AmpReports();
        report.setName(TEST_REPORT_NAME);
        PersistenceManager.getSession().save(report);
    }

    private void deleteTestReport() {
        AmpReports report = (AmpReports) testReportCriteria().uniqueResult();
        PersistenceManager.getSession().delete(report);
    }

    private static Criteria testReportCriteria() {
        return PersistenceManager.getSession().createCriteria(AmpReports.class)
                .add(Property.forName("name").eq(TEST_REPORT_NAME));
    }
}

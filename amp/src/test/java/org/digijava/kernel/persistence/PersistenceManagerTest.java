package org.digijava.kernel.persistence;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import javax.validation.ConstraintViolationException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;

/**
 * @author Octavian Ciubotaru
 */
@Tag("databasetests")
public class PersistenceManagerTest {

    private static final String TEST_REPORT_NAME = "test report name 1234567890";

    @BeforeAll
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
            PersistenceManager.inTransaction(() -> Assertions.assertTrue(testReportExists()));
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
            Assertions.assertEquals("trigger rollback", e.getMessage());
        }

        PersistenceManager.inTransaction(() -> Assertions.assertFalse(testReportExists()));
    }

    @Test
    public void testGetSessionNotAllowedOutsideInTransactionMethod() {
        assertThrows(IllegalStateException.class, PersistenceManager::getSession);
    }

    @Test
    public void testRecursiveTransactions() {
        try {
            PersistenceManager.inTransaction(() -> {
                saveTestReport();
                PersistenceManager.inTransaction(() -> {
                    Assertions.assertTrue(testReportExists());
                });
                Assertions.assertTrue(testReportExists());
            });

            PersistenceManager.inTransaction(() -> Assertions.assertTrue(testReportExists()));
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

                Assertions.assertTrue(session.isOpen());
                Assertions.assertTrue(session.isConnected());
                Assertions.assertTrue(session.isDirty());
            });

            Session session = sessionRef.get();
            Assertions.assertFalse(session.isOpen());
            Assertions.assertFalse(session.isConnected());
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
        Assertions.assertNotEquals(sessionRef1.get(), sessionRef2.get());
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

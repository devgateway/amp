package org.digijava.module.aim.util;

import java.util.Collection;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.hibernate.Session;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class QuartzJobClassUtils {
    public static Collection<AmpQuartzJobClass> getAllJobClasses() {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String queryString = "select cls from " + AmpQuartzJobClass.class.getName() + " cls";
            Query query = session.createQuery(queryString);
            return query.list();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static AmpQuartzJobClass getJobClassesById(Long id) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            return (AmpQuartzJobClass) session.load(AmpQuartzJobClass.class, id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static AmpQuartzJobClass getJobClassesByName(String name) {
        try {
            SessionFactory sessionFactory = PersistenceManager.sf();
            Session session = sessionFactory.getCurrentSession();

            String queryString = "select cls from " + AmpQuartzJobClass.class.getName() + " cls where cls.name=:name";
            Query query = session.createQuery(queryString);
            query.setString("name", name);
            if (!query.list().isEmpty()) {
                return (AmpQuartzJobClass) query.iterate().next();
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static AmpQuartzJobClass getJobClassesByClassfullName(String classfullname) {
        Transaction tx = null;
        Session session = null;

        try {
            PersistenceManager.initialize(true);
            session = PersistenceManager.getRequestDBSession();

            tx = session.getTransaction();
            String queryString = "select cls from " + AmpQuartzJobClass.class.getName() + " cls where cls.classFullname= '" + classfullname + "'";
//            String qr = "SELECT * FROM AMP_QUARTZ_JOB_CLASS WHERE jc_class_fullname=:classfullname LIMIT 1";
            Query query = session.createQuery(queryString);
//            query.setParameter("classfullname", classfullname);
            AmpQuartzJobClass result = (AmpQuartzJobClass) query.getSingleResult();
            tx.commit();

            return result;

        } catch (Exception ex) {
//            if (tx != null) {
//                tx.rollback();
//            }
            ex.printStackTrace();
            return null;

        }
    }




    public static void deleteJobClasses(Long id) {
        AmpQuartzJobClass jc = getJobClassesById(id);
        if (jc != null) {
            try {
                Session session = PersistenceManager.getRequestDBSession();
//beginTransaction();
                session.delete(jc);
              //  tr.commit();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void updateJobClasses(AmpQuartzJobClass jc) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.update(jc);
      //     tr.commit();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void addJobClasses(AmpQuartzJobClass jc) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.save(jc);
         //   tr.commit();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public QuartzJobClassUtils() {
    }
}

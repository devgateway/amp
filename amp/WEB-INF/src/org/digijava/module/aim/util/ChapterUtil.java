package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpChapter;
import org.digijava.module.aim.dbentity.AmpImputation;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ChapterUtil {
    private static Logger logger = Logger.getLogger(ChapterUtil.class);
    private static DecimalFormat df = new DecimalFormat(
            "###########################");

    public static AmpChapter getChapterByCode(String chapterCode)
            throws DgException, HibernateException, SQLException {
        AmpChapter chapter = null;
        if(chapterCode != null) {
            Session hs = PersistenceManager.getRequestDBSession();
            chapter = (AmpChapter) hs.get(AmpChapter.class, chapterCode);
        }
        return chapter;
    }

    public static AmpImputation getImputationByCode(String imputationCode)
            throws DgException, HibernateException, SQLException {
        Session hs = PersistenceManager.getRequestDBSession();
        AmpImputation imp = (AmpImputation) hs.get(AmpImputation.class,
                imputationCode);
        return imp;
    }

    public static void saveChapter(AmpChapter chapter) throws DgException,
            HibernateException, SQLException {
        Session hs = PersistenceManager.getRequestDBSession();
//beginTransaction();
        hs.saveOrUpdate(chapter);
        //t.commit();
    }

    public static void saveImputation(AmpImputation imp) throws DgException,
            HibernateException, SQLException {
        Session hs = PersistenceManager.getRequestDBSession();
//beginTransaction();
        hs.saveOrUpdate(imp);
        //transaction.commit();
    }

    public static Collection<Integer> getDistinctChapterYearList() {
        return PersistenceManager.getSession().createQuery("SELECT distinct(year) FROM " + AmpChapter.class.getName()).list();
    }

    
    
    public static Collection<String> getChapterListForYear(Integer year) {
        return PersistenceManager.getSession().createQuery("SELECT code from " + AmpChapter.class.getName() + " WHERE year=:year").setInteger("year", year).list();
    }

    public static String getNumberFromCell(Cell c) {
        if (c.getCellType() == Cell.CELL_TYPE_STRING) {
            return c.getStringCellValue();
        } else if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return df.format(c.getNumericCellValue());
        } else
            throw new RuntimeException("Unsupported Cell Type "
                    + c.getCellType());
    }

}

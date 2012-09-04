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
import org.hibernate.Query;
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
			PersistenceManager.releaseSession(hs);
		}
		return chapter;
	}

	public static AmpImputation getImputationByCode(String imputationCode)
			throws DgException, HibernateException, SQLException {
		Session hs = PersistenceManager.getRequestDBSession();
		AmpImputation imp = (AmpImputation) hs.get(AmpImputation.class,
				imputationCode);
		PersistenceManager.releaseSession(hs);
		return imp;
	}

	public static void saveChapter(AmpChapter chapter) throws DgException,
			HibernateException, SQLException {
		Session hs = PersistenceManager.getRequestDBSession();
//beginTransaction();
		hs.saveOrUpdate(chapter);
		//t.commit();
		PersistenceManager.releaseSession(hs);
	}

	public static void saveImputation(AmpImputation imp) throws DgException,
			HibernateException, SQLException {
		Session hs = PersistenceManager.getRequestDBSession();
//beginTransaction();
		hs.saveOrUpdate(imp);
		//transaction.commit();
		PersistenceManager.releaseSession(hs);
	}

	public static Collection<Integer> getDistinctChapterYearList() {
		Collection<Integer> ret = null;
		try {
			Session hs = PersistenceManager.getRequestDBSession();
			Query query = hs.createQuery("SELECT distinct(year) FROM "
					+ AmpChapter.class.getName());
			ret = query.list();
			PersistenceManager.releaseSession(hs);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (DgException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return ret;
	}

	
	
	public static Collection<String> getChapterListForYear(Integer year) {
		Collection<String> ret = null;
		try {
			Session hs = PersistenceManager.getRequestDBSession();
			Query query = hs.createQuery("SELECT code from " + AmpChapter.class.getName()
					+ " WHERE year=:year");
			query.setInteger("year", year);
			ret = query.list();
			PersistenceManager.releaseSession(hs);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		} catch (DgException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return ret;
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

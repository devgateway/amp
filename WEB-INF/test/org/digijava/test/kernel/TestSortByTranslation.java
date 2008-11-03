package org.digijava.test.kernel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TranslationCallback;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.test.util.DigiTestBase;
import org.hibernate.Query;
import org.hibernate.Session;

public class TestSortByTranslation
    extends DigiTestBase {

  public class TestSBTCallback
      implements TranslationCallback {
    public String getSiteId(Object o) {
      return (String) ( (Object[]) o)[1];
    }

    public String getTranslationKey(Object o) {
      StringBuffer retVal = new StringBuffer("site:");
      retVal.append( (String) ( (Object[]) o)[1]);
      return retVal.toString();
    }

    public String getDefaultTranslation(Object o) {
      return (String) ( (Object[]) o)[2];
    }

  }

  public TestSortByTranslation(String name) {
    super(name,
          "org/digijava/test/kernel/conf/sortByTranslationTest.properties");
  }

  public void testSortByTranslation() throws Exception {
    List sites = getSites();
    Locale locale = new Locale();

    locale.setCode(getTestProperty("sortByTranslationTest.locale"));

    List translated =
        TrnUtil.sortByTranslation(sites, locale, new TestSBTCallback(), true);

/*
    List translated =
        TrnUtil.sortByTranslation(sites, locale, new TestSBTCallback());
        */

    List translatedSiteNames = new ArrayList();
    TestSBTCallback cb = new TestSBTCallback();
    TranslatorWorker trnWork = TranslatorWorker.getInstance();

    Iterator it = sites.iterator();

    while (it.hasNext()) {
      Object item = it.next();
      Site site = SiteCache.getInstance().getSite(cb.getSiteId(item));
      String trnString = null;

      try {
        Message trnMess = trnWork.get(cb.getTranslationKey(item), locale.getCode(),
                                      String.valueOf(site.getId()));
        if (trnMess == null && site.getParentId() != null) {
          Site root = SiteCache.getInstance().getRootSite(site);
          trnMess = trnWork.get(cb.getTranslationKey(item), locale.getCode(),
                                String.valueOf(root.getId()));
        }

        if (trnMess == null) {
          trnString = cb.getDefaultTranslation(item);
        }
        else {
          trnString = trnMess.getMessage();
        }
        translatedSiteNames.add(new String [] {trnString, site.getSiteId()});
      }
      catch (WorkerException ex) {
        logger.debug("Unable translation for specified key ", ex);
      }
    }

    Comparator testComparator = new Comparator(){
      public int compare(Object o1, Object o2) {
        return ((String[])o1)[0].compareTo(((String[])o2)[0]);
      }
    };


    Collections.sort(translatedSiteNames, testComparator);

    Iterator trIt = translated.iterator();
    boolean sortedCollectionsEqual = true;

    for (int siteIndex = 0; trIt.hasNext(); siteIndex ++) {
      String trnUtilSortedId = cb.getSiteId(trIt.next());
      String testSortedId = ((String[])translatedSiteNames.get(siteIndex))[1];
      if (!trnUtilSortedId.equals(testSortedId)) {
        sortedCollectionsEqual = false;
        break;
      }
    }
    assertEquals(sortedCollectionsEqual, true);
  }



  private List getSites() throws Exception {
    List sites = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      String queryString = "select rs.id, rs.siteId, rs.name from " +
          Site.class.getName() + " rs";
      Query q = session.createQuery(queryString);
      sites = q.list();
    }
    catch (Exception ex) {
      logger.debug("Unable to get site list from database ", ex);
      throw new Exception(
          "Unable to get site list from database ", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.warn("releaseSession() failed ", ex2);
      }
    }

    return sites;
  }
}
package org.digijava.module.ampharvester.util;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbUtil {

  public static void saveObjectTX(Object obj, Session session) throws DgException, HibernateException {
    Transaction tx = null;
    if (session == null) {
      session = PersistenceManager.getRequestDBSession();
    }
    try {
      tx = session.beginTransaction();
      session.saveOrUpdate(obj);
      tx.commit();
    } finally {
      if (tx != null && !tx.wasCommitted()) {
        tx.rollback();
      }
    }

  }

  public static void saveObject(Object obj, Session session) throws DgException, HibernateException {
    session.saveOrUpdate(obj);
  }

  public static void saveListTX(List objs, Session session) throws DgException, HibernateException {
    Transaction tx = null;
    if (session == null) {
      session = PersistenceManager.getRequestDBSession();
    }
    try {
      tx = session.beginTransaction();

      for (Object obj : objs) {
        session.saveOrUpdate(obj);
      }

      tx.commit();
    } finally {
      if (tx != null && !tx.wasCommitted()) {
        tx.rollback();
      }
    }
  }

  public static ResultSet execStatement(String sql, Session session) throws DgException, HibernateException {
    try {
      Statement st = null;
      if (session == null) {
        session = PersistenceManager.getRequestDBSession();
      }
      try {
        st = session.connection().createStatement();

        return st.executeQuery(sql);
      } finally {
        st.close();
      }
    } catch (Exception ex) {
      throw new DgException(ex);
    }
  }

  public static void deleteObjectTX(Object obj, Session session) throws DgException, HibernateException {

    Transaction tx = null;
    if (session == null) {
      session = PersistenceManager.getRequestDBSession();
    }
    try {
      tx = session.beginTransaction();
      session.delete(obj);
      tx.commit();
    } finally {
      if (tx != null && !tx.wasCommitted()) {
        tx.rollback();
      }
    }
  }

  public static void deleteObject(Object obj, Session session) throws DgException, HibernateException {
    session.delete(obj);
  }

  public static Object loadObject(String oql, Session session) throws DgException, HibernateException {
    return loadObject(oql, session, null);
  }

  public static List loadList(String OQL, Session session) throws DgException, HibernateException {
    return loadList(OQL, session, null);
  }

  public static List loadList(String OQL, Session session, Map<String, Object> param) throws DgException, HibernateException {
    if (session == null) {
      session = PersistenceManager.getRequestDBSession();
    }
    Query q = session.createQuery(OQL);
    if (param != null) {
      for (String key : param.keySet()) {
        q.setParameter(key, param.get(key));
      }
    }
    return q.list();
  }

  public static Object loadObject(String OQL, Session session, Map<String, Object> param) throws DgException, HibernateException {
    if (session == null) {
      session = PersistenceManager.getRequestDBSession();
    }
    Query q = session.createQuery(OQL);
    if (param != null) {
      for (String key : param.keySet()) {
        q.setParameter(key, param.get(key));
      }
    }
    List list = q.list();

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    } else {
      return null;
    }
  }

////////////////////////////////////////////


  public static Editor getEditor(String siteId, String language, String editorKey, Session session) throws DgException, HibernateException {

    String oql = " from " + Editor.class.getName() + " as obj " +
        " where upper(obj.siteId) = upper(:sSiteId) and" +
        " upper(obj.language) = upper(:sLanguage) and" +
        " upper(obj.editorKey) = upper(:sEditorKey)";

    Map<String, Object> param = new HashMap();
    param.put("sSiteId", siteId);
    param.put("sLanguage", language);
    param.put("sEditorKey", editorKey);

    return (Editor)loadObject(oql, session, param);
  }

  public static AmpCategoryClass getAmpCategoryClassByKey(String key, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpCategoryClass.class.getName() + " as obj " +
        " where upper(obj.keyName) = upper(:sKey) ";
    Map<String, Object> param = new HashMap();
    param.put("sKey", key);

    return (AmpCategoryClass)loadObject(oql, session, param);
  }

  public static AmpOrganisation getAmpOrganisationByCode(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpOrganisation.class.getName() + " as obj " +
        " where upper(obj.orgCode) = upper(:sKey) ";
    Map<String, Object> param = new HashMap();
    param.put("sKey", code);

    return (AmpOrganisation)loadObject(oql, session, param);
  }

  public static List<AmpLocation> getAmpLocationByName(String name, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpLocation.class.getName() + " as obj " +
        " where upper(obj.name) = upper(:sKey) ";
    Map<String, Object> param = new HashMap();
    param.put("sKey", name);

    return loadList(oql, session, param);
  }

  public static AmpLocation getAmpLocationByName(String name, String region, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpLocation.class.getName() + " as obj " +
        " where upper(obj.country) = upper(:sName) ";
    Map<String, Object> param = new HashMap();
    param.put("sName", name);

    if (region != null) {
      oql += " and upper(obj.ampRegion.name) = upper(:sRegion) ";
      param.put("sRegion", region);
    }

    return (AmpLocation)loadObject(oql, session, param);
  }

  public static void deleteObjectSet(Set set, Session session) throws DgException, HibernateException {
    deleteObjects(new LinkedList(set), session);
  }

  public static void deleteObjects(List list, Session session) throws DgException, HibernateException {
    for (Object elem : list) {
      deleteObject(elem, session);
    }
  }

  public static void deleteRegionalFundings(AmpLocation location, Set set, Session session) throws DgException, HibernateException {
    List list = new LinkedList();

    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
      AmpRegionalFunding arf = (AmpRegionalFunding)iter.next();

      if (arf.getRegion().getName().trim().equalsIgnoreCase(location.getRegion().trim())) {
        list.add(arf);
        iter.remove();
      }
    }

    deleteObjects(list, session);
  }

  public static void deleteComponentFunding(Long componentId, Long activityId, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpComponentFunding.class.getName() + " as obj " +
        " where obj.component.ampComponentId = :componentId and obj.activity.ampActivityId= :activityId ";
    Map<String, Object> param = new HashMap();
    param.put("componentId", componentId);
    param.put("activityId", activityId);

    deleteObjects(loadList(oql, session, param), session);
  }

  public static AmpActivity getAmpActivityById(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpActivity.class.getName() + " as obj " +
        " where upper(obj.ampId) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpActivity)loadObject(oql, session, param);
  }

  public static List<AmpLocation> getAmpLocation(Session session) throws DgException, HibernateException {
    String oql = " from " + AmpLocation.class.getName() + " as obj ";

    return loadList(oql, session, null);
  }

  public static List<AmpTeam> getAmpTeam(Session session) throws DgException, HibernateException {
    String oql = " from " + AmpTeam.class.getName() + " as obj ";

    return loadList(oql, session, null);
  }

  public static AmpTeam getAmpTeamById(long id, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpTeam.class.getName() + " as obj " +
        " where obj.ampTeamId = :iId ";
    Map<String, Object> param = new HashMap();
    param.put("iId", id);

    return (AmpTeam)loadObject(oql, session, param);
  }

  public static AmpCurrency getAmpCurrency(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpCurrency.class.getName() + " as obj " +
        " where upper(obj.currencyCode) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpCurrency)loadObject(oql, session, param);
  }

  public static AmpRegion getAmpRegion(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpRegion.class.getName() + " as obj " +
        " where upper(obj.name) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpRegion)loadObject(oql, session, param);
  }

  public static AmpSector getAmpSector(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpSector.class.getName() + " as obj " +
        " where upper(obj.sectorCode) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpSector)loadObject(oql, session, param);
  }

  public static AmpTheme getAmpTheme(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpTheme.class.getName() + " as obj " +
        " where upper(obj.themeCode) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpTheme)loadObject(oql, session, param);
  }

//  Remove for head
//  public static AmpTermsAssist getAmpTermsAssist(String code, Session session) throws DgException, HibernateException {
//    String oql = " from " + AmpTermsAssist.class.getName() + " as obj " +
//        " where upper(obj.termsAssistCode) = upper(:sCode) ";
//    Map<String, Object> param = new HashMap();
//    param.put("sCode", code);
//
//    return (AmpTermsAssist)loadObject(oql, session, param);
//  }

  public static AmpCategoryValue getAmpCategoryValue(String key, int index, Session session)throws DgException, HibernateException {
    String oql = " from " + AmpCategoryValue.class.getName() + " as obj " +
        " where upper(obj.ampCategoryClass.keyName) = upper(:sKey) and "+
        " obj.index =  :iIndex";
    Map<String, Object> param = new HashMap();
    param.put("sKey", key);
    param.put("iIndex", index);

    return (AmpCategoryValue)loadObject(oql, session, param);
  }

  public static AmpComponent getAmpComponent(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpComponent.class.getName() + " as obj " +
        " where upper(obj.code) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpComponent)loadObject(oql, session, param);
  }

  public static AmpRole getAmpRole(String code, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpRole.class.getName() + " as obj " +
        " where upper(obj.roleCode) = upper(:sCode) ";
    Map<String, Object> param = new HashMap();
    param.put("sCode", code);

    return (AmpRole)loadObject(oql, session, param);
  }

  public static AmpComponentFunding getAmpComponentFunding(long id, Session session) throws DgException, HibernateException {
    String oql = " from " + AmpComponentFunding.class.getName() + " as obj " +
        " where obj.component.ampComponentId = :iId ";
    Map<String, Object> param = new HashMap();
    param.put("iId", id);

    return (AmpComponentFunding)loadObject(oql, session, param);
  }

}

package org.digijava.module.budget.helper;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BudgetDbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);
    
    /**
     * 
     * @return ArrayList of Departments
     */
    public static ArrayList<AmpDepartments> getDepartments(){
        Session session = null;
        Query q = null;
        session = PersistenceManager.getSession();
        String queryString = new String();
        queryString = "select d from " + AmpDepartments.class.getName() + " d order by d.name";
        q = session.createQuery(queryString);
        ArrayList<AmpDepartments> departments =new ArrayList<AmpDepartments>(q.list());
        Collections.sort(departments);
        return departments;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public static AmpBudgetSector getBudgetSectorById(Long id){

        String queryString = "select s from " + AmpBudgetSector.class.getName() + " s where s.idsector=:id";
        return (AmpBudgetSector) PersistenceManager.getSession().createQuery(queryString).setLong("id", id).uniqueResult();
    }
    
    public static boolean existsBudgetSector(String name ,String code , Long id){
        String queryString = "select s from " + AmpBudgetSector.class.getName() +" s where (s.sectorname=:name or s.code=:code) " ;
        if (id != null) {
            queryString += " and s.idsector != :id" ;
        }
        Query q = PersistenceManager.getSession().createQuery(queryString);
        if (id != null) {
            q.setLong("id", id);
        }           
        q.setParameter("name", name);
        q.setParameter("code", code);
        return !(q.list().isEmpty());
    }
    
    public static AmpDepartments getBudgetDepartmentById(Long id){
        return (AmpDepartments) PersistenceManager.getSession().get(AmpDepartments.class, id);
    }
    
    /**
     * 
     * @return ArrayList of budget sectors
     */
    public static ArrayList<AmpBudgetSector> getBudgetSectors(){
        String queryString = "select s from " + AmpBudgetSector.class.getName() + " s order by s.sectorname";
        return new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).list());
    }
    
    /**
     * 
     * @param orgid
     * @return AmpDepartments ArrayList
     */
    public static Collection<AmpDepartments> getDepartmentsbyOrg(Long orgid){
        ArrayList<AmpDepartments> departments;
        
        String queryString = "select dep from " + AmpDepartments.class.getName() +
        " dep inner join dep.organisations org"+
        " where (org.ampOrgId=:orgid)";
            
        departments = new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).setLong("orgid", orgid).list());
        Collections.sort(departments);
        return departments;
    }
    
    /**
     * 
     * @param budgedsectorid
     * @return organizations ArrayList
     */
    public static Collection<AmpOrganisation> getOrganizationsBySector(Long budgedsectorid){
        String queryString = "select org from " + AmpOrganisation.class.getName() + " org inner join org.budgetsectors s" +
            " where (s.idsector=:budgedsectorid) and (org.deleted is null or org.deleted = false) ";
            
        ArrayList<AmpOrganisation> orgs = new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).setLong("budgedsectorid", budgedsectorid).list());
        Collections.sort(orgs);
        return orgs;
    }
    /**
     * 
     * @param sector
     */
    public static void SaveBudgetSector(AmpBudgetSector sector) {
        PersistenceManager.getSession().save(sector);
    }
    
    /**
     * 
     * @param sector
     */
    public static void UpdateBudgetSector(AmpBudgetSector sector) {
        PersistenceManager.getSession().update(sector);
    }
    
    /**
     * 
     * @param id
     */
    
    public static void DeleteSector(Long id){
        Session session;
        AmpBudgetSector s = null;
        session = PersistenceManager.getSession();
        s = (AmpBudgetSector) session.load(AmpBudgetSector.class, id);
        session.delete(s);
    }
    
    /**
     * 
     * @return
     */
    public static ArrayList<AmpTheme> getBudgetPrograms() {
        String queryString = "select p from " + AmpTheme.class.getName() +
            " p where p.parentThemeId IN(select pp.ampThemeId from " + AmpTheme.class.getName()+
            " pp where pp.indlevel=0 and pp.isbudgetprogram=1)";
        return new ArrayList<>(PersistenceManager.getSession().createQuery(queryString).list());
    }
    
    /**
     * 
     * @return
     */
    public static ArrayList<AmpTheme> getBudgetProgramsLevel_0(){
        String queryString = "select p from " + AmpTheme.class.getName() + " p where p.indlevel = 0";
        return new ArrayList<AmpTheme>(PersistenceManager.getSession().createQuery(queryString).list());
    }
    
    /**
     * 
     * @param dep
     */
    public static void saveDepartment(AmpDepartments dep) throws DgException {
        PersistenceManager.getSession().save(dep);
    }
    
    /**
     * 
     * @param id
     */
    public static void DeleteDepartment(Long id){
        Session session;
        AmpDepartments s;
        session = PersistenceManager.getSession();
        s = (AmpDepartments) session.load(AmpDepartments.class, id);
        session.delete(s);
    }
    
    public static void UpdateBudgetDepartment(AmpDepartments dep){
        PersistenceManager.getSession().update(dep);
    }   
    
    public static boolean existsDepartment(String name, String code, Long id) {
        String queryString = "select s from " + AmpDepartments.class.getName() +" s where (s.name=:name or s.code=:code) " ;
        if (id != null) {
            queryString += " and s.id!=:id" ;
        }
        Query q = PersistenceManager.getSession().createQuery(queryString).setParameter("name", name).setParameter("code", code);
        if (id != null){
            q.setLong("id", id);
        }
        return !q.list().isEmpty();
    }
}

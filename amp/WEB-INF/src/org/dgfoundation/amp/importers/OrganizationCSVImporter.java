/**
 * 
 */
package org.dgfoundation.amp.importers;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.type.StringType;


/**
 * @author mihai
 *
 */
public class OrganizationCSVImporter extends CSVImporter {

    private static Logger logger = Logger.getLogger(OrganizationCSVImporter.class);
    
    
    
    public OrganizationCSVImporter(String importFileName, String[] columnNames,
            Properties extraProperties) {
        super(importFileName, columnNames, extraProperties);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.importers.AmpImporter#getImportedTypes()
     */
    @Override
    protected Class[] getImportedTypes() {
        return new Class[]{AmpOrganisation.class, AmpOrgGroup.class};
    }
    
    public AmpOrgGroup getAmpOrgGroupByCode(String code) throws HibernateException {
        AmpOrgGroup orgGrp=null;
           String queryString = "select o from "
               + AmpOrgGroup.class.getName() + " o "
               + "where (o.orgGrpCode=:code)";
           Query qry = session.createQuery(queryString);
           qry.setParameter("code", code, StringType.INSTANCE);
           Iterator itr = qry.list().iterator();
           if (itr.hasNext()) {
            orgGrp = (AmpOrgGroup) itr.next();
           }
           
           return orgGrp;
    }

    public AmpOrgType getAmpGroupTypeByName(String name) throws HibernateException {
        AmpOrgType orgGrp=null;
        String queryString = "select o from "
                + AmpOrgType.class.getName() + " o "
                // + "where (o.orgType=:name)";
                + String.format("where (%s=:name)", AmpOrgType.hqlStringForName("o"));
           Query qry = session.createQuery(queryString);
           qry.setParameter("name", name, StringType.INSTANCE);
           Iterator itr = qry.list().iterator();
           if (itr.hasNext()) {
            orgGrp = (AmpOrgType) itr.next();
           }
           return orgGrp;
           
          
    }

    
    @Override
    protected void saveToDB(Map<String, String> o) throws HibernateException {
        
        logger.info("importing: "+o);
        
//beginTransaction();

        //try to locate the org group
        AmpOrgGroup ampOrgGroupByCode = getAmpOrgGroupByCode(o.get("Group code"));
        //if the org grp is null, we create and read a new one:
        if(ampOrgGroupByCode==null) {
            ampOrgGroupByCode=new AmpOrgGroup();
            ampOrgGroupByCode.setOrgGrpCode(o.get("Group code"));
            ampOrgGroupByCode.setOrgGrpName(o.get("Groupe"));
            
            //try to locate the org group type:
            AmpOrgType aot=getAmpGroupTypeByName(o.get("Group type"));
            if(aot==null) {
                aot=new AmpOrgType();
                aot.setOrgType(o.get("Group type"));
                session.save(aot);
            }
            ampOrgGroupByCode.setOrgType(aot);
            
            session.save(ampOrgGroupByCode);
        }
        
        //now import the org
        AmpOrganisation ao=new AmpOrganisation();
        ao.setOrgGrpId(ampOrgGroupByCode);
        ao.setOrgCode(o.get("Code"));
        ao.setName(o.get("Organisations"));
        ao.setAcronym(o.get("acronym"));
        
        //try to locate the org type:
        AmpOrgType aot=getAmpGroupTypeByName(o.get("type"));
        if(aot==null) {
            aot=new AmpOrgType();
            aot.setOrgType(o.get("type"));
            session.save(aot);
        }
        //ao.setOrgTypeId(aot);
        
        
        session.save(ao);
        
        //tx.commit();
        
    }

    


}

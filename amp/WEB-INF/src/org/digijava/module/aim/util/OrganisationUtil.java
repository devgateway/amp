/**
 * 
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nadejda Mandrescu
 */
public class OrganisationUtil {
    
    @SuppressWarnings("serial")
    public static final Map<String, String> ROLE_CODE_TO_COLUMN_MAP = new HashMap<String, String>() {{
        put(Constants.ROLE_CODE_DONOR, ColumnConstants.DONOR_AGENCY);
        put(Constants.ROLE_CODE_IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY);
        //put(Constants.ROLE_CODE_REPORTING_AGENCY, null);
        put(Constants.ROLE_CODE_BENEFICIARY_AGENCY, ColumnConstants.BENEFICIARY_AGENCY);
        put(Constants.ROLE_CODE_EXECUTING_AGENCY, ColumnConstants.EXECUTING_AGENCY);
        put(Constants.ROLE_CODE_RESPONSIBLE_ORG, ColumnConstants.RESPONSIBLE_ORGANIZATION);
        put(Constants.ROLE_CODE_CONTRACTING_AGENCY, ColumnConstants.CONTRACTING_AGENCY);
        put(Constants.ROLE_CODE_REGIONAL_GROUP, ColumnConstants.REGIONAL_GROUP);
        put(Constants.ROLE_CODE_SECTOR_GROUP, ColumnConstants.SECTOR_GROUP);
        put(Constants.ROLE_CODE_COMPONENT_FUNDING_ORGANIZATION, ColumnConstants.COMPONENT_FUNDING_ORGANIZATION);
        put(Constants.ROLE_CODE_COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION, ColumnConstants.COMPONENT_SECOND_RESPONSIBLE_ORGANIZATION);
    }};
    
    protected static Logger logger = LoggerFactory.getLogger(OrganisationUtil.class);
    
    /**
     * @return a list of role codes (e.g. 'BA', 'DN') that are enabled in Feature Manager
     */
    public static final List<String> getVisibleRoleCodes() {
        Set<String> visibleColumns = ColumnsVisibility.getVisibleColumns();
        List<String> roles = new ArrayList<String>();
        for (Entry<String, String> roleColumn : ROLE_CODE_TO_COLUMN_MAP.entrySet()) {
            if (visibleColumns.contains(roleColumn.getValue())) {
                roles.add(roleColumn.getKey());
            }
        }
        
        return roles;
    }
    
    /**
     * Get all organisations
     * 
     */
    public static List<AmpOrganisation> getAllOrganisations() {
        Session session = null;
        Query qry = null;
        List<AmpOrganisation> orgs = new ArrayList<AmpOrganisation>();
        
        try  {
            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpOrganisation.class.getName() + " org";
            qry = session.createQuery(queryString);
            orgs = qry.list();
        } catch(Exception ex) {
            throw new RuntimeException("Cannot get organizations, ", ex);
        }
        
        return orgs;
    }
    
    /**
     * Get all organization groups
     * 
     */
    public static List<AmpOrgGroup> getAllOrgGroups() {
        Session session = null;
        Query qry = null;
        List<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>();
        
        try  {
            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpOrgGroup.class.getName() + " orggrp";
            qry = session.createQuery(queryString);
            orgGroups = qry.list();
        } catch(Exception ex) {
            throw new RuntimeException("Cannot get organization groups, ", ex);
        }
        
        return orgGroups;
    }
    
    /**
     * Get all organisations
     * 
     */
    public static List<AmpOrgType> getAllOrgTypes() {
        Session session = null;
        Query qry = null;
        List<AmpOrgType> orgGroups = new ArrayList<AmpOrgType>();
        
        try  {
            session = PersistenceManager.getRequestDBSession();
            String queryString = " from " + AmpOrgType.class.getName() + " orgtype";
            qry = session.createQuery(queryString);
            orgGroups = qry.list();
        } catch(Exception ex) {
            throw new RuntimeException("Cannot get organization types, ", ex);
        }
        
        return orgGroups;
    }
    
     public static String getComputationOrgsQry(AmpTeam team) {
            String orgIds = "";
            if (team.getComputation() != null && team.getComputation()) {
                Set<AmpOrganisation> orgs = team.getOrganizations();
                Iterator<AmpOrganisation> orgIter = orgs.iterator();
                while (orgIter.hasNext()) {
                    AmpOrganisation org = orgIter.next();
                    orgIds += org.getAmpOrgId() + ",";
                }

            }
            
            return orgIds;
     }

    /**
     * Gets the {@link AmpOrganisation} {@link AmpRole}S from DB
     * @return a {@link List} of {@link AmpRole}S
     */
    public static List<AmpRole> getOrgRoles() {
        return PersistenceManager.getRequestDBSession()
                .createCriteria(AmpRole.class)
                .list();
    }
    
    public static void checkOrganisationNamesSanity(Session session) {
        List<String> orgNamesWithTrailSpaces = OrganisationUtil.getOrganisationNamesWithTrailSpaces(session);
        List<String> orgNamesWithoutTrailSpaces = OrganisationUtil.getOrganisationNamesWithoutTrailSpaces(session);
    
        List<String> duplicatedOrgNames = orgNamesWithTrailSpaces.stream()
                .filter(name -> orgNamesWithoutTrailSpaces.contains(name.trim()))
                .collect(Collectors.toList());
    
        duplicatedOrgNames.forEach(orgName -> logger.warn(
                String.format("Found organisations with the same name: '%s' and '%s", orgName, orgName.trim())));
    }
    
    public static List<String> getOrganisationNamesWithTrailSpaces(Session session) {
        return session.createCriteria(AmpOrganisation.class)
                .add(Restrictions.sqlRestriction("name <> TRIM(name)"))
                .add(Restrictions.or(Restrictions.eq("deleted", false), Restrictions.isNull("deleted")))
                .setProjection(Projections.distinct(Projections.property("name")))
                .list();
    }
    
    public static List<String> getOrganisationNamesWithoutTrailSpaces(Session session) {
        return session.createCriteria(AmpOrganisation.class)
                .add(Restrictions.sqlRestriction("name = TRIM(name)"))
                .add(Restrictions.or(Restrictions.eq("deleted", false), Restrictions.isNull("deleted")))
                .setProjection(Projections.distinct(Projections.property("name")))
                .list();
    }
}

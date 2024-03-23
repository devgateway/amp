/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.DatabaseWaver;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.kernel.util.collections.HierarchyMember;
import org.digijava.kernel.util.collections.HierarchyMemberFactory;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.*;
import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;
import static org.digijava.kernel.ampapi.endpoints.ndd.DashboardService.MAX_LEVELS;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_DESTINATION_PROGRAM;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_SOURCE_PROGRAM;

public class ProgramUtil {
    private static final Logger logger = Logger.getLogger(ProgramUtil.class);

    @Deprecated
    public static final int YAERS_LIST_START = 2000;
    public static final String NATIONAL_PLANNING_OBJECTIVES = "National Planning Objectives";
    public static final String NATIONAL_PLAN_OBJECTIVE = "National Plan Objective";
    public static final String PRIMARY_PROGRAM = "Primary Program";
    public static final String SECONDARY_PROGRAM = "Secondary Program";
    public static final String TERTIARY_PROGRAM = "Tertiary Program";
    public static final String INDIRECT_PRIMARY_PROGRAM = "Indirect Primary Program";
    public static final int NATIONAL_PLAN_OBJECTIVE_KEY = 1;
    public static final int PRIMARY_PROGRAM_KEY = 2;
    public static final int SECONDARY_PROGRAM_KEY = 3;

    @SuppressWarnings("serial")
    public static final Map<String, String> NAME_TO_COLUMN_MAP = new HashMap<String, String>() {{
        put(NATIONAL_PLAN_OBJECTIVE, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
        put(PRIMARY_PROGRAM, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1);
        put(SECONDARY_PROGRAM, ColumnConstants.SECONDARY_PROGRAM_LEVEL_1);
        put(TERTIARY_PROGRAM, ColumnConstants.TERTIARY_PROGRAM_LEVEL_1);
        put(INDIRECT_PRIMARY_PROGRAM, ColumnConstants.INDIRECT_PRIMARY_PROGRAM_LEVEL_1);
    }};

    public static final ImmutableList<String> PROGRAM_NAMES = ImmutableList.of(NATIONAL_PLANNING_OBJECTIVES,
            PRIMARY_PROGRAM, SECONDARY_PROGRAM, TERTIARY_PROGRAM);


        public static Collection getAllIndicatorsFromPrograms(Collection programs) throws AimException{
            try {
                Collection result= null;
                if (programs!= null && programs.size() >0){
                    result = new ArrayList();
                    long t = 1;
                    for (Object program : programs) {
                        AmpTheme OldProg = (AmpTheme) program;
                        AmpTheme prog = getThemeById(OldProg.getAmpThemeId());
                        Set indicators = prog.getIndicators();
                        if (indicators != null && indicators.size() > 0) {
                            for (Object o : indicators) {
                                AmpThemeIndicators indicator = (AmpThemeIndicators) o;
                                ActivityIndicator aiBean = new ActivityIndicator();
                                aiBean.setIndicatorId(indicator.getAmpThemeIndId());

                                aiBean.setIndicatorName(indicator.getName());
                                aiBean.setIndicatorCode(indicator.getCode());
                                aiBean.setIndicatorValId(t);
                                aiBean.setActivityId(t);
                                aiBean.setDefaultInd(false);
                                ++t;
                                result.add(aiBean);
                            }
                        }
                    }
                }
                return result;

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AimException("Cannot get programs for activity.",e);
            }

        }

        /**
         * Gets a theme/program by code
         * @param code theme code
         * @return
         */
        public static AmpTheme getThemeByCode(String code) {
            Session session = null;
            AmpTheme theme = null;

            try {
                session = PersistenceManager.getRequestDBSession();
                String qryStr = "select theme from " + AmpTheme.class.getName()
                        + " theme where (theme.themeCode=:code)";
                Query qry = session.createQuery(qryStr);
                qry.setParameter("code", code, StringType.INSTANCE);
                Iterator itr = qry.list().iterator();
                if (itr.hasNext()) {
                    theme = (AmpTheme) itr.next();
                }
            } catch (Exception e) {
                logger.error("Exception from getTheme()");
                logger.error(e.getMessage());
            }
            return theme;
        }

        public static AmpTheme getTheme(String name) {
            return getTheme(name, null);
        }

        public static AmpTheme getTheme(Long id) {
            return getTheme(null, id);
        }

        public static AmpTheme getTheme(String name, Long id) {
            Session session = null;
            AmpTheme theme = null;

            try {
                session = PersistenceManager.getRequestDBSession();
                String themeNameHql = AmpTheme.hqlStringForName("theme");
                String qryStr = "select theme from " + AmpTheme.class.getName() + " theme "
                        + ((id != null) ? " where (ampThemeId=:id)" : " where (" + themeNameHql + "=:name)");
                Query qry = session.createQuery(qryStr);
                if (id != null) {
                    qry.setParameter("id", id);
                } else {
                    qry.setParameter("name", name, StringType.INSTANCE);
                }
                qry.setCacheable(false);
                session.setCacheMode(CacheMode.REFRESH);
                Iterator itr = qry.list().iterator();
                if (itr.hasNext()) {
                    theme = (AmpTheme) itr.next();
                }
            } catch (Exception e) {
                logger.error("Exception from getTheme()");
                logger.error(e.getMessage());
            }
            return theme;
        }

        /**
         * Returns AmpThemeIndicators.
         * @param keyword String -
         * @return Coll AmpThemeIndicators
         */

        @Deprecated
        public static Collection getThemeindicators(String keyword) {
            Session session = null;
            Query qry = null;
            Collection ThemeIndicators = new ArrayList();
            keyword=keyword.toLowerCase();

            try {
                  session = PersistenceManager.getRequestDBSession();
                String queryString = "select t from " + AmpThemeIndicators.class.getName()
                        + " t where lower(name) like '%"+keyword+"%'";
                qry = session.createQuery(queryString);
                ThemeIndicators = qry.list();
            } catch (Exception e) {
                logger.error("Unable to get all themes : "+e);
                logger.debug("Exceptiion " + e);
            }
            return ThemeIndicators;
        }

        /**
         * Load theme by ID.
         * @param ampThemeId
         * @return
         * @throws DgException
         */
        public static AmpTheme getThemeById(Long ampThemeId) {
            try {
                return (AmpTheme) PersistenceManager.getRequestDBSession().load(AmpTheme.class, ampThemeId);
            } catch (Exception e) {
                throw new RuntimeException("Cannot load AmpTheme with id " + ampThemeId, e);
            }
        }

        public static Collection<AmpTheme> getAncestorThemes(AmpTheme theme) throws DgException {
            HashSet<AmpTheme> returnSet     = new HashSet<AmpTheme>();
            AmpTheme temp                           = getThemeById(theme.getAmpThemeId());
            if (temp != null)
                    temp                                        = temp.getParentThemeId();
            while ( temp!=null ) {
                returnSet.add(temp);
                temp        = temp.getParentThemeId();
            }
            return returnSet;
        }

        /**
         * Retrieves top level themes.
         * @return
         * @throws DgException
         */
        @SuppressWarnings("unchecked")
        public static List<AmpTheme> getParentThemes()
        {
            try
            {
                String queryString = "select t from " + AmpTheme.class.getName()
                        + " t where t.parentThemeId is null";
                Query qry = PersistenceManager.getRequestDBSession().createQuery(queryString);
                qry.setCacheable(false);
                return (List<AmpTheme>) qry.list();
            } catch (Exception e) {
                throw new RuntimeException("Cannot search parent themes", e);
            }
        }

        /**
         * returns list of of all parent themes which are configured
         * @return
         */
        @SuppressWarnings("unchecked")
        public static List<AmpTheme> getConfiguredParentThemes(boolean excludeIndirect) {
            try {
                String queryString = "select aaps.defaultHierarchy from "
                        + AmpActivityProgramSettings.class.getName() + " aaps WHERE aaps.defaultHierarchy IS NOT NULL ";
                if (excludeIndirect) {
                    queryString += " where ap.name <> :indirectName";
                }
                Query qry = PersistenceManager.getRequestDBSession().createQuery(queryString);
                if (excludeIndirect) {
                    qry.setParameter("indirectName", INDIRECT_PRIMARY_PROGRAM, StringType.INSTANCE);
                }

                return (List<AmpTheme>) qry.list();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Retrieves all themes from db.
         * @return
         * @throws DgException
         */
        @SuppressWarnings("unchecked")
        public static List<AmpTheme> getAllThemes() throws DgException
        {
            return getAllThemes(false);
        }

        public static Map<Long, AmpThemeSkeleton> getAllThemesFaster() throws DgException
        {
            Map<Long, AmpThemeSkeleton> themes = AmpThemeSkeleton.populateThemesTree(-1);
            return themes;
        }


        public static List<AmpTheme> getAllThemes(boolean includeSubThemes) throws DgException {
            return getAllThemes(includeSubThemes, false);
        }

        /**
         * Returns All AmpThemes including sub Themes if parameter is true.
         * @param includeSubThemes boolean false - only top level Themes, true - all themes
         * @return
         * @throws DgException
         */
        @SuppressWarnings("unchecked")
        public static List<AmpTheme> getAllThemes(boolean includeSubThemes, boolean getInvisible) throws DgException
        {
            Session session = null;
            Query qry = null;
            List<AmpTheme> themes = new ArrayList<AmpTheme>();

            try {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select t from " + AmpTheme.class.getName()+ " t where 1=1 ";
                if (!getInvisible) {
                    queryString += " and ((t.deleted is null) OR (t.deleted = false))";
                }

                if (!includeSubThemes) {
                    queryString += " and t.parentThemeId is null ";
                }
                qry = session.createQuery(queryString);
                session.setCacheMode(CacheMode.REFRESH);
                qry.setCacheable(false);
                themes = qry.list();
                themes.sort(new Comparator<AmpTheme>() {
                    public int compare(AmpTheme a, AmpTheme b) {
                        return a.getName().compareTo(b.getName());
                    }
                });
            }
            catch (Exception e) {
                throw new DgException("Cannot load themes hierarchy",e);
            }
            return themes;
        }

        /**
         * Returns list of years for drop-down list.
         * @return
         */
        public static Collection<LabelValueBean> getYearsBeanList(){
            String startYear=FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START);
            int year=Integer.parseInt(startYear);
            return getYearsBeanList(year);
        }

        public static Collection<LabelValueBean> getYearsBeanList(int from){
            Collection<LabelValueBean> result=new ArrayList<LabelValueBean>();
            int start=from;
            Calendar now=Calendar.getInstance();
            int end=now.get(Calendar.YEAR)+1;
            for (int i = start; i <= end; i++) {
                result.add(new LabelValueBean(String.valueOf(i),String.valueOf(i)));
            }
            return result;
        }

        @Deprecated
        public static Collection getAllThemeIndicators() throws AimException
        {
            Collection colThInd = new ArrayList();
            Collection colTh = null;
            colTh = getAllPrograms();
            for (Object value : colTh) {
                AmpTheme ampTh1 = (AmpTheme) value;
                AllThemes tempAllThemes = new AllThemes();
                tempAllThemes.setProgramId(ampTh1.getAmpThemeId());
                tempAllThemes.setProgramName(ampTh1.getName());
                Collection allInds = new ArrayList();
                for (Object o : getThemeIndicators(ampTh1.getAmpThemeId())) {
                    AmpPrgIndicator ampPrg = (AmpPrgIndicator) o;
                    AllPrgIndicators prgInd = new AllPrgIndicators();
                    prgInd.setIndicatorId(ampPrg.getIndicatorId());
                    prgInd.setName(ampPrg.getName());
                    prgInd.setCode(ampPrg.getCode());
                    prgInd.setType(ampPrg.getType());
                    prgInd.setCreationDate(ampPrg.getCreationDate());
                    prgInd.setCategory(ampPrg.getCategory());
                    prgInd.setNpIndicator(ampPrg.isNpIndicator());
                    prgInd.setSector(ampPrg.getIndSectores());
                    allInds.add(prgInd);

                }
                tempAllThemes.setAllPrgIndicators(allInds);
                colThInd.add(tempAllThemes);
            }
            return colThInd;
        }

        // New function added for the program Indicator Manager by pcsingh

        @Deprecated
        public static Collection getAllThemesIndicators() throws AimException
        {
            Collection colThInd = new ArrayList();
            Collection colTh = null;
            colTh = getAllMainPrograms();
            for (Object o : colTh) {
                AmpTheme ampTh1 = (AmpTheme) o;
                AllThemes tempAllThemes = new AllThemes();
                tempAllThemes.setProgramId(ampTh1.getAmpThemeId());
                tempAllThemes.setProgramName(ampTh1.getName());
                Collection allInds = new ArrayList();
                for (Object value : getThemeIndicators(ampTh1.getAmpThemeId())) {
                    AmpPrgIndicator ampPrg = (AmpPrgIndicator) value;
                    AllPrgIndicators prgInd = new AllPrgIndicators();
                    prgInd.setIndicatorId(ampPrg.getIndicatorId());
                    prgInd.setName(ampPrg.getName());
                    prgInd.setCode(ampPrg.getCode());
                    prgInd.setType(ampPrg.getType());
                    prgInd.setCreationDate(ampPrg.getCreationDate());
                    prgInd.setCategory(ampPrg.getCategory());
                    prgInd.setNpIndicator(ampPrg.isNpIndicator());
                    allInds.add(prgInd);
                }
                tempAllThemes.setAllPrgIndicators(allInds);
                colThInd.add(tempAllThemes);
            }
            return colThInd;
        }

        // New function added for the program Indicator Manager by pcsingh
        public static Collection getAllMainPrograms() throws AimException
        {
            Session session = null;
            Query qry = null;
            Collection colPrg = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                String queryString = " from "
                                    + AmpTheme.class.getName() + " th";
                qry = session.createQuery(queryString);
                colPrg = qry.list();
            }
            catch(Exception ex)
            {
                logger.error("Unable to get all the Themes");
                logger.debug("Exception " + ex);
                throw new AimException(ex);
            }
            Collection mainProgram = new ArrayList();;
            for (Object o : colPrg) {
                AmpTheme tmpTheme = (AmpTheme) o;
                if (tmpTheme.getParentThemeId() == null || tmpTheme.getParentThemeId().getAmpThemeId().intValue() == 0) {
                    mainProgram.add(tmpTheme);
                }
            }
            return mainProgram;
        }


    public static List<AmpTheme> getAllPrograms() {
        Session session = null;
        Query qry = null;
        List<AmpTheme> colPrg = new ArrayList<AmpTheme>();

        try {
            session = PersistenceManager.getRequestDBSession();
            session.setCacheMode(CacheMode.REFRESH);
            String queryString = " from " + AmpTheme.class.getName() + " th";
            qry = session.createQuery(queryString);
            qry.setCacheable(false);
            colPrg = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get all the Themes");
            logger.debug("Exception " + ex);
            throw new RuntimeException("Cannot get programs, ", ex);
        }

        return colPrg;
    }

    public static AmpTheme getAmpThemesAndSubThemesHierarchy(AmpTheme parent) {

        try {
                /*
                We must create new program object because if you modify the name of program
                the changes will be saved in db even though you don't save collection, strange issue....
                */
            AmpTheme parentWithNewName = new AmpTheme();
            parentWithNewName.setName(parent.getName().toUpperCase());
            parentWithNewName.setAmpThemeId(parent.getAmpThemeId());

            List<AmpTheme> dbChildrenReturnSet =
                    (List<AmpTheme>) ProgramUtil.getAllSubThemesByParentIdWihtChildren(parent.getAmpThemeId());
            parent.getChildren().addAll(dbChildrenReturnSet);

        } catch (DgException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public static String getHierarchyName(AmpTheme prog) {
        StringBuilder result = new StringBuilder();
        List<AmpTheme> progs = new ArrayList<AmpTheme>();
        AmpTheme curProg = prog;
        while (curProg.getParentThemeId() != null) {
            curProg = curProg.getParentThemeId();
            progs.add(curProg);
        }


        Collections.reverse(progs);

        for (AmpTheme p : progs) {
            result.append(p.getName()).append(" > ");
        }

        result.append(prog.getName());
        return result.toString();
    }

        public static Collection getSectorIndicator(Long themeIndicatorId)
        {
            Session session = null;
            Collection col = new ArrayList();
            try
            {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select SectInd from "
                                    + AmpIndicatorSector.class.getName()
                                    + " SectInd where (SectInd.themeIndicatorId=:themeIndicatorId)";
                Query qry = session.createQuery(queryString);
                qry.setParameter("themeIndicatorId",themeIndicatorId,LongType.INSTANCE);
                for (Object o : qry.list()) {
                    AmpIndicatorSector IndSector = (AmpIndicatorSector) o;
                    AmpIndSectors Indsectors = new AmpIndSectors();
                    Indsectors.setAmpIndicatorSectorId(IndSector.getAmpIndicatorSectorId());
                    Indsectors.setSectorId(IndSector.getSectorId());
                    //Indsectors.setThemeIndicatorId(IndSector.getThemeIndicatorId());
                    col.add(Indsectors);

                }
            }
            catch(Exception e1) {
                logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
                logger.debug("Exception : "+e1);
                e1.printStackTrace();
            }
            return col;
        }

        @Deprecated
        public static Collection getThemeIndicators(Long ampThemeId)
        {
            Session session = null;
            AmpTheme tempAmpTheme = null;
            Collection themeInd = new ArrayList();

            try
            {
                session = PersistenceManager.getRequestDBSession();
                tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
                Set themeIndSet = tempAmpTheme.getIndicators();
                for (Object o : themeIndSet) {
                    AmpThemeIndicators tempThemeInd = (AmpThemeIndicators) o;
                    AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
                    Long ampThemeIndId = tempThemeInd.getAmpThemeIndId();
                    tempPrgInd.setIndicatorId(ampThemeIndId);
                    tempPrgInd.setName(tempThemeInd.getName());
                    tempPrgInd.setCode(tempThemeInd.getCode());
                    tempPrgInd.setCreationDate(DateConversion.convertDateToLocalizedString(tempThemeInd.getCreationDate()));
                    tempPrgInd.setPrgIndicatorValues(getThemeIndicatorValues(ampThemeIndId));
                    tempPrgInd.setIndSectores(getSectorIndicator(ampThemeIndId));
                    themeInd.add(tempPrgInd);
                }
            }
            catch(Exception ex) {
                logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
            return themeInd;
        }

    public static Collection getProgramIndicators(Long programId)
            throws DgException {
        Set indicators = new HashSet();
        ArrayList programs = (ArrayList) getRelatedThemes(programId);
        if (programs != null) {
            for (AmpTheme program : (Iterable<AmpTheme>) programs) {
                Set inds = IndicatorUtil.getIndicatorThemeConnections(program);
                if (inds != null) {
                    indicators.addAll(inds);
                }
            }
        }
        return indicators;
    }

        @Deprecated
        public static Collection getThemeIndicatorValues(Long themeIndicatorId)
        {
            Session session = null;
            Collection col = new ArrayList();
            try
            {
                session = PersistenceManager.getRequestDBSession();
                String queryString = "select thIndValId from "
                                    + AmpThemeIndicatorValue.class.getName()
                                    + " thIndValId where (thIndValId.indicatorId=:indicatorId)";
                Query qry = session.createQuery(queryString);
                qry.setParameter("indicatorId",themeIndicatorId,LongType.INSTANCE);
                for (Object o : qry.list()) {
                    AmpThemeIndicatorValue tempThIndVal = (AmpThemeIndicatorValue) o;
                    AmpPrgIndicatorValue tempPrgIndVal = new AmpPrgIndicatorValue();
                    tempPrgIndVal.setIndicatorValueId(tempThIndVal.getAmpThemeIndValId());
                    tempPrgIndVal.setValueType(tempThIndVal.getValueType());
                    tempPrgIndVal.setValAmount(tempThIndVal.getValueAmount());
                    tempPrgIndVal.setCreationDate(DateConversion.convertDateToLocalizedString(tempThIndVal.getCreationDate()));
                    col.add(tempPrgIndVal);
                }
            }
            catch(Exception e1) {
                logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
                logger.debug("Exception : "+e1);
            }
            return col;
        }

        @Deprecated
        public static Collection getThemeIndicatorValuesDB(Long themeIndicatorId)
        {
                Session session = null;
                Collection col = null;
                try
                {
                        session = PersistenceManager.getRequestDBSession();
                        String queryString = "select thIndValId from "
                                                                + AmpThemeIndicatorValue.class.getName()
                                                                + " thIndValId where (thIndValId.themeIndicatorId=:themeIndicatorId)";
                        Query qry = session.createQuery(queryString);
                        qry.setParameter("themeIndicatorId",themeIndicatorId,LongType.INSTANCE);
                        col = qry.list();
                }
                catch(Exception e1) {
                        logger.error("Error in retrieving the values for indicator with ID : "+themeIndicatorId);
                        logger.debug("Exception : "+e1);
                }
                return col;
        }

        //WWWTTTFFFF!!!!????!!!
        public static Collection getAllSubThemes(Long parentThemeId)
        {
            Session session = null;
            Query qry = null;
            Collection tempCol1 = new ArrayList();
            Collection tempCol2 = new ArrayList();
            Collection tempCol3 = new ArrayList();
            Collection tempCol4 = new ArrayList();
            Collection tempCol5 = new ArrayList();
            Collection tempCol6 = new ArrayList();
            Collection tempCol7 = new ArrayList();
            Collection tempCol8 = new ArrayList();
            Collection allSubThemes = new ArrayList();
            try
            {
                session = PersistenceManager.getRequestDBSession();
                // level 1 starts
                String queryString1 = "select subT from " +AmpTheme.class.getName()
                                    + " subT where (subT.parentThemeId=:parentThemeId)";
                qry = session.createQuery(queryString1);
                qry.setParameter("parentThemeId",parentThemeId,LongType.INSTANCE);
                tempCol1 = qry.list();
                if(!tempCol1.isEmpty())
                {
                    for (Object o : tempCol1) {
                        AmpTheme ampTheme1 = (AmpTheme) o;
                        parentThemeId = ampTheme1.getAmpThemeId();
                        allSubThemes.add(ampTheme1);
                        //  level 2 starts
                        String queryString2 = "select subT from " + AmpTheme.class.getName()
                                + " subT where (subT.parentThemeId=:parentThemeId)";
                        qry = session.createQuery(queryString2);
                        qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                        tempCol2 = qry.list();
                        if (!tempCol2.isEmpty()) {
                            for (Object o4 : tempCol2) {
                                AmpTheme ampTheme2 = (AmpTheme) o4;
                                parentThemeId = ampTheme2.getAmpThemeId();
                                allSubThemes.add(ampTheme2);
                                //  level 3 starts
                                String queryString3 = "select subT from " + AmpTheme.class.getName()
                                        + " subT where (subT.parentThemeId=:parentThemeId)";
                                qry = session.createQuery(queryString3);
                                qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                tempCol3 = qry.list();
                                if (!tempCol3.isEmpty()) {
                                    for (Object o3 : tempCol3) {
                                        AmpTheme ampTheme3 = (AmpTheme) o3;
                                        parentThemeId = ampTheme3.getAmpThemeId();
                                        allSubThemes.add(ampTheme3);
                                        //  level 4 starts
                                        String queryString4 = "select subT from " + AmpTheme.class.getName()
                                                + " subT where (subT.parentThemeId=:parentThemeId)";
                                        qry = session.createQuery(queryString4);
                                        qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                        tempCol4 = qry.list();
                                        if (!tempCol4.isEmpty()) {
                                            for (Object o2 : tempCol4) {
                                                AmpTheme ampTheme4 = (AmpTheme) o2;
                                                parentThemeId = ampTheme4.getAmpThemeId();
                                                allSubThemes.add(ampTheme4);
                                                //  level 5 starts
                                                String queryString5 = "select subT from " + AmpTheme.class.getName()
                                                        + " subT where (subT.parentThemeId=:parentThemeId)";
                                                qry = session.createQuery(queryString5);
                                                qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                                tempCol5 = qry.list();
                                                if (!tempCol5.isEmpty()) {
                                                    for (Object o1 : tempCol5) {
                                                        AmpTheme ampTheme5 = (AmpTheme) o1;
                                                        parentThemeId = ampTheme5.getAmpThemeId();
                                                        allSubThemes.add(ampTheme5);
                                                        //  level 6 starts
                                                        String queryString6 = "select subT from " + AmpTheme.class.getName()
                                                                + " subT where (subT.parentThemeId=:parentThemeId)";
                                                        qry = session.createQuery(queryString6);
                                                        qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                                        tempCol6 = qry.list();
                                                        if (!tempCol6.isEmpty()) {
                                                            for (Object element : tempCol6) {
                                                                AmpTheme ampTheme6 = (AmpTheme) element;
                                                                parentThemeId = ampTheme6.getAmpThemeId();
                                                                allSubThemes.add(ampTheme6);
                                                                //  level 7 starts
                                                                String queryString7 = "select subT from " + AmpTheme.class.getName()
                                                                        + " subT where (subT.parentThemeId=:parentThemeId)";
                                                                qry = session.createQuery(queryString7);
                                                                qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                                                tempCol7 = qry.list();
                                                                if (!tempCol7.isEmpty()) {
                                                                    for (Object value : tempCol7) {
                                                                        AmpTheme ampTheme7 = (AmpTheme) value;
                                                                        parentThemeId = ampTheme7.getAmpThemeId();
                                                                        allSubThemes.add(ampTheme7);
                                                                        //  level 8 starts
                                                                        String queryString8 = "select subT from " + AmpTheme.class.getName()
                                                                                + " subT where (subT.parentThemeId=:parentThemeId)";
                                                                        qry = session.createQuery(queryString8);
                                                                        qry.setParameter("parentThemeId", parentThemeId, LongType.INSTANCE);
                                                                        tempCol8 = qry.list();
                                                                        if (!tempCol8.isEmpty()) {
                                                                            for (Object item : tempCol8) {
                                                                                AmpTheme ampTheme8 = (AmpTheme) item;
                                                                                allSubThemes.add(ampTheme8);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch(Exception e1) {
                logger.error("Unable to get all the Sub-Themes");
                logger.debug("Exception : "+e1);
            }
            return allSubThemes;
        }

        /**
         * Returns all sub themes of specified one.
         * @param parentThemeId
         * @return
         */
        public static List<AmpTheme> getSubThemes(Long parentThemeId) throws DgException {
            try {
                String queryString = "from " + AmpTheme.class.getName() +
                    " subT where subT.parentThemeId.ampThemeId=:parentThemeId";
                Query qry = PersistenceManager.getRequestDBSession().createQuery(queryString);
                qry.setParameter("parentThemeId",parentThemeId);
                List<AmpTheme> subThemes = qry.list();
                return subThemes;
            } catch(Exception e1) {
                throw new DgException("Cannot search sub themes for theme with id="+parentThemeId,e1);
            }
        }

        /**
         * Return all subchildren in the tree
         * Recursively iterates on all child programs till the end of the branch.
         * @param parentThemeId db ID of the parent program
         * @return collection of AmpTheme beans
         * @throws AimException if enything goes wrong
         */
        public static Collection getAllSubThemesFor(Long parentThemeId) throws AimException
        {
            Collection subThemes = new ArrayList();
            try
            {
                Collection progs = getSubThemes(parentThemeId);
                if (progs != null && progs.size()>0){
                    subThemes.addAll(progs);
                    for (Iterator iter = progs.iterator(); iter.hasNext();) {
                        AmpTheme child = (AmpTheme) iter.next();
                        Collection col = getAllSubThemes(child.getAmpThemeId());
                        subThemes.addAll(col);
                    }
                }
            }
            catch(Exception e1)
            {
                logger.error("Unable to get all the sub-themes");
                logger.debug("Exception : "+e1);
                throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
            }
            return subThemes;
        }


         /**
         * Returns all subchildren in the tree
         * Recursively iterates on all children programs till the end of the branch.
                 * The names of children programs will be decorated with dashes.
                 * Use the method only for better presentation purpose
         * @param parentThemeId db ID of the parent program
                 * @param depth specified the level of program in hierarchy.
                 * e.i. 0 means first level program, 1 second level, etc.
                 * We use it to add "--" to the name of program for better presentation.
         * @return collection of AmpTheme beans
         * @throws AimException if anything goes wrong
                 * @see #getAmpThemesAndSubThemes(AmpTheme parent)
                 * @see #getAllSubThemesFor(Collection parentThemes)
         */
        public static List getAllSubThemesByParentId(Long parentThemeId, int depth) throws AimException
        {
            List subThemes = new ArrayList();
            try
            {
                Collection progs = getSubThemes(parentThemeId);
                if (progs != null && progs.size()>0){
                    for (Iterator iter = progs.iterator(); iter.hasNext();) {
                        AmpTheme child = (AmpTheme) iter.next();
                                                AmpTheme childWithNewName=new AmpTheme();
                                                String name="";
                                                for(int i=0;i<depth;i++){
                                                name+="--";
                                                }
                                                childWithNewName.setName(name+"--"+child.getName());
                                                childWithNewName.setAmpThemeId(child.getAmpThemeId());
                        List col = getAllSubThemesByParentId(child.getAmpThemeId(),depth+1);
                                                Collections.reverse(col);
                                                col.add(childWithNewName);
                                                Collections.reverse(col);
                        subThemes.addAll(col);
                    }
                }
            }
            catch(Exception e1)
            {
                logger.error("Unable to get all the sub-themes");
                logger.debug("Exception : "+e1);
                throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
            }

            return subThemes;
        }

        /**
         * Returns all subchildren in the tree
         * @param parentThemeId db ID of the parent program
         * @return collection of AmpTheme beans
         * @throws AimException if anything goes wrong
                 * @see #getAmpThemesAndSubThemes(AmpTheme parent)
                 * @see #getAllSubThemesFor(Collection parentThemes)
         */
        public static Collection getAllSubThemesByParentIdWihtChildren(Long parentThemeId) throws AimException
        {
            Collection progs = new ArrayList();
            try
            {
                progs = getSubThemes(parentThemeId);
                if (progs != null && progs.size()>0){
                    for (Iterator iter = progs.iterator(); iter.hasNext();) {
                        AmpTheme child = (AmpTheme) iter.next();
                        child.getChildren().addAll(getAllSubThemesByParentIdWihtChildren(child.getAmpThemeId()));
                    }
                }
            }
            catch(Exception e1)
            {
                logger.error("Unable to get all the sub-themes");
                logger.debug("Exception : "+e1);
                throw new AimException("Cannot get sub themes for "+parentThemeId,e1);
            }

            return progs;
        }

    /**
     * Returns List of programs and sub-programs using {@link #getAllSubThemesByParentId(Long parentThemeId, int depth) } method.
     *  The names of the programs are embellished
     * (The main parent program will be in upper case and  dashes will be added to children programs)
     *  for better presentation.
     *
     * @param parent parent program
     * @return collection of AmpTheme beans
     *
     */
    public static List<AmpTheme> getAmpThemesAndSubThemes(AmpTheme parent) {
        List<AmpTheme> ret = new ArrayList<AmpTheme>();

        try {
            /*
             We must create new program object because if you modify the name of program
             the changes will be saved in db even though you don't save collection, strange issue....
            */
            AmpTheme parentWithNewName=new AmpTheme();
            parentWithNewName.setName(parent.getName().toUpperCase());
            parentWithNewName.setAmpThemeId(parent.getAmpThemeId());
            ret.add(parentWithNewName);
            List<AmpTheme> dbChildrenReturnSet =
                ProgramUtil.getAllSubThemesByParentId(parent.getAmpThemeId(),0);
      //  Collections.reverse(dbChildrenReturnSet);
        ret.addAll(dbChildrenReturnSet);

        } catch (DgException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }


    /**
     * Return all subchildren of the parent program
     * Recursively iterates on all child programs till the end of the branch using
     * {@link #getAmpThemesAndSubThemes(AmpTheme parent)} .
     * The method is used for better presentation purpose only
     *
     * @param parentThemes collection of  parent programs
     * @return collection of AmpTheme beans
     * @throws AimException if anything goes wrong
     */
    public static List getAllSubThemesFor(Collection parentThemes) throws AimException {
        List<AmpTheme> programs = new ArrayList();

        Iterator<AmpTheme> programsIter = parentThemes.iterator();
        while (programsIter.hasNext()) {
            AmpTheme program = programsIter.next();
            programs.addAll(getAmpThemesAndSubThemes(program));
        }
        return programs;

    }

        /**
         * @deprecated use {@link IndicatorUtil} methods.
         * @param allPrgInd
         * @param ampThemeId
         */
        @Deprecated
        public static void saveEditThemeIndicators(AllPrgIndicators allPrgInd, Long ampThemeId)
        {
            Session session = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                AmpTheme tempAmpTheme = null;
                tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
                AmpThemeIndicators ampThemeInd = saveEditPrgInd(allPrgInd, tempAmpTheme);
//session.flush();
                saveEditPrgIndValues(allPrgInd.getThemeIndValues(),ampThemeInd);
            }
            catch(Exception ex)
            {
                logger.error("Exception from saveEditThemeIndicators() : " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
        }

        /**
         * @deprecated use {@link IndicatorUtil} methods.
         * @param allPrgInd
         * @param tempAmpTheme
         * @return
         */
        @Deprecated
        public static AmpThemeIndicators saveEditPrgInd(AllPrgIndicators allPrgInd, AmpTheme tempAmpTheme)
        {
            Session session = null;
            Transaction tx = null;
            AmpThemeIndicators ampThemeInd = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                ampThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,allPrgInd.getIndicatorId());
                ampThemeInd.setName(allPrgInd.getName());
                ampThemeInd.setCode(allPrgInd.getCode());
                ampThemeInd.setType(allPrgInd.getType());
                ampThemeInd.setDescription(allPrgInd.getDescription());
                ampThemeInd.setCreationDate(DateConversion.getDate(allPrgInd.getCreationDate()));
                ampThemeInd.setCategory(allPrgInd.getCategory());
                ampThemeInd.setNpIndicator(allPrgInd.isNpIndicator());
                Set ampThemeSet = new HashSet();
                ampThemeSet.add(tempAmpTheme);
//beginTransaction();
                session.saveOrUpdate(ampThemeInd);
                //tx.commit();
            }
            catch(Exception ex)
            {
                logger.error("Exception from saveEditPrgInd() : " + ex.getMessage());
                ex.printStackTrace(System.out);
                if (tx != null)
                {
                    try
                    {
                        tx.rollback();
                    }
                    catch (Exception trbf)
                    {
                        logger.error("Transaction roll back failed : "+trbf.getMessage());
                        trbf.printStackTrace(System.out);
                    }
                }
            }
            return ampThemeInd;
        }

        /**
         * We do not have program indicators, but have universal {@link AmpIndicator}.
         * Use {@link IndicatorUtil} class to work with indicators.
         * @param prgIndValues
         * @param ampThemeInd
         */
        @Deprecated
        public static void saveEditPrgIndValues(Collection prgIndValues, AmpThemeIndicators ampThemeInd)
        {
            Session session = null;
            Transaction tx = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                for (Object prgIndValue : prgIndValues) {
                    AmpThemeIndicatorValue ampThIndVal = null;
                    AmpPrgIndicatorValue ampPrgIndVal = (AmpPrgIndicatorValue) prgIndValue;
                    if (ampPrgIndVal.getIndicatorValueId() == null)
                        ampThIndVal = new AmpThemeIndicatorValue();
                    else
                        ampThIndVal = (AmpThemeIndicatorValue) session.load(AmpThemeIndicatorValue.class, ampPrgIndVal.getIndicatorValueId());
                    ampThIndVal.setValueAmount(ampPrgIndVal.getValAmount());
                    ampThIndVal.setCreationDate(DateConversion.getDate(ampPrgIndVal.getCreationDate()));
                    ampThIndVal.setValueType(ampPrgIndVal.getValueType());
                    ampThIndVal.setThemeIndicatorId(ampThemeInd);
//beginTransaction();
                    session.saveOrUpdate(ampThIndVal);
                    //tx.commit();
                }
            }
            catch(Exception ex)
            {
                logger.error("Exception from saveEditPrgIndValues() : " + ex.getMessage());
                ex.printStackTrace(System.out);
                if (tx != null)
                {
                    try
                    {
                        tx.rollback();
                    }
                    catch (Exception trbf)
                    {
                        logger.error("Transaction roll back failed : "+trbf.getMessage());
                        trbf.printStackTrace(System.out);
                    }
                }
            }
        }

        /**
         * This was saving theme indicator but deprecated now.
         * @deprecated there are no Theme Indicators any more. use {@link IndicatorUtil} methods.
         * @param tempPrgInd
         * @param ampThemeId
         */
        @Deprecated
        public static void saveThemeIndicators(AmpPrgIndicator tempPrgInd,Long ampThemeId)
        {
            Session session = null;
            Transaction tx = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                AmpTheme tempAmpTheme = null;
                tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
                AmpThemeIndicators ampThemeInd=null;
                if(tempPrgInd.getIndicatorId()!=null){
                    ampThemeInd=(AmpThemeIndicators)session.load(AmpThemeIndicators.class,tempPrgInd.getIndicatorId());
                }else{
                ampThemeInd = new AmpThemeIndicators();
                }
                ampThemeInd.setName(tempPrgInd.getName());
                ampThemeInd.setCode(tempPrgInd.getCode());
                ampThemeInd.setType(tempPrgInd.getType());
                ampThemeInd.setCreationDate(DateConversion.getDate(tempPrgInd.getCreationDate()));
                ampThemeInd.setCategory(tempPrgInd.getCategory());
                ampThemeInd.setNpIndicator(tempPrgInd.isNpIndicator());
                ampThemeInd.setDescription(tempPrgInd.getDescription());
                if(ampThemeInd.getSectors()==null){
                   ampThemeInd.setSectors(new HashSet());
                }

                Long[] sectorIds = tempPrgInd.getSector();
                    Set sectors = new HashSet();
                   Collection sect=tempPrgInd.getIndSectores();
                   if(sect!=null&&sect.size()>0){
                       for (ActivitySector sector : (Iterable<ActivitySector>) sect) {
                           if (tempPrgInd.getIndicatorId() == null || !SectorUtil.getIndIcatorSector(tempPrgInd.getIndicatorId(), sector.getSectorId())) {
                               AmpIndicatorSector amps = new AmpIndicatorSector();
                               amps.setThemeIndicatorId(ampThemeInd);
                               amps.setSectorId(SectorUtil.getAmpSector(sector.getSectorId()));
                               ampThemeInd.getSectors().add(amps);
                           }
                       }
                   }

                Set ampThemeSet = new HashSet();
                ampThemeSet.add(tempAmpTheme);
                ampThemeInd.setThemes(ampThemeSet);
//beginTransaction();
                Iterator itr = ampThemeInd.getThemes().iterator();
                while(itr.hasNext()){
                AmpTheme theme = (AmpTheme)itr.next();
                    itr.remove();
                }
                session.saveOrUpdate(ampThemeInd);
                //tempAmpTheme.getIndicators().add(ampThemeInd);
                //session.saveOrUpdate(tempAmpTheme);

                if(tempPrgInd.getPrgIndicatorValues()!=null && tempPrgInd.getPrgIndicatorValues().size()!=0){
                    for (Object o : tempPrgInd.getPrgIndicatorValues()) {
                        AmpPrgIndicatorValue prgIndValue = (AmpPrgIndicatorValue) o;
                        AmpThemeIndicatorValue indValue = new AmpThemeIndicatorValue();
                        indValue.setValueType(prgIndValue.getValueType());
                        indValue.setValueAmount(prgIndValue.getValAmount());
                        indValue.setCreationDate(DateConversion.getDate(prgIndValue.getCreationDate()));
                        indValue.setThemeIndicatorId(ampThemeInd);
                        session.save(indValue);
                    }
                }
                //tx.commit();
            }
            catch(Exception ex){
                logger.error("Exception from saveThemeIndicators() : " + ex.getMessage());
                ex.printStackTrace(System.out);
                if (tx != null){
                    try{
                        tx.rollback();
                    }catch (Exception trbf){
                        logger.error("Transaction roll back failed : "+trbf.getMessage());
                        trbf.printStackTrace(System.out);
                    }
                }
            }
        }

        public static void deleteTheme(Long themeId) throws AimException, DgException
        {
            ArrayList colTheme = (ArrayList)getRelatedThemes(themeId);
            int colSize = colTheme.size();
            for(int i=colSize-1; i>=0; i--)
            {
                AmpTheme ampTh = (AmpTheme) colTheme.get(i);
                try {
                    Session sess = PersistenceManager.getSession();
                    AmpTheme tempTheme = (AmpTheme) sess.load(AmpTheme.class,ampTh.getAmpThemeId());
                    tempTheme.setDeleted(true);
                    sess.saveOrUpdate(tempTheme);
                } catch (HibernateException e) {
                    logger.error(e.getMessage(), e);
                    throw new AimException("Cannot delete theme with id "+themeId,e);
                }
            }
        }

        public static AllPrgIndicators getThemeIndicator(Long indId)
        {
            Session session = null;
            AllPrgIndicators tempPrgInd = new AllPrgIndicators();

            try{
                session = PersistenceManager.getRequestDBSession();
                AmpThemeIndicators tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
                tempPrgInd.setIndicatorId(tempInd.getAmpThemeIndId());

                tempPrgInd.setName(tempInd.getName());
                tempPrgInd.setCode(tempInd.getCode());
                tempPrgInd.setType(tempInd.getType());
                tempPrgInd.setDescription(tempInd.getDescription());
                tempPrgInd.setCreationDate(DateConversion.convertDateToLocalizedString(tempInd.getCreationDate()));
                tempPrgInd.setCategory(tempInd.getCategory());
                tempPrgInd.setNpIndicator(tempInd.isNpIndicator());
                tempPrgInd.setThemes(tempInd.getThemes());
                tempPrgInd.setSector(getSectorIndicator(indId));
//session.flush();
            }
            catch(Exception e){
                logger.error("Unable to get the specified Indicator");
                logger.debug("Exception : "+e);
            }
            return tempPrgInd;
        }

        @Deprecated
        public static AmpThemeIndicators getThemeIndicatorById(Long indId){
            Session session = null;
            AmpThemeIndicators tempInd = new AmpThemeIndicators();

            try{
                session = PersistenceManager.getRequestDBSession();
                tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
//session.flush();
            }
            catch(Exception e){
                logger.error("Unable to get the specified Indicator");
                logger.debug("Exception : "+e);
            }
            return tempInd;
        }

        @Deprecated
        public static AllPrgIndicators getThemeIndValues(Long indId)
        {
            AllPrgIndicators programInd = getThemeIndicator(indId);
            programInd.setThemeIndValues(getThemeIndicatorValues(indId));
            return programInd;
        }

        /**
         * @deprecated use {@link IndicatorUtil} mthods.
         * @param allPrgInd
         */
        @Deprecated
        public static void saveIndicator(AllPrgIndicators allPrgInd)
        {
            Session session = null;
            Transaction tx = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                AmpThemeIndicators tempThemeInd = null;
                tempThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,allPrgInd.getIndicatorId());
                tempThemeInd.setName(allPrgInd.getName());
                tempThemeInd.setCode(allPrgInd.getCode());
                tempThemeInd.setType(allPrgInd.getType());
                tempThemeInd.setCategory(allPrgInd.getCategory());
                tempThemeInd.setNpIndicator(allPrgInd.isNpIndicator());
//beginTransaction();
                session.saveOrUpdate(tempThemeInd);
                //tx.commit();
            }
            catch(Exception ex)
            {
                logger.error("Exception from saveIndicator() : " + ex.getMessage());
                ex.printStackTrace(System.out);
                if (tx != null)
                {
                    try
                    {
                        tx.rollback();
                    }
                    catch (Exception trbf)
                    {
                        logger.error("Transaction roll back failed : "+trbf.getMessage());
                        trbf.printStackTrace(System.out);
                    }
                }
            }
        }

        /**
         * @deprecated use {@link IndicatorUtil}
         * @param indId
         */
        @Deprecated
        public static void deleteProgramIndicator(Long indId){
            Session session = null;
            Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            AmpIndicator tempThemeInd = (AmpIndicator) session.load(AmpIndicator.class,indId);
//          tempThemeInd.getThemes().remove(tempThemeInd);
            session.delete(tempThemeInd);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Unable to delete the themes");
            logger.debug("Exception : "+e);
        }
    }

        public static void updateTheme(AmpTheme editeTheme)
        {
            Session session = null;
            Transaction tx = null;
            try
            {
                session = PersistenceManager.getRequestDBSession();
                AmpTheme tempAmpTheme = null;
                tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,editeTheme.getAmpThemeId());
                tempAmpTheme.setName(editeTheme.getName());
                tempAmpTheme.setThemeCode(editeTheme.getThemeCode());
                tempAmpTheme.setBudgetProgramCode(editeTheme.getBudgetProgramCode());
                tempAmpTheme.setDescription(editeTheme.getDescription());
                tempAmpTheme.setTypeCategoryValue( editeTheme.getTypeCategoryValue());

                tempAmpTheme.setLeadAgency( editeTheme.getLeadAgency() );
                tempAmpTheme.setTargetGroups( editeTheme.getTargetGroups() );
                tempAmpTheme.setBackground( editeTheme.getBackground() );
                tempAmpTheme.setObjectives( editeTheme.getObjectives() );
                tempAmpTheme.setOutputs( editeTheme.getOutputs() );
                tempAmpTheme.setBeneficiaries( editeTheme.getBeneficiaries() );
                tempAmpTheme.setEnvironmentConsiderations( editeTheme.getEnvironmentConsiderations() );

                tempAmpTheme.setExternalFinancing(editeTheme.getExternalFinancing());
                tempAmpTheme.setInternalFinancing(editeTheme.getInternalFinancing());
                tempAmpTheme.setTotalFinancing(editeTheme.getTotalFinancing());
                tempAmpTheme.setShowInRMFilters(editeTheme.getShowInRMFilters());

//beginTransaction();
                session.update(tempAmpTheme);
                //tx.commit();
            }
            catch(Exception ex)
            {
                logger.error("Exception from saveIndicator() : " + ex.getMessage());
                ex.printStackTrace(System.out);
                if (tx != null)
                {
                    try
                    {
                        tx.rollback();
                    }
                    catch (Exception trbf)
                    {
                        logger.error("Transaction roll back failed : "+trbf.getMessage());
                        trbf.printStackTrace(System.out);
                    }
                }
            }
        }


        public static Collection getRelatedThemes(Long id) throws DgException
        {
            AmpTheme ampThemetemp = new AmpTheme();
            ampThemetemp = getThemeById(id);
            Collection<AmpTheme> themeCol = getSubThemes(id);
            Collection tempPrg = new ArrayList();
            tempPrg.add(ampThemetemp);
            if(!themeCol.isEmpty())
            {
                AmpTheme tempTheme = new AmpTheme();
                for (AmpTheme ampTheme : themeCol) {
                    tempTheme = ampTheme;
                    tempPrg.addAll(getRelatedThemes(tempTheme.getAmpThemeId()));
                }
            }
            return tempPrg;
        }


        public static String getThemesHierarchyXML(Collection<AmpTheme> allAmpThemes) throws Exception {
            StringBuilder result = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            result.append("<progTree>\n");
            if (allAmpThemes != null && allAmpThemes.size() > 0) {

                //make hieararchy of programs wrapped into TreeItem
                Collection themeTree = CollectionUtils.getHierarchy(allAmpThemes,
                        new ProgramHierarchyDefinition(), new XMLtreeItemFactory());

                //get XML from each top level item. They will handle subitems.
                for (Object o : themeTree) {
                    TreeItem item = (TreeItem) o;
                    result.append(item.getXml());
                }
            }
            result.append("</progTree>\n");
            return result.toString();
        }
        public static AmpActivityProgramSettings getAmpActivityProgramSettings(Long id) throws DgException {
            return (AmpActivityProgramSettings) PersistenceManager.getRequestDBSession().load(AmpActivityProgramSettings.class, id);
        }

        public static AmpActivityProgramSettings getAmpActivityProgramSettings(String name) throws DgException {
            Session session = null;
            AmpActivityProgramSettings programSettings=null;

            try {
                    session = PersistenceManager.getRequestDBSession();
                    String queryString = "select ap from "
                                    + AmpActivityProgramSettings.class.getName()+ " ap "
                                    + "where ap.name=:name";
                    Query qry = session.createQuery(queryString);
                    qry.setParameter("name", name,StringType.INSTANCE);
                    qry.setCacheable(false);
                programSettings = (AmpActivityProgramSettings) qry.uniqueResult();


            } catch (Exception ex) {
                    logger.debug("Unable to search " + ex);
                    throw new DgException(ex);
                    }
            return programSettings;
    }

      public static List<AmpActivityProgramSettings> getAmpActivityProgramSettingsList(boolean excludeIndirect) {
          String queryString = "select ap from " + AmpActivityProgramSettings.class.getName() + " ap";
          if (excludeIndirect) {
              queryString += " where ap.name <> :indirectName";
          }
          Query qry = PersistenceManager.getSession().createQuery(queryString);
          if (excludeIndirect) {
              qry.setParameter("indirectName", INDIRECT_PRIMARY_PROGRAM, StringType.INSTANCE);
          }

          List<AmpActivityProgramSettings> programSettings = qry.list();
          if (programSettings.isEmpty()) {
              programSettings = createDefaultAmpActivityProgramSettingsList();
          }
          return programSettings;
    }

    /**
     * Returns a list of all enabled program settings. If no program settings are enabled, a default list is created.
     * @return a list of all enabled program settings
     */
    public static List<AmpActivityProgramSettings> getEnabledProgramSettings() {
        List<AmpActivityProgramSettings> programSettings = getAmpActivityProgramSettingsList(true);
        if (programSettings.isEmpty()) {
            programSettings = createDefaultAmpActivityProgramSettingsList();
        }

        Iterator<AmpActivityProgramSettings> iterator = programSettings.iterator();

        while(iterator.hasNext()){
            AmpActivityProgramSettings programSetting = iterator.next();
            if(programSetting.getDefaultHierarchy() == null) {
                iterator.remove();
            }
        }

        return programSettings;
    }

    public static List createDefaultAmpActivityProgramSettingsList() {
        Session session = PersistenceManager.getSession();

        AmpActivityProgramSettings settingNPO=new AmpActivityProgramSettings("National Plan Objective");
        AmpActivityProgramSettings settingPP=new AmpActivityProgramSettings("Primary Program");
        AmpActivityProgramSettings settingSP=new AmpActivityProgramSettings("Secondary Program");
        session.save(settingNPO);
        session.save(settingPP);
        session.save(settingSP);

        List<AmpActivityProgramSettings> programSettings = new ArrayList<>();
        programSettings.add(settingNPO);
        programSettings.add(settingPP);
        programSettings.add(settingSP);
        return programSettings;
}

    public static String printHierarchyNames(AmpTheme child) {
        return printHierarchyNames(child, false);
    }

    public static String printHierarchyNames(AmpTheme child, boolean insertNewLine) {
        Session session = null;
        Transaction tx = null;
        String names = "";
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.refresh(child);

            AmpTheme parent = child.getParentThemeId();
            if (parent == null) {
                    return names;
            }
            else {
                    names = printHierarchyNames(parent, insertNewLine);
                    names += "[" + parent.getName() + "] ";
                    if (insertNewLine)
                        names +="<br/>";
            }
            //tx.rollback();
        } catch (Exception ex) {
            logger.error("Unable to get Hierarchy: " + ex);
            ex.printStackTrace();
        }
        return names;
    }

    public static void saveAmpActivityProgramSettings(List settings) throws
        DgException {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getSession();
            if (settings != null) {
                for (Object o : settings) {
                    AmpActivityProgramSettings setting = (AmpActivityProgramSettings) o;
                    if (setting.getDefaultHierarchy() != null
                            && setting.getDefaultHierarchy().getAmpThemeId() != null) {
                        AmpActivityProgramSettings oldSetting = (AmpActivityProgramSettings) session.
                                get(AmpActivityProgramSettings.class, setting.getAmpProgramSettingsId());
                        oldSetting.setAllowMultiple(setting.isAllowMultiple());
                        if (setting.getDefaultHierarchy().getAmpThemeId() != -1) {
                            oldSetting.setDefaultHierarchy(setting.getDefaultHierarchy());
                        } else {
                            oldSetting.setDefaultHierarchy(null);
                        }

                        if (setting.getStartDate() != null) {
                            oldSetting.setStartDate(setting.getStartDate());
                        } else {
                            oldSetting.setStartDate(null);
                        }

                        if (setting.getEndDate() != null) {
                            oldSetting.setEndDate(setting.getEndDate());
                        } else {
                            oldSetting.setEndDate(null);
                        }

                        session.update(oldSetting);
                        session.flush();
                    }

                }
            }

        } catch (Exception ex) {
            logger.error("Unable to save program Setting  " + ex);
            throw new DgException(ex);
        }

    }



    public static String renderLevel(Collection themes, int level, HttpServletRequest request) {
         //requirements for translation purposes
         TranslatorWorker translator = TranslatorWorker.getInstance();
         String translatedText = TranslatorWorker.translateText("No Programs present");
        if (themes == null || themes.size() == 0) {
            return "<center><b>" + translatedText + "</b></<center>";
        }
         StringBuilder retVal;
        retVal = new StringBuilder("<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\" valign=\"top\" align=\"left\" bgcolor=\"#ffffff\" border=\"0\" style=\"border-collapse: collapse;\">\n");
        int rc = 0;
        for (Object o : themes) {
            TreeItem item = (TreeItem) o;
            AmpTheme theme = (AmpTheme) item.getMember();
            retVal.append("<tr><td>&nbsp;</td><td width=\"100%\">\n");


            // visible div start
            retVal.append("<div>");// id=\"div_theme_"+theme.getAmpThemeId()+"\"";
            retVal.append(" <table class=\"inside\" width=\"100%\" border=\"0\" style=\"margin-bottom:1px\">");
            if (rc++ % 2 == 0) {
                retVal.append("<tr bgcolor=\"#F2F2F2\" class=\"tableEven\" onmouseover=\"this.className='Hovered'\" onmouseout=\"this.className='tableEven'\">");
            } else {
                retVal.append("<tr bgcolor=\"#F2F2F2\" class=\"tableOdd\" onmouseover=\"this.className='Hovered'\" onmouseout=\"this.className='tableOdd'\">");
            }
            retVal.append("   <td class=\"inside\" width=\"1%\" >");
            retVal.append("     <img id=\"img_").append(theme.getAmpThemeId()).append("\" onclick=\"expandProgram(").append(theme.getAmpThemeId()).append(")\" src=\"/TEMPLATE/ampTemplate/images/tree_plus.gif\"/>\n");
            retVal.append("     <img id=\"imgh_").append(theme.getAmpThemeId()).append("\" onclick=\"collapseProgram(").append(theme.getAmpThemeId()).append(")\" src=\"/TEMPLATE/ampTemplate/images/tree_minus.gif\"  style=\"display : none;\"/>\n");
            retVal.append("   </td>");
            if (level > 1) {
                retVal.append("   <td class=\"inside\" width=\"1%\">");
                retVal.append("     <img src=\"/TEMPLATE/ampTemplate/images/link_out_bot.gif\"/>\n");
                retVal.append("   </td>");
                retVal.append("   <td class=\"inside\" width=\"1%\">");
                retVal.append("     <img src=\"").append(getLevelImage(level)).append("\" />\n");
                retVal.append("   </td>");
            }
            retVal.append("   <td  class=\"progName inside\">");
            retVal.append("    <a href=\"javascript:editProgram(").append(theme.getAmpThemeId()).append(")\" style=\"font-weight:bold;\">").append(DbUtil.filter(((AmpTheme) item.getMember()).getName())).append("</a>\n");
            retVal.append("   </td>");
            retVal.append("   <td class=\"progCode inside\"  width=\"45%\" nowrap=\"nowrap\">(").append(DbUtil.filter(((AmpTheme) item.getMember()).getThemeCode())).append(")</td>");
            retVal.append("   <td class=\"inside\" nowrap=\"nowrap\" width=\"10%\">");
            retVal.append("     <a href=\"javascript:addSubProgram('5','").append(theme.getAmpThemeId()).append("','").append(level).append("','").append(DbUtil.filter(theme.getEncodeName())).append("')\">").append(TranslatorWorker.translateText("Add Sub Program")).append("</a> \n");
            retVal.append("   </td>");
            retVal.append("   <td class=\"inside\" nowrap=\"nowrap\" width=\"10%\">");
            retVal.append("     <a href=\"javascript:assignIndicators('").append(theme.getAmpThemeId()).append("')\">").append(TranslatorWorker.translateText("Manage Indicators")).append("</a>\n");
            retVal.append("   </td>");
            retVal.append("   <td class=\"inside\" width=\"12\">");
            retVal.append("     <a href=\"/aim/themeManager.do~event=delete~themeId=").append(theme.getAmpThemeId()).append("\" onclick=\"return deleteProgram()\"><img src=\"/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif\" border=\"0\"></a>");
            retVal.append("   </td>");
            retVal.append(" </tr></table>");
            retVal.append("</div>\n");

            // hidden div start
            retVal.append("<div id=\"div_theme_").append(theme.getAmpThemeId()).append("\" style=\"display : none;\">\n");
            if (item.getChildren() != null || item.getChildren().size() > 0) {
                retVal.append(renderLevel(item.getChildren(), level + 1, request));
            }
            retVal.append("</div>\n");

            retVal.append("</td></tr>\n");
        }
        retVal.append("</table>\n");
        return retVal.toString();
    }

    public static String getLevelImage(int level) {
        switch (level) {
        case 0: case 1:
            return "../ampTemplate/images/arrow_right.gif";
        case 2:
            return "../ampTemplate/images/square1.gif";
        case 3:
            return "../ampTemplate/images/square2.gif";
        case 4:
            return "../ampTemplate/images/square3.gif";
        case 5:
            return "../ampTemplate/images/square4.gif";
        case 6:
            return "../ampTemplate/images/square5.gif";
        case 7:
            return "../ampTemplate/images/square6.gif";
        case 8:
            return "../ampTemplate/images/square7.gif";
        }
        return "../ampTemplate/images/arrow_right.gif";
    }


    /**
     * Fetches the names of last version of activities that have a specific program
     * @param programId the program id to be selected
     * @return
     */
    public static List<String> getActivityNamesUsingProgram( Long programId ) {
        return PersistenceManager.getSession().doReturningWork(conn -> {
            String query = "SELECT distinct(aa.name) "
                    + "FROM amp_activity aa "
                    + "JOIN amp_activity_program aap "
                    + "ON aap.amp_activity_id = aa.amp_activity_id "
                    + "WHERE aap.amp_program_id = " + programId;
            return SQLUtils.fetchAsList(conn, query, 1);
        });
    }

    public static String getNameOfProgramSettingsUsed(Long programId) {
        Collection programSettings   = getProgramSetttingsUsed(programId);

        StringBuilder result   = new StringBuilder();
        for (Object programSetting : programSettings) {
            AmpActivityProgramSettings aaps = (AmpActivityProgramSettings) programSetting;
            if (aaps.getName() != null)
                result.append("'").append(aaps.getName()).append("'").append(", ");
        }
        if ( result.length() > 0 )
            return result.substring(0, result.length() - 2);
        else
            return null;
    }

    public static Collection getProgramSetttingsUsed(Long programId) {
        Session sess                        = null;
        try {
            sess = PersistenceManager.getRequestDBSession();
            String qryString        = "select a from " + AmpActivityProgramSettings.class.getName() + " a where (a.defaultHierarchy=:program) ";
            Query qry           = sess.createQuery(qryString);
            qry.setParameter("program", programId, LongType.INSTANCE);
            Collection result   = qry.list();
            return result;
        }
        catch (Exception e) {
            // TODO: handle exception
             e.printStackTrace();
             return null;
        }
    }


    public static HashMap<Long, AmpTheme> prepareStructure(Collection<AmpTheme> col) {
        HashMap<Long, AmpTheme> ret = new HashMap<Long, AmpTheme>();
        if (col != null && col.size() > 0) {
            for (AmpTheme prog : col) {
                AmpTheme parent = prog.getParentThemeId();
                if (parent != null)
                    parent.getChildren().add(prog);
                ret.put(prog.getAmpThemeId(), prog);
            }
        }

        return ret;
    }

    /**
     * @param userSelection     collection of AmpTheme objects corresponding to the filters selected by the user
     * @param activityFilterCol this collection will contain the selected objects and their descendants (this
     *                          collection will be used to filter the activities)
     * @param columnDataCol     this collection will contain the selected objects, their descendants and their
     *                          ancestors (this collection will be used to filter out column data).
     *                          One needs the information about ancestors in multi-level hierarchy reports otherwise
     *                          the report engine won't know to which higher level hierarchy an activity belongs to.
     * @throws DgException
     */
    public static void collectFilteringInformation(Collection<AmpTheme> userSelection, Collection<AmpTheme> activityFilterCol, Set<AmpTheme> columnDataCol)
    {
        try
        {
            for (AmpTheme program : userSelection) {
                Collection<AmpTheme> descendentPrograms = ProgramUtil.getRelatedThemes(program.getAmpThemeId());
                activityFilterCol.addAll(descendentPrograms);
                columnDataCol.addAll(descendentPrograms);
                columnDataCol.addAll(ProgramUtil.getAncestorThemes(program));
            }
        }
        catch(DgException e)
        {
            throw new RuntimeException(e);
        }
    }
        /**
         * Hierarchy member factory.
         * Used to create XML enabled members.
         */
        public static class XMLtreeItemFactory implements HierarchyMemberFactory{
            public HierarchyMember createHierarchyMember(){
                TreeItem item=new TreeItem();
                item.setChildren(new ArrayList());
                return item;
            }
        }

        public static class ProgramHierarchyDefinition implements
                HierarchyDefinition {
            public Object getObjectIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                return i.getAmpThemeId();

            }

            public Object getParentIdentity(Object object) {
                AmpTheme i = (AmpTheme) object;
                if (i.getParentThemeId() == null) {
                    return null;
                }
                else {
                    return i.getParentThemeId().getAmpThemeId();
                }
            }
        }

        public static class HierarchicalProgramComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                AmpTheme i1 = (AmpTheme) o1;
                AmpTheme i2 = (AmpTheme) o2;

                Long sk1 = i1.getAmpThemeId();
                Long sk2 = i2.getAmpThemeId();

                return sk1.compareTo(sk2);
            }

        }

        /**
         * recursively get all ancestors (parents) of a set of AmpCategoryValueLocations, by a wave algorithm
         * @param inIds
         * @return
         */
        public static Set<Long> getRecursiveAscendantsOfPrograms(Collection<Long> inIds)
        {
            return AlgoUtils.runWave(inIds,
                    new DatabaseWaver("SELECT DISTINCT(parent_theme_id) FROM amp_theme WHERE (parent_theme_id "
                            + "IS NOT NULL) AND (amp_theme_id IN ($))"));
        }

        /**
         * recursively get all children of a set of AmpCategoryValueLocations, by a wave algorithm
         * @param inIds
         * @return
         */
        public static Set<Long> getRecursiveChildrenOfPrograms(Collection<Long> inIds)
        {
            return AlgoUtils.runWave(inIds,
                    new DatabaseWaver("SELECT DISTINCT amp_theme_id FROM amp_theme WHERE parent_theme_id IN ($)"));
        }


         /**
         * Used to sort indicators by name
         */
        public static class IndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AmpThemeIndicators indic1 = (AmpThemeIndicators) obj1;
                AmpThemeIndicators indic2 = (AmpThemeIndicators) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }

        }

        public static class HelperAmpThemeNameComparator implements Comparator {
            public int compare(Object obj1, Object obj2) {
                AmpTheme theme1 = (AmpTheme) obj1;
                AmpTheme theme2 = (AmpTheme) obj2;
                return theme1.getName().compareTo(theme2.getName());
            }
        }

        public static class HelperAmpIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AmpPrgIndicator indic1 = (AmpPrgIndicator) obj1;
                AmpPrgIndicator indic2 = (AmpPrgIndicator) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }

        }

        public static class HelperAllPrgIndicatorNameComparator implements Comparator {

            public int compare(Object obj1, Object obj2) {
                AllPrgIndicators indic1 = (AllPrgIndicators) obj1;
                AllPrgIndicators indic2 = (AllPrgIndicators) obj2;
                return indic1.getName().compareTo(indic2.getName());
            }
        }

        public static class HelperAllMEIndicatorNameComparator implements Comparator {
           public int compare(Object obj1, Object obj2) {
               AllMEIndicators  indic1 = (AllMEIndicators) obj1;
               AllMEIndicators indic2 = (AllMEIndicators) obj2;
               return indic1.getName().compareTo(indic2.getName());
           }
        }
        public static class HelperAllIndicatorBeanNameComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getName().toLowerCase().compareTo(indic2.getName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanNameDescendingComparator implements Comparator<IndicatorsBean> {
            public int compare(IndicatorsBean obj1, IndicatorsBean obj2) {
                return -obj1.getName().toLowerCase().compareTo(obj2.getName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanSectorComparator implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getSectorName().toLowerCase().compareTo(indic2.getSectorName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanSectorDescendingComparator implements Comparator<IndicatorsBean> {
            public int compare(IndicatorsBean obj1, IndicatorsBean obj2) {
                return -obj1.getSectorName().toLowerCase().compareTo(obj2.getSectorName().toLowerCase());
            }
        }

        public static class HelperAllIndicatorBeanTypeComparator
            implements Comparator {
            public int compare(Object obj1, Object obj2) {
                IndicatorsBean indic1 = (IndicatorsBean) obj1;
                IndicatorsBean indic2 = (IndicatorsBean) obj2;
                return indic1.getType().compareTo(indic2.getType());

            }
        }

        public static class ThemeIdComparator implements Comparator<AmpTheme>{

            public int compare(AmpTheme thm1, AmpTheme thm2) {
                return thm1.getAmpThemeId().compareTo(thm2.getAmpThemeId());
            }

        }

        public static List<String> getAllProgramColumnNames() {
            List<String> cNames = new ArrayList<String>();
            for (String base : PROGRAM_NAMES) {
                for (int i = 1; i < 9; i++)
                    cNames.add(String.format("%s Level %d", base, i));
            }
            return cNames;
        }
    public static AmpTheme getTopLevelProgram(AmpTheme program) {
        if (program.getParentThemeId() != null && program.getIndlevel() > 1) {
            program = getTopLevelProgram(program.getParentThemeId());
        }
        return program;
    }

    public static boolean isSourceMappedProgram(final AmpActivityProgramSettings setting) {
        return isSettingMappedProgram(setting, MAPPING_SOURCE_PROGRAM);
    }

    public static boolean isDestinationMappedProgram(final AmpActivityProgramSettings setting) {
        return isSettingMappedProgram(setting, MAPPING_DESTINATION_PROGRAM);
    }

    private static boolean isSettingMappedProgram(final AmpActivityProgramSettings setting,
                                                  final String mappedProgramGS) {
        if (setting != null) {
            String dstProgram = FeaturesUtil.getGlobalSettingValue(mappedProgramGS);
            if (StringUtils.isNotBlank(dstProgram)) {
                return setting.getDefaultHierarchy() != null
                        && setting.getDefaultHierarchy().getAmpThemeId().equals(Long.valueOf(dstProgram));
            }
        }

        return false;
    }

    public static Map<AmpTheme, Set<AmpTheme>> loadProgramMappings() {

        Session session = PersistenceManager.getRequestDBSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<AmpThemeMapping> criteriaQuery = criteriaBuilder.createQuery(AmpThemeMapping.class);
        Root<AmpThemeMapping> root = criteriaQuery.from(AmpThemeMapping.class);

        criteriaQuery.select(root);
        Query<AmpThemeMapping> query = session.createQuery(criteriaQuery);
        query.setCacheable(false);

        List<AmpThemeMapping> list = query.list();

        TreeMap<AmpTheme, Set<AmpTheme>> mappedPrograms = list.stream().collect(groupingBy(
                AmpThemeMapping::getSrcTheme,
                () -> new TreeMap<>(Comparator.comparing(AmpTheme::getAmpThemeId)),
                mapping(AmpThemeMapping::getDstTheme, toCollection(ProgramUtil::newSetComparingById))));

        includeAncestors(mappedPrograms);

        return mappedPrograms;
    }

    private static void includeAncestors(Map<AmpTheme, Set<AmpTheme>> mapping) {
        Set<AmpTheme> queued = newSetComparingById();
        Deque<AmpTheme> queue = new ArrayDeque<>(mapping.keySet());

        while (!queue.isEmpty()) {
            AmpTheme srcProgram = queue.removeFirst();
            AmpTheme srcParentProgram = srcProgram.getParentThemeId();

            if (srcParentProgram != null) {
                Set<AmpTheme> dstParentPrograms = mapping.computeIfAbsent(srcParentProgram, p -> newSetComparingById());

                mapping.getOrDefault(srcProgram, emptySet()).stream()
                        .map(AmpTheme::getParentThemeId)
                        .forEach(dstParentPrograms::add);

                if (!queued.contains(srcParentProgram)) {
                    queued.add(srcParentProgram);
                    queue.add(srcParentProgram);
                }
            }
        }
    }

    private static Set<AmpTheme> newSetComparingById() {
        return new TreeSet<>(Comparator.comparing(AmpTheme::getAmpThemeId));
    }

    public static Set<AmpTheme> getProgramsIncludingAncestors(Set<AmpTheme> programs) {
        Set<AmpTheme> dstThemes = new HashSet<>();
        for (AmpTheme p : programs) {
            dstThemes.add(p);
            AmpTheme parent = p.getParentThemeId();
            while (parent != null) {
                dstThemes.add(parent);
                parent = parent.getParentThemeId();
            }
        }

        return dstThemes;
    }

    public static Integer getMaxDepth(AmpTheme program, Integer currentLevel) {
        if (currentLevel == null || program.getIndlevel() > currentLevel) {
            currentLevel = program.getIndlevel();
        }
        if (currentLevel == MAX_LEVELS) {
            // TODO: to allow more levels we need to refactor backend and frontend.
            return currentLevel;
        }
        if (program.getSiblings() != null) {
            for (AmpTheme child : program.getSiblings()) {
                currentLevel = getMaxDepth(child, currentLevel);
            }
        }
        return currentLevel;
    }

    /**
     * Returns the default hierarchy programs for the themes
     * @return List<AmpTheme>
     */
    public static List<AmpTheme> getDefaultHierarchyPrograms () {
        AmpTheme indirectProgram = NDDService.getDstIndirectProgramRoot();

        List<AmpTheme> defaultHierarchyPrograms = new ArrayList<>();

        try {
            defaultHierarchyPrograms = getAllThemes();
        } catch (DgException e) {
            logger.error("Error while getting all themes", e);
        }

        if (indirectProgram != null) {
            defaultHierarchyPrograms.remove(indirectProgram);
        }

        return defaultHierarchyPrograms;
    }

    public static AmpActivityProgramSettings getProgramSettingFromTheme(AmpTheme theme) {
        AmpActivityProgramSettings setting = null;
        List<AmpActivityProgramSettings> settings = getAmpActivityProgramSettingsList(false);


        while (theme.getIndlevel() != 1) {
            theme = theme.getParentThemeId();
        }

        for (AmpActivityProgramSettings s : settings) {
            if (s.getDefaultHierarchy() != null && s.getDefaultHierarchy().getAmpThemeId().equals(theme.getRootTheme().getAmpThemeId())) {
                setting = s;
                break;
            }
        }




        return setting;
    }
}

/*
 * @Author Priyajith C
 */
package org.digijava.module.aim.startup;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.apache.jackrabbit.util.TransientFileFactory;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.PledgesToActivitiesBridge;
import org.dgfoundation.amp.ar.dimension.ARDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedViewsRepository;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.importers.GazeteerCSVImporter;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.swagger.SwaggerConfigurer;
import org.digijava.kernel.content.ContentRepositoryManager;
import org.digijava.kernel.job.cachedtables.PublicViewColumnsUtil;
import org.digijava.kernel.lucene.LuceneModules;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettings;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.OrganisationUtil;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

import javax.management.MBeanServer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class AMPStartupListener extends HttpServlet implements
        ServletContextListener {

    /**
     * 
     */
    private static final long serialVersionUID = 5724776790911414323L;

    private static Logger logger = Logger.getLogger(AMPStartupListener.class);
    
    /**
     * READ ONLY, the result of calling ServletContext.getRealPath("/")
     */
    public static String SERVLET_CONTEXT_ROOT_REAL_PATH = null;
        
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //destroy jackrabbit
        try {
            DocumentManagerUtil.shutdownRepository(sce.getServletContext() );
            TransientFileFactory.shutdown();
        } catch(Throwable t) {
            t.printStackTrace();
        }
        
        try {
            PersistenceManager.closeUnclosedSessionsFromTraceMap();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        logger.info("The AMP ServletContext has been terminated.");
    }

    protected void initNiReports() throws AMPException {
        AmpReportsSchema.init();
    }

    public void contextInitialized(ServletContextEvent sce) {
        PersistenceManager.inTransaction(() -> contextInitializedInternal(sce));
    }
    
    public void contextInitializedInternal(ServletContextEvent sce) {
        logger.debug("I am running with a new code!!!!");
        
        
        ServletContext ampContext = null;

        try {
            ampContext = sce.getServletContext();
            SERVLET_CONTEXT_ROOT_REAL_PATH = ampContext.getRealPath("/");
            ampContext.setAttribute(Constants.ME_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.AA_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.PI_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.CL_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.DC_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.SC_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.MS_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.LB_FEATURE, Boolean.TRUE);
            ampContext.setAttribute(Constants.SA_FEATURE, Boolean.TRUE);

            if (FeaturesUtil.getDefaultFlag() != null)
                ampContext.setAttribute(Constants.DEF_FLAG_EXIST, Boolean.TRUE);


                AmpReportsSchema.getInstance().maintainDescriptions();



            AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
            // get the default amp template
            AmpTemplatesVisibility currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            FeaturesUtil.setAmpTreeVisibility(ampContext, null, ampTreeVisibility);

            ampContext.setAttribute("FMcache", "read");
            new RecreateFMEntries().doIt(ampContext);

            // currentTemplate=FeaturesUtil.getTemplateVisibility(FeaturesUtil.getGlobalSettingValueLong("Visibility
            // Template"),session);
            // ampTreeVisibilityAux.buildAmpTreeVisibilityMultiLevel(currentTemplate);
            // ampTreeVisibilityAux2.displayVisibilityTreeForDebug(ampTreeVisibilityAux);

            Collection ampColumns = FeaturesUtil.getAMPColumnsOrder();
            ampContext.setAttribute("ampColumnsOrder", ampColumns);

            GlobalSettings globalSettings = GlobalSettings.getInstance();
            globalSettings.setShowComponentFundingByYear(FeaturesUtil.isShowComponentFundingByYear());
            FeaturesUtil.switchLogicInstance();

            ampContext.setAttribute(Constants.GLOBAL_SETTINGS, globalSettings);
            
            maintainMondrianCaches();

            // Lucene indexation
            LuceneUtil.checkActivityIndex(sce.getServletContext());
            LuceneUtil.checkPledgeIndex(sce.getServletContext());
            
            //LuceneUtil.createHelp(sce.getServletContext());
            //ampContext.setAttribute(Constants.LUCENE_INDEX, idx); //deprecated
            LuceneWorker.init(sce.getServletContext(),new LuceneModules());

            PermissionUtil.getAvailableGates(ampContext);

            // initialize permissible simple name singleton
            GatePermConst.availablePermissiblesBySimpleNames = new Hashtable<>();
            for (int i = 0; i < GatePermConst.availablePermissibles.length; i++) {
                GatePermConst.availablePermissiblesBySimpleNames.put(
                        GatePermConst.availablePermissibles[i].getSimpleName(),
                        GatePermConst.availablePermissibles[i]);
            }

            performSanityOperationsOnSchema(ampContext);

//          logger.info("Checking if any MTEF columns need to be created...");
//          DynamicColumnsUtil.createInexistentMtefColumns(ampContext);
            
            logger.info("loading the i18ned views description and checking for consistency with the database...");
            for(String viewName:InternationalizedViewsRepository.i18Models.keySet())
            {
                String s = InternationalizedViewsRepository.i18Models.get(viewName).toString();
                //logger.info("loaded " + s);
            }
            
            logger.info("loading the activity->pledge view twins configuration and checking for consistency...");
            int a = PledgesToActivitiesBridge.ACTIVITY_VIEW_TO_PLEDGE_VIEW.size();

            logger.info("loading the column ancestorship relationships and checking for consistency with the database...");
            logger.info("loaded relationships for " + ARDimension.columnAncestors.size() + " columns"); // DO NOT DELETE THIS LINE! it has the sideeffect of checking database for consistency (else it will crash anyway at the first run report)
            
            runCacheRefreshingQuery("update_location_level_caches_internal", "location");
            runCacheRefreshingQuery("update_program_level_caches_internal", "program");
            runCacheRefreshingQuery("update_sector_level_caches_internal", "sector");
            runCacheRefreshingQuery("update_organisation_caches_internal", "organisation");
            ContentRepositoryManager.initialize();
            checkDatabaseSanity();
            initNiReports();
            importGazeteer();
            registerEhCacheMBeans();
            initAPI();
//            testWebClient();
//            runQuery();

            new SwaggerConfigurer().configure();

            DgEmailManager.triggerStaticInitializers();
        } catch (Throwable e) {
            logger.error("Exception while initialising AMP :" + e.getMessage(), e);
            throw new Error(e);
        }
    }
    public void testWebClient()
    {
        logger.info("Making webclient request");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        WebClient webClient = context.getBean("webClient", WebClient.class);

        // Use the WebClient to make HTTP requests
        webClient.get()
                .uri("https://api.example.com/data")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> System.out.println("Response: " + response));

    }

    public static void runQuery()
    {
        logger.info("Creating trubudget relations");
        Session session = PersistenceManager.openNewSession();

        Transaction transaction = session.beginTransaction();

        // Using Hibernate's native SQL execution
        session.doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                String intentSeq="CREATE SEQUENCE IF NOT EXISTS trubudget_intent_seq;";
//                String groupSeq="CREATE SEQUENCE IF NOT EXISTS trubudget_intent_group_seq;";
                statement.executeUpdate(intentSeq);
//                statement.executeUpdate(groupSeq);
//                String createIntentGroupSql ="CREATE TABLE IF NOT EXISTS trubudget_intent_group ("
//                        +"trubudget_intent_group_id BIGINT DEFAULT nextval('trubudget_intent_group_seq') PRIMARY KEY,"
//                        +"trubudget_intent_group_name VARCHAR(255)"
//                        +");";
//                statement.executeUpdate(createIntentGroupSql);
                String createIntentSql = "CREATE TABLE IF NOT EXISTS trubudget_intent ("
                        + "trubudget_intent_id BIGINT DEFAULT nextval('trubudget_intent_seq') PRIMARY KEY,"
                        + "trubudget_intent_name VARCHAR(255) UNIQUE,"
                        + "trubudget_intent_display_name VARCHAR(255)"
                        + ")";
                statement.executeUpdate(createIntentSql);
                String insertStatement="INSERT INTO trubudget_intent (trubudget_intent_name, trubudget_intent_display_name)\n" +
                        "VALUES\n" +
                        "    ('global.listPermissions', 'List Permissions'),\n" +
                        "    ('global.grantPermission', 'Grant Permission'),\n" +
                        "    ('global.grantAllPermissions', 'Grant All Permissions'),\n" +
                        "    ('global.revokePermission', 'Revoke Permission'),\n" +
                        "    ('global.createProject', 'Create Project'),\n" +
                        "    ('global.createUser', 'Create User'),\n" +
                        "    ('global.enableUser', 'Enable User'),\n" +
                        "    ('global.disableUser', 'Disable User'),\n" +
                        "    ('global.listAssignments', 'List Assignments'),\n" +
                        "    ('global.createGroup', 'Create Group'),\n" +
                        "    ('network.registerNode', 'Register Node'),\n" +
                        "    ('network.list', 'List Nodes'),\n" +
                        "    ('network.listActive', 'List Active Nodes'),\n" +
                        "    ('network.voteForPermission', 'Vote for Permission'),\n" +
                        "    ('network.approveNewOrganization', 'Approve New Organization'),\n" +
                        "    ('network.approveNewNodeForExistingOrganization', 'Approve New Node for Existing Organization'),\n" +
                        "    ('network.declineNode', 'Decline Node'),\n" +
                        "    ('provisioning.start', 'Start Provisioning'),\n" +
                        "    ('provisioning.end', 'End Provisioning'),\n" +
                        "    ('provisioning.get', 'Get Provisioning Status') ON CONFLICT (trubudget_intent_name) DO NOTHING;\n";
                statement.executeUpdate(insertStatement);

//                String addColumnSql = "ALTER TABLE trubudget_intent ADD COLUMN IF NOT EXISTS intent_group INTEGER";
//                statement.executeUpdate(addColumnSql);
//
//                String addForeignKeySql = "ALTER TABLE trubudget_intent ADD CONSTRAINT fk_intent_group "
//                        + "FOREIGN KEY (intent_group) REFERENCES trubudget_intent_group (trubudget_intent_group_id)";
//                statement.executeUpdate(addForeignKeySql);
                String relationSql="CREATE TABLE IF NOT EXISTS user_trubudget_intent (\n" +
                        "    user_id BIGINT REFERENCES DG_USER(id),\n" +
                        "    trubudget_intent_id BIGINT REFERENCES trubudget_intent(trubudget_intent_id),\n" +
                        "    PRIMARY KEY (user_id, trubudget_intent_id)\n" +
                        ");";
                statement.executeUpdate(relationSql);
                String insertIntoGlobalSettings="INSERT INTO amp_global_settings(settingsname,settingsvalue,possiblevalues,description,section,value_translatable,internal)" +
                        "VALUES\n" +
                        " (nextval('amp_global_settings_seq'),'isEnabled','true','t_Boolean','Is Trubudget enabled for this deployment','trubudget',NULL,'true'),"+
                        " (nextval('amp_global_settings_seq'),'baseUrl','http://localhost:8080/api/','','Trubudget base url','trubudget',NULL,'true'),"+
                        " (nextval('amp_global_settings_seq'),'rootUser','root','','Trubudget username for root user','trubudget',NULL,'true'),"+
                        "(nextval('amp_global_settings_seq'),'rootPassword','root-secret','','Trubudget password for root user','trubudget',NULL,'true'),"+
                        "(nextval('amp_global_settings_seq'),'organization','KfW','','Organization name to be used for this deployment','trubudget',NULL,'true') ON CONFLICT (settingsname,section) DO NOTHING";
                statement.executeUpdate(insertIntoGlobalSettings);
            } catch (SQLException e) {
                // Handle the exception
                e.printStackTrace();
            }
        });

        transaction.commit();
        session.close();
    }

    public void registerEhCacheMBeans() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        CacheManager cacheManager = CacheManager.getInstance();
        ManagementService.registerMBeans(cacheManager, mBeanServer, true, true, true, true);
    }

    private void printResultIfNonVoid(Set<String> result) {
        if (result.size() > 0) {
            logger.info(String.format("Result: %d items, {%s}", result.size(), String.join(", ", result)));
        }
    }
    
    private void performSanityOperationsOnSchema(ServletContext ampContext) {
        logger.info("Checking for new columns in AmpReportSchema to be backported...");
        printResultIfNonVoid(AmpReportsSchema.getInstance().synchronizeAmpColumnsBackport(ampContext));
        logger.info("Checking for new measures in AmpReportSchema to be backported...");
        printResultIfNonVoid(AmpReportsSchema.getInstance().synchronizeAmpMeasureBackport());
        logger.info("Checking for columns in AmpReportSchema to be migrated to measures...");
        printResultIfNonVoid(AmpReportsSchema.getInstance().migrateColumns());
    }

    /**
     * runs a "cache refresh" function and checks that it returned ok
     * @param funcName
     * @param what
     */
    protected void runCacheRefreshingQuery(String funcName, String what) {
        String query = String.format("SELECT %s() FROM (select 1) AS dual", funcName);
        java.util.List<?> shouldBe1 = PersistenceManager.getSession().createNativeQuery(query).list(); // force recreation of the location cached at each AMP startup
        
        if (shouldBe1.size() != 1)
            throw new Error("recreating AMP " + what + " caches failed");

        if (PersistenceManager.getLong(shouldBe1.get(0)).longValue() != 1L)
            throw new Error("recreating AMP " + what + " caches returned the wrong value: " + shouldBe1.get(0));
    }
    
    protected void checkDatabaseSanity()
    {
        Session session = null; 
        try{
            session = PersistenceManager.getSession();
            
            ReportsUtil.checkDatabaseSanity(session);
            ReportsUtil.checkFilteringConfigurationSanity(session);
            ReportsUtil.checkPledgesViewsSanity(session);
            CurrencyUtil.checkDatabaseSanity(session);
            OrganisationUtil.checkOrganisationNamesSanity(session);
        }catch(Exception e){
            throw new Error("database does not conform to minimum sanity requirements, shutting down AMP", e);
        }finally {
            PersistenceManager.cleanupSession(session);
        }
    }
    
    public void maintainMondrianCaches()
    {
        java.sql.Connection connection = null;
        try {
            connection = PersistenceManager.getJdbcConnection();    
            connection.setAutoCommit(false);
        
            // make sure that, in case the following SQL stuff fails, at least the Java side executed correctly and committed its stuff
            connection.setAutoCommit(true);
            PublicViewColumnsUtil.maintainPublicViewCaches(connection, false); // let Java do all the repetitive work
            connection.setAutoCommit(false); // this will commit any unfinished transaction started by PublicViewColumnsUtil
        }       
        catch(Exception e) {
            logger.error("some serious error happened while maintaining Mondrian caches", e);
        }
        finally {
            PersistenceManager.closeQuietly(connection);
        }
    }
    
    public void importGazeteer () {
        try { 
            Properties prop = new Properties();
            prop.put("token", "\t");
            String[] columnNames = { "geonameId", "name", "asciiName", "alternateNames", "latitude", "longitude", "featureClass",
                    "featureCode", "countryIso", "cc2", "admin1", "admin2", "admin3", "admin4", "population", "elevation",
                    "gtopo30", "timezone", "lastModified" };
            
            GazeteerCSVImporter importer = new GazeteerCSVImporter(SERVLET_CONTEXT_ROOT_REAL_PATH+System.getProperty("file.separator")+ "doc"+System.getProperty("file.separator")+"gazeteer.csv",columnNames,prop);
            
            if (importer.isTableEmpty()) {
                importer.performImport();
            }
        }
        catch (Exception e){
            logger.error("cannot import gazeteer",e);
        }
    }

    private void initAPI() {
        ApiError.configureComponentClassToIdMap();
    }
}

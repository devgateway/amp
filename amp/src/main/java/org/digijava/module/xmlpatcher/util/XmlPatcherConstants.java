/**
 * XmlPatcherConstants.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.util;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Constants of the XML Patcher
 */
public final class XmlPatcherConstants {

    public static final String xsdLocation="/doc/xmlpatcher.xsd";
    public static final String xslLocation="/doc/xmlpatcher.xsl";
    public static final String jaxbPackage="org.digijava.module.xmlpatcher.jaxb";
    
    
    public static final String patchDirName = "xmlpatches";
    public static final String testPatchDirName = "testxmlpatches";
    public static final String schedulersPackage = "org.digijava.module.xmlpatcher.scheduler.";

    public static final String CONDITION_CUSTOM = "custom";
    
    public static final class TriggerTypes {
        public static final String ALL="all";
        public static final String ANY="any";
    }
    
    /**
     * Describes states of the patch execution.
     * 
     * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
     * @see org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch#getState()
     */
    public static final class PatchStates {
        /**
         * The patch is open. It has either: - not been executed yet due to
         * conditions that do not apply OR - executed successfully but the patch
         * is of type closeOnSuccess=false, so was left open to be re-run
         */
        public static final short OPEN = 0;
        /**
         * The patch has been successfully applied and is now in close state
         */
        public static final short CLOSED = 1;

        /**
         * The patch has been run but it failed. It has been put to failed state
         * and the developer needs to check what's wrong.
         */
        public static final short FAILED = 2;
        
        /**
         * The patch has been deprecated by another newer patch.
         */
        public static final short DEPRECATED = 3;
        
        /**
         * The pach is no longer available (file was deleted) and was marked deleted
         */
        public static final short DELETED=4;

        
        public static String toString(short state) {
            switch(state) {
            case OPEN: return "OPEN";
            case CLOSED: return "CLOSED";
            case FAILED: return "FAILED";
            case DEPRECATED: return "DEPRECATED";
            case DELETED: return "DELETED";
            }
            return "UNKNOWN";
        }
    }

    /**
     * Defines properties used by the patcher schedulers.
     * 
     * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
     * @see org.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler
     */
    public static final class SchedulerProperties {
        /**
         * @see org.digijava.module.xmlpatcher.scheduler.LocationPriorityXmlPatcherScheduler
         */
        public static final String LOCATION_PRIORITY = "locationPriority";
    }

    /**
     * @see org.digijava.module.xmlpatcher.worker.XmlPatcherScriptWorker
     */
    public static final class ScriptLangs {
        /**
         * Generic sql language. Executable by any SQL database. Useful for
         * simple insert/update/delete statements
         */
        public static final String SQL = "sql";

        /**
         * Hibernate query language. We can use this to query AMP hibernate
         * objects and also perform simple update/delete tasks.
         */
        public static final String HQL = "hql";

        /**
         * BeanShell scripting. Very flexible java-like scripting language
         * 
         * @see http://www.beanshell.org
         */
        public static final String BSH = "bsh";

        /**
         * MySQL specific query language
         */
        public static final String MYSQL = "mysql";

        /**
         * Oracle specific query language
         */
        public static final String ORACLE = "oracle";

        /**
         * PostgreSQL specific query language
         */
        public static final String POSTGRES = "postgres";

        /**
         * Listing the languages that are considered generic(database
         * independent)
         */
        public static final String[] generics = new String[] { SQL, HQL, BSH };

        /**
         * Native languages
         */
        public static final String[] natives = new String[] { MYSQL, ORACLE,
                POSTGRES };

        /**
         * All available languages
         */
        public static final String[] all = new String[] { SQL, HQL, BSH, MYSQL,
                ORACLE, POSTGRES };
    }

}

package org.dgfoundation.amp.codegenerators;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsEngineForTesting;
import org.dgfoundation.amp.nireports.TestcasesReportsSchema;
import org.dgfoundation.amp.testutils.ActivityIdsFetcher;
import org.digijava.kernel.persistence.PersistenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 * Abstract scaffold for classes that generate data for hardcoded reports schema. 
 * @author acartaleanu
 *
 */
public abstract class CodeGenerator {
    
    public static String PACKAGE_NAME = "nireports.testcases.generic";
    public static boolean OBFUSCATE_TEXT = false;
    
    public abstract String generate();
    protected abstract String getFilePart1();
    protected abstract String getFilePart2();
    protected abstract String getCanonicalNameWithCells(String name);
    
    protected final String name;
    protected final String clazz;
    
    protected CodeGenerator(String name, String clazz) {
        this.name = name;
        this.clazz = clazz;
    }
    
    public String getPath() {
        String path = "/home/octavian/codegen/" ;
        //String path = System.getProperty("user.dir")
//              + "/src/test/java/org/dgfoundation/amp/" + PACKAGE_NAME + "/nicolumns/";
        return path;
    }
    
    public void generateToFile() throws FileNotFoundException, UnsupportedEncodingException {
        String path = getPath()
                + getCanonicalNameWithCells(this.name) + ".java";
        
        File file = new File(path);
        file.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.print(String.format("package org.dgfoundation.amp.%s;\n\n", PACKAGE_NAME));
        writer.print(String.format(getFilePart1(), this.clazz, getCanonicalNameWithCells(this.name), getCanonicalNameWithCells(this.name)));
        writer.print(this.generate());
        writer.println(getFilePart2());
        writer.close();
        System.out.println("Generated " + getCanonicalNameWithCells(this.name) + ".java");
    }
    
    public static String escape(String a){
        if (a != null)
            return "\"" + a + "\"";
        return "null";
    }
    
    static MessageDigest md5digester = AlgoUtils.getMD5Digester();
    
    public static String anon(String input) {
        if (!OBFUSCATE_TEXT)
            return cleanup(input);
        return input == null ? null : AlgoUtils.digestString(md5digester, input).substring(0, 8);
    }
    
    public static String cleanup(String input) {
        if (input == null) return null;
        String res = input.replace('\n', ' ').replace('\r', ' ').replace('"', '\'').trim();
        return res;
    }
    
    public static String pad(String a, int length) {
        if (a.length() > length)
            return a;
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < length - a.length(); i++)
            bld.append(" ");
        return a + bld.toString();
    }
    
    
    /**
     * runs a given lambda in the context of a fully initialized NiReports engine,
     *  which will have its activity filters overridden to generate ids corresponding to a given list of names in English
     * @param activityNames
     * @param runnable
     */
    protected void runInEngineContext(List<String> activityNames, ExceptionConsumer<NiReportsEngine> runnable) {
        TestcasesReportsSchema.workspaceFilter = new ActivityIdsFetcher(activityNames);
        NiReportsEngineForTesting engine = new NiReportsEngineForTesting(TestcasesReportsSchema.instance, runnable);
        engine.execute(); // will run runnable in the engine's context
    }
    
    protected Map<Long, String> getActivityNames() {
        String query = "SELECT amp_activity_id, name FROM amp_activity_version WHERE amp_team_id IN "
                + "(SELECT amp_team_id FROM amp_team WHERE "
                + "1=1"
                //+ "name = 'test workspace'"
                + ")"
                + "AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity)";
        return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
    }
    
}

package org.digijava.module.xmlpatcher.core;

import org.dgfoundation.amp.algo.AlgoUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a class encoding a SQL patch to be used by {@link SimpleSQLPatcher}
 * @author Dolghier Constantin
 *
 */
public class SimpleSQLPatch implements Comparable<SimpleSQLPatch>{
    
    final static MessageDigest digester = AlgoUtils.getMD5Digester();
    
    /**
     * a developer-assigned id to identify the patch. Patches are applied in increasing order of this id
     */
    public final String id;
    
    /**
     * a hash encoding the contents of the patch, equals concat(md5(queries[i]))
     */
    public final String hash;
    
    /**
     * the SQL queries to run
     */
    public final List<String> queries;
    
    public SimpleSQLPatch(String id, String... queries){
        this.id = id;
        this.queries = Collections.unmodifiableList(Arrays.asList(queries));
        this.hash = computeHash(this.queries);
    }
    
    static String computeHash(List<String> queries){
        StringBuilder bld = new StringBuilder();
        for(String query:queries)
            bld.append(AlgoUtils.digestString(digester, query));
        return bld.toString();
    }
    
    public int compareTo(SimpleSQLPatch oth){
        return this.id.compareTo(oth.id);
    }
}

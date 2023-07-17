package org.dgfoundation.amp.ar.viewfetcher;

import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.spatial.dialect.postgis.PostgisPG10Dialect;
import org.hibernate.spatial.dialect.postgis.PostgisPG82Dialect;
import org.hibernate.type.StringType;

public class AmpPostgresDialect extends PostgisPG82Dialect {
    private static final long serialVersionUID = 1L;

    
    /* the HQL function **NEEDS** to  have the same name and order of arguments as the SQL one, else
     the SQL-HQL duality in
     constructing queries will not
     work!*/
    public AmpPostgresDialect() {
        super();
        registerFunction("translate_field", 
                new VarArgsSQLFunction(StringType.INSTANCE, "translate_field(",", ", ")"));

    }
}

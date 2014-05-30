package org.dgfoundation.amp.ar.viewfetcher;

import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StringType;

public class AmpPostgresDialect extends org.hibernate.dialect.PostgreSQLDialect
{
	public AmpPostgresDialect()
	{
		super();
		//registerFunction("translate_field", new StandardSQLFunction("translate_field", StringType.INSTANCE));
//		registerFunction("translate_field", 
//				new SQLFunctionTemplate(StringType.INSTANCE, "translate_field('?1, ?2, ?3, ?4, ?5, ?6, ?7)"));
		
		registerFunction("translate_field", // the HQL function **NEEDS** to have the same name and order of arguments as the SQL one, else the SQL-HQL duality in constructing queries will not work!
				new VarArgsSQLFunction(StringType.INSTANCE, "translate_field(", ", " , ")" ));
		
	}
}

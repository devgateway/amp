package org.dgfoundation.amp.oracle;

import org.hibernate.dialect.Oracle10gDialect;
/**
 * 
 * @author Sebas
 * This a custom dialect class, the purpose  is to customize the sequence generation 
 */
public class AMPOracleDialect  extends Oracle10gDialect{
    
    @Override
    public Class getNativeIdentifierGeneratorClass() {
         return AMPSequenceGenerator.class;
    
    }
}

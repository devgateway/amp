package org.dgfoundation.amp.oracle;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;


/**
 * 
 * @author Sebas This class is used to generate the sequences with an START
 *         value equals to max(id) + 1 the class needs to have access to the CREATE_AMP_SEQ procedure
 /*
PROCEDURE CREATE_AMP_SEQ    (   seqName IN varchar2 ,
           tableName IN varchar2,
           idName varchar2)
        IS
        BEGIN
          DECLARE
           NID NUMBER;
           TGR_NAME VARCHAR2(30);
        BEGIN

        TGR_NAME:=SUBSTR(seqName,0, LENGTH(seqName)-3)||'TGR';
        EXECUTE IMMEDIATE  'SELECT MAX('||idName||') + 1 from '||tableName INTO NID;

        if NID is null then
        NID:=1;
        end if;

        EXECUTE IMMEDIATE  'CREATE  SEQUENCE '||seqName||' INCREMENT BY 1 START WITH ' ||TO_CHAR(NID)|| ' MAXVALUE 1E24 MINVALUE 1 NOCYCLE CACHE 20 NOORDER';


        EXECUTE IMMEDIATE ('CREATE OR REPLACE TRIGGER '||trim(TGR_NAME)
        ||' BEFORE INSERT OR UPDATE ON '||trim(tableName)
        ||' REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW'
        ||' DECLARE '
        ||' v_newVal NUMBER(12) := 0;'
        ||' v_incval NUMBER(12) := 0;'
        ||' BEGIN'
        ||' IF INSERTING AND :new.'||trim(idName)||' IS NULL THEN '
        ||' SELECT  '||seqName||'.NEXTVAL INTO v_newVal FROM DUAL;'
        ||' IF v_newVal = 1 THEN'
        ||'  SELECT max('||trim(idName)||') INTO v_newVal FROM '||trim(tableName)||';'
        || ' v_newVal := v_newVal + 1;'
        ||' LOOP'
        ||' EXIT WHEN v_incval>=v_newVal;'
        ||' SELECT '||trim(seqName)||'.nextval INTO v_incval FROM dual;'
        ||' END LOOP;'
        ||' END IF;'
        ||' mysql_utilities.identity := v_newVal; '
        ||' :new.'||trim(idName)||' := v_newVal;'
        ||' END IF;'
        ||' END;');


        EXECUTE IMMEDIATE  'ALTER TRIGGER '||TGR_NAME||' enable';
        COMMIT;
        END;
        END;

*/
 
/**
 * @deprecated
 *
 */
public class AMPSequenceGenerator extends SequenceGenerator {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AMPSequenceGenerator.class.getName());
    private String parameters;
    private String sequenceName;
    private String tableName;
    private String pkColumnName;

    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.parameters = params.getProperty(PARAMETERS);
        this.sequenceName = null;// THIS CLASS IS DEPRECATED, does not worth fixing PropertiesHelper.getString(SEQUENCE, params, "hibernate_sequence");
        this.tableName = params.getProperty(SequenceGenerator.TABLE);
        this.pkColumnName = params.getProperty(SequenceGenerator.PK);
        super.configure(type, params, serviceRegistry);
    }

    public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
        try {

            String[] ddl = dialect.getCreateSequenceStrings(sequenceName);
            if (parameters != null) {
                ddl[ddl.length - 1] += ' ' + parameters;
            } else {
                ddl[0] = "call CREATE_AMP_SEQ ('" + this.sequenceName + "', '" + this.tableName + "','" + this.pkColumnName + "') ";
            }

            return ddl;
        } catch (Exception e) {
            log.error("Can't create the DLL for sequence " + this.sequenceName + " on table " + this.tableName + " using column " + this.pkColumnName);
            throw new HibernateException("Can't create the DLL for sequence " + this.sequenceName + " on table " + this.tableName + " using column " + this.pkColumnName, e);

        }
    }

}

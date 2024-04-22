/**
 * XmlPatcherSQLLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.jaxb.Script;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Provides support to process generic SQL lang-type Use this with all
 *         generic SQL scripts, that do not require special server-specific
 *         queries
 */
public class XmlPatcherSQLLangWorker extends XmlPatcherLangWorker {

    /**
     * @param entity
     * @param log
     */
    public XmlPatcherSQLLangWorker(Lang entity, Script parentEntity,
            AmpXmlPatchLog log) {
        super(entity, parentEntity, log);
    }

    
    @Override
    protected boolean processSelectStatement()
            throws XmlPatcherLangWorkerException {
        Connection con = null;
        try {
            con = XmlPatcherUtil.getConnection();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(getEntity().getValue());
            //ugly hard coded, get only the 1st object of the SELECT
            if(!resultSet.next()) return true;
            returnValue=resultSet.getObject(1);
            return true;
        } catch (SQLException e) {
            throw new XmlPatcherLangWorkerException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new XmlPatcherLangWorkerException(e);
            }
        }
    }

    @Override
    /**
     * Invoked only if the updateQueryType is true (query is an update query).
     * 
     * @return true if execution is successful
     * @throws XmlPatcherLangWorkerException
     */
    protected boolean processUpdateStatement()
            throws XmlPatcherLangWorkerException {
        try {
            // get the jdbc connection from the Session Factory
            Connection con = XmlPatcherUtil.getConnection();

            con.setAutoCommit(false); // prevent auto commits. We'd like to
            // rollback the entire portion if needed
            Statement statement = con.createStatement();

            // tokenize the SQL using the delimiter specified as attribute
            // (default=";")
            StringTokenizer stok = new StringTokenizer(getEntity().getValue()
                    .trim(), getEntity().getDelimiter());
            java.util.List<String> statements = new ArrayList<>();
            while (stok.hasMoreTokens()) {
                String sqlCommand = stok.nextToken();
                statements.add(sqlCommand);
                if (sqlCommand.trim().isEmpty())
                    continue;
                statement.addBatch(sqlCommand);
            }

            // try to execute the batches and commit the whole transaction
            // if things go wrong, rollback the connection and set it back to
            // autocommit=true
            try {
                //logger.info(String.format("running statements: %s", statements));
                statement.executeBatch();
                con.commit();
            } catch (BatchUpdateException e) {
                //logger.error(String.format("\t====>the query FAILED!\n: %s", statements));
                con.rollback();
                throw new XmlPatcherLangWorkerException(e);
            } finally {
                con.setAutoCommit(true);
                con.close();
            }

            return true;

        } catch (HibernateException e) {
            throw new XmlPatcherLangWorkerException(e);
        } catch (SQLException e) {
            throw new XmlPatcherLangWorkerException(e);
        }
    }

}

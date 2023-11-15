package org.digijava.kernel.user;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.sql.SQLException;
import java.sql.Statement;

//@Component
public class SqlStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Code to run when the application context is refreshed
        System.out.println("Application started. Performing query tasks...");

        // Add your initialization logic here
        runQuery();
    }
    public void runQuery()
    {
        Session session = PersistenceManager.openNewSession();

                Transaction transaction = session.beginTransaction();

        // Using Hibernate's native SQL execution
        session.doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                String createIntentSql = "CREATE TABLE IF NOT EXISTS trubudget_intent ("
                        + "trubudget_intent_id SERIAL PRIMARY KEY,"
                        + "trubudget_intent_name VARCHAR(255),"
                        + "intent_group INTEGER,"
                        + "FOREIGN KEY (intent_group) REFERENCES trubudget_intent_group (trubudget_intent_group_id)"
                        + ")";
                statement.executeUpdate(createIntentSql);
                String createIntentGroupSql ="CREATE TABLE IF NOT EXISTS trubudget_intent_group ("
                        +"trubudget_intent_group_id SERIAL PRIMARY KEY,"
                        +"trubudget_intent_group_name VARCHAR(255)"
                        +");";
                statement.executeUpdate(createIntentGroupSql);
                String addColumnSql = "ALTER TABLE trubudget_intent ADD COLUMN intent_group INTEGER";
                statement.executeUpdate(addColumnSql);

                String addForeignKeySql = "ALTER TABLE trubudget_intent ADD CONSTRAINT fk_intent_group "
                        + "FOREIGN KEY (intent_group) REFERENCES trubudget_intent_group (trubudget_intent_group_id)";
                statement.executeUpdate(addForeignKeySql);
            } catch (SQLException e) {
                // Handle the exception
            }
        });

        transaction.commit();
        session.close();
    }

}
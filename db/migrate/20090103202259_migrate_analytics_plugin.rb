# This should actually use migrations from the plugin's directory but as they are not working currently (desert bug?)
# I added them here.

class MigrateAnalyticsPlugin < ActiveRecord::Migration
  def self.up
    execute <<-END_SQL
      CREATE VIEW project_province_count AS
        SELECT COUNT (*) AS provinces, t1.project_id AS project_id
        FROM(SELECT DISTINCT ON (project_id, province_id) province_id, project_id FROM geo_relevances) t1
        GROUP BY project_id;
              
      CREATE VIEW project_payment_totals AS
        SELECT projects.id AS project_id, projects.donor_id, projects.data_status, accessible_fundings."year", accessible_fundings.currency,
          SUM(accessible_fundings.payments_q1 + accessible_fundings.payments_q2 + 
            accessible_fundings.payments_q3 + accessible_fundings.payments_q4) 
          AS total FROM projects LEFT JOIN accessible_fundings ON projects.id = accessible_fundings.project_id
        GROUP BY projects.id, projects.donor_id, projects.data_status, accessible_fundings."year", accessible_fundings.currency;
        
      CREATE VIEW province_payments AS
        SELECT t1.province_id, project_payment_totals."year", project_payment_totals."currency",
          project_payment_totals.data_status, project_payment_totals.donor_id, 
          SUM(project_payment_totals.total / project_province_count.provinces) AS payments
        FROM (SELECT DISTINCT ON (project_id, province_id) * FROM geo_relevances) t1, project_province_count, project_payment_totals
        WHERE (project_province_count.project_id = t1.project_id) AND (project_payment_totals.project_id = t1.project_id)
        GROUP BY t1.province_id, project_payment_totals."year", project_payment_totals."currency", 
          project_payment_totals.data_status, project_payment_totals.donor_id;
    END_SQL
    
    execute <<-END_SQL
      CREATE VIEW project_dac_sector_shares AS
      	SELECT SUM(sector_relevances.amount) AS amount, projects.id AS project_id, sector_relevances.dac_sector_id
      	FROM projects, sector_relevances
      	WHERE sector_relevances.project_id = projects.id
      	GROUP BY projects.id, sector_relevances.dac_sector_id;
      	
      CREATE VIEW sector_payments AS
        SELECT project_dac_sector_shares.dac_sector_id, project_payment_totals."year", project_payment_totals."currency",
          project_payment_totals.data_status, project_payment_totals.donor_id, 
          (SUM(project_payment_totals.total)  * (100 / project_dac_sector_shares.amount)) AS payments
        FROM project_payment_totals, project_dac_sector_shares
        WHERE project_dac_sector_shares.project_id = project_payment_totals.project_id 
      	  AND project_dac_sector_shares.dac_sector_id IS DISTINCT FROM NULL 
      		AND project_payment_totals.total IS DISTINCT FROM NULL
        GROUP BY project_dac_sector_shares.dac_sector_id, project_payment_totals."year", project_payment_totals."currency", 
          project_payment_totals.data_status, project_payment_totals.donor_id, project_dac_sector_shares.amount
        ORDER BY payments DESC;
    END_SQL
  end

  def self.down
    execute <<-END_SQL
      DROP VIEW project_province_count;
      DROP VIEW project_payment_totals;
      DROP VIEW province_payments;
    END_SQL
    
    execute <<-END_SQL
      DROP VIEW project_dac_sector_shares;
      DROP VIEW sector_payments;
    END_SQL
  end
end

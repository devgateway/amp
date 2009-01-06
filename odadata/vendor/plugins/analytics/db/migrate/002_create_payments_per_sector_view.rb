class CreatePaymentsPerSectorView < ActiveRecord::Migration
  def self.up
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
      DROP VIEW project_dac_sector_shares;
      DROP VIEW sector_payments;
    END_SQL
  end
end



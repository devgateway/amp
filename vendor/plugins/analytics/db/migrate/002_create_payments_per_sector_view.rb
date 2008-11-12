class CreatePaymentsPerSectorView < ActiveRecord::Migration
  def self.up
    execute <<-END_SQL
      CREATE VIEW sector_payments AS
        SELECT projects.dac_sector_id, project_payment_totals."year", project_payment_totals."currency",
          project_payment_totals.data_status, project_payment_totals.donor_id, 
          SUM(project_payment_totals.total) AS payments
        FROM projects, project_payment_totals
        WHERE project_payment_totals.project_id = projects.id AND projects.dac_sector_id IS DISTINCT FROM NULL 
          AND project_payment_totals.total IS DISTINCT FROM NULL
        GROUP BY projects.dac_sector_id, project_payment_totals."year", project_payment_totals."currency", 
          project_payment_totals.data_status, project_payment_totals.donor_id
        ORDER BY payments DESC;
    END_SQL
  end

  def self.down
    execute <<-END_SQL
      DROP VIEW sector_payments;
    END_SQL
  end
end



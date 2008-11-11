class CreatePaymentsPerProvinceViews < ActiveRecord::Migration
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
  end

  def self.down
    execute <<-END_SQL
      DROP VIEW project_province_count;
      DROP VIEW project_payment_totals;
      DROP VIEW province_payments;
    END_SQL
  end
end

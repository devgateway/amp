class CreateAccessibleFundingsView < ActiveRecord::Migration
  def self.up
    execute <<-END_SQL
      CREATE VIEW accessible_fundings AS
        SELECT
          donors.id AS donor_id,
          projects.id AS project_id,
          projects.data_status AS data_status,
          exchange_rates.currency AS currency,
          fundings.year AS year,
          round(fundings.payments_q1 * (exchange_rates.euro_rate / local_rates.euro_rate)) AS payments_q1,
          round(fundings.payments_q2 * (exchange_rates.euro_rate / local_rates.euro_rate)) AS payments_q2,
          round(fundings.payments_q3 * (exchange_rates.euro_rate / local_rates.euro_rate)) AS payments_q3,
          round(fundings.payments_q4 * (exchange_rates.euro_rate / local_rates.euro_rate)) AS payments_q4,
          round(fundings.commitments * (exchange_rates.euro_rate / local_rates.euro_rate)) AS commitments
        FROM fundings
        JOIN projects ON (fundings.project_id = projects.id)
        JOIN donors ON (projects.donor_id = donors.id)
        JOIN exchange_rates ON (exchange_rates.year = fundings.year)
        JOIN exchange_rates AS local_rates ON (local_rates.year = fundings.year AND local_rates.currency = donors.currency)
      ;

      CREATE VIEW accessible_forecasts AS
        SELECT
          donors.id AS donor_id,
          projects.id AS project_id,
          projects.data_status AS data_status,
          exchange_rates.currency AS currency,
          funding_forecasts.year AS year,
          round(funding_forecasts.payments * (exchange_rates.euro_rate / local_rates.euro_rate)) AS payments,
          round(funding_forecasts.commitments * (exchange_rates.euro_rate / local_rates.euro_rate)) AS commitments
        FROM funding_forecasts
        JOIN projects ON (funding_forecasts.project_id = projects.id)
        JOIN donors ON (projects.donor_id = donors.id)
        JOIN exchange_rates ON (exchange_rates.year = funding_forecasts.year)
        JOIN exchange_rates AS local_rates ON (local_rates.year = funding_forecasts.year AND local_rates.currency = donors.currency)
      ;
    END_SQL
  end

  def self.down
    execute 'DROP VIEW accessible_fundings'
    execute 'DROP VIEW accessible_forecasts'
  end
end

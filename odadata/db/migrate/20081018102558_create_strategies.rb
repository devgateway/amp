class CreateStrategies < ActiveRecord::Migration
  def self.up
    create_table :country_strategies do |t|
      t.string  :strategy_number
      t.string  :strategy_number_es
      
      t.text    :description
      t.text    :description_es
          
      t.string  :website
      t.text    :comment
      t.boolean :strategy_paper
      
      t.date    :start
      t.date    :end
      
      t.integer :total_amount_foreseen
      
      t.integer :programming_responsibility
      t.integer :project_appraisal_responsibility
      t.integer :tenders_responsibility
      t.integer :commitments_and_payments_responsibility
      t.integer :monitoring_and_evaluation_responsibility
      
      t.integer :commitment_to_budget_support
      t.integer :commitment_to_sectorwide_approaches_and_common_funds
      t.integer :commitment_to_projects
      
      t.integer :donor_id
    end
    
    create_table :total_odas do |t|
      t.integer :commitments
      t.integer :year
      t.integer :disbursements
      
      t.references :country_strategy
    end
    
    create_table :sector_amounts do |t|
      t.integer :amount
      
      t.references :country_strategy, :focal_sector
    end

    create_table :sector_details do |t|
      t.integer :amount
      
      t.references :country_strategy, :focal_sector
      t.string  :focal_sector_type
    end
  end

  def self.down
    drop_table :country_strategies
    drop_table :total_odas
    drop_table :sector_amounts
    drop_table :sector_details
  end
end

class AddDonorDetails < ActiveRecord::Migration
  def self.up
    add_column :donors, :un_agency, :boolean
    add_column :donors, :oda_on_budget_1, :string
    add_column :donors, :oda_on_budget_1_currency, :string
    add_column :donors, :oda_on_budget_2, :string
    add_column :donors, :oda_on_budget_2_currency, :string
    add_column :donors, :oda_on_budget_3, :string
    add_column :donors, :oda_on_budget_3_currency, :string
    add_column :donors, :oda_on_budget_4,  :string
    add_column :donors, :oda_on_budget_4_currency,  :string
    add_column :donors, :parallel_piu, :string
    add_column :donors, :yearly_missions, :string
    add_column :donors, :yearly_coordinated_missions, :string
    add_column :donors, :yearly_analytic_works,  :string
    add_column :donors, :yearly_coordinated_analytic_works, :string
    add_column :donors, :gbs_multiyear_agreement, :boolean
    add_column :donors, :gbs_multiyear_agreement_years, :integer
    add_column :donors, :gbs_question_1, :boolean
    add_column :donors, :gbs_question_2, :boolean
    add_column :donors, :gbs_question_3, :boolean
    add_column :donors, :gbs_question_4, :text
  end

  def self.down
    remove_column :donors, :un_agency
    remove_column :donors, :oda_on_budget_1
    remove_column :donors, :oda_on_budget_1_currency
    remove_column :donors, :oda_on_budget_2
    remove_column :donors, :oda_on_budget_2_currency
    remove_column :donors, :oda_on_budget_3
    remove_column :donors, :oda_on_budget_3_currency
    remove_column :donors, :oda_on_budget_4
    remove_column :donors, :oda_on_budget_4_currency
    remove_column :donors, :parallel_piu
    remove_column :donors, :yearly_missions
    remove_column :donors, :yearly_coordinated_missions
    remove_column :donors, :yearly_analytic_works
    remove_column :donors, :yearly_coordinated_analytic_works
    remove_column :donors, :gbs_multiyear_agreement
    remove_column :donors, :gbs_multiyear_agreement_years
    remove_column :donors, :gbs_question_1
    remove_column :donors, :gbs_question_2
    remove_column :donors, :gbs_question_3
    remove_column :donors, :gbs_question_4
  end
end

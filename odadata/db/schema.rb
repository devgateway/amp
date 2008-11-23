# This file is auto-generated from the current state of the database. Instead of editing this file, 
# please use the migrations feature of Active Record to incrementally modify your database, and
# then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your database schema. If you need
# to create the application database on another system, you should be using db:schema:load, not running
# all the migrations from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20081123111827) do

  create_table "agencies", :force => true do |t|
    t.string "name"
    t.string "type"
    t.string "contact_name"
    t.string "contact_phone"
    t.string "contact_email"
  end

  create_table "cofundings", :force => true do |t|
    t.integer "project_id"
    t.integer "donor_id"
    t.integer "amount"
    t.string  "currency"
  end

  add_index "cofundings", ["donor_id", "project_id"], :name => "index_cofundings_on_project_id_and_donor_id"

  create_table "contracted_agencies_projects", :id => false, :force => true do |t|
    t.integer "project_id"
    t.integer "contracted_agency_id"
  end

  create_table "country_strategies", :force => true do |t|
    t.string  "website"
    t.text    "comment"
    t.boolean "strategy_paper"
    t.date    "start"
    t.date    "end"
    t.integer "total_amount_foreseen"
    t.integer "programming_responsibility"
    t.integer "project_appraisal_responsibility"
    t.integer "tenders_responsibility"
    t.integer "commitments_and_payments_responsibility"
    t.integer "monitoring_and_evaluation_responsibility"
    t.integer "commitment_to_budget_support"
    t.integer "commitment_to_sectorwide_approaches_and_common_funds"
    t.integer "commitment_to_projects"
    t.integer "donor_id"
  end

  create_table "country_strategy_translations", :force => true do |t|
    t.string  "locale"
    t.string  "strategy_number"
    t.text    "description"
    t.integer "country_strategy_id"
  end

  create_table "crs_sector_translations", :force => true do |t|
    t.string  "locale"
    t.string  "name"
    t.text    "description"
    t.integer "crs_sector_id"
  end

  create_table "crs_sectors", :force => true do |t|
    t.integer "code"
    t.integer "dac_sector_id"
  end

  add_index "crs_sectors", ["dac_sector_id"], :name => "index_crs_sectors_on_dac_sector_id"

  create_table "dac_sector_translations", :force => true do |t|
    t.string  "locale"
    t.string  "name"
    t.text    "description"
    t.integer "dac_sector_id"
  end

  create_table "dac_sectors", :force => true do |t|
    t.integer "code"
  end

  create_table "districts", :force => true do |t|
    t.string  "name"
    t.string  "code"
    t.integer "province_id"
  end

  create_table "donor_agencies", :force => true do |t|
    t.string  "name"
    t.string  "code"
    t.string  "acronym"
    t.integer "donor_id"
  end

  create_table "donor_translations", :force => true do |t|
    t.string  "locale"
    t.string  "name"
    t.integer "donor_id"
  end

  create_table "donors", :force => true do |t|
    t.string  "code"
    t.string  "currency"
    t.boolean "cofunding_only"
    t.text    "institutions_responsible_for_oda"
    t.integer "total_staff_in_country"
    t.integer "total_expatriate_staff"
    t.integer "total_local_staff"
    t.string  "officer_responsible"
    t.text    "field_office_address"
    t.string  "field_office_phone"
    t.string  "field_office_email"
    t.string  "field_office_website"
    t.string  "head_of_mission_name"
    t.string  "head_of_mission_email"
    t.string  "head_of_cooperation_name"
    t.string  "head_of_cooperation_email"
    t.string  "first_focal_point_name"
    t.string  "first_focal_point_email"
    t.string  "second_focal_point_name"
    t.string  "second_focal_point_email"
  end

  create_table "exchange_rates", :force => true do |t|
    t.integer "year"
    t.string  "currency"
    t.float   "euro_rate"
  end

  create_table "funding_forecasts", :force => true do |t|
    t.integer "project_id"
    t.integer "year"
    t.integer "payments"
    t.integer "commitments"
    t.boolean "on_budget",   :default => false
    t.boolean "on_treasury", :default => false
  end

  add_index "funding_forecasts", ["project_id"], :name => "index_funding_forecasts_on_project_id"

  create_table "fundings", :force => true do |t|
    t.integer "project_id"
    t.integer "year"
    t.integer "payments_q1"
    t.integer "payments_q2"
    t.integer "payments_q3"
    t.integer "payments_q4"
    t.integer "commitments"
    t.boolean "on_budget",   :default => false
    t.boolean "on_treasury", :default => false
  end

  add_index "fundings", ["project_id"], :name => "index_fundings_on_project_id"

  create_table "geo_relevances", :force => true do |t|
    t.integer "project_id"
    t.integer "province_id"
    t.integer "district_id"
  end

  create_table "glossaries", :force => true do |t|
    t.string "model"
    t.string "method"
    t.string "locale"
    t.text   "description"
  end

  add_index "glossaries", ["locale", "method", "model"], :name => "index_glossaries_on_model_and_method_and_locale"

  create_table "historic_fundings", :force => true do |t|
    t.integer "project_id"
    t.integer "payments"
    t.integer "commitments"
  end

  add_index "historic_fundings", ["project_id"], :name => "index_historic_fundings_on_project_id"

  create_table "implementing_agencies_projects", :id => false, :force => true do |t|
    t.integer "project_id"
    t.integer "implementing_agency_id"
  end

  create_table "mdg_relevances", :force => true do |t|
    t.integer "project_id"
    t.integer "mdg_id"
    t.integer "target_id"
  end

  create_table "mdg_translations", :force => true do |t|
    t.string  "locale"
    t.string  "name"
    t.text    "description"
    t.integer "mdg_id"
  end

  create_table "mdgs", :force => true do |t|
  end

  create_table "plugin_schema_info", :id => false, :force => true do |t|
    t.string  "plugin_name"
    t.integer "version"
  end

  create_table "projects", :force => true do |t|
    t.text     "title"
    t.text     "description"
    t.string   "donor_project_number"
    t.integer  "oecd_number"
    t.string   "recipient_country_budget_nr"
    t.integer  "recipient_code"
    t.integer  "region_code"
    t.string   "income_code"
    t.date     "start"
    t.date     "end"
    t.text     "comments"
    t.string   "website"
    t.integer  "type_of_aid"
    t.integer  "grant_loan"
    t.integer  "national_regional"
    t.integer  "type_of_implementation"
    t.string   "government_counterpart"
    t.integer  "prj_status"
    t.integer  "data_status",                 :default => 0
    t.string   "input_state"
    t.integer  "donor_id"
    t.integer  "donor_agency_id"
    t.integer  "dac_sector_id"
    t.integer  "crs_sector_id"
    t.integer  "country_strategy_id"
    t.integer  "government_counterpart_id"
    t.integer  "gender_policy_marker"
    t.integer  "environment_policy_marker"
    t.integer  "biodiversity_marker"
    t.integer  "climate_change_marker"
    t.integer  "desertification_marker"
    t.string   "officer_responsible_name"
    t.string   "officer_responsible_phone"
    t.string   "officer_responsible_email"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "provinces", :force => true do |t|
    t.string "name"
    t.string "code"
  end

  create_table "provinces_sector_details", :id => false, :force => true do |t|
    t.integer "province_id"
    t.integer "sector_detail_id"
  end

  create_table "roles", :force => true do |t|
    t.string "title"
    t.text   "description"
    t.string "layout"
  end

  create_table "sector_amounts", :force => true do |t|
    t.integer "amount"
    t.integer "country_strategy_id"
    t.integer "focal_sector_id"
  end

  create_table "sector_details", :force => true do |t|
    t.integer "amount"
    t.integer "country_strategy_id"
    t.integer "focal_sector_id"
    t.string  "focal_sector_type"
  end

  create_table "settings", :id => false, :force => true do |t|
    t.string "key"
    t.text   "value"
  end

  create_table "target_translations", :force => true do |t|
    t.string  "locale"
    t.text    "name"
    t.integer "target_id"
  end

  create_table "targets", :force => true do |t|
    t.integer "mdg_id"
  end

  create_table "total_odas", :force => true do |t|
    t.integer "commitments"
    t.integer "year"
    t.integer "disbursements"
    t.integer "country_strategy_id"
  end

  create_table "type_of_aid_translations", :force => true do |t|
    t.string  "locale"
    t.string  "name"
    t.integer "type_of_aid_id"
  end

  create_table "types_of_aid", :force => true do |t|
  end

  create_table "users", :force => true do |t|
    t.string   "name",                      :limit => 100, :default => ""
    t.string   "email",                     :limit => 100
    t.integer  "role_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "crypted_password",          :limit => 40
    t.string   "salt",                      :limit => 40
    t.string   "remember_token",            :limit => 40
    t.datetime "remember_token_expires_at"
    t.integer  "donor_id"
  end

  add_index "users", ["email"], :name => "index_users_on_email", :unique => true

end

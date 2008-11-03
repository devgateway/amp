class ConsistenciesController < ApplicationController
  access_rule 'focal_point'
  before_filter :ensure_open_data_input
  
  def show
    year = params[:year].to_i || Time.now.year  
    @consistency = Consistency.new(current_donor, year)
  end
  
end
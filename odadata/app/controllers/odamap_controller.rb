class OdamapController < ApplicationController
  
  def index
    render :layout => "odamap"
  end
  
  # Proxy for WxS requests
  def wxs
    map = OdaMap::CachedRequest.new(params)
    
    render :text => map.data, :content_type => map.content_type
  end
  
  def query
    map = OdaMap::Base.new
    
    begin
      res_l1, res_l2 = map.query_by_lon_lat(params[:lon].to_f, params[:lat].to_f)
      l1 = GeoLevel1.find_by_name(res_l2.getValue(2))
      l2 = l1.geo_level2s.find_by_name(res_l2.getValue(3))
    rescue
      nil
    end
        
    render :json => {
      :level1 => l1,
      :level2 => l2,
      #:level1_geom => res_l1.toWKT,
      #:level2_geom => res_l2.toWKT
    }
  end
  
  def report
    fields = [:factsheet_link, :donor, :donor_project_number, :title, :total_commitments, :total_payments, "total_commitments_#{Time.now.year-1}", "total_payments_#{Time.now.year-1}", "commitments_forecast_#{Time.now.year}", "payments_forecast_#{Time.now.year}", :start, :end]
    
    unify_location_parameters!(params)
    report = Report::Html.new_from_params(params, fields)
    
    render :inline => report.output, :layout => "currency_report_window"
  end
  
private
  # Our reporting system can't handle both in the same query, geo_level1s and geo_level2s,
  # so that we have to convert the geo_level1s parameters into geo_level2s by
  # retrieving the linked municipalities
  def unify_location_parameters!(params)
    if params.keys.include?("geo_level1s") && params.keys.include?("geo_level2s")
      params["geo_level1s"].each do |l1, v|
        GeoLevel1.find(l1).geo_level2_ids.each do |l2|
          params["geo_level2s"][l2] = 1
        end
      end
      
      params.delete("geo_level1s")
    end
  end
end

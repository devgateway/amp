require 'tile_cache'

class OdamapController < ApplicationController
  
  def index
    render :layout => "odamap"
  end
  
  # Proxy for WxS requests
  def wxs
    wms = TileCache::Services::WMS.new(params)
    map = wms.get_map
    
    render :text => map.data, :content_type => map.layer.format, :layout => false
  end
  
  def query
    province = Province.first(:select => 'id, name', :conditions => ["the_geom && ST_GeomFromText('POINT(? ?)', -1)", params[:lon].to_f, params[:lat].to_f])
    district = District.first(:select => 'id, name', :conditions => ["the_geom && ST_GeomFromText('POINT(? ?)', -1)", params[:lon].to_f, params[:lat].to_f])
          
    render :json => {
      :level1 => province,
      :level2 => district,
      #:level1_geom => res_l1.toWKT,
      #:level2_geom => res_l2.toWKT
    }
  end
  
  def report
    fields = [:factsheet_link, :donor, :donor_project_number, :title, :total_commitments, :total_payments, "total_commitments_#{Time.now.year-1}", "total_payments_#{Time.now.year-1}", "commitments_forecast_#{Time.now.year}", "payments_forecast_#{Time.now.year}", :start, :end]
    
    report = Report::Html.new_from_params(params, fields)
    
    render :inline => report.output, :layout => "currency_report_window"
  end
end
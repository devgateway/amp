require 'tile_cache'

class OdamapController < ApplicationController
  
  def index
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
end
module OdaMap
  class CachedRequest < OdaMap::Base
    require 'digest/md5'
    
    attr_reader :content_type
    
    def initialize(params)
      super()
      
      @params = params
      strip_wxs_params!
      
      @cache = OdaMap::Cache::DiskCache.new(@params)
    end
    
    def data
      @data ||= 
        if @cache.hit?
          @content_type = @cache.content_type
          @cache.get
        else
          request = build_ows_request
          generate_data!(request)
          
          # Save content_type for later access
          @content_type = msIO_stripStdoutBufferContentType
          
          map_data = msIO_getStdoutBufferBytes
          @cache.store!(map_data)
        end
    end
  
  ####  
  private
    def strip_wxs_params!
      %{ controller action }.each do |key|
        @params.delete(key)
      end
    end
    
    # Parse query parameters
    def build_ows_request
      request = OWSRequest.new
      @params.each { |k, v| request.setParameter(k, v) }
      
      request
    end
    
    def generate_data!(request)
      # Buffer output to stdout and dispatch
      msIO_installStdoutToBuffer
      @map.OWSDispatch(request)
    end
  end
end
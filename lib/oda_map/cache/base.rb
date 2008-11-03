module OdaMap
  module Cache
    class Base
      require 'digest/md5'
      
      def initialize(request)
        @key = key_for_request(request)
      end
      
    protected
      def key_for_request(request)
        Digest::MD5.hexdigest(request.to_s)
      end
    end
  end
end
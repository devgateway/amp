class Ruport::AggregationError < RuntimeError; end

class Ruport::Aggregator
  class_inheritable_accessor :ra_providers
  self.ra_providers = {}
  
  class << self
    def provides(field, &block)
      self.ra_providers[field.to_sym] = block
    end
  end
    
  def initialize(options)
    @middleware = [options[:middleware]].flatten.compact
    @fields = options[:fields]
    begin
      @records = find_records(options)
    rescue NoMethodError
      raise Ruport::AggregationError, "Missing `find_records` method for aggregator #{self.class_name}!"
    end
  end
  
  def data
    prepare if self.respond_to?(:prepare)
    
    @table = Table(@fields) do |t|
      @records.each do |r|
        record = {}
        @fields.each { |f| record[f] = get_value(r, f) }
        call_middleware_hook(:process_record, r, record)
        t << record
      end
    end
    
    finalize if self.respond_to?(:finalize)
    @table
  end
  
protected
  def get_value(r, f)
    if self.ra_providers && self.ra_providers[f.to_sym]
      self.ra_providers[f.to_sym].call(r)
    elsif r.respond_to?(f)
      r.send(f)
    else
      raise Ruport::AggregationError, "Failed to obtain data for field #{f}, please specify a provider"
    end
  end

private  
  def call_middleware_hook(name, *args)
    @middleware.each do |mw|
      mw.send(name, *args) if mw.respond_to?(name)
    end
  end
end
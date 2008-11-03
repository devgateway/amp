# (c) 2006 Pascal Ehlert for NativeHost Networks, released under the GPL 2 license

class OrderedHash < Hash
  alias_method :store, :[]=
  alias_method :each_pair, :each
  attr_accessor :keys
  
  def initialize(elements = [])
    @keys = UnifiedArray.new
    elements.each { |k, v| self[k] = v }
  end
  
  def []=(key, val)
    @keys << key
    super
  end
  
  def delete(key)
    @keys.delete(key)
    super
  end
  
  def each
    @keys.each { |k| yield k, self[k] }
  end
  
  def each_key
    @keys.each { |k| yield k }
  end
  
  def each_value
    @keys.each { |k| yield self[k] }
  end
end
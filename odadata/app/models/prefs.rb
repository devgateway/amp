class Prefs < ActiveRecord::Base
  set_primary_key 'key'  
  set_table_name 'settings'
  @memory = {}
  
  def value
    YAML::load(read_attribute(:value))
  end
  
  def value=(new_value)
    write_attribute(:value, new_value.to_yaml)
  end
  
  
  class << self
    
    def method_missing_with_cached_access(method, *args)
      method_name = method.to_s
      
      if method_name =~ /=$/
        clean_method_name = method_name.sub('=', '')
        set_value(clean_method_name, args.first)
      else
        begin
          get_value(method_name)
        rescue ActiveRecord::RecordNotFound
          method_missing_without_cached_access(method, *args)
        end
      end
    end
    alias_method_chain :method_missing, :cached_access
    
  private
    def get_value(key)
      return @memory[key] if @memory.has_key?(key)

      r = find(key)
      @memory[key] = r.value
    end

    def set_value(key, value)
      attrib = find_or_initialize_by_key(key)
      attrib.update_attributes!(:value => value)
      @memory.delete(key)
    end
    
  end
end

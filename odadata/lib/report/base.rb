module Report
  class Base
    attr_reader :fields
     
    def self.new_from_projects(projects, fields)
      obj = allocate
      obj.send(:initialize_from_projects, projects, fields)
      obj
    end
    
    def self.new_from_params(params, fields = nil)
      obj = allocate
      obj.send(:initialize_from_params, params, fields)
      obj
    end
    
    def initialize_from_params(params, fields)
      @params = params
      @fields = fields || parse_query_options
      @proxy = get_proxy_class
    end
    
    def initialize_from_projects(projects, fields)
      @proxy = get_proxy_class
      @fields = fields
      @projects = projects.map { |p| @proxy.new(p) }
    end
    
      
    def projects
      @projects ||= 
        Project.published.ordered.all(
          :include => [:donor, :mdg_relevances, :geo_relevances, :sector_relevances, :fundings], 
          :conditions => build_sql_conditions
        ).map { |p| @proxy.new(p) }
    end
    
    # Builds the report table as an Report::Data::Table object
    def data
      @table ||= Report::Data::Table.new(projects.map { |p| p.build_record(@fields) })
    end
    
    # ===========================
    # = Abstract Output Methods =
    # ===========================
    def output_start
      raise 'Called abstract method: output_start'
    end
    
    def output_head
      raise 'Called abstract method: output_head'
    end
    
    def output_body
      raise 'Called abstract method: output_body'
    end
    
    def output_end
      raise 'Called abstract method: output_end'
    end
    
    def output
      raise 'Called abstract method: output'
    end
    
  protected
    # Parse and order query options from input form
    def parse_query_options
      options = @params[:query_options]
      options.delete_if {|k,v| v.to_i == 0 if v.respond_to?(:to_i)}
      options.sort_by {|k, v| v.to_i}.map { |e| e[0] }
    end
    
    # Parse checkbox results
    def parse_checkboxes(res)
      if res[:all].to_i == 1
        :ignore
      else
        # Delete unchecked fields
        res.delete_if {|k,v| v.to_i != 1}
  
        # Return array of checked fields, if none was set we ignore these options
        res.keys.empty? ? :ignore : res.keys
      end
    end  

    def parsed_params # :nodoc:
      p = []
  
      Report::RELATIONS.each do |keys, handler|
        keys.to_a.each do |key| 
          # Get values from checkboxes.. If there is no input for the relation, ignore.
          values = 
          if @params[key].blank? 
            RAILS_DEFAULT_LOGGER.debug("Report Builder: No input for relation: '#{key}'")
            :ignore 
          else
            parse_checkboxes(@params[key])
          end
          
          p << [key, values, handler]
        end
      end 
  
      return p
    end
    
    # Build conditions for SQL query
    def build_sql_conditions
      conditions = []
      
      parsed_params.each do |key, values, handler|
        next if values == :ignore
        conditions << handler.call(key, values)       
      end
    
      # Merge arrays to find-compatible conditions array
      ActiveRecord::Base.merge_conditions(*conditions)
    end
  
    # Returns related proxy class for this format
    def get_proxy_class
      format = self.class.to_s.demodulize
      Report::Proxy.const_get(format) rescue raise "Can't find appropriate report proxy for format: #{format}" 
    end
  end
end
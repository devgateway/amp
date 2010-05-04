class ActiveRecord::Base
  protected
    def self.construct_count_options_from_args(*args)
      name_and_options = super
      if name_and_options[0].is_a?(String)
        if name_and_options[0].include?('DISTINCT')
          name_and_options[0] = "DISTINCT #{quoted_table_name}.#{primary_key}"
        elsif name_and_options[0].include?("#{table_name}.*")
          name_and_options[0] = " #{quoted_table_name}.#{primary_key}"
        elsif name_and_options[0] =~ /\.\*$/
          name_and_options[0] = '*'
        end
      elsif name_and_options[0].is_a?(Symbol)
        if name_and_options[1].key?(:select)
          if name_and_options[1][:select].include?('DISTINCT')
            name_and_options[1][:select] = "DISTINCT #{quoted_table_name}.#{primary_key}"
          elsif name_and_options[1][:select].include?("#{table_name}.*")
            name_and_options[1][:select] = " #{quoted_table_name}.#{primary_key}"
          elsif name_and_options[1][:select] =~ /\.\*$/
            name_and_options[1][:select] = '*'
          end
        end
      end
      name_and_options
    end
end
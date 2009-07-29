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
      end
      name_and_options
    end
end
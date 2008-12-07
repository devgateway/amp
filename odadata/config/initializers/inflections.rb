ActiveSupport::Inflector.inflections do |inflect|
  inflect.plural      /type_of_aid$/, 'types_of_aid'
  inflect.singular    /types_of_aid$/, 'type_of_aid'
  
  inflect.uncountable %w( consistency_information )
end
module AttributeDecorator
  # Adds reader and writer methods for decorating one or more attributes:
  # <tt>attribute_decorator :date_of_birth</tt> adds <tt>date_of_birth</tt> and <tt>date_of_birth=(new_date_of_birth)</tt> methods.
  #
  # Options are:
  # * <tt>:class_name</tt> - specify the class name of the decorator class.
  # * <tt>:decorates</tt> - specifies the attributes that should be wrapped by the decorator class.
  #   Takes an array of attributes or a single one. If none is specified the same name as the name of the attribute_decorator is assumed.
  #
  # The decorator class should implement a class method called <tt>parse</tt>, which takes 1 argument.
  # In that method your decorator class is responsible for returning an instance of itself with the attribute(s) parsed and assigned.
  #
  # Your decorator class's initialize method should take as it's arguments the attributes that were specified
  # to the <tt>:decorates</tt> option in the same order.
  # You should also implement a #to_a method which should return the parsed values as an array, 
  # again in the same order as specified with the <tt>:decorates</tt> option.
  #
  # If you wish to use <tt>validates_decorator</tt>, your decorator class should also implement a <tt>valid?</tt> instance method,
  # which is responsible for checking the validity of the value(s). See <tt>validates_decorator</tt> for more info.
  #
  #   class CompositeDate
  #     attr_accessor :day, :month, :year
  #     
  #     # Gets the value from Artist#date_of_birth= and will return a CompositeDate instance with the :day, :month and :year attributes set.
  #     def self.parse(value)
  #       day, month, year = value.scan(/(\d\d)-(\d\d)-(\d{4})/).flatten.map { |x| x.to_i }
  #       new(day, month, year)
  #     end
  #     
  #     # Notice that the order of arguments is the same as specified with the :decorates option.
  #     def initialize(day, month, year)
  #       @day, @month, @year = day, month, year
  #     end
  #     
  #     # Here we return the parsed values in the same order as specified with the :decorates option.
  #     def to_a
  #       [@day, @month, @year]
  #     end
  #     
  #     # Returns wether or not this CompositeDate instance is valid.
  #     def valid?
  #       @day != 0 && @month != 0 && @year != 0
  #     end
  #   end
  #
  #   class Artist < ActiveRecord::Base
  #     extend AttributeDecorator
  #     
  #     attribute_decorator :date_of_birth, :class_name => 'CompositeDate', :decorates => [:day, :month, :year]
  #     validates_decorator :date_of_birth, :message => 'is not a valid date'
  #   end
  #
  # Option examples:
  #   attribute_decorator :date_of_birth, :class_name => 'CompositeDate', :decorates => [:day, :month, :year]
  #   attribute_decorator :gps_location, :class_name => 'GPSCoordinator', :decorates => :location
  #   attribute_decorator :balance, :class_name => 'Money'
  def attribute_decorator(attr, options)
    options.assert_valid_keys(:class, :class_name, :decorates)
    
    if options[:decorates].nil?
      options[:decorates] = [attr]
    elsif !options[:decorates].is_a?(Array)
      options[:decorates] = [options[:decorates]]
    end
    
    define_attribute_decorator_reader(attr, options)
    define_attribute_decorator_writer(attr, options)
  end
  
  # Validates wether the decorated attribute is valid by sending the decorator instance the #valid? message.
  #
  #   class CompositeDate
  #     attr_accessor :day, :month, :year
  #     
  #     def self.parse(value)
  #       day, month, year = value.scan(/(\d\d)-(\d\d)-(\d{4})/).flatten.map { |x| x.to_i }
  #       new(day, month, year)
  #     end
  #     
  #     def initialize(day, month, year)
  #       @day, @month, @year = day, month, year
  #     end
  #     
  #     def to_a
  #       [@day, @month, @year]
  #     end
  #     
  #     # Returns wether or not this CompositeDate instance is valid.
  #     def valid?
  #       @day != 0 && @month != 0 && @year != 0
  #     end
  #   end
  #
  #   class Artist < ActiveRecord::Base
  #     extend AttributeDecorator
  #     
  #     attribute_decorator :date_of_birth, :class_name => 'CompositeDate', :decorates => [:day, :month, :year]
  #     validates_decorator :date_of_birth, :message => 'is not a valid date'
  #   end
  #
  #   artist = Artist.new
  #   artist.date_of_birth = '31-12-1999'
  #   artist.valid? # => true
  #   artist.date_of_birth = 'foo-bar-baz'
  #   artist.valid? # => false
  #   artist.errors.on(:date_of_birth) # => "is not a valid date"
  #
  # Configuration options:
  # * <tt>:message</tt> - A custom error message (default is: "is invalid").
  # * <tt>:on</tt> - Specifies when this validation is active (default is <tt>:save</tt>, other options <tt>:create</tt>, <tt>:update</tt>).
  # * <tt>:if</tt> - Specifies a method, proc or string to call to determine if the validation should
  #   occur (e.g. <tt>:if => :allow_validation</tt>, or <tt>:if => Proc.new { |user| user.signup_step > 2 }</tt>).  The
  #   method, proc or string should return or evaluate to a true or false value.
  # * <tt>:unless</tt> - Specifies a method, proc or string to call to determine if the validation should
  #   not occur (e.g. <tt>:unless => :skip_validation</tt>, or <tt>:unless => Proc.new { |user| user.signup_step <= 2 }</tt>).  The
  #   method, proc or string should return or evaluate to a true or false value.
  def validates_decorator(*attrs)
    configuration = { :message => I18n.translate('active_record.error_messages')[:invalid], :on => :save }
    configuration.update attrs.extract_options!
    
    invalid_keys = configuration.keys.select { |key| key == :allow_nil || key == :allow_blank }
    raise ArgumentError, "Unknown key(s): #{ invalid_keys.join(', ') }" unless invalid_keys.empty?
    
    validates_each(attrs, configuration) do |record, attr, value|
      record.errors.add(attr, configuration[:message]) unless record.send(attr).valid?
    end
  end
  
  private
  
  def define_attribute_decorator_reader(attr, options)
    class_eval do
      define_method(attr) do
        (options[:class] ||= options[:class_name].constantize).new(*options[:decorates].map { |attribute| read_attribute(attribute) })
      end
    end
  end
  
  def define_attribute_decorator_writer(attr, options)
    class_eval do
      define_method("#{attr}_before_type_cast") do
        instance_variable_get("@#{attr}_before_type_cast") || send(attr).to_s
      end
      
      define_method("#{attr}=") do |value|
        instance_variable_set("@#{attr}_before_type_cast", value)
        values = (options[:class] ||= options[:class_name].constantize).parse(value).to_a
        options[:decorates].each_with_index { |attribute, index| write_attribute attribute, values[index] }
        value
      end
    end
  end
end
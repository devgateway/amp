require File.expand_path('../test_helper', __FILE__)

describe "AttributeDecorator::attribute_decorator" do
  before do
    AttributeDecorator::Initializer.setup_database
    @artist = Artist.new
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
  end
  
  it "should be defined" do
    Artist.should.respond_to :attribute_decorator
  end
  
  it "should take a name for the decorator and define a reader and writer method for it" do
    %w{ date_of_birth date_of_birth= }.each { |method| @artist.should.respond_to method }
  end
  
  it "should not take any other options than :class_name and :decorates" do
    lambda {
      Artist.class_eval do
        attribute_decorator :foo, :some_other_option => true
      end
    }.should.raise ArgumentError
  end
end

describe "AttributeDecorator, an attribute decorator in general" do
  before do
    AttributeDecorator::Initializer.setup_database
    
    @artist = Artist.create(:day => 31, :month => 12, :year => 1999)
    @decorator = @artist.date_of_birth
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
    Artist.class_eval do
      attribute_decorator :date_of_birth, :class_name => 'CompositeDate', :decorates => [:day, :month, :year]
    end
  end
  
  it "should only use String#constantize once and cache the result" do
    klass_name_string = 'CompositeDate'
    
    Artist.class_eval do
      attribute_decorator :date_of_birth, :class_name => klass_name_string, :decorates => [:day, :month, :year]
    end
    
    klass_name_string.expects(:constantize).times(1).returns(CompositeDate)
    2.times { @artist.date_of_birth }
  end
  
  it "should work with a real pointer to a wrapper class instead of a string as well" do
    Artist.class_eval do
      attribute_decorator :date_of_birth, :class => CompositeDate, :decorates => [:day, :month, :year]
    end
    
    @artist.date_of_birth.to_s.should == "31-12-1999"
  end
  
  it "should also work with an anonymous wrapper class" do
    Artist.class_eval do
      attribute_decorator :date_of_birth, :decorates => [:day, :month, :year], :class => (Class.new(CompositeDate) do
        # Reversed implementation of the super class.
        def to_s
          "#{@year}-#{@month}-#{@day}"
        end
      end)
    end
    
    2.times { @artist.date_of_birth.to_s.should == "1999-12-31" }
  end
end

describe "AttributeDecorator, an attribute decorator for multiple attributes" do
  before do
    AttributeDecorator::Initializer.setup_database
    
    @artist = Artist.create(:day => 31, :month => 12, :year => 1999)
    @decorator = @artist.date_of_birth
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
  end
  
  it "should return a instance of the decorator class specified by the :class_name option" do
    @artist.date_of_birth.should.be.instance_of CompositeDate
  end
  
  it "should have assigned the values, which the attribute decorator decorates, to the decorator instance" do
    @decorator.day.should == 31
    @decorator.month.should == 12
    @decorator.year.should == 1999
  end
  
  it "should return the value before type cast when the value was set with the setter" do
    @artist.date_of_birth = '01-02-2000'
    @artist.date_of_birth_before_type_cast.should == '01-02-2000'
  end
  
  it "should return the value before type cast when the value was just read from the database" do
    date_of_birth_as_string = @artist.date_of_birth.to_s
    @artist.reload
    @artist.date_of_birth_before_type_cast.should == date_of_birth_as_string
  end
  
  it "should parse the value assigned through the setter method" do
    @artist.date_of_birth = '01-02-2000'
    @artist.day.should == 1
    @artist.month.should == 2
    @artist.year.should == 2000
  end
end

describe "AttributeDecorator, an attribute decorator for one attribute" do
  before do
    AttributeDecorator::Initializer.setup_database
    
    @artist = Artist.create(:location => 'amsterdam')
    @decorator = @artist.gps_location
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
  end
  
  it "should return a instance of the decorator class specified by the :class_name option" do
    @artist.gps_location.should.be.instance_of GPSCoordinator
  end
  
  it "should have assigned the values, which the attribute decorator decorates, to the decorator instance" do
    @decorator.location.should == 'amsterdam'
  end
  
  it "should return the value before type cast when the value was set with the setter" do
    @artist.gps_location = 'rotterdam'
    @artist.gps_location_before_type_cast.should == 'rotterdam'
  end
  
  it "should return the value before type cast when the value was just read from the database" do
    gps_location_as_string = @artist.gps_location.to_s
    @artist.reload
    @artist.gps_location_before_type_cast.should == gps_location_as_string
  end
  
  it "should parse the value assigned through the setter method" do
    @artist.gps_location = 'amsterdam'
    @artist.location.should == '+1, +1'
    
    @artist.gps_location = 'rotterdam'
    @artist.location.should == '-1, -1'
  end
end

describe "AttributeDecorator, an attribute decorator for an already existing attribute" do
  before do
    AttributeDecorator::Initializer.setup_database
    
    @artist = Artist.create(:start_year => 1999)
    @decorator = @artist.start_year
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
  end
  
  it "should return a instance of the decorator class specified by the :class option" do
    @artist.start_year.should.be.instance_of Year
  end
  
  it "should have assigned the value, which the attribute decorator decorates, to the decorator instance" do
    @decorator.start_year.should == 1999
  end
  
  it "should store the original value that was passed to the decorator writer method in a foo_before_type_cast instance variable" do
    @artist.start_year = '40 bc'
    @artist.start_year_before_type_cast.should == '40 bc'
  end
  
  it "should send the value that was passed to the decorator writer method to a ::parse class method on the decorator class" do
    @artist.start_year = '40 bc'
    @artist.read_attribute(:start_year).should.be -41
  end
end

describe "AttributeDecorator, a attribute decorator validator" do
  before do
    AttributeDecorator::Initializer.setup_database
    
    @artist = Artist.create(:start_year => 1999)
    @decorator = @artist.start_year
  end
  
  after do
    AttributeDecorator::Initializer.teardown_database
    
    Artist.instance_variable_set(:@validate_callbacks, [])
    Artist.instance_variable_set(:@validate_on_update_callbacks, [])
  end
  
  it "should send #valid? to the decorator to check if the value is valid" do
    Artist.class_eval do
      validates_decorator :date_of_birth, :start_year
    end
    
    @artist.start_year = 40
    @artist.should.be.valid
    
    @artist.start_year = 'abcde'
    @artist.should.not.be.valid
    @artist.errors.on(:start_year).should == "is invalid"
  end
  
  it "should take a options hash to configure the validations more detailed" do
    Artist.class_eval do
      validates_decorator :start_year, :message => 'is not a valid date', :on => :update
    end
    
    artist = Artist.new(:start_year => 'abcde')
    artist.should.be.valid
    
    artist.save!
    artist.should.not.be.valid
    artist.errors.on(:start_year).should == 'is not a valid date'
  end
  
  it "should not take the :allow_nil option" do
    lambda {
      Artist.class_eval do
        validates_decorator :start_year, :allow_nil => true
      end
    }.should.raise ArgumentError
  end
  
  it "should not take the :allow_blank option" do
    lambda {
      Artist.class_eval do
        validates_decorator :start_year, :allow_blank => true
      end
    }.should.raise ArgumentError
  end
end
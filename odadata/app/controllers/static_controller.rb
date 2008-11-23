class StaticController < ApplicationController
  def home
  end
  
  def links
    @donors_with_websites = Donor.main.ordered.delete_if { |d| !d.field_office_website }
  end
  
  def downloads
  end
end

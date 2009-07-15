class PopulateDonorDetails < ActiveRecord::Migration
  def self.up
    Donor.all.each do |donor|
      Donor::FIRST_YEAR_OF_RELEVANCE.upto(Time.now.year) do |y|
      donor_detail = donor.donor_details.find_or_create_by_year(y)
      donor_detail.update_attributes({:total_staff_in_country =>  donor.total_staff_in_country,
                                      :total_expatriate_staff =>  donor.total_expatriate_staff,
                                      :total_local_staff =>  donor.total_local_staff})
      end
    end
  end

  def self.down
    Donor.all.each do |donor|
      donor.update_attributes({:total_staff_in_country =>  donor.donor_details.first.total_staff_in_country,
                                      :total_expatriate_staff =>  donor.donor_details.first.total_expatriate_staff,
                                      :total_local_staff =>  donor.donor_details.first.total_local_staff})
    end

  end
end

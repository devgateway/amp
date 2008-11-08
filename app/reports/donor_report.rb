class DonorReport
  include ReportsHelper
  include I18nHelper
  
  def initialize
    @donors = Donor.main.ordered.all.select { |d| d.projects.published.any? }
  end
  
  def data
    @data ||= @donors.map do |donor|
      {
        ll(:terms, :donor) => donor.name,
        ll(:reports, :no_projects) => donor.projects.published.size,
        ll(:reports, :list) => projects_list_link(donor)
      }
    end
  end
  
  def to_html
    Report::Data::Table.new(data).inspect
  end
end
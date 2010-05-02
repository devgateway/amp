class ExchangeRate < ActiveRecord::Base
  SOURCES = %w(partners sistafe)
  
  # Validation
  validates_presence_of :currency, :euro_rate, :source
  validates_inclusion_of :source, :in => SOURCES
  validates_uniqueness_of :year, :scope => [:source, :currency], :message => I18n.t("exchange_rates.error.year_already_taken")
  validates_numericality_of :euro_rate
  
  class << self
    def find_rate(from, to, year = Time.now.year, source = SOURCES.first)
      find(:first,
        :select => "target.euro_rate / source.euro_rate AS rate, ABS(#{year} - source.year) AS diff",
        :from => 'exchange_rates source, exchange_rates target',
        :conditions => ['source.currency = ? AND target.currency = ? AND target.year = source.year AND source.source = ? AND target.source = ?',
          to, from, source, source],
        :order => 'diff ASC'
      ).try(:rate).try(:to_f) || raise(ActiveRecord::RecordNotFound)
    end
    
    def available_currencies
      find(:all, :select => 'DISTINCT currency', :order => 'currency ASC').map(&:currency)
    end
  
    def available_currencies_for_select
      available_currencies.collect { |c| [c, c] }
    end
  end
end

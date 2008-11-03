require File.dirname(__FILE__) + '/../test_helper'

class SectorAmountTest < Test::Unit::TestCase
  fixtures :sector_amounts
  fixtures :sectors
  fixtures :country_strategies
  fixtures :projects

  # Replace this with your real tests.
  def test_amount
    assert_equal 1000000, CountryStrategy.find(1).strategies_sectors.amount
  end
end

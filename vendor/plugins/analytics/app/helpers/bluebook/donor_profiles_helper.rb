module Bluebook::DonorProfilesHelper
  def in_millions(value, decimals = 2)
    (value.base_value / 1_000_000).round_to(decimals)
  end
end
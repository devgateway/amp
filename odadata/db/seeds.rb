require 'active_record/fixtures'

Dir.glob(Rails.root + "db/fixtures/seeds/*.yml").each do |file|
  Fixtures.create_fixtures("db/fixtures/seeds", File.basename(file, '.*'))
end
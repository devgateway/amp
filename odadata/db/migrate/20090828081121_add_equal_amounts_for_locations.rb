class AddEqualAmountsForLocations < ActiveRecord::Migration
  def self.up
    Project.all.each do |p|
      amount = 100.0 / p.geo_relevances.size
      p.geo_relevances.each do |g|
        g.amount = amount
      end
      
      p.save(false)
    end
  end

  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end

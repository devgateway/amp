class ResetInputStates < ActiveRecord::Migration
  def self.up
    execute "UPDATE projects SET input_state = 'profile_information'"
  end

  def self.down
  end
end

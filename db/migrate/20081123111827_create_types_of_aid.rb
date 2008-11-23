class CreateTypesOfAid < ActiveRecord::Migration
  def self.up
    create_table :types_of_aid do |t|
    
    end
    
    create_table :type_of_aid_translations do |t|
      t.string :locale
      t.string :name
      
      t.references :type_of_aid
    end
    
  end

  def self.down
    drop_table :types_of_aid
    drop_table :type_of_aid_translations
  end
end

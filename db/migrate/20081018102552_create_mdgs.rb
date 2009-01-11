class CreateMdgs < ActiveRecord::Migration
  def self.up
    create_table :mdgs do |t|
      t.string  :name
      t.string  :name_es
      
      t.text    :description
      t.text    :description_es
    end
    
    create_table :targets do |t|   
      t.text  :name
      t.text  :name_es
               
      t.references :mdg
    end
    
    create_table :mdg_relevances do |t|
      t.references :project, :mdg, :target
    end
  end

  def self.down
    drop_table :mdgs
    drop_table :targets
    drop_table :mdg_relevances
  end
end

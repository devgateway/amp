class CreateMdgs < ActiveRecord::Migration
  def self.up
    create_table :mdgs do |t|

    end
    
    create_table :mdg_translations do |t|
      t.string    :locale
      t.string    :name
      t.text      :description    
      
      t.references :mdg
    end
    
    create_table :targets do |t|              
      t.references :mdg
    end
    
    create_table :target_translations do |t|
      t.string    :locale
      t.text      :name
      
      t.references :target
    end
    
    create_table :mdg_relevances do |t|
      t.references :project, :mdg, :target
    end
  end

  def self.down
    drop_table :mdgs
    drop_table :targets
    drop_table :mdg_translations
    drop_table :target_translations
    drop_table :mdg_relevances
  end
end

class ComplexReport < ActiveRecord::Base
  belongs_to :user
  
  def data=(object)
    write_attribute(:data, Marshal.dump(object))
  end
  
  # This deserializes the data and preloads all missing constants because Marshal 
  # doesn't seem to play nicely with the original autoload.
  def data
    Marshal.load(read_attribute(:data))
  rescue ArgumentError => error
    lazy_load ||= Hash.new { |hash, hash_key| hash[hash_key] = true; false }
    if error.to_s[/undefined class/] && !lazy_load[error.to_s.split.last.constantize] then retry
    else raise error end
  end
end

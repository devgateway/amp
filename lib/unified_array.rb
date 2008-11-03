# UnifiedArray stores an ordered set of unique items
# If you add an item to it that already exists, it
# simply ignores it.

class UnifiedArray < Array
  def <<(item)
    super unless self.include?(item)
  end
  
  def insert(pos, item)
    super unless self.include?(item)
  end
  
  # Merges this array with another while trying to preserve the order
  # of the elements as good as possible.
  # [:a, :b, :c].merge([:a, :d, :e, :c, :f]) => [:a, :d, :e, :b, :c, :f]
  def merge(other)
    lastfound_idx = -2 # Will be -1 when inserting, unless changed
    
    other.each do |e|
      if self.include?(e)
        lastfound_idx = self.index(e)
      else
        self.insert(lastfound_idx+1, e)
      end
    end
    
    self
  end
end
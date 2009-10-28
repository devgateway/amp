# All core extensions (c) 2006-2007 Pascal Ehlert for NativeHost Networks and/or ODAdata
# Released under the GPL 2 license

class Integer
  def years_from_now
    Time.now.year..Time.now.year+self 
  end 
end

class Hash
  def extract!(defaults)
    extracted = self.reverse_merge(defaults)
    extracted.delete_if { |k, v| !defaults.include?(k) }
    self.delete_if { |k, v| defaults.include?(k) }
    
    extracted
  end
end

class Time
  def quarter
    (month / 3.0).ceil
  end
end

class Float
  def round_to(x)
     (self * 10**x).round.to_f / 10**x
   end
end

module Enumerable
  def group_by
    inject({}) do |groups, element|
      res = yield(element)
      [res].flatten.each do |key|
        (groups[key] ||= []) << element
      end
      
      groups
    end
  end
end
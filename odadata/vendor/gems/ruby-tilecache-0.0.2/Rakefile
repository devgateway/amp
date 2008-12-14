require 'rubygems'
require 'rake'
require 'echoe'

Echoe.new('ruby-tilecache', '0.0.2') do |p|
  p.description     = "An implementation of TileCache from MetaCarta, written in pure Ruby"
  p.url             = "http://www.odadata.eu/ruby-tilecache"
  p.author          = "Pascal Ehlert"
  p.email           = "pascal.ehlert@odadata.eu"
  p.ignore_pattern  = []
  p.development_dependencies = []
end

Dir["#{File.dirname(__FILE__)}/tasks/*.rake"].sort.each { |ext| load ext }

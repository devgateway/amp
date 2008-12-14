$:.unshift(File.dirname(__FILE__)) unless
  $:.include?(File.dirname(__FILE__)) || $:.include?(File.expand_path(File.dirname(__FILE__)))

require 'tile_cache/bounds'
require 'tile_cache/tile'
require 'tile_cache/meta_tile'
require 'tile_cache/layer'
require 'tile_cache/meta_layer'
require 'tile_cache/config_parser'
require 'tile_cache/services'
  
module TileCache  
  # Exception classes
  class InvalidBounds < StandardError; end
  class InvalidResolution < StandardError; end
  class InvalidConfiguration < StandardError; end
  class CacheError < StandardError; end
  class LayerNotFound < StandardError; end
  
  CONFIG_PATHS = []
  CONFIG_PATHS << File.join(RAILS_ROOT, 'config', 'tilecache.yml') if ENV["RAILS_ENV"]
  CONFIG_PATHS << File.join(ENV["HOME"], ".tilecache.yml")
  CONFIG_PATHS << "/etc/tilecache.yml"
end



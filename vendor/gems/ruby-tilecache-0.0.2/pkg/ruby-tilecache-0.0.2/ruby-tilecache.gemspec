# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{ruby-tilecache}
  s.version = "0.0.2"

  s.required_rubygems_version = Gem::Requirement.new(">= 1.2") if s.respond_to? :required_rubygems_version=
  s.authors = ["Pascal Ehlert"]
  s.date = %q{2008-12-11}
  s.description = %q{An implementation of TileCache from MetaCarta, written in pure Ruby}
  s.email = %q{pascal.ehlert@odadata.eu}
  s.extra_rdoc_files = ["lib/tile_cache/bounds.rb", "lib/tile_cache/caches/disk.rb", "lib/tile_cache/caches.rb", "lib/tile_cache/config_parser.rb", "lib/tile_cache/layer.rb", "lib/tile_cache/layers/map_server.rb", "lib/tile_cache/layers.rb", "lib/tile_cache/meta_layer.rb", "lib/tile_cache/meta_tile.rb", "lib/tile_cache/services/wms.rb", "lib/tile_cache/services.rb", "lib/tile_cache/tile.rb", "lib/tile_cache.rb", "README.rdoc"]
  s.files = ["Changelog", "lib/tile_cache/bounds.rb", "lib/tile_cache/caches/disk.rb", "lib/tile_cache/caches.rb", "lib/tile_cache/config_parser.rb", "lib/tile_cache/layer.rb", "lib/tile_cache/layers/map_server.rb", "lib/tile_cache/layers.rb", "lib/tile_cache/meta_layer.rb", "lib/tile_cache/meta_tile.rb", "lib/tile_cache/services/wms.rb", "lib/tile_cache/services.rb", "lib/tile_cache/tile.rb", "lib/tile_cache.rb", "Rakefile", "README.rdoc", "spec/spec_helper.rb", "spec/tile_cache_spec.rb", "Manifest", "ruby-tilecache.gemspec"]
  s.has_rdoc = true
  s.homepage = %q{http://www.odadata.eu/ruby-tilecache}
  s.rdoc_options = ["--line-numbers", "--inline-source", "--title", "Ruby-tilecache", "--main", "README.rdoc"]
  s.require_paths = ["lib"]
  s.rubyforge_project = %q{ruby-tilecache}
  s.rubygems_version = %q{1.3.1}
  s.summary = %q{An implementation of TileCache from MetaCarta, written in pure Ruby}

  if s.respond_to? :specification_version then
    current_version = Gem::Specification::CURRENT_SPECIFICATION_VERSION
    s.specification_version = 2

    if Gem::Version.new(Gem::RubyGemsVersion) >= Gem::Version.new('1.2.0') then
    else
    end
  else
  end
end

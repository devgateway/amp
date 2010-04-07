require 'set'
require 'diff/lcs'
require 'diff/lcs/hunk'

namespace 'db:fixxtures' do
  desc "Load YAML and Ruby fixtures into the current environment's database."
  task :load => :environment do
    require 'active_record/fixtures'
    
    [ '', RAILS_ENV ].each do |env|
      dir = File.join(RAILS_ROOT, 'db', 'fixtures', env)
      
      files = if file = ENV['FILE']
        [ File.join(dir, file) ]
      else
        Dir.glob(File.join(dir, '*.*')).sort
      end
      
      files.each do |fixture|
        next unless File.exist?(fixture)
        basename = fixture.scan(/([^.\/]+)\.[^.]+/).to_s
        puts(("Reading " + fixture + '.').foreground(:cyan))
        
        if env == '' and not Dir.glob(File.join(dir, RAILS_ENV, basename + '.*')).empty?
          raise 'You cannot have both a global fixture file and an environment-specific fixture file for the same table'
        end
        
        case File.extname(fixture)
          when '.yml'
            same = true
            klass = basename.classify.constantize
            
            existing = klass.all
            existing_attrs = Set.new existing.map(&:attributes)
            yaml = Set.new YAML::load_file(fixture).values
            
            existing_ids = existing_attrs.map { |x| x['id'] }
            yaml_ids = yaml.map { |x| x['id'] }
            
            yaml.each { |hash| hash.values.each { |x| x.gsub!("<%%", "<%") if x.is_a?(String) } }
            to_delete = existing_ids - yaml_ids
            
            unless to_delete.empty?
              same = false
              
              puts "The following records will be deleted...".foreground(:cyan)
              puts ""
              
              existing_attrs.reject! do |hash|
                if to_delete.include? hash['id']
                  puts((" * " + hash.inspect).foreground(:red))
                  true
                else
                  false
                end
              end
              
              puts ""
              print "Is this okay? (y/n) ".foreground(:yellow)
              
              unless STDIN.gets.strip.casecmp('y') == 0
                puts(("Skipping " + fixture + '.').foreground(:cyan))
                next
              end
            end
            
            to_add = yaml_ids - existing_ids
            
            unless to_add.empty?
              same = false
              
              puts "The following records will be added...".foreground(:cyan)
              puts ""
              
              yaml.reject! do |hash|
                if to_add.include? hash['id']
                  puts((" * " + hash.inspect).foreground(:green))
                  true
                else
                  false
                end
              end
              
              puts ""
              print "Is this okay? (y/n) ".foreground(:yellow)
              
              unless STDIN.gets.strip.casecmp('y') == 0
                puts(("Skipping " + fixture + '.').foreground(:cyan))
                next
              end
            end
            
            unless existing_attrs == yaml
              same = false
              
              puts "The following records will be modified...".foreground(:cyan)
              puts ""
              
              yaml = yaml.classify { |x| x['id'] }
              
              existing.each do |record|
                new_hash = yaml[record.id]
                next unless new_hash
                new_hash = new_hash.to_a.first
                old_hash = record.attributes
                next if old_hash == new_hash
                
                old_hash_old = old_hash.dup
                old_hash_large = {}
                new_hash_large = {}
                
                old_hash.reject! do |k,v|
                  v = v.to_s
                  o = new_hash[k].to_s
                  
                  if v.empty? or o.empty? or (v.size < 32 and o.size < 32)
                    false
                  else
                    old_hash_large[k] = v
                    true
                  end
                end
                
                new_hash.reject! do |k,v|
                  v = v.to_s
                  o = old_hash_old[k].to_s
                  
                  if v.empty? or o.empty? or (v.size < 32 and o.size < 32)
                    false
                  else
                    new_hash_large[k] = v
                    true
                  end
                end
                
                puts "Changes for #{record.yaml_key}:".foreground(:cyan)
                
                old_changes = old_hash.reject { |k,v| v == new_hash[k] }
                puts((" - " + old_changes.inspect).foreground(:red)) unless old_changes.empty?
                
                new_changes = new_hash.reject { |k,v| v == old_hash[k] }
                puts((" + " + new_changes.inspect).foreground(:green)) unless new_changes.empty?
                
                puts "" unless old_changes.empty? and new_changes.empty?
                
                old_hash_large.each do |k,v|
                  old_string = v.split("\n").map(&:chomp)
                  new_string = new_hash_large[k].split("\n").map(&:chomp)
                  
                  diffs = Diff::LCS.diff(old_string, new_string)
                  next if diffs.empty?
                  
                  file_length_difference = 0
                  hunk = old_hunk = nil
                  
                  puts "Diff for #{record.yaml_key}/#{k}:".foreground(:cyan)
                  
                  diffs.each do |piece|
                    begin
                      hunk = Diff::LCS::Hunk.new(old_string, new_string, piece, 3, file_length_difference)
                      file_length_difference = hunk.file_length_difference
                      next unless old_hunk
                      
                      if hunk.overlaps?(old_hunk)
                        hunk.unshift(old_hunk)
                      else
                        puts old_hunk.diff(:unified).gsub(/^@@ .+ @@$/) { |x| x.foreground(:magenta) }
                      end
                    ensure
                      old_hunk = hunk
                    end
                  end
                  
                  puts old_hunk.diff(:unified).gsub(/^@@ .+ @@$/) { |x| x.foreground(:magenta) }
                  puts ""
                end
              end
              
              print "Is this okay? (y/n) ".foreground(:yellow)
              
              unless STDIN.gets.strip.casecmp('y') == 0
                puts(("Skipping " + fixture + '.').foreground(:cyan))
                next
              end
            end
            
            unless same
              Fixtures.create_fixtures(dir, basename)
              puts(("Finished " + fixture + '.').foreground(:cyan))
            else
              puts(("Nothing to do for " + fixture + '.').foreground(:cyan))
            end
          when '.rb' then load fixture
        end
      end
    end
  end
  
  desc "Dumps records for the given MODEL into a YAML file."
  task :dump => :environment do
    raise 'No MODEL name given' if ENV['MODEL'].blank?
    klass = ENV['MODEL'].constantize
    file = klass.table_name + '.yml'
    
    File.open(file, 'w') do |f|
      f << klass.all.inject({}) do |hash,record|
        hash.merge(record.yaml_key => record.attributes)
      end.to_yaml.sub("--- ", '').gsub(/^[^ ]+: /, "\n\\0").gsub("<%", "<%%").lstrip
    end
    
    puts "Written " + file + '.'
  end
end
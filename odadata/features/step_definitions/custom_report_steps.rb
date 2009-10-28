Then /^the report table should have the headings "([^\"]*)"$/ do |headings|
  within("table thead") do |content|
    headings.split(', ').each do |h|
      content.should contain("#{h}")
    end
  end
end
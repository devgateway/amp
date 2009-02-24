# A dramatically simpler version than that found in acl_system2
# It is SLOWER because it uses instance_eval to analyse the conditional, but it's DRY.
class Authorization::AccessControlHandler

  # Takes a string (which may be a complex conditional string or a single word as a string
  # or symbol) and checks if the user has those roles
  def process(string, user)
    return(check('', user)) if string.blank?
    if string =~ /^([^()\|&!]+)$/ then check($1, user) # it is simple enough to just pump through
    else instance_eval("!! (#{parse(string)})") # give it the going-over
    end
  end
  
  # Super-simple parsing, turning single or multiple & and | into && and ||. Wraps all the roles
  # in a check call to be evaluated.
  def parse(string)
    string.gsub(/(\|+|\&+)/) { $1[0,1]*2 }.gsub(/([^()|&! ]+)/) { "check('#{$1}', user)" }
  end
  
  # The heart of the system, all credit to Ezra for the original algorithm
  # Defaults to false if there is no user or that user does not have a roles association
  # Defaults to true if the role is blank
  def check(role, user)
    return(false) if user.blank? || !user.respond_to?(:role)
    return(true) if role.blank?
    user.role.title.downcase == role.downcase
  end

end
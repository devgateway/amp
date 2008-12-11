module I18nHelper
  @@scope_chain = []
  
  def l_scope(*scope, &block)
    ActiveSupport::Deprecation.warn("Use proper controller/action based scoping instead of l_scope!")
    @@scope_chain.push(scope)
    begin
      yield
    ensure
      @@scope_chain.pop
    end
  end
  
  def lc(*args)      
    scope = params[:controller].split('/') << normalize_action(params[:action])
    ll(*(scope + args))
  end
  
  def ll(*args)
    options = args.extract_options!
    args_scoped = @@scope_chain.flatten + args
    I18n.t(args_scoped.map(&:to_s).join("."), options)
  end
  
  # Translates options using the app: options: section in the appropriate language file
  def translate_options(options)
    options.map do |option|
      # Prevent the modification of the original object
      option = option.clone
      to_translate = option[1].is_a?(String) ? 1 : 0
      key = option[to_translate].downcase.gsub(/\W+/, "_").to_sym
      option[to_translate] = ll(:options, key)
      
      option
    end
  end
  
protected
  # This returns the get instead of the post actions in case an error occurs in the forms
  # so that we don't need to have translation twice.
  def normalize_action(action)
    case action
    when 'create' then 'new'
    when 'update' then 'edit'
    else action
    end
  end
end
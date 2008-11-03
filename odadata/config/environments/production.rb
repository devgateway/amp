# Settings specified here will take precedence over those in config/environment.rb
#require 'erubis/helpers/rails_helper'

# The production environment is meant for finished, "live" apps.
# Code is not reloaded between requests
config.cache_classes = true

# Use a different logger for distributed setups
# config.logger = SyslogLogger.new

# Full error reports are disabled and caching is turned on
config.action_controller.consider_all_requests_local = false
config.action_controller.perform_caching             = true

# Enable serving of images, stylesheets, and javascripts from an asset server
#config.action_controller.asset_host                  = "http://nic_assets.odadata.eu"

# Disable delivery errors, bad email addresses will be ignored
# config.action_mailer.raise_delivery_errors = false

# Use ActiveRecord session store
# config.action_controller.session_store = :active_record_store

# Suppress newline after erb statements
# ActionView::Base.erb_trim_mode = '>'
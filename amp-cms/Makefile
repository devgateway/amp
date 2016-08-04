# This file can be used to install, update, or destroy an environment.

# To execute a target ('command'), just type `make` followed by the
# name of the command.


# The default target, if no target argument is provided to `make`.
# Update an environment with the latest changes from code.
update:
	@echo 'Running database updates...'
	@drush updatedb --yes

	@echo 'Running update scripts...'
	@drush update-scripts-run --run-all --yes

	@echo 'Revert all enabled feature modules...'
	@drush features-revert-all --yes

	@echo 'Clearing the website cache...'
	@drush cache-clear all

	@echo 'Securing the site settings...'
	@chmod a-w sites/default/settings*.php sites/default


define INFO_TEXT
The environment specific configuration file "sites/default/settings.custom.php"
does not exist. Did you prepare your environment?
  $$ make prepare-production
or
  $$ make prepare-preprod
or
  $$ make prepare-staging
or
  $$ make prepare-local

endef
export INFO_TEXT
# Install a fresh environment.
install:
	@# Make sure the environment is prepared.
	@test -f "sites/default/settings.custom.php" || (echo "$$INFO_TEXT"; exit 8)

	@echo 'Installing a new environment...'
	@echo ' - this might take a couple of minutes.'
	@drush site-install ampbase --yes

	@echo 'Setting up the new environment...'
	@drush update-scripts-run --run-all --yes

	@echo 'Revert all enabled feature modules...'
	@drush features-revert-all --yes

	@echo 'Clearing the website cache...'
	@drush cache-clear all

	@echo 'Securing the site settings.'
	@chmod a-w sites/default/settings*.php sites/default
	@echo ''
	@echo '[DONE] Use oneUser/oneUser for credentials!'

# Prepare environment for local development.
prepare-local:
	@echo 'Creating file: ".htaccess"'
	@test -f .htaccess || cp .htaccess{_sample,}
	@echo 'Creating file: "sites/default/update_scripts/environment/setup.local.php"'
	@test -f sites/default/update_scripts/environment/setup.local.php || \
		cp sites/default/update_scripts/environment/setup.local{_sample,}.php
	@echo 'Creating and updating file: "sites/default/settings.custom.php"'
	@test -f sites/default/settings.custom.php || (\
		chmod ug+w sites/default sites/default/settings*.php && \
		cp sites/default/settings.{sample,custom}.php \
	)
	@sed -i -e "s/define('PROJECT_ENVIRONMENT', '\w\+')/define('PROJECT_ENVIRONMENT', 'local')/" sites/default/settings.custom.php
	@echo 'Creating and updating file: "sites/default/drushrc.custom.php"'
	@test -f sites/default/drush.custom.php || (\
		chmod ug+w sites/default sites/default/drushrc*.php && \
		cp sites/default/drushrc.{sample,custom}.php \
	)
	sed -i -e "s@// \(\$$options\['uri'\].*\)@\1@g" sites/default/drushrc.custom.php

# Prepare environment for staging.
prepare-staging:
	@echo 'Creating file: ".htaccess"'
	@test -f .htaccess || cp .htaccess{_sample,}
	@echo 'Creating and updating file: "sites/default/settings.custom.php"'
	@test -f sites/default/settings.custom.php || \
		chmod ug+w sites/default sites/default/settings*.php && \
		cp sites/default/settings.{sample,custom}.php
	@sed -i -e "s/define('PROJECT_ENVIRONMENT', '\w\+')/define('PROJECT_ENVIRONMENT', 'staging')/" sites/default/settings.custom.php

# Prepare environment for preprod.
prepare-preprod:
	@echo 'Creating file: ".htaccess"'
	@test -f .htaccess || cp .htaccess{_sample,}
	@echo 'Creating and updating file: "sites/default/settings.custom.php"'
	@test -f sites/default/settings.custom.php || \
		chmod ug+w sites/default sites/default/settings*.php && \
		cp sites/default/settings.{sample,custom}.php
	@sed -i -e "s/define('PROJECT_ENVIRONMENT', '\w\+')/define('PROJECT_ENVIRONMENT', 'preprod')/" sites/default/settings.custom.php

# Prepare environment for production.
prepare-production:
	@echo 'Creating file: ".htaccess"'
	@test -f .htaccess || cp .htaccess{_sample,}
	@echo 'Creating and updating file: "sites/default/settings.custom.php"'
	@test -f sites/default/settings.custom.php || \
		chmod ug+w sites/default sites/default/settings*.php && \
		cp sites/default/settings.{sample,custom}.php
	@sed -i -e "s/define('PROJECT_ENVIRONMENT', '\w\+')/define('PROJECT_ENVIRONMENT', 'production')/" sites/default/settings.custom.php


# Remove environment specific files.
clean:
	@echo 'Removing environment specific files.'
	@chmod ug+w sites/default
	@test -f sites/default/settings.custom.php && \
		chmod ug+w sites/default/settings.custom.php || \
		true
	@rm -f \
		.htaccess \
		sites/default/drushrc.custom.php \
		sites/default/settings.custom.php \
		sites/default/update_scripts/environment/setup.local.php


# Clear the environment.
destroy:
	@# Make sure the environment is prepared.
	@test -f "sites/default/settings.custom.php" || (echo "No settings.custom.php file."; exit 16)

	@echo -e '\E[33m'"[WARNING] This will ERASE the database and the user uploaded files."'\033[0m'
	@read -p 'Press [Enter] key to continue or CTRL+C to cancel...'
	@echo 'Erasing the database content...'
	@drush sql-drop --yes
	@echo 'Fixing permissions...'
	@chmod +w sites/default sites/default/settings.php
	@echo 'Erasing the user uploaded files...'
	@rm -rf sites/default/files/*


# Clear the site cache.
cc:
	@echo 'Clearing the website cache...'
	@drush cache-clear all


# Clear generated and temporary files.
cf:
	@echo 'Clearing generated and temporary files...'
	@rm -rf \
		sites/default/files/css \
		sites/default/files/ctools \
		sites/default/files/js \
		sites/default/files/languages \
		sites/default/files/less \
		sites/default/files/styles \
		sites/default/files/temp-downloads


# Copy media module icons to the site files directory.
fm:
	@echo 'Copying media module icons to the site files directory.'
	@drush php-eval "module_load_include('install', 'media'); _media_install_copy_icons();"


# Bring the site offline.
offline:
	@echo 'Bringing the site offline.'
	@drush variable-set -y --always-set maintenance_mode 1


# Bring the site online.
online:
	@echo 'Bringing the site online.'
	@drush variable-set -y --always-set maintenance_mode 0

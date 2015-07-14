
update:
	@chmod +x ./drush/drush

	@echo "Running database updates..."
	@./drush/drush updatedb --yes

	@echo "Running update scripts..."
	@./drush/drush update-scripts-run --run-all --yes

	@echo "Revert all enabled feature modules..."
	@./drush/drush features-revert-all --yes

	@echo "Clearing the website cache..."
	@./drush/drush cache-clear all


cc:
	@chmod +x ./drush/drush

	@echo "Clearing the website cache..."
	@./drush/drush cache-clear all


# Generic Environment Setup Instructions

> NOTE: Make sure you update the commands for your environment. These are defaults for local development environments.

## AMP Environment Setup Instructions:

0.  You first need a database dump.

1.  Go to the project folder:
    ```
    # For local environments:
    cd /opt/devgateway/amp212-local/
    ```

2.  Import a database dump:
    ```
    # If the backup was made using the amp backup scripts in PGSQL format, use:
    pg_restore --verbose --clean --no-owner --no-privileges --username amp_user --dbname amp_local amp.pgsql
    # or
    zcat amp.pgsql.gz | pg_restore --verbose --clean --no-owner --no-privileges --username amp_user --dbname amp_local
    ```

3.  Prepare the database:
    ```
    psql --username amp_user --dbname amp_local

    # Prepare the DB for the local environment:
    # TODO: Add instructions on how to generate a new password.
    => UPDATE dg_site_domain SET site_domain='amp212.local';
    => UPDATE dg_user SET password='a9993e364706816aba3e25717850c26c9cd0d89d';
    => UPDATE amp_global_settings SET settingsvalue = 'false' WHERE settingsname = 'Secure Server';
    ```

3.  Build the environment:
    ```
    # TODO: Improve the script to accept DB credentials via a settings file or CLI.
    amp-build
    ```
    NOTE: This might take a while! You can add `time` before the command name to see the execution time, it should take
          about 14 minutes the first time and 5m after that.

4.  Restart tomcat:
    ```
    sudo systemctl restart tomcat7.service
    ```

4.  Check if the project has been installed properly, go to: [amp212.local/login](http://amp212.local/login)
    NOTE:
    > The domain root redirects to `/portal`.


## CMS Environment Setup Instructions:

1.  Go to the project folder:
    ```
    # For local environments:
    cd /var/www/amp212-cms-local/
    ```

2.  Prepare drupal for installation, run these inside the VM in the project folder:
    *   Prepare the environment by running one of the following commands for your environment:
        ```
        # Prepare environment for local development.
        make prepare-local

        # Prepare environment for staging.
        make prepare-staging

        # Prepare environment for preprod.
        make prepare-preprod

        # Prepare environment for production.
        make prepare-production
        ```
        > NOTE: For staging, preprod and production environments you will need to update the database credentials in
        >       `settings.custom.php`.

    *   Check the database connection settings of the project:
        ```
        drush sqlc
        ```
        You should see the DB prompt with the project database selected. To quit, just execute `\q`.

    *   For local development environments copy the sample update script:
        ```
        cp sites/default/update_scripts/environment/setup.local{_sample,}.php
        ```

3.  Install or import an Environment:
    *   If you want a new and clean `amp-cms` installation run:
        ```
        make install
        ```
        > NOTE: This might take a while! You can add `time` before the command name to see the execution time, it should
                take about 3 minutes.

    *   Alternatively, **IF** you have a database dump, you can import it using:
        ```
        # If the backup was made using `drush dump`:
        zcat DUMP.sql.gz | drush sqlc

        # If the backup was made using the amp backup scripts in PGSQL format.
        pg_restore --verbose --clean --no-owner --no-privileges --username amp_cms_user --dbname amp_cms_local amp-cms.pgsql
        # or
        zcat amp-cms.pgsql.gz | pg_restore --verbose --clean --no-owner --no-privileges --username amp_cms_user --dbname amp_cms_local

        # Make sure to update the project settings for the current environment.
        make update

        # You can generate a one-time-login link for the main Drupal use and you can change the password from the UI:
        drush user-login
        # or you can change the password from the CLI by running:
        drush user-password oneUser
        ```

4.  Check if the project has been installed properly, go to: [amp212.local/portal](http://amp212.local/portal)

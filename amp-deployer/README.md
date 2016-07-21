# Aid Management Platform Deployer


## Requirements

Please see the **happy-deployer** requirements and setup instructions: https://github.com/devgateway/happy-deployer

## HOST: Environment Setup Instructions:

1.  Clone the repo:
    ```
    mkdir -p /workspace/amp211/
    cd /workspace/amp211/
    svn checkout https://svn.dgfoundation.org/amp/branches/AMP_2_12_RELEASE/ .
    ```
    NOTE: You can provide `--username xxx` if your current user name is not the same as the SVG user.

2.  Build the VM and install all required software.
    ```
    cd /workspace/amp211/amp-deployer/
    vagrant up --provision
    ```
    NOTES:

    *   This might take a while! You can add `time` before the command name to see the execution time, it should take about 10 minutes.
        ```
        time vagrant up --provision
        ```

    *   Ignore this message, it just means your computer is slow :p
        ```
        default: Error: Remote connection disconnect. Retrying...
        ```

    *   This one means that you don't have any VirtualBox Guest Additions in the VM, and that you should have patience.
        ```
        No installation found.
        ```

4.  Make sure that your `/etc/hosts` file has been updated and contains the following entries:
    ```
    10.10.10.212  amp.local  # ...
    ```

5.  You can now SSH into the VM.
    ```
    vagrant ssh
    ```
    NOTE: On windows you can run `vagrant ssh-config` to view the information required for [PuTTY](http://www.chiark.greenend.org.uk/~sgtatham/putty/).

6.  For now the visualisations can be be access on [amp.local](http://amp.local/).
    To setup the Drupal project continue with the next set of instructions.


## GUEST: Environment Setup Instructions:

1.  Once inside the VM, run to go into the project folder:
    ```
    cd /var/www/amp212-cms-local/
    ```
    For a full list of aliases, just run the `alias` command.

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
        For staging, preprod and production environments you will need to update the database credentials in `settings.custom.php`.

    *   Check the database connection settings of the project:
        ```
        drush sqlc
        ```
        You should see the DB prompt with the project database selected. To quit, just execute `\q`.

    *   For local development environments copy the sample update script:
        ```
        cp sites/default/update_scripts/environment/setup.local{_sample,}.php
        ```
        Please note that other `setup.*.php` files exist for other environments.

3.  Install the project.
    *   If you want a new and clean installation run:
        ```
        make install
        ```

    *   Alternatively, **IF** you have a database dump, you can import it using:
        ```
        zcat DUMP.sql.gz | drush sqlc
        # Make sure to update the project settings for the current environment.
        make
        ```

4.  Check if the project has been installed properly, for local development go to: [amp.local](http://amp.local/)


### Install ansible on a Enterprise Linux 7 host
```
make bootstrap-el7
```

### Provision a Vagrant VM using the extra vagrant files
```
ansible-playbook --extra-vars="@Vagrantsettings.yml" main.yml
# OR:
ansible-playbook --extra-vars="@Vagrantsettings.local.yml" main.yml
```

### Provision remote preprod hosts
```
ansible-playbook --inventory="inventory.conf" --extra-vars="hosts=preprod" main.yml
```

### Discover information from systems
see: http://docs.ansible.com/ansible/playbooks_variables.html#information-discovered-from-systems-facts
```
ansible all --inventory inventory.conf -m setup
ansible preprod --inventory inventory.conf -m setup
```


# Setting up local development environments

## OS Setup Instructions

Follow these steps for configuring a VM using Vagrant for local development environments:


0.  Please see the **happy-deployer** requirements and setup instructions: https://github.com/devgateway/happy-deployer

1.  Clone the repo:
    ```
    mkdir -p /workspace/amp212/
    cd /workspace/amp212/
    git clone --branch=develop git@github.com:devgateway/amp.git .
    ```

2.  Build the VM and install all required software:
    ```
    cd /workspace/amp212/amp-deployer/
    vagrant up --provision
    ```
    > NOTES:
    > * This might take a while! You can add `time` before the command name to see the execution time, it should take
        about 13 minutes.
        ```
        time vagrant up --provision
        ```

    > * Ignore this message, it just means your computer is slow :p
        ```
        default: Error: Remote connection disconnect. Retrying...
        ```

4.  Make sure that your `/etc/hosts` file has been updated and contains the following entries:
    ```
    10.10.10.212  amp.local  # VAGRANT: ...
    ```

5.  You can now SSH into the VM:
    ```
    vagrant ssh
    ```
    > NOTE: On windows you can run `vagrant ssh-config` to view the information required for
    >       [PuTTY](http://www.chiark.greenend.org.uk/~sgtatham/putty/).

5.  See the [Generic Environment Setup Instructions](setup-generic.md) sections for how to the setup AMP or/and
    AMP CMS (Public Portal) environments.

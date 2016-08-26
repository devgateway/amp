
# Setting up production, preprod or testing environments

## OS Setup Instructions

Follow these steps for configuring a server for testing (staging, preprod) or production environments:

1.  Clone the repo:
    ```
    mkdir -p /opt/devgateway/amp212/
    cd /opt/devgateway/amp212/
    git clone --branch=develop git@github.com:devgateway/amp.git .
    ```

2.  Install and configure ansible:
    ```
    make bootstrap-el7
    ```

3.  Prepare a set of variables for the environment
    ```
    # Create an environment file
    cp amp-deployer/environments/sample.yml amp-deployer/environments/TLS-staging.yml
    # or
    cp amp-deployer/environments/sample.yml amp-deployer/environments/MDA-preprod.yml

    # Then update the files to add new DBs, VirtualHost settings and so on...
    # TODO: Add instructions on how to configure environment files.
    ```

4.  Provision the environment:
    ```
    make provision ENVIRONMENT=TLS-staging
    # or
    make provision ENVIRONMENT=MDA-preprod
    ```

5.  See the [Generic Environment Setup Instructions](setup-generic.md) sections for how to the setup AMP or/and
    AMP CMS (Public Portal) environments.

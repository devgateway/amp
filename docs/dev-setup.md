---
layout: page
title: Dev Setup
permalink: /dev-setup/
---

## Local Development Setup

This page describes how to setup a local development environment for the Aid Management Platform.

### Prerequisites

- [Java 8](https://www.oracle.com/java/technologies/downloads/#java8)
- [PostgreSQL 14](https://www.postgresql.org/download/)
- [PosGIS](https://postgis.net/install/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- [Node.JS 16](https://nodejs.org/dist/v16.9.1/) (Or install it using your package manager of choice)
- [Apache Tomcat 8](https://tomcat.apache.org/download-80.cgi)

### Setup

#### 1. Clone the repository and initial setup

- You can clone the repository using the following command:

```bash
git clone git@github.com:devgateway/amp.git
```

 - or  using IntelliJ IDEA as shown below:

```bash
File -> New -> Project from Version Control -> Git
```

- another method using IntelliJ IDEA is shown below:

|   ![Select Git](/assets/img/Intellij_choose_VCS.png)  | ![Clone amp repository using Intellij](/assets/img/Intellij_clone_repo.png)     |

then you can choose the directory where you want to clone the repository, paste the repository URL and click on the `Clone` button.

- Wait from the cloning process to finish.
  
- Alternatively, you can clone the repository using the `git clone` command

- After cloning the repository, you need to select the `amp` directory in the cloned repository and open it as a project in IntelliJ IDEA.

- You will be prompted to import it as an Eclipse or Maven project. Select `Maven` and click on `OK`.

[![Import amp as a Maven project](/assets/img/Intellij_import_maven.png)](/assets/img/Intellij_import_maven.png)

- Wait for the project dependencies to sync and Intellij to finish indexing the project.
  
#### 2. Setup PostgreSQL

After installing PostgreSQL, you need to create a database and a user for the AMP application.

You can do this by running the following commands:

#### 2.1 If you are using Unix/Linux or MacOS

- Connect to the PostgreSQL server using the `postgres` user:

```bash
sudo -u postgres psql
```

- Create a database role `amp`

```sql
CREATE ROLE 'amp' NOLOGIN SUPERUSER CREATEDB CREATEROLE INHERIT NOREPLICATION CONNECTION LIMIT -1;
```

- Create a database `amp_<country_name>_<major_version>` owned by the `amp` user

```sql
CREATE DATABASE 'amp_<country_name>_<major_version>' OWNER amp; 
```

#### 3. Setup PosGIS

After installing Postgres, you need to install the PostGIS extension.

You can do this by running the following command:

```bash
CREATE EXTENSION postgis;
```


#### 2.2 Restoring the database from a backup

- If it's an archive – extract it. (for .7z – 7za e `<filename.7z>` ; for .tar.gz – tar xzvf `<filename.tar.gz>` ; for .tar.bz2  – tar xjvf `<filename.tar.bz2>`)
- Move it to a location accessible to the postgres user (e.g. /tmp is a good place).
- Change to the user postgres.
- If it is a text dump, run the following command:

```bash
psql -U amp -d db_name -f /tmp/<filename.sql>
```

- If it is a binary dump, run the following command:

```bash
pg_restore -U amp -d db_name /tmp/<filename.dump>
```

- Wait for the restore to complete.
- Run the following command to update the site domain

``` sql
UPDATE dg_site_domain SET site_domain = 'localhost';
```

##### 2.3 If you are using Windows

- Open the `pgAdmin` application
- Right click on `Servers` and select `Create -> Server`
- Enter a name for the server and click `Save`
- Expand the server and right click on `Databases` and select `Create -> Database`
- Enter a name for the database and click `Save`
- Expand the database and right click on `Schemas` and select `Create -> Schema`
- Enter a name for the schema and click `Save`
- Expand the schema and right click on `Tables` and select `Create -> Table`
- Enter a name for the table and click `Save`

#### 4. Setup Apache Tomcat

- Download the `apache-tomcat-8.5.65.zip` file from [here](https://tomcat.apache.org/download-80.cgi)
- Extract the `apache-tomcat-8.5.65.zip` file.
- Place the extracted folder in a location of your choice that can be accessed by IntelliJ IDEA.

#### 5. Setup IntelliJ IDEA

After importing the project into IntelliJ IDEA, you need to configure the project to use the database, maven and Tomcat server.

You can follow the steps below to setup the project:

##### 5.1 Configure Maven

- Open the `amp` project in IntelliJ IDEA
- Click on the `Choose File` menu on the top right corner as shown below:
  
  [![Click Configuration Button](/assets/img/Intellij_configuration_button.png)](/assets/img/Intellij_configuration_button.png)

- Click on the `Edit Configurations...` option as shown below:
  
  [![Click Edit Configurations](/assets/img/Intellij_edit_configurations.png)](/assets/img/Intellij_edit_configurations.png)

- Click on the `+` button and select `Maven`
- Enter a name prefarably `amp` for the configuration
- Add the following to the maven run configuration:
  
  ```bash
    clean compile war:exploded -DserverName=local -Djdbc.db=<db_name> -Djdbc.user=<db_username> -Djdbc.password=<db_password> -Djdbc.port=5432
    ```

    [![Add Maven Run Configuration](/assets/img/Intellij_maven_run_configuration.png)](/assets/img/Intellij_maven_run_configuration.png)

- For concurrent builds, you can add the following flag to skip npm builds

    ```bash
    -Dskip.npm=true
        ```

- Click `OK` to save the configuration
- Click on the Run Button on the top right corner as shown below or press `Shift + F10` on Windows or `⌃R` on Mac:
  
  [![Click Run Button](/assets/img/Intellij_run_button.png)](/assets/img/Intellij_run_button.png)

- Wait for maven to finish building the project
  
##### 5.2 Configure Tomcat

- Click on the `Edit Configurations...` option as shown below:
  
  [![Click Edit Configurations](/assets/img/Intellij_edit_configurations.png)](/assets/img/Intellij_edit_configurations.png)

- Click on the `+` button and select `Tomcat Server -> Local`. You will see the following screen:
  
  [![Add Tomcat Server](/assets/img/Intellij_tomcat_configuration.png)](/assets/img/Intellij_tomcat_configuration.png)

- Add the following to the VM Options

  ```bash
  -Djava.awt.headless=true -Dsun.net.client.defaultConnectTimeout=30000 -Dsun.net.client.defaultReadTimeout=30000
    ```

- Select `Update classes and resources` for the `On frame deactivation` option
- Select `Update classes and resources` for the `On update action` option

- Click on the configure button next to the `Application Server` field and select the folder where tomcat is located folder as shown below:
- Click on the `+` button to select the tomcat folder

| [![Add Tomcat Server](/assets/img/Intellij_add_tomcat.png)](/assets/img/Intellij_add_tomcat.png) | [![Select Tomcat Server](/assets/img/Intellij_select_tomcat_server.png)](/assets/img/Intellij_select_tomcat_server.png) |

- Click `OK` to save the configuration

- Under the `Deployment` tab, click on the `+` button and select `Artifact...` as shown below:
  
  [![Add Artifact](/assets/img/Intellij_add_artifact.png)](/assets/img/Intellij_add_artifact.png)

- Select the `amp:war exploded` artifact and click `OK` as shown below:
  
  [![Select Artifact](/assets/img/Intellij_select_artifact.png)](/assets/img/Intellij_select_artifact.png)

- Set the Application context to `/` as shown below:
  
  [![Set Application Context](/assets/img/Intellij_set_application_context.png)](/assets/img/Intellij_set_application_context.png)

- Click `OK` to save the configuration

#### 7. Running the application

You can run the application by following the steps below:

- Open the `amp` project in IntelliJ IDEA
- Click on the `Run` menu
- Run the application using the Tomcat configuration you created earlier
- Click the run button and wait for the application to start
- You will be able to access the application at `http://localhost:8080/` where you will see the login page as shown below:
  
  [![Login Page](/assets/img/AMP_login_page.png)](/assets/img/AMP_login_page.png)

- You can login using the following credentials for normal users:

  ```bash
    Username: atl@amp.org
    Password: your_atl_password
    ```

- You can login using the following credentials for admin users:
  
  ```bash
    Username: admin@amp.org
    Password: your_admin_password
    ```
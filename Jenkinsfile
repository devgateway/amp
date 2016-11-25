#!groovy

// Important: What is BRANCH_NAME?
// It is branch name for builds triggered from branches.
// It is PR-<pr-id> for builds triggered from pull requests.
def tag = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9_-]/, "-").toLowerCase()

def branch = env.CHANGE_ID == null ? BRANCH_NAME : null
def pr = env.CHANGE_ID

println "Branch: ${branch}"
println "Pull request: ${pr}"
println "Tag: ${tag}"

def codeVersion
def dbVersion

stage('Build') {
    node {
        checkout scm
        withEnv(["PATH+MAVEN=${tool 'M339'}/bin"]) {

            // Build AMP
            sh "cd amp && mvn -T 4 clean compile war:exploded -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver -Dmaven.test.skip=true -Dapidocs=true -DbuildVersion=AMP -DbuildSource=${tag} -e"

            // Find AMP version
            codeVersion = (readFile('amp/TEMPLATE/ampTemplate/site-config.xml') =~ /(?s).*<\!ENTITY ampVersion "([\d\.]+)">.*/)[0][1]

            // Build Docker images & push it
            sh "docker build -q -t localhost:5000/amp-webapp:${tag} --build-arg AMP_EXPLODED_WAR=target/amp-AMP --build-arg AMP_PULL_REQUEST='${pr}' --build-arg AMP_BRANCH='${branch}' amp"
            sh "docker push localhost:5000/amp-webapp:${tag} > /dev/null"

            // Cleanup after Docker & Maven
            sh "docker rmi localhost:5000/amp-webapp:${tag}"
            sh "cd amp && mvn clean -Djdbc.db=dummy"
        }
    }
}

def deployed = false
def country

// If this stage fails then next stage will retry deployment. Otherwise next stage will be skipped.
stage('Deploy') {

    // Find list of countries which have database dumps compatible with ${codeVersion}
    def countries
    node {
        countries = sh(returnStdout: true, script: "ssh sulfur 'cd /opt/amp_dbs && amp-db ls ${codeVersion}'").trim()
        if (countries == "") {
            println "There are no database backups compatible with ${codeVersion}"
            currentBuild.result = 'FAILURE'
        }
    }

    timeout(time: 3, unit: 'DAYS') {
        milestone()
        country = input message: "Proceed with deploy?", parameters: [choice(choices: countries, name: 'country')]
        milestone()
    }

    node {
        try {
            // Find latest database version compatible with ${codeVersion}
            dbVersion = sh(returnStdout: true, script: "ssh sulfur 'cd /opt/amp_dbs && amp-db find ${codeVersion} ${country}'").trim()

            // Deploy AMP
            sh "ssh sulfur 'cd /opt/docker/amp && ./up.sh ${tag} ${country} ${dbVersion}'"

            deployed = true
        } catch (e) {
            currentBuild.result = 'UNSTABLE'
        }
    }
}

// Retry deploy with the same country.
stage('Deploy again') {
    if (deployed) {
        println 'Already deployed, skipping this step.'
    } else {
        timeout(time: 7, unit: 'DAYS') {
            milestone()
            input message: "Proceed with repeated deploy for ${country}?"
            milestone()
        }
        node {
            sh "ssh sulfur 'cd /opt/docker/amp && ./up.sh ${tag} ${country} ${dbVersion}'"
            currentBuild.result = 'SUCCESS'
        }
    }
}

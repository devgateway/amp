#!groovy

// Important: What is BRANCH_NAME?
// It is branch name for builds triggered from branches.
// It is PR-<pr-id> for builds triggered from pull requests.
def tag
if (BRANCH_NAME ==~ /feature\/AMP-\d+.*/) {
    def jiraId = (BRANCH_NAME =~ /feature\/AMP-(\d+).*/)[0][1]
    tag = "feature-${jiraId}"
} else {
    tag = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9_-]/, "-").toLowerCase()
}

// Record original branch or pull request for cleanup jobs
def branch = env.CHANGE_ID == null ? BRANCH_NAME : null
def pr = env.CHANGE_ID
def registryKey = env.AMP_REGISTRY_PRIVATE_KEY
def changePretty = (pr != null) ? "pull request ${pr}" : "branch ${branch}"

println "Branch: ${branch}"
println "Pull request: ${pr}"
println "Tag: ${tag}"

def dbVersion
def country
def ampUrl
def dockerRepo = "registry.developmentgateway.org/"

def updateGitHubCommitStatus(context, message, state) {
    repoUrl = sh(returnStdout: true, script: "git config --get remote.origin.url").trim()
    lastAuthor = sh(returnStdout: true, script: "git log --pretty=%an -n 1").trim()
    ref = lastAuthor.equals("Jenkins") ? "HEAD~1" : "HEAD"
    commitSha = sh(returnStdout: true, script: "git rev-parse ${ref}").trim()

    step([
        $class: 'GitHubCommitStatusSetter',
        reposSource: [$class: "ManuallyEnteredRepositorySource", url: repoUrl],
        commitShaSource: [$class: "ManuallyEnteredShaSource", sha: commitSha],
        contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
        statusBackrefSource: [$class: "ManuallyEnteredBackrefSource", backref: "${BUILD_URL}"],
        errorHandlers: [[$class: 'ShallowAnyErrorHandler']],
        statusResultSource: [
            $class: "ConditionalStatusResultSource",
            results: [[$class: "AnyBuildResult", message: message, state: state]]
        ]
    ])
}

// Run checkstyle only for PR builds
stage('Checkstyle') {
    node {
//         if (branch == null) {
            try {
                checkout scm

                updateGitHubCommitStatus('jenkins/checkstyle', 'Checkstyle in progress', 'PENDING')

                docker.image('maven:3.8.4-jdk-8').inside("-e HOME=${env.WORKSPACE}") {
                    sh "cd amp && mvn -B inccheckstyle:check -DbaseBranch=remotes/origin/${CHANGE_TARGET}"
                }

                updateGitHubCommitStatus('jenkins/checkstyle', 'Checkstyle success', 'SUCCESS')
            } catch(e) {
                updateGitHubCommitStatus('jenkins/checkstyle', 'Checkstyle found violations', 'ERROR')
            }
//         }
    }
}

def legacyMvnOptions = "-Djdbc.user=amp " +
        "-Djdbc.password=amp122006 " +
        "-Djdbc.db=amp " +
        "-Djdbc.host=db " +
        "-Djdbc.port=5432 " +
        "-DdbName=postgresql " +
        "-Djdbc.driverClassName=org.postgresql.Driver"

def launchedByUser = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause').size() > 0
def codeVersion
def countries

// Run fail fast tests
stage('Quick Test') {
    // Find list of countries which have database dumps compatible with ${codeVersion}
    node {
        checkout scm

        // Find AMP version
        codeVersion = readMavenPom(file: 'amp/pom.xml').version
        println "AMP Version: ${codeVersion}"

        countries = sh(returnStdout: true,
                script: "ssh boad.aws.devgateway.org 'cd /opt/amp_dbs && amp-db ls ${codeVersion} | sort'")
                .trim()
        if (countries == "") {
            println "There are no database backups compatible with ${codeVersion}"
            currentBuild.result = 'FAILURE'
        }
    }

    // Allow user to specify country before tests are run
    if (launchedByUser) {
        timeout(15) {
            milestone()
            country = input(
                    message: "Proceed with test, build and deploy?",
                    parameters: [choice(choices: countries, name: 'country')])
            milestone()
        }
    }

    node {
        try {
            checkout scm

            updateGitHubCommitStatus('jenkins/failfasttests', 'Testing in progress', 'PENDING')

            def testStatus = 1
            docker.image('maven:3.8.4-jdk-8').inside("-e HOME=${env.WORKSPACE}") {
                testStatus = sh returnStatus: true, script: "cd amp && mvn -B clean test -Dskip.npm -Dskip.gulp ${legacyMvnOptions}"
            }

            // Archive unit test report
            junit 'amp/target/surefire-reports/TEST-*.xml'

            if (testStatus != 0) {
                error "Tests command returned an error code!"
            }

            updateGitHubCommitStatus('jenkins/failfasttests', 'Fail fast tests: success', 'SUCCESS')
        } catch (e) {
            updateGitHubCommitStatus('jenkins/failfasttests', 'Fail fast tests: error', 'ERROR')

            throw e
        }
    }
}

stage('Build') {

    if (country == null) {
        timeout(15) {
            milestone()
            country = input(
                    message: "Proceed with build and deploy?",
                    parameters: [choice(choices: countries, name: 'country')])
            milestone()
        }
    }

    ampUrl = "http://amp-${country}-${tag}.stg.ampsite.net/"

    node {
        checkout scm

        def image = "${dockerRepo}amp-webapp:${tag}"
        def format = branch != null ? "%H" : "%P"
        def hash = sh(returnStdout: true, script: "git log --pretty=${format} -n 1").trim()
        sh(returnStatus: true, script: "docker pull ${image} > /dev/null")
        def imageIds = sh(returnStdout: true, script: "docker images -q -f \"label=git-hash=${hash}\"").trim()
        sh(returnStatus: true, script: "docker rmi ${image} > /dev/null")

        if (imageIds.equals("")) {
            try {
//                 sh returnStatus: true, script: 'tar -xf /var/amp-node-cache.tar'

                // Build AMP
                withEnv(['DOCKER_BUILDKIT=1']) {
                    sshagent (credentials: ['GitHubDgReadOnlyKey']) {
                        docker.build('maven-3.8.4-jdk-8', '--ssh=default ./maven').inside {
                            sh "cd amp && mvn -B -T 4 clean compile war:exploded ${legacyMvnOptions} -DskipTests -DbuildSource=${tag} -e"
                        }
                    }
                }

                // Build Docker images & push it
                sh "docker build -q -t ${image} --build-arg AMP_EXPLODED_WAR=target/amp --build-arg AMP_PULL_REQUEST='${pr}' --build-arg AMP_BRANCH='${branch}' --build-arg AMP_REGISTRY_PRIVATE_KEY='${registryKey}' --label git-hash='${hash}' amp"
                sh "docker push ${image} > /dev/null"
            } finally {

                sh "cat ${env.WORKSPACE}/.npm/_logs/*"

                // Cleanup after Docker & Maven
                sh returnStatus: true, script: "docker rmi ${image}"

                docker.image('maven:3.8.4-jdk-8').inside("-e HOME=${env.WORKSPACE}") {
                    sh returnStatus: true, script: "cd amp && mvn -B clean -Djdbc.db=dummy"
                }

//                 sh returnStatus: true, script: "tar -cf /var/amp-node-cache.tar --remove-files" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/gis-layers-manager/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-settings/node" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-settings/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-translate/node" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-translate/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-state/node" +
//                         " amp/TEMPLATE/ampTemplate/node_modules/amp-state/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/gisModule/dev/node" +
//                         " amp/TEMPLATE/ampTemplate/gisModule/dev/node_modules" +
//                         " amp/TEMPLATE/ampTemplate/dashboard/dev/node" +
//                         " amp/TEMPLATE/ampTemplate/dashboard/dev/node_modules" +
//                         " amp/TEMPLATE/reamp/node" +
//                         " amp/TEMPLATE/reamp/node_modules"
            }
        }
    }
}

def deployed = false

// If this stage fails then next stage will retry deployment. Otherwise next stage will be skipped.
stage('Deploy') {
    node {
        try {
            // Find latest database version compatible with ${codeVersion}
            dbVersion = sh(returnStdout: true, script: "cd /opt/amp_dbs && amp-db find ${codeVersion} ${country}").trim()

            // Deploy AMP
            sh "cd /opt/docker/amp && ./up.sh ${tag} ${country} ${dbVersion}"

            slackSend(channel: 'amp-ci', color: 'good', message: "Deploy AMP - Success\nDeployed ${changePretty} will be ready for testing at ${ampUrl} in about 3 minutes")

            deployed = true
        } catch (e) {
            slackSend(channel: 'amp-ci', color: 'warning', message: "Deploy AMP - Failed\nFailed to deploy ${changePretty}")

            currentBuild.result = 'UNSTABLE'
        }
    }
}

// Retry deploy with the same country.
stage('Deploy again') {
    if (deployed) {
        println 'Already deployed, skipping this step.'
    } else {
        timeout(time: 1, unit: 'HOURS') {
            milestone()
            input message: "Proceed with repeated deploy for ${country}?"
            milestone()
        }
        node {
            try {
                sh "cd /opt/docker/amp && ./up.sh ${tag} ${country} ${dbVersion}"

                slackSend(channel: 'amp-ci', color: 'good', message: "Deploy AMP - Success\nDeployed ${changePretty} will be ready for testing at ${ampUrl} in about 3 minutes")

                currentBuild.result = 'SUCCESS'
            } catch (e) {
                slackSend(channel: 'amp-ci', color: 'warning', message: "Deploy AMP - Failed\nFailed to deploy ${changePretty}")

                throw e
            }
        }
    }
}

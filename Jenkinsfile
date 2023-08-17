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
def pgVersion = 14
def country
def ampUrl
def dockerRepo = "798366298150.dkr.ecr.us-east-1.amazonaws.com/"

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

def codeVersion
def countries
def environment

stage('Build') {
    timeout(15) {
        milestone()
        environment = input(
                message: "Server to deploy",
                parameters: [choice(choices: ["${env.AMP_STAGING_HOSTNAME}", "${env.AMP_DE_HOSTNAME}"], name: 'environment')])
        milestone()
    }

    println "Using environment: ${environment}"

    node {
        checkout scm

        // Find AMP version
        codeVersion = readMavenPom(file: 'amp/pom.xml').version
        println "AMP Version: ${codeVersion}"

        countries = sh(returnStdout: true,
                script: "ssh ${environment} 'cd /opt/amp_dbs && amp-db ls ${codeVersion} | sort'")
                .trim()
        if (countries == "") {
            println "There are no database backups compatible with ${codeVersion}"
            currentBuild.result = 'FAILURE'
        }
    }

    timeout(15) {
        milestone()
        country = input(
                message: "Proceed with build and deploy?",
                parameters: [choice(choices: countries, name: 'country')])
        milestone()
    }

    println "Let set amp url based on ${environment}"

    if ("${environment}".toLowerCase().contains("ampdevde")) {
        ampUrl = "http://amp-${country}-${tag}.de.ampsite.net/"
    } else {
        ampUrl = "http://amp-${country}-${tag}.stg.ampsite.net/"
    }

    println "amp url is ${ampUrl}"

    node {
        checkout scm

        def image = "${dockerRepo}amp/webapp:${tag}"
        def hash = sh(returnStdout: true, script: "git log --pretty=%H -n 1").trim()

        docker.withRegistry("https://798366298150.dkr.ecr.us-east-1.amazonaws.com", "ecr:us-east-1:aws-ecr-credentials-id") {
            try {
                updateGitHubCommitStatus('jenkins/build', 'Build in progress', 'PENDING')

                sshagent(credentials: ['GitHubDgReadOnlyKey']) {
                    withEnv(['DOCKER_BUILDKIT=1']) {
                        sh "docker build " +
                                "--progress=plain " +
                                "--ssh default " +
                                "-t ${image} " +
                                "--build-arg BUILD_SOURCE='${tag}' " +
                                "--build-arg AMP_PULL_REQUEST='${pr}' " +
                                "--build-arg AMP_BRANCH='${branch}' " +
                                "--build-arg AMP_REGISTRY_PRIVATE_KEY='${registryKey}' " +
                                "--label git-hash='${hash}' " +
                                "amp"
                    }
                }
                sh "docker push ${image} > /dev/null"

                updateGitHubCommitStatus('jenkins/build', 'Built successfully', 'SUCCESS')
            } catch (e) {
                updateGitHubCommitStatus('jenkins/build', 'Build failed', 'ERROR')
                throw e
            } finally {
                // Cleanup after Docker & Maven
                sh returnStatus: true, script: "docker rmi ${image}"
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
            dbVersion = sh(returnStdout: true, script: "ssh ${environment} 'cd /opt/amp_dbs && amp-db find ${codeVersion} ${country}'").trim()

            // Deploy AMP
            sh "ssh ${environment} 'amp-up2 ${tag} ${country} ${dbVersion} ${pgVersion}'"

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
                sh "ssh ${environment} 'amp-up2 ${tag} ${country} ${dbVersion} ${pgVersion}'"

                slackSend(channel: 'amp-ci', color: 'good', message: "Deploy AMP - Success\nDeployed ${changePretty} will be ready for testing at ${ampUrl} in about 3 minutes")

                currentBuild.result = 'SUCCESS'
            } catch (e) {
                slackSend(channel: 'amp-ci', color: 'warning', message: "Deploy AMP - Failed\nFailed to deploy ${changePretty}")

                throw e
            }
        }
    }
}

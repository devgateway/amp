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
def imageRegistry = "798366298150.dkr.ecr.us-east-1.amazonaws.com"
def imageName = "${imageRegistry}/amp"
def imageTag
def environment = "staging"

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
def countries = "bfaso\nchad\nciv\ndrc\negypt\nethiopia\ngambia\nhaiti\nhonduras\njordan\nkosovo\n" +
  "kyrgyzstan\nliberia\nmadagascar\nmalawi\nmoldova\nnepal\nniger\nsenegal\ntanzania\ntimor\ntimor\nuganda"

stage('Build') {

    timeout(15) {
        milestone()
        country = input(
                message: "Proceed with build and deploy?",
                parameters: [choice(choices: countries, name: 'country')])
        milestone()
    }

    node {
        checkout scm

        // Find AMP version
        codeVersion = readMavenPom(file: 'amp/pom.xml').version
        println "AMP Version: ${codeVersion}"

        def shortHash = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
        imageTag = "${tag}-${shortHash}"
        def imageRef = "${imageName}:${imageTag}"

        docker.withRegistry("https://${imageRegistry}", 'ecr:us-east-1:aws-ecr-credentials-id') {
            try {
                updateGitHubCommitStatus('jenkins/build', 'Build in progress', 'PENDING')

                sshagent(credentials: ['GitHubDgReadOnlyKey']) {
                    withEnv(['DOCKER_BUILDKIT=1']) {
                        sh "docker build " +
                                "--progress=plain " +
                                "--ssh default " +
                                "-t ${imageRef} " +
                                "--build-arg BUILD_SOURCE='${tag}' " +
                                "--build-arg AMP_PULL_REQUEST='${pr}' " +
                                "--build-arg AMP_BRANCH='${branch}' " +
                                "--build-arg AMP_REGISTRY_PRIVATE_KEY='${registryKey}' " +
                                "amp"
                    }
                }
                sh "docker push ${imageRef} > /dev/null"

                updateGitHubCommitStatus('jenkins/build', 'Built successfully', 'SUCCESS')
            } catch (e) {
                updateGitHubCommitStatus('jenkins/build', 'Build failed', 'ERROR')
                throw e
            } finally {
                // Cleanup after Docker & Maven
                sh returnStatus: true, script: "docker rmi ${imageRef}"
            }
        }
    }
}

stage('Deploy') {
    node {
        checkout scm

        docker.withRegistry("https://${imageRegistry}", 'ecr:us-east-1:aws-ecr-credentials-id') {
            sshagent(credentials: ['GitOpsKey']) {
                sh "docker run " +
                  "--rm " +
                  "-v `pwd`/git-ops-up.sh:/git-ops-up.sh " +
                  "-v \$(readlink -f \$SSH_AUTH_SOCK):/ssh-agent " +
                  "-e SSH_AUTH_SOCK=/ssh-agent " +
                  "${imageRegistry}/gitops-runner " +
                  "./git-ops-up.sh ${tag} ${country} ${imageTag} ${codeVersion} ${environment}"
            }
        }
    }
}

stage('Undeploy') {
    milestone()
    country = input(message: "Undeploy amp-${tag}-${country}.k8s.dgstg.org?")
    milestone()

    node {
        checkout scm

        docker.withRegistry("https://${imageRegistry}", 'ecr:us-east-1:aws-ecr-credentials-id') {
            sshagent(credentials: ['GitOpsKey']) {
                sh "docker run " +
                  "--rm " +
                  "-v `pwd`/git-ops-down.sh:/git-ops-down.sh " +
                  "-v \$(readlink -f \$SSH_AUTH_SOCK):/ssh-agent " +
                  "-e SSH_AUTH_SOCK=/ssh-agent " +
                  "${imageRegistry}/gitops-runner " +
                  "./git-ops-down.sh ${tag} ${country}"
            }
        }
    }
}

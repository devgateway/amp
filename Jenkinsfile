#!groovy

def tag = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9_-]/, "-").toLowerCase()
def branch = env.CHANGE_ID == null ? BRANCH_NAME : null
def pr = env.CHANGE_ID

println "Branch: ${branch}"
println "Pull request: ${pr}"
println "Tag: ${tag}"

stage('Build') {
    node {
        checkout scm
        withEnv(["PATH+MAVEN=${tool 'M339'}/bin"]) {
            sh "cd amp && mvn -T 4 clean compile war:exploded -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver -Dmaven.test.skip=true -Dapidocs=true -DbuildVersion=AMP -e"

            sh "docker build -q -t localhost:5000/amp-webapp:${tag} --build-arg AMP_EXPLODED_WAR=target/amp-AMP --build-arg AMP_PULL_REQUEST='${pr}' --build-arg AMP_BRANCH='${branch}' amp"
            sh "docker push localhost:5000/amp-webapp:${tag} > /dev/null"
            sh "docker rmi localhost:5000/amp-webapp:${tag}"

            sh "cd amp && mvn clean -Djdbc.db=dummy"
        }
    }
}

milestone()

def country
timeout(time: 7, unit: 'DAYS') {
    country = input message: "Proceed with deploy?", parameters: [choice(choices: 'bfaso\n' +
            'nepal\n' +
            'tanzania\n' +
            'drc\n' +
            'liberia\n' +
            'honduras\n' +
            'timor\n' +
            'senegal\n' +
            'gambia\n' +
            'ethiopia\n' +
            'civ', name: 'country')]
}

def host = "amp-${country}-${tag}-tc9.ampsite.net"

milestone()

stage('Deploy') {
    node {
        sh "ssh sulfur \"cd /opt/docker/amp && ./up.sh ${tag} ${country} ${host}\""
    }
}

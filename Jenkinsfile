#!groovy

def tag = BRANCH_NAME.replaceAll(/[^a-zA-Z0-9_-]/, "-").toLowerCase()

println "Pull request: ${CHANGE_ID}"
println "Tag: ${tag}"

stage('Build') {
    node {
        try {
            checkout scm
            withEnv(["PATH+MAVEN=${tool 'M339'}/bin"]) {
                sh "cd amp && mvn -T 4 clean compile war:exploded -Djdbc.user=amp -Djdbc.password=amp122006 -Djdbc.db=amp -Djdbc.host=db -Djdbc.port=5432 -DdbName=postgresql -Djdbc.driverClassName=org.postgresql.Driver -Dmaven.test.skip=true -Dapidocs=true -DbuildVersion=AMP -e"
                
                sh "docker build -q -t localhost:5000/amp-webapp:${tag} --build-arg AMP_EXPLODED_WAR=target/amp-AMP --build-arg AMP_PULL_REQUEST='${CHANGE_ID}' amp"
                sh "docker push localhost:5000/amp-webapp:${tag} > /dev/null"
                sh "docker rmi localhost:5000/amp-webapp:${tag}"
            }
        } catch (e) {
            echo "Caught: ${e}"
            currentBuild.result = "FAILED"
        }
        //step([$class: 'Mailer', recipients: 'amp-developer@developmentgateway.org'])
    }
}

stage('Deploy') {
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

    node {
        sh "ssh sulfur \"cd /opt/docker/amp && ./up.sh ${tag} ${country} ${host}\""
    }
}

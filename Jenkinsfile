pipeline {
    agent {
        node {
            label 'dungeon-master'
        }
    }

    options {
        ansiColor('xterm')
    }

    environment {
        BRANCH_NAME = "${env.CHANGE_BRANCH == null ? env.BRANCH_NAME : env.CHANGE_BRANCH}"
        TIMESTAMP = sh(returnStdout: true, script: 'date +%Y.%m.%d-%k.%M.%S').trim()
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'rm -rf .gradle'
                    sh 'gradle clean'
                    sh 'gradle build'
                }
            }
        }

        stage('Push to nexus') {
            when {
                branch 'main'
            }

            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh "docker login -u $USERNAME -p $PASSWORD owa.gigachadus.ru"
                        sh "docker build . -t owa.gigachadus.ru/study-tg-gateway:${env.TIMESTAMP}"
                        sh "docker push owa.gigachadus.ru/study-tg-gateway:${env.TIMESTAMP}"
                        sh "docker tag owa.gigachadus.ru/study-tg-gateway:${env.TIMESTAMP} owa.gigachadus.ru/study-tg-gateway:latest"
                        sh "docker push owa.gigachadus.ru/study-tg-gateway:latest"
                        sh "docker image rm owa.gigachadus.ru/study-tg-gateway:${env.TIMESTAMP}"
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }

            steps {
                script {
                    sh 'docker rm -f study-tg-gateway-prod || true'
                    withCredentials([string(credentialsId: 'study-connect-tg-token', variable: 'TOKEN')]) {
                        sh "docker run --restart always --name study-tg-gateway-prod --network master-prod-network -d owa.gigachadus.ru/study-tg-gateway-prod:latest --master.uri=study-master-prod:8080 --bot.token=$TOKEN"
                    }
                }
            }
        }
    }
}

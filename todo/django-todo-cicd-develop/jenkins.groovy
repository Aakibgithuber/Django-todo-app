pipeline {
    agent any
    stages {
        stage('Build and Tag Image') {
            steps {
                sh 'docker build -t myimage:latest .'
                sh 'docker tag myimage:latest myrepo/myimage:latest'
            }
        }
        stage('Push to Dockerhub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh "docker login -u ${USERNAME} -p ${PASSWORD}"
                    sh 'docker push myrepo/myimage:latest'
                }
            }
        }
        stage('Pull and Run Image') {
            steps {
                sh 'docker pull myrepo/myimage:latest'
                sh 'docker run -d --name mycontainer myrepo/myimage:latest'
            }
        }
    }
}

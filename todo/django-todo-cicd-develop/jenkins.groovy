pipeline {
    agent any
 stages {
  stage('Docker Build and Tag') {
           steps {
              
                sh 'docker build -t todoapp:latest .' 
                  sh 'docker tag todoapp newtodoapp/todoapp:latest'
                sh 'docker tag todoapp newtodoapp/todoapp:$BUILD_NUMBER'
               
          }
        }
     
  stage('Publish image to Docker Hub') {
          
            steps {
        withDockerRegistry([ credentialsId: "dockerHub", url: "" ]) {
          sh  'docker push newtodoapp/todoapp:latest'
          sh  'docker push newtodoapp/todoapp:$BUILD_NUMBER' 
        }
                  
          }
        }
     
      stage('Run Docker container on Jenkins Agent') {
             
            steps {
                sh "docker run -d -p 4030:80 newtodoapp/todoapp"
                
            }
        }
 stage('Run Docker container on remote hosts') {
             
            steps {
                sh "docker -H ssh://jenkins@192.168.49.1 run -d -p 4001:80 newtodoapp/todoapp"
 
            }
        }
    }
}

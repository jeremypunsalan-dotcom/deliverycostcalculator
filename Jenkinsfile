pipeline {
	environment {
    	registry = "jeremypunsalandotcom/deliverycostcalculator"
    	registryCredential = 'dockerhub'
    	dockerImage = ''
	}
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }
        stage('Image Build') {
      		steps{
        		script {
          			dockerImage = docker.build registry + ":$BUILD_NUMBER"
        		}
      		}
    	}
    	stage('Image Deploy') {
      		steps{
        		script {
          			docker.withRegistry( '', registryCredential ) {
            			dockerImage.push()
          			}
        		}
      		}
    	}
    	stage('Image Delete') {
      		steps{
        		sh "docker rmi $registry:$BUILD_NUMBER"
      		}
    	}
    	
    }
}
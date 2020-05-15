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
    	stage('Image Deploy to Registry') {
      		steps{
        		script {
          			docker.withRegistry( '', registryCredential ) {
            			dockerImage.push()
          			}
        		}
      		}
    	}
    	stage('GKE Deployment') {
      		steps{
                sh "sed -i 's/jeremypunsalandotcom/deliverycostcalculator:${env.BUILD_ID}/g' deployment-deployment.yaml"
                step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME, location: env.LOCATION, manifestPattern: 'deployment-deployment.yaml', credentialsId: env.CREDENTIALS_ID, verifyDeployments: true])
            }
    	}
    	stage('GKE Service') {
      		steps{
                step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME, location: env.LOCATION, manifestPattern: 'deployment-service.yaml', credentialsId: env.CREDENTIALS_ID, verifyDeployments: true])
            }
    	}
    	stage('GKE Ingress') {
      		steps{
                step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME, location: env.LOCATION, manifestPattern: 'deployment-ingress.yaml', credentialsId: env.CREDENTIALS_ID, verifyDeployments: true])
            }
    	}
    	stage('Image Delete') {
      		steps{
        		sh "docker rmi $registry:$BUILD_NUMBER"
      		}
    	}
    	
    }
}
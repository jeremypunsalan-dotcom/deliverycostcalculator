pipeline {
	environment {
    	registry = "jeremypunsalandotcom/deliverycostcalculator"
    	registryCredential = 'dockerhub'
    	dockerImage = ''
    	PROJECT_ID = 'excellent-style-276509'
        CLUSTER_NAME = 'jeremypunsalandotcom-cluster'
        LOCATION = 'us-central1-c'
        CREDENTIALS_ID = 'jeremypunsalandotcom'
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
    	stage('Image Delete') {
      		steps{
        		sh "docker rmi $registry:$BUILD_NUMBER"
      		}
    	}
    	stage('GKE Deployment') {
      		steps{
                sh "sed -i 's/deliverycostcalculator:latest/deliverycostcalculator:${env.BUILD_ID}/g' deployment-deployment.yaml"
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
    	
    }
}
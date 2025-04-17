pipeline {
    agent any

    environment {
        namespace = "${namespace}"
        dockerImage = "${image}"
        deployment = "${deployment}"
	relicname = "${relicname}"
	relickey = "${relickey}"
    }

    tools {
        maven 'MAVEN_JENKINS'
        jdk 'JAVA21'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Testes Unitários') {
            steps {
                echo 'Testing..'
                //sh 'mvn test'
            }
        }
        stage('Deploy imagem Gitlab') {
            steps {

                echo 'Deploying.... no gitlab'
                sh 'cp -r /var/jenkins_home/newrelic newrelic'
                //login docker
				sh 'docker login --username=unipix --password=nexus63987 http://registry.unipix.com.br'

                //constroi a imagem nova do microservico
				sh 'docker build --no-cache -t registry.unipix.com.br/'+dockerImage+' . '+'--build-arg RELICNAME=$relicname --build-arg RELICKEY=$relickey'

				//push imagem para gitlab  (para depois ser atualizada no cluster)
				sh 'docker push registry.unipix.com.br/' + dockerImage

				sh 'docker rmi registry.unipix.com.br/' + dockerImage

				sh 'rm -rf *'

				sh 'docker builder prune --force'

            }
        }
        stage('Update Cluster Kubernetes') {
            steps {

                echo 'Updating.... kubernetes cluster'

                sh "kubectl --kubeconfig=/var/jenkins_home/ccm${namespace} rollout restart deployment/${deployment} --namespace=${namespace}"

            }
        }






    }

	post {
    	always  {
        	discordSend description: "Finalizado: resultado ${currentBuild.currentResult}  (<${env.BUILD_URL}|Open>)  ", result: currentBuild.currentResult, title: JOB_NAME, webhookURL: "https://discord.com/api/webhooks/1029480091136303215/Yf-qujOwN69B3uMQrWLVk2gWHCt5U6WlCDpFO7SdNtLIxpeP89jwteyWX1VVn3mM2A30"
        }
    }
}

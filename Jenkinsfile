pipeline {
    agent any

    environment {
        GITHUB_REPO_URL = 'https://github.com/Ashmi2004/SPE.git'
    }

    stages {
        stage('Clone Git') {
            steps {
                script {
                    // Checkout the code from the GitHub repository
                    git branch: 'main', url: "${GITHUB_REPO_URL}"
                }
            }
        }
        stage('Maven Build Job Portal Service') {
            steps {
                echo "Building Job Portal"
                sh "cd JobPortal; mvn clean install"
            }
        }
        stage('Maven Build Blockchain Service') {
            steps {
                echo "Building Blockchain"
                sh "cd BlockChain; mvn clean install"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image
                    docker.build("jobportal", './JobPortal')
                    docker.build("jobportalfrontend","./frontend")
                    docker.build("blockchain","./BlockChain")
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script{
                    docker.withRegistry('', 'DockerHubCred') {
                    echo "pushing docker images"
                    sh 'docker tag jobportal halwaikeladdoo/jobportal';
                    sh 'docker push halwaikeladdoo/jobportal';
                    sh 'docker tag jobportalfrontend halwaikeladdoo/jobportalfrontend';
                    sh 'docker push halwaikeladdoo/jobportalfrontend';
                    sh 'docker tag blockchain halwaikeladdoo/blockchain';
                    sh 'docker push halwaikeladdoo/blockchain';
                    }
                 }
            }
        }

        stage('Delete image from local system'){
            steps{
                echo "Deleting docker image in docker"
                sh 'docker rmi halwaikeladdoo/jobportal';
                sh 'docker rmi halwaikeladdoo/jobportalfrontend';
                sh 'docker rmi halwaikeladdoo/blockchain';
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                echo 'Running ansible playbook yml file'
                sh 'ansible-playbook -i inventory.ini playbook.yml'
            }
        }

    }
}

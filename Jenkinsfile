pipeline {
    agent any
    tools {
        jdk 'temurin-jdk21'
        maven 'maven3'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout the code from the repository
                    git branch: 'master', url: 'https://github.com/kireobat/oauth-api.git'
                }
            }
        }
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21-alpine'
                }
            }
            steps {
                script {
                    // Build the project to generate the JAR file in the target directory
                    sh 'mvn -Dmaven.repo.local=/tmp/.m2/repository clean package'
                }
            }
        }
        stage('Build and Push Docker Image') {
            agent {
                docker {
                    image 'docker:20.10'
                }
            }
            steps {
                script {
                    // Build and push the Docker image
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
                        def app = docker.build("kireobat/oauth-api")
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                    }
                }
            }
        }
        stage('Deploy to Portainer') {
            steps {
                withCredentials(
                    [
                        usernamePassword(
                            credentialsId: 'docker',
                            passwordVariable: 'PORTAINER_PASSWORD',
                            usernameVariable: 'PORTAINER_USERNAME'
                        ),
                        usernamePassword(
                            credentialsId: 'oauth_api_postgres',
                            passwordVariable: 'POSTGRES_PASSWORD',
                            usernameVariable: 'POSTGRES_USERNAME'
                        ),
                        usernamePassword(
                            credentialsId: 'github_oauth_app',
                            passwordVariable: 'GITHUB_SECRET',
                            usernameVariable: 'GITHUB_CLIENT_ID'
                        ),
                        string(
                            credentialsId: 'postgres_url',
                            variable: 'POSTGRES_URL'
                        )
                    ]) {
                    script {
                        // Obtain JWT token for Portainer API
                        def response = httpRequest(
                            url: 'https://docker.kireobat.eu/api/auth',
                            httpMode: 'POST',
                            contentType: 'APPLICATION_JSON',
                            requestBody: """{
                                "username": "${PORTAINER_USERNAME}",
                                "password": "${PORTAINER_PASSWORD}"
                                }"""
                        )

                        def token = readJSON(text: response.content).jwt

                        // Define the container name
                        def containerName = "oauth-api"
                        def hostPort = null // Initialize hostPort

                        // Check if the container exists
                        def existingContainerResponse = httpRequest(
                            url: "https://docker.kireobat.eu/api/endpoints/2/docker/containers/${containerName}/json",
                            httpMode: 'GET',
                            contentType: 'APPLICATION_JSON',
                            customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]]
                        )

                        if (existingContainerResponse.status == 200) {
                            // Container exists, retrieve its port configuration
                            def existingContainer = readJSON(text: existingContainerResponse.content)
                            def portBindings = existingContainer.HostConfig.PortBindings

                            // Extract the host port from the existing container
                            if (portBindings && portBindings['8080/tcp']) {
                                hostPort = portBindings['8080/tcp'][0].HostPort
                            }

                            // Stop and remove the existing container
                            def stopResponse = httpRequest(
                                url: "https://docker.kireobat.eu/api/endpoints/2/docker/containers/${containerName}/stop",
                                httpMode: 'POST',
                                contentType: 'APPLICATION_JSON',
                                customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]]
                            )

                            // Check if the stop was successful
                            if (stopResponse.status != 204) {
                                echo "Failed to stop the container: ${stopResponse.content}"
                            } else {
                                echo "Container stopped successfully."
                            }

                            // Remove the existing container
                            def removeResponse = httpRequest(
                                url: "https://docker.kireobat.eu/api/endpoints/2/docker/containers/${containerName}",
                                httpMode: 'DELETE',
                                contentType: 'APPLICATION_JSON',
                                customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]],
                                requestBody: """{
                                    "force": true
                                }"""
                            )

                            // Check if the remove was successful
                            if (removeResponse.status != 204) {
                                echo "Failed to remove the container: ${removeResponse.content}"
                            } else {
                                echo "Container removed successfully."
                            }
                        } else {
                            echo "No existing container found with the name ${containerName}. Proceeding to create a new one with a random port."
                        }

                        // Deploy the container to Portainer
                        def deployResponse = httpRequest(
                            url: 'https://docker.kireobat.eu/api/endpoints/2/docker/containers/create',
                            httpMode: 'POST',
                            contentType: 'APPLICATION_JSON',
                            customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]],
                            requestBody: """{
                                "Name": "${containerName}",
                                "Image": "kireobat/oauth-api:latest",
                                "Env": [
                                    "SPRING_DATASOURCE_URL=${POSTGRES_URL}/${POSTGRES_USERNAME}",
                                    "SPRING_DATASOURCE_USER=${POSTGRES_USERNAME}",
                                    "SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}",
                                    "GITHUB_CLIENT_ID=${GITHUB_CLIENT_ID}",
                                    "GITHUB_SECRET=${GITHUB_SECRET}"
                                ],
                                "HostConfig": {
                                    "PortBindings": {
                                        "${hostPort ? '8080/tcp': '8080/tcp'}": [
                                            {
                                                "HostPort": "${hostPort ?: ''}"
                                            }
                                        ]
                                    }
                                }
                            }"""
                        )

                        def deployResponseContent = deployResponse.content.toString()

                        // Check response for success or failure
                        if (deployResponseContent.contains("error")) {
                            error "Failed to deploy to Portainer: ${response}"
                        } else {
                            echo "Successfully deployed to Portainer."
                        }

                        // Extract the container ID from the response
                        def containerId = new groovy.json.JsonSlurper().parseText(deployResponse.content).Id

                        // Start the container
                        def startResponse = httpRequest(
                            url: "https://docker.kireobat.eu/api/endpoints/2/docker/containers/${containerId}/start",
                            httpMode: 'POST',
                            contentType: 'APPLICATION_JSON',
                            customHeaders: [[name: 'Authorization', value: "Bearer ${token}"]]
                        )

                        // Check response for success or failure
                        if (startResponse.status != 204) {
                            error "Failed to start the container: ${startResponse.content}"
                        } else {
                            echo "Container started successfully."
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            // Clean up any resources or perform actions regardless of success or failure
            echo "Pipeline completed."
        }
    }
}
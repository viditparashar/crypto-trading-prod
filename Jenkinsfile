pipeline {
  agent any
  environment {
    DOCKER_IMAGE = "viditparashar12/trading-prod"
    DOCKERHUB_CRED = 'dockerhub-creds'
  }
  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Build (Maven)') {
      steps { sh './mvnw clean package -DskipTests' }
    }

    stage('Build Docker Image') {
      steps {
        script {
          def COMMIT = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
          sh "docker build -t ${DOCKER_IMAGE}:${COMMIT} -t ${DOCKER_IMAGE}:latest ."
          env.COMMIT = COMMIT
        }
      }
    }

    stage('Login & Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CRED}", usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
          script {
            sh "echo \"$DH_PASS\" | docker login -u \"$DH_USER\" --password-stdin"
            sh "docker push ${DOCKER_IMAGE}:${COMMIT}"
            sh "docker push ${DOCKER_IMAGE}:latest"
          }
        }
      }
    }

    stage('Deploy') {
      when { branch 'main' }
      steps {
        script {
          // Use the commit tag if present, otherwise fallback to latest
          def TAG = COMMIT ?: 'latest'
          // Pull new image, stop old container, run new one
          sh "docker pull ${DOCKER_IMAGE}:${TAG} || docker pull ${DOCKER_IMAGE}:latest"
          sh "docker rm -f trading-prod || true"
          sh "docker run -d --name trading-prod --network appnet -p 8081:8081 ${DOCKER_IMAGE}:${TAG}"
        }
      }
    }
  }

  post {
    success { echo "CI+CD successful." }
    failure { echo "CI+CD failed." }
  }
}
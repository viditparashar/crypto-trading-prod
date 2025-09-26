pipeline {
  agent any
  environment {
    DOCKER_IMAGE = "viditparashar12/trading-prod"
    DOCKERHUB_CRED = 'dockerhub-creds'
  }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build (Maven)') { steps { sh './mvnw clean package -DskipTests' } }
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
          sh '''
            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
            docker push ${DOCKER_IMAGE}:${COMMIT}
            docker push ${DOCKER_IMAGE}:latest
          '''
        }
      }
    }
    stage('Deploy') {
      steps {
        script {
          def TAG = env.COMMIT ?: 'latest'
          sh "docker pull ${DOCKER_IMAGE}:${TAG} || docker pull ${DOCKER_IMAGE}:latest"
          sh "docker rm -f trading-prod || true"
          sh "docker run -d --name trading-prod --network appnet -p 8081:8081 ${DOCKER_IMAGE}:${TAG}"
        }
      }
    }
  }
}

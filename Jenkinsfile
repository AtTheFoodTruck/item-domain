pipeline {
  agent any
  stages {
    stage('Build Gradle') {
      steps {
        sh '''sudo chmod 777 gradlew
./gradlew clean build --exclude-task test
'''
        sh 'ls'
      }
    }

    stage('Build Docker') {
      steps {
        sh 'ls'
        script {
          backend_user = docker.build("goalgoru/backend_item")
        }

      }
    }

    stage('Docker push') {
      steps {
        script {
          docker.withRegistry('https://registry.hub.docker.com/', registryCredential) {
            backend_user.push("latest")
            backend_user.push("${BUILD_NUMBER}")
          }
        }

      }
    }

  }
  environment {
    registryCredential = 'dockerhub_cred'
  }
}

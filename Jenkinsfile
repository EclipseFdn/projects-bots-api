@Library('common-shared') _

pipeline {

  agent any

  environment {
    APP_NAME = 'projects-bots-api'
    NAMESPACE = 'foundation-internal-webdev-apps'
    IMAGE_NAME = 'eclipsefdn/projects-bots-api'
    CONTAINER_NAME = 'app'
    ENVIRONMENT = 'production'
    //TODO: use GIT_COMMIT
    TAG_NAME = 'latest'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timeout(time: 30, unit: 'MINUTES')
  }

  stages {
    stage('Deploy to cluster') {
      agent {
        kubernetes {
          label 'kubedeploy-agent'
          yaml '''
          apiVersion: v1
          kind: Pod
          spec:
            containers:
            - name: kubectl
              image: eclipsefdn/kubectl:okd-c1
              command:
              - cat
              tty: true
              resources:
                limits:
                  cpu: 1
                  memory: 1Gi
              volumeMounts:
              - mountPath: "/home/default/.kube"
                name: "dot-kube"
                readOnly: false
            - name: jnlp
              resources:
                limits:
                  cpu: 1
                  memory: 1Gi
            volumes:
            - name: "dot-kube"
              emptyDir: {}
          '''
        }
      }
      when {
        environment name: 'ENVIRONMENT', value: 'production'
      }
      steps {
        container('kubectl') {
          updateContainerImage([
            namespace: "${env.NAMESPACE}",
            selector: "app=${env.APP_NAME},environment=${env.ENVIRONMENT}",
            containerName: "${env.CONTAINER_NAME}",
            newImageRef: "${env.IMAGE_NAME}:${env.TAG_NAME}"
          ])
        }
      }
    }
  }
  post {
    always {
      deleteDir() /* clean up workspace */
      //sendNotifications currentBuild
    }
  }
}


def call(Map cfg) {

    pipeline {
        agent any

        tools {
            jdk cfg.jdk ?: 'jdk-17'
            maven cfg.maven ?: 'maven-3.9'
        }

        environment {
            APP_NAME = cfg.appName
            IMAGE    = cfg.image
            ENV      = cfg.env ?: 'dev'
        }

        stages {

            stage('Checkout') {
                steps { checkout scm }
            }

            stage('Build') {
                steps {
                    mavenBuild(
                        goals: cfg.goals ?: 'clean package',
                        skipTests: cfg.skipTests ?: false
                    )
                }
            }

            stage('Quality') {
                when { branch 'main' }
                steps { sonarScan() }
            }

            stage('Docker Build & Push') {
                when { branch 'main' }
                steps {
                    dockerBuild(image: IMAGE)
                }
            }

            stage('Deploy') {
                when { branch 'main' }
                steps {
                    k8sDeploy(
                        app: APP_NAME,
                        env: ENV,
                        image: IMAGE
                    )
                }
            }
        }

        post {
            success { notify("✅ ${APP_NAME} deployed successfully") }
            failure { notify("❌ ${APP_NAME} pipeline failed") }
        }
    }
}
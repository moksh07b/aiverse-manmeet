pipeline {
    agent any

    environment{
        MAVEN_HOME = 'C:\\Program Files\\Maven\\apache-maven-3.9.9'
        PATH = "${MAVEN_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('build') {
            steps {
                bat 'java -version'
                bat 'mvn -v'
                
            }
        }

        stage('test') {
            steps {
                // Run the application and create the deployment package.
                   bat 'mvn test'

        // Archive the JUnit test results
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat """
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \\
                          -DskipTests \\
                          -Dsonar.projectKey=Manmeetkaur06_aiverse \\
                          -Dsonar.organization=manmeetkaur06 \\
                          -Dsonar.host.url=https://sonarcloud.io
                    """
                }
            }
        }

        stage('deploy') {
            steps {
                withAWS(credentials: 'aws-cred', region: 'eu-north-1') {
                    bat '''
                        # Upload the jar file to S3
                        aws s3 cp target/aiverse-0.0.1-SNAPSHOT.jar s3://myaiversebucket/aiverse-${BUILD_NUMBER}.jar

                        # Create a new Elastic Beanstalk application version referencing the jar file
                        aws elasticbeanstalk create-application-version \
                          --application-name aiverse \
                          --version-label v${BUILD_NUMBER} \
                          --source-bundle S3Bucket=myaiversebucket,S3Key=aiverse-${BUILD_NUMBER}.jar

                        # Update the Elastic Beanstalk environment to the new version
                        aws elasticbeanstalk update-environment \
                          --environment-name Aiverse-env \
                          --version-label v${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('release') {
            steps {
                withAWS(credentials: 'aws-cred', region: 'eu-north-1') {
                    bat '''
                        aws deploy create-deployment \
                          --application-name aiverse \
                          --deployment-group-name aiverse-group \
                          --s3-location bucket=myaiversebucket,key=deployment-${BUILD_NUMBER}.zip,bundleType=zip \
                          --deployment-config-name CodeDeployDefault.AllAtOnce \
                          --description "Production deployment for build ${BUILD_NUMBER}"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline created successfully!"
        }
        failure {
            echo "failed!"
        }
    }
}

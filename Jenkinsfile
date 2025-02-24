pipeline {
    agent any

    environment {
    JAVA_HOME    = "/usr/lib/jvm/java-21-openjdk-amd64"
    MAVEN_HOME   = "/opt/apache-maven-3.9.9"
    AWS_CLI_PATH = "/usr/local/bin/aws" // Updated to the correct AWS CLI path
    PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${AWS_CLI_PATH}:$PATH"

}


    stages {
        stage('build') {
            steps {
                // Clone repository and set up environment, then build and run tests.
                git branch: 'new', url: 'https://github.com/moksh07b/aiverse-manmeet.git'
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn clean verify'
            }
        }

        stage('test') {
            steps {
                // Run the application and create the deployment package.
                sh 'nohup java -jar target/aiverse-0.0.1-SNAPSHOT.jar &'
                sh '''
                    zip -r deployment-${BUILD_NUMBER}.zip target/aiverse-0.0.1-SNAPSHOT.jar appspec.yml scripts/
                '''
            }
        }

        stage('analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \\
                          -DskipTests \\
                          -Dsonar.projectKey=Manmeetkaur06_aiverse \\
                          -Dsonar.organization=manmeetkaur06 \\
                          -Dsonar.host.url=https://sonarcloud.io
                    """
                }
            }
        }
        
        // stage("Deploy and Release"){
        //     steps{
        //         sh "mvn spring-boot:run"
        //     }
        // }

        stage('deploy') {
            steps {
                withAWS(credentials: 'aws-cred', region: 'eu-north-1') {
                    sh '''
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
                    sh '''
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
        always{
                emailext(
                    subject: "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${currentBuild.result}",
                    body : "The Build Ended in ${currentBuild.result}",
                    to : "mokshbansal07@gmail.com",
                    from: "jenkins@example.com", 
                    replyTo: "jenkins@example.com", 
                    mimeType: "text/html"
                    
                )
        }
    }
 }

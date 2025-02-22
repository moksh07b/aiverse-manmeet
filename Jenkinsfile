pipeline {
    agent any

    tools {
    jdk 'jdk'
    maven 'maven'
}


    stages {
        stage('build') {
            steps {
                sh 'java -version'
                sh 'mvn -v'
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
        
        stage('Deploy') {
            steps {
                sh "mvn spring-boot:run"
            }
        }
    
        
        
    }

    
    
}

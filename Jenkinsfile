pipeline {
    agent any 

    triggers {
        pollSCM('* * * * *')
    }
    // Got permission denied while trying to connect to the Docker daemon socket at unix.
    // sudo usermod -a -G docker jenkins
    // restart jenkins server ->  sudo service jenkins restart
    stages {
        
        stage('Maven Compile') {
            steps {
                echo '----------------- This is a compile phase ----------'
                sh 'mvn clean compile'
            }
        }
        
         stage('Maven Test') {
            steps {
                echo '----------------- This is a compile phase ----------'
                sh 'mvn clean test'
            }
        }
        
        stage('Maven Build') {
             steps {
                echo '----------------- This is a build phase ----------'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo '----------------- This is a build docker image phase ----------'
                sh '''
                    docker image build -t java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot .
                '''
            }
        }

        stage('Docker Deploy') {
            steps {
                echo '----------------- This is a docker deployment phase ----------'
                sh '''
                 (if  [ $(docker ps -a | grep java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot | cut -d " " -f1) ]; then \
                        echo $(docker rm -f java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot); \
                        echo "---------------- successfully removed java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot ----------------"
                     else \
                    echo OK; \
                 fi;);
            docker container run --restart always --name java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot -p 8082:8082 -d java-fsd-phase4-assessment-ecommerce-kitchenstory-0.0.1-snapshot
            '''
            }
        }
    }
}
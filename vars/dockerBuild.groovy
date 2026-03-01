def call(Map cfg) {
    stage('Docker Build') {
        sh """
          docker build -t ${cfg.image}:${BUILD_NUMBER} .
          docker push ${cfg.image}:${BUILD_NUMBER}
        """
    }
}
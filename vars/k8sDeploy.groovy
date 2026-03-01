def call(Map cfg) {
    stage('K8s Deploy') {
        sh """
          kubectl set image deployment/${cfg.app} \
          ${cfg.app}=${cfg.image}:${BUILD_NUMBER} \
          -n ${cfg.env}
        """
    }
}
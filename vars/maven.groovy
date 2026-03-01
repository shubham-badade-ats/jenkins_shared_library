def call(Map cfg = [:]) {
    stage('Maven Build') {
        sh """
          mvn ${cfg.goals} \
          -DskipTests=${cfg.skipTests}
        """
    }
}
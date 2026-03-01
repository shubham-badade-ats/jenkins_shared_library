def call(String msg) {
    slackSend(
        channel: '#deployments',
        message: msg
    )
}
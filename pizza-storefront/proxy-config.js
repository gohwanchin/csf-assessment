module.exports = [
    {
        context: [ '/api/**'], //match these requests
        target: 'http://localhost:8080', //SpringBoot url
        secure: false
    }
]
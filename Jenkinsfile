// Testing Ki Pathshala - Season 5 framework pipeline (declarative).
// Needs on the agent: JDK 21, Maven, Chrome/Chromium (see ci/jenkins/Dockerfile)
// Plugins: Pipeline, Git, JUnit, Allure, Timestamper.
def MODULES = ['selenium-testng', 'playwright-java', 'restassured-api', 'cucumber-bdd']

pipeline {
    agent any

    parameters {
        choice(name: 'MODULE', choices: ['all'] + MODULES, description: 'Which module to test')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser for the UI modules')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browsers without a window')
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    triggers {
        cron('H 2 * * *')   // nightly, around 02:00
    }

    environment {
        // test failures make the build UNSTABLE (yellow) instead of stopping it, so reports are always published
        MVN = 'mvn -B -ntp -Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Prepare') {
            steps {
                sh 'java -version && mvn -v'
                sh 'rm -rf allure-results */target/allure-results */target/surefire-reports'
            }
        }

        stage('Tests') {
            parallel {
                stage('selenium-testng') {
                    when { expression { params.MODULE in ['all', 'selenium-testng'] } }
                    steps {
                        dir('selenium-testng') {
                            sh "${MVN} clean test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                        }
                    }
                }
                stage('playwright-java') {
                    when { expression { params.MODULE in ['all', 'playwright-java'] } }
                    steps {
                        dir('playwright-java') {
                            script {
                                def pwBrowser = params.BROWSER == 'chrome' ? 'chromium' : params.BROWSER
                                sh "${MVN} exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args='install ${pwBrowser}'"
                                // browsers are installed above, so Playwright must not download all of them again
                                withEnv(['PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1']) {
                                    sh "${MVN} clean test -Dbrowser=${pwBrowser} -Dheadless=${params.HEADLESS}"
                                }
                            }
                        }
                    }
                }
                stage('restassured-api') {
                    when { expression { params.MODULE in ['all', 'restassured-api'] } }
                    steps {
                        dir('restassured-api') {
                            sh "${MVN} clean test"
                        }
                    }
                }
                stage('cucumber-bdd') {
                    when { expression { params.MODULE in ['all', 'cucumber-bdd'] } }
                    steps {
                        dir('cucumber-bdd') {
                            sh "${MVN} clean test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit testResults: '*/target/surefire-reports/*.xml', allowEmptyResults: true
            // merge the results of all modules into ONE folder -> ONE Allure report with one history
            sh 'mkdir -p allure-results && for d in */target/allure-results; do [ -d "$d" ] && cp -r "$d"/. allure-results/; done; true'
            allure results: [[path: 'allure-results']]
        }
    }
}

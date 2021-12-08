package util

class JobSharedUtils {

    static defaultBuildDiscarder(def job) {
        job.with {
            logRotator {
                daysToKeep(30)
                numToKeep(10)
                artifactDaysToKeep(60)
                artifactNumToKeep(5)
            }
        }
    }

    static gitParameters(def params, def gitRepositoryUrl, def branch) {
        params.with {
            stringParam {
                name ("GIT_REPOSITORY_URL")
                defaultValue(gitRepositoryUrl)
            }
            stringParam {
                name ("GIT_REPOSITORY_BRANCH")
                defaultValue(branch)
            }
        }
    }

    static mavenParameters(def params, def mavenSettingsXml) {
        params.with {
            stringParam {
                name ("MAVEN_HOME")
                defaultValue("/opt/apache/maven")
            }
            stringParam {
                name ("JAVA_HOME")
                defaultValue("/opt/oracle/java")
            }
            stringParam {
                name ("MAVEN_SETTINGS_XML")
                defaultValue(mavenSettingsXml)
            }
            stringParam {
                name ("MAVEN_OPTS")
                defaultValue("-Dmaven.wagon.http.ssl.insecure=true -Dhttps.protocols=TLSv1.2")
            }
        }
    }

    static defaultPublishers(def job) {
        job.with {
            archiveJunit('**/target/surefire-reports/*.xml') {
                allowEmptyResults()
                retainLongStdout()
                healthScaleFactor(1.0)
            }
            extendedEmail()
            wsCleanup()
        }
    }

    static defaultSettings(def job) {
        job.with {
            preBuildCleanup()
            timeout {
                absolute(600)
            }
        }
    }

    static defaultMaven(def job) {
        job.with {
            env('MAVEN_HOME', '/opt/apache-maven-3.6.3')
            env('PATH', '$PATH:$MAVEN_HOME/bin')
        }
    }
}

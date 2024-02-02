package util

class JobSharedUtils {

    public static final String DEFAULT_SCHEDULE = 'H/10 * * * *'

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

    static harmoniaParameters(def params, def harmoniaRepositoryUrl = 'https://github.com/jboss-set/harmonia', def branch = 'main') {
        params.with {
            stringParam {
                name ("HARMONIA_REPO")
                defaultValue(harmoniaRepositoryUrl)
            }
            stringParam {
                name ("HARMONIA_BRANCH")
                defaultValue(branch)
            }
        }
    }

    /* default to /not/there as some jobs may not want to have the jdk/mvn in /opt mounted
       and may use /opt for other usage. */
    static toolsDirParameters(def params, def toolsDir = '/not/there', def toolsMount = '/not/there') {
        params.with {
            stringParam {
                name ("TOOLS_DIR")
                defaultValue(toolsDir)
                description("Note that if the dummy value '/not/there' is used, the /opt folder is NOT added as a volume.")
            }
            stringParam {
                name ("TOOLS_MOUNT")
                defaultValue(toolsMount)
                description("Note that if the dummy value '/not/there' is used, the /opt folder is NOT added as a volume.")
            }
        }
    }

    static podmanImageParameter(def params, def imageName = 'localhost/automatons') {
        params.with {
            stringParam {
                name ("BUILD_PODMAN_IMAGE")
                defaultValue(imageName)
            }
        }
    }

    static mavenParameters(Map args) {
        if (args.mavenSettingsXml == null) {
            args.mavenSettingsXml = '/opt/tools/settings.xml'
        }

        if (args.javaHome == null) {
            args.javaHome = '/opt/oracle/java'
        }

        args.params.with {
            stringParam {
                name ("MAVEN_HOME")
                defaultValue("/opt/apache/maven")
            }
            stringParam {
                name ("JAVA_HOME")
                defaultValue(args.javaHome)
            }
            stringParam {
                name ("MAVEN_SETTINGS_XML")
                defaultValue(args.mavenSettingsXml)
            }
            stringParam {
                name ("MAVEN_OPTS")
                defaultValue("-Dmaven.wagon.http.ssl.insecure=true -Dhttps.protocols=TLSv1.2")
            }
        }
    }

    static doDisableConcurrentBuilds(def properties) {
        properties.with {
            disableConcurrentBuilds {
                abortPrevious(false)
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

    static customParams(def params, def customParams) {
        customParams.delegate = params
        customParams.resolveStrategy = Closure.DELEGATE_FIRST
        customParams.call()
    }
}

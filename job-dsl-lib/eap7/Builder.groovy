package eap7

import util.JobSharedUtils

class Builder {

    String jobName
    String branch
    String schedule = JobSharedUtils.DEFAULT_SCHEDULE
    String parentJobname = ''
    String mavenSettingsXml
    String javaHome='/opt/oracle/java'
    String harmoniaScript = 'eap-job/olympus.sh'
    String harmoniaBranch = 'main'
    String gitRepositoryUrl = 'git@github.com:jbossas/jboss-eap7.git'
    def customParams

    def buildAndTest(factory) {
        build(factory)
        test(factory)
    }

    def build(factory) {

        if (jobName == null) {
            jobName = 'eap-' + branch
        }

        factory.with {
            pipelineJob(jobName + '-build') {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/wildfly-pipeline'))
                        sandbox()
                    }
                }
                JobSharedUtils.defaultBuildDiscarder(delegate)
                triggers {
                    scm(schedule)
                }
                parameters {
                    commonParameters(delegate)
                    if (customParams != null) {
                        JobSharedUtils.customParams(delegate, customParams)
                    }
                }
            }
        }
    }

    def test(factory) {
        factory.with {
            pipelineJob(jobName + '-testsuite') {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/wildfly-pipeline'))
                        sandbox()
                    }
                }
                JobSharedUtils.defaultBuildDiscarder(delegate)
                parameters {
                    commonParameters(delegate)
                    stringParam {
                        name("PARENT_JOBNAME")
                        defaultValue(parentJobname)
                    }
                    if (customParams != null) {
                        JobSharedUtils.customParams(delegate, customParams)
                    }
                }
            }
        }
    }

    def commonParameters(params) {
        JobSharedUtils.gitParameters(params, gitRepositoryUrl, branch)
        JobSharedUtils.mavenParameters(params: params, javaHome: javaHome, mavenSettingsXml: mavenSettingsXml)
        params.with {
            stringParam {
                name ("HARMONIA_SCRIPT")
                defaultValue(harmoniaScript)
                description("The variable is the path, inside the Harmonia folder to the script being used. The job will execute the script or report an error if the script does not exist inside the folder.")
            }
            stringParam {
                name("HARMONIA_BRANCH")
                defaultValue(harmoniaBranch)
                description("The variable contains the name of the branch of the Harmonia folder to use for this job. Normally it's should be 'main', but it can be changed to experiment changes on the Harmonia's script without affecting other jobs.")
            }
            stringParam {
                name("HARMONIA_REPO")
                defaultValue("https://github.com/jboss-set/harmonia.git")
                description("This the URL to the Harmonia's repo used. It can be override so that user can create their own fork, implement any change they want, without having to modify the pipeline directly.")
            }
        }
    }
}

package vbe

import util.JobSharedUtils

class Builder {

    String jobName
    String branch
    String schedule = JobSharedUtils.DEFAULT_SCHEDULE
    String parentJobname = ''
    String mavenSettingsXml
    String javaHome='/opt/oracle/java'
    String harmoniaScript = 'eap-job/olympus.sh'
    String gitRepositoryUrl = 'git@github.com:jbossas/jboss-eap7.git'
    String vbeChannels = '${VBE_EAP_74_CHANNEL}'
    String vbeRepositoryNames = 'jboss-eap-7.4-product-repository'

    def buildAndTest(factory) {
        build(factory)
        test(factory)
    }

    def build(factory) {

        if (jobName == null) {
            jobName = 'vbe-eap-' + branch
        }

        factory.with {
            pipelineJob(jobName + '-build') {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/vbe-wildfly-pipeline'))
                        sandbox()
                    }
                }
                JobSharedUtils.defaultBuildDiscarder(delegate)
                triggers {
                    scm(schedule)
                }
                parameters {
                    commonParameters(delegate)
                    stringParam {
                        name ("VBE_EXTENSION")
                        defaultValue('Anything')
                        description('If present, VBE will run, dont change this.')
                    }
                    stringParam {
                        name ("VBE_REPOSITORY_NAMES")
                        defaultValue(vbeRepositoryNames)
                        description('Comma separated list of repository names - as present in maven, if present, this will be used to trim repositories present in reactor.')
                    }
                    stringParam {
                        name ("VBE_CHANNELS")
                        defaultValue(vbeChannels)
                        description('Comma seprated list of URIs to yaml channels')
                    }
                    stringParam {
                        name ("VBE_LOG_FILE")
                        defaultValue('../vbe.update.log.yaml')
                        description('Log file name, if not set, default will be used')
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
                        script(readFileFromWorkspace('pipelines/vbe-wildfly-pipeline'))
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
            }
        }
    }
}


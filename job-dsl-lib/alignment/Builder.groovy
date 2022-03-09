package alignment

import util.JobSharedUtils

class Builder {
    String branch
    String jobName
    String jobSuffix
    String repoUrl
    String toAddress
    String id
    String reportTitle
    String rule
    String logger_project_code

    void build(factory) {
        if (jobName == null) {
            jobName = 'component-alignment-' + jobSuffix
        }
        factory.with {
            pipelineJob(jobName) {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/component-alignment-pipeline'))
                        sandbox()
                    }
                }
                properties {
                    JobSharedUtils.doDisableConcurrentBuilds(delegate)
                }
                JobSharedUtils.defaultBuildDiscarder(delegate)
                triggers {
                    cron('@weekly')
                }
                parameters {
                    JobSharedUtils.mavenParameters(params: delegate)
                    JobSharedUtils.gitParameters(delegate, repoUrl, branch)
                    stringParam {
                        name("TO_ADDRESS")
                        defaultValue(toAddress)
                    }
                    stringParam {
                        name("HARMONIA_SCRIPT")
                        defaultValue('upgrade-components-report.sh')
                    }
                    stringParam {
                        name("JOB_NAME")
                        defaultValue(id)
                    }
                    stringParam {
                        name("RULE_NAME")
                        defaultValue(rule)
                    }
                    stringParam {
                        name("REPORT_TITLE")
                        defaultValue(reportTitle)
                    }
                    stringParam {
                        name("LOGGER_PROJECT_CODE")
                        defaultValue(logger_project_code)
                    }
                    stringParam {
                        name("COMPONENT_UPGRADE_LOGGER")
                        defaultValue(COMP_ALIGMENT_UPGRADE_LOGGER)
                    }
                }
            }
        }
    }
}
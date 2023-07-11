package alignment

import util.JobSharedUtils

class Builder {

    String jobName
    String projectRepositoryUrl
    String projectRepositoryBranch
    String configRepositoryUrl
    String configRepositoryBranch
    String configFile
    String binaryVersion
    String loggerCode
    String loggerUri
    String subject
    String fromAddr
    String toAddr

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
                    stringParam {
                        name ("PROJECT_REPOSITORY_URL")
                        defaultValue(projectRepositoryUrl)
                    }
                    stringParam {
                        name ("PROJECT_REPOSITORY_BRANCH")
                        defaultValue(projectRepositoryBranch)
                    }
                    stringParam {
                        name ("CONFIG_REPOSITORY_URL")
                        defaultValue(configRepositoryUrl)
                    }
                    stringParam {
                        name ("CONFIG_REPOSITORY_BRANCH")
                        defaultValue(configRepositoryBranch)
                    }
                    stringParam {
                        name ("CONFIG_FILE")
                        defaultValue(configFile)
                    }
                    stringParam {
                        name ("BINARY_VERSION")
                        defaultValue(binaryVersion)
                    }
                    stringParam {
                        name ("LOGGER_CODE")
                        defaultValue(loggerCode)
                    }
                    stringParam {
                        name ("LOGGER_URI")
                        defaultValue(loggerUri)
                    }
                    stringParam {
                        name ("SUBJECT")
                        defaultValue(subject)
                    }
                    stringParam {
                        name ("FROM_ADDR")
                        defaultValue(fromAddr)
                    }
                    stringParam {
                        name ("TO_ADDR")
                        defaultValue(toAddr)
                    }
                }
                publishers {
                    archiveArtifacts('report.html')
                }
            }
        }
    }
}

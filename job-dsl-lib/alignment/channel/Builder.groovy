package alignment.channel

import util.JobSharedUtils

class Builder {

    String jobName
    String subject
    String fromAddr
    String toAddr

    void build(factory) {
        factory.with {
            pipelineJob(jobName) {
                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/component-alignment-channel-pipeline'))
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
            }
        }
    }
}

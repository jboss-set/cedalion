package alignment.channel

import util.JobSharedUtils

class Builder {

    String jobName
    String subject
    String fromAddr
    String toAddr
    String channelUrl
    String repositories

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
                        description("E-mail subject")
                    }
                    stringParam {
                        name ("FROM_ADDR")
                        defaultValue(fromAddr)
                        description("E-mail sender address")
                    }
                    stringParam {
                        name ("TO_ADDR")
                        defaultValue(toAddr)
                        description("E-mail recipient address")
                    }
                    stringParam {
                        name ("CHANNEL_URL")
                        defaultValue(channelUrl)
                        description("Channel that will be used to obtain base versions.")
                    }
                    text {
                        name("REPOSITORIES")
                        defaultValue(repositories)
                        description("Lines in format \"id::url\"")
                    }
                }
            }
        }
    }
}

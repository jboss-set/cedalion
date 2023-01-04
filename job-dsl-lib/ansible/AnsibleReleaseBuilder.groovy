package ansible

class AnsibleReleaseBuilder {

    String projectName
    String projectUpstreamName
    String gitUrl = "https://github.com/ansible-middleware/"
    String branch = "main"
    String jobPrefix = 'ansible-release-'
    String jobSuffix = ''
    String pipelineFile = 'pipelines/ansible-release-pipeline'
    String releaseName = ""

    def build(factory) {
        return factory.with {
            pipelineJob(jobPrefix + projectName + jobSuffix) {

                definition {
                    cps {
                        script(readFileFromWorkspace(pipelineFile))
                        sandbox()
                    }
                }
                logRotator {
                    daysToKeep(30)
                    numToKeep(10)
                    artifactDaysToKeep(60)
                    artifactNumToKeep(5)
                }
                parameters {
                    stringParam {
                      name("COLLECTION_NAME")
                      defaultValue(projectName)
                    }
                    stringParam {
                      name("PROJECT_UPSTREAM_NAME")
                      defaultValue(projectUpstreamName ?: projectName)
                    }
                    stringParam {
                      name("GIT_REPOSITORY_URL")
                      defaultValue(gitUrl)
                    }
                    stringParam {
                      name("GIT_REPOSITORY_BRANCH")
                      defaultValue(branch)
                    }
                    stringParam {
                      name("RELEASE_NAME")
                      defaultValue(releaseName)
                    }
                }
            }
        }
    }
}

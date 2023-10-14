package ansible

class AnsibleReleaseBuilder extends AbstractAnsibleBuilder {

    String jobPrefix = 'ansible-release-'
    String pipelineFile = 'pipelines/ansible-release-pipeline'
    String fullRelease = 'True'
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
                      name("PROJECT_UPSTREAM_NAME")
                      defaultValue(projectUpstreamName ?: projectName)
                    }
                     stringParam {
                      name("COLLECTION_NAME")
                      defaultValue(projectName)
                    }
                    stringParam {
                      name("GIT_REPOSITORY_BRANCH")
                      defaultValue(branch)
                    }
                    stringParam {
                      name ("HARMONIA_REPO")
                      defaultValue(harmoniaGitUrl)
                    }
                    stringParam {
                      name ("HARMONIA_BRANCH")
                      defaultValue(harmoniaBranch)
                    }
                    stringParam {
                      name("RELEASE_NAME")
                      defaultValue(releaseName)
                    }
                    stringParam {
                      name("FULL_RELEASE")
                      defaultValue(fullRelease)
                    }
                }
            }
        }
    }
}

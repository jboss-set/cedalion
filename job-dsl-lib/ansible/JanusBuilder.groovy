package ansible

import util.JobSharedUtils

class JanusBuilder extends AbstractAnsibleBuilder {

    String playbook = 'playbooks/job.yml'
    String upstreamCollectionName = ""
    boolean setupTrigger = false

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
                      name("PROJECT_NAME")
                      defaultValue(projectName)
                    }
                    stringParam {
                      name("PROJECT_UPSTREAM_NAME")
                      defaultValue(projectUpstreamName ?: projectName)
                    }
                    stringParam {
                      name ("PATH_TO_SCRIPT")
                      defaultValue(pathToScript)
                    }
                    JobSharedUtils.gitParameters(delegate, gitUrl, branch)
                    stringParam {
                      name("CHECKOUT_GIT_PROJECT")
                      defaultValue(checkoutProject)
                    }
                    stringParam {
                      name("COPY_FROM_PARENT_JOB")
                      defaultValue(copyFromParentJob)
                    }
                    JobSharedUtils.harmoniaParameters(delegate)
                    JobSharedUtils.podmanImageParameter(delegate, podmanImage)
                    stringParam {
                      name("MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL")
                      defaultValue(downloadServerUrl != null ? downloadServerUrl : MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL)
                    }
                    stringParam {
                      name ("JENKINS_JOBS_VOLUME_ENABLED")
                      defaultValue('True')
                    }
                    JobSharedUtils.toolsDirParameters(delegate)
                    stringParam {
                      name ("PLAYBOOK")
                      defaultValue(playbook)
                    }
                    stringParam {
                      name ("JANUS_GIT_REPOSITORY_URL")
                      defaultValue("https://github.com/ansible-middleware/janus.git")
                    }
                    stringParam {
                      name ("JANUS_BRANCH")
                      defaultValue("main")
                    }
                    stringParam {
                      name("UPSTREAM_COLLECTION_NAME")
                      defaultValue(upstreamCollectionName)
                    }
                    stringParam {
                      name("INTERNAL_GIT_REPOSITORY_URL")
                      defaultValue("git@gitlab:")
                    }
                }
            }
        }
    }
}

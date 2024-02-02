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
                triggers {
                    setupTrigger ? upstream('ansible-ci-' + (upstreamCollectionName ?: projectUpstreamName),'FAILURE') : scm (schedule)
                }
                parameters {
                    JobSharedUtils.projectName(delegate, projectName)
                    JobSharedUtils.projectUpstreamName(delegate, '' + (projectName ?: projectUpstreamName) )
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
                    JobSharedUtils.middlewareDownloadReleaseURL(delegate, downloadServerUrl)
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

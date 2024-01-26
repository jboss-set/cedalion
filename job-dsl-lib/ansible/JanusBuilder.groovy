package ansible

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
                    stringParam {
                      name ("GIT_REPOSITORY_URL")
                      defaultValue(gitUrl)
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_BRANCH")
                      defaultValue(branch)
                    }
                    stringParam {
                      name("CHECKOUT_GIT_PROJECT")
                      defaultValue(checkoutProject)
                    }
                    stringParam {
                      name("COPY_FROM_PARENT_JOB")
                      defaultValue(copyFromParentJob)
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
                      name ("BUILD_PODMAN_IMAGE")
                      defaultValue(podmanImage)
                    }
                    stringParam {
                      name("MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL")
                      defaultValue(downloadServerUrl != null ? downloadServerUrl : MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL)
                    }
                    stringParam {
                      name ("JENKINS_JOBS_VOLUME_ENABLED")
                      defaultValue('True')
                    }
                    stringParam {
                      name ("TOOLS_DIR")
                      defaultValue("/not/there")
                      description("This dummy value ensures the /opt folder is NOT added as a volume")
                    }
                    stringParam {
                      name ("TOOLS_MOUNT")
                      defaultValue("/not/there")
                      description("This dummy value ensures the /opt folder is NOT added as a volume")
                    }
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

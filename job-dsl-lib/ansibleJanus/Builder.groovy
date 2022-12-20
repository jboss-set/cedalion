package ansibleJanus

class Builder {

    String projectName
    String projectUpstreamName
    String projectUrl
    String branch = "main"
    String schedule = 'H/10 * * * *'
    String jobPrefix = ''
    String jobSuffix = ''
    String pipelineFile
    String podmanImage = "localhost/ansible"
    String pathToScript
    String downloadServerUrl

    String playbook = 'playbooks/job.yml'

    def build(factory) {
        factory.with {
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
                    scm (schedule)
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
                      defaultValue(projectUrl)
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_BRANCH")
                      defaultValue(branch)
                    }
                    stringParam {
                      name ("HARMONIA_REPO")
                      defaultValue('https://github.com/jboss-set/harmonia.git')
                    }
                    stringParam {
                      name ("HARMONIA_BRANCH")
                      defaultValue('olympus')
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
                }
            }
        }
    }
}

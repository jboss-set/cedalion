package ansibleDownstreamRunner

class Builder {

    String projectName
    String projectUpstreamName
    String gitUrl = "https://github.com/ansible-middleware/"
    String branch = "main"
    String schedule = 'H/10 * * * *'
    String jobPrefix = ''
    String jobSuffix = ''
    String pipelineFile
    String podmanImage
    String pathToScript
    String downloadServerUrl

    String playbook = 'playbooks/playbook.yml'

    String collections
    String products_paths

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
                      defaultValue(gitUrl + projectName + ".git")
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
                      name("JENKINS_JOBS_VOLUME_ENABLED")
                      defaultValue('True')
                    }
                    stringParam {
                      name ("CONTAINER_UID")
                      defaultValue('0')
                    }
                    stringParam {
                      name("CONTAINER_USERNAME")
                      defaultValue('root')
                    }
                    stringParam {
                      name("CONTAINER_COMMAND")
                      defaultValue('/usr/sbin/init')
                    }
                    stringParam {
                      name("SYSTEMD_ENABLED")
                      defaultValue('True')
                    }
                    stringParam {
                      name("TOOLS_DIR")
                      defaultValue("/not/there")
                    }
                    stringParam {
                      name ("PATHS_TO_PRODUCTS_TO_DOWNLOAD")
                      defaultValue(products_paths)
                      description("A comma separated set of paths specifying where the product archive is located on the middleware release download server.")
                    }
                    stringParam {
                      name ("PLAYBOOK")
                      defaultValue(playbook)
                    }
                     stringParam {
                      name ("COLLECTIONS_TO_INSTALL")
                      defaultValue(collections)
                      description("A comma separated list of the Red Hat collections to install. Ex: 'redhat_csp_download,jboss_eap'. Note that non Redhat collection will be installed automatically when the requirements.yml is processed.")
                    }
                }
            }
        }
    }
}

package ansibleDownstreamRunner

class Builder {

    String projectName
    String jobPrefix = 'ansible-downstream-runner-'
    String jobSuffix = ''
    String schedule = 'H/10 * * * *'
    String podmanImage = "localhost/molecule-slave"
    String pathToScript = "ansible-validate-downstream-collection.sh"
    String playbook = 'playbooks/playbook.yml'
    String collections
    String products_paths

    def build(factory) {
        factory.with {
            pipelineJob(jobPrefix + projectName + jobSuffix) {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/ansible-downstream-runner-pipeline'))
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
                      name ("COLLECTIONS_TO_INSTALL")
                      defaultValue(collections)
                      description("A comma separated list of the Red Hat collections to install. Ex: 'redhat_csp_download,jboss_eap'. Note that non Redhat collection will be installed automatically when the requirements.yml is processed.")
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
                      name ("PATH_TO_SCRIPT")
                      defaultValue(pathToScript)
                    }
                    stringParam {
                      name ("BUILD_PODMAN_IMAGE")
                      defaultValue(podmanImage)
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
                      name("MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL")
                      defaultValue(MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL)
                    }
                }
            }
        }
    }
}

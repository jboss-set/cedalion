package ansibleCi

class Builder {

    String projectName
    String projectUpstreamName
    String projectPrefix = "ansible-ci"
    String moleculeBuildId
    String gitUrl = "https://github.com/ansible-middleware/"
    String branch = "main"
    String scenarioName = "--all"
    String schedule = 'H/10 * * * *'
    String pipelineFile = "pipelines/ansible-ci-pipeline"
    String pathToScript  = "molecule.sh"
    String downloadServerUrl

    def build(factory) {
        factory.with {
            pipelineJob(projectPrefix + '-' + projectName) {

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
                      defaultValue("localhost/molecule-runner")
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
                      name ("BUILD_MOLECULE_SLAVE_SSHD_PORT")
                      defaultValue(moleculeBuildId)
                    }
                    stringParam {
                      name ("JENKINS_JOBS_VOLUME_ENABLED")
                      defaultValue('True')
                    }
                    stringParam {
                      name("MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL")
                      defaultValue(downloadServerUrl ?: MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL)
                    }
                    stringParam {
                      name("SCENARIO_NAME")
                      defaultValue(scenarioName)
                    }
                }
            }
        }
    }
}

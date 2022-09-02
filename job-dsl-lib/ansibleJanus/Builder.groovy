package ansibleJanus

class Builder {

    String projectName
    String projectUpstreamName
    String jobPrefix = 'ansible-janus'
    String jobSuffix = ''
    String branch = "main"
    String schedule = 'H/10 * * * *'
    String podmanImage = "localhost/ansible"
    String pathToScript = "ansible-playbook.sh"
    String playbook = 'playbook.yml'

    def build(factory) {
        factory.with {
            pipelineJob(jobPrefix + projectName + jobSuffix) {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/ansible-janus-pipeline'))
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
                      defaultValue("https://github.com/ansible-middleware/" + projectName + ".git")
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_BRANCH")
                      defaultValue(branch)
                    }
                    stringParam {
                      name ("BUILD_PODMAN_IMAGE")
                      defaultValue(podmanImage)
                    }
                    stringParam {
                      name ("PLAYBOOK")
                      defaultValue(playbook)
                    }
                }
            }
        }
    }
}

package ansibleCollection

class Builder {

    String collectionName
    String downstreamName = ""
    String branch = "main"
    String schedule = 'H/10 * * * *'
    String podmanImage = "localhost/ansible"
    String pathToScript = "build-collection.sh"

    def build(factory) {
        factory.with {
            pipelineJob('ansible-collection-' + collectionName) {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/ansible-collection-pipeline'))
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
                      name("COLLECTION_NAME")
                      defaultValue(collectionName)
                    }
                    stringParam {
                      name ("PATH_TO_SCRIPT")
                      defaultValue(pathToScript)
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_URL")
                      defaultValue("https://github.com/ansible-middleware/" + collectionName + ".git")
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
                      name ("VERSION")
                      defaultValue("")
                    }
                    stringParam {
                      name ("PROJECT_DOWNSTREAM_NAME")
                      defaultValue(downstreamName)
                    }
                }
            }
        }
    }
}

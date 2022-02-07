package ansible

class Builder {

    String collectionName
    String branch = "main"
    String schedule = 'H/10 * * * *'

    def build(factory) {
        factory.with {
            pipelineJob('ansible-collection-' + collectionName) {

                definition {
                    cps {
                        script(readFileFromWorkspace('pipelines/ansible-pipeline'))
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
                      defaultValue("build-collection.sh")
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_URL")
                      defaultValue("https://github.com/ansible-middleware/" + collectionName + ".git")
                    }
                    stringParam {
                      name ("GIT_REPOSITORY_BRANCH")
                      defaultValue("main")
                    }
                    stringParam {
                      name ("BUILD_PODMAN_IMAGE")
                      defaultValue("localhost/ansible")
                    }
                    stringParam {
                      name ("VERSION")
                      defaultValue("")
                    }
                }
            }
        }
    }
}

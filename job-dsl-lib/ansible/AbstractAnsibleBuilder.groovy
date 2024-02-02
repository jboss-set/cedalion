package ansible

import util.JobSharedUtils

class AbstractAnsibleBuilder {

    String projectName
    String projectUpstreamName
    String gitUrl = "https://github.com/ansible-middleware/"
    String branch = "main"
    String checkoutProject = "True"
    String schedule = 'H/10 * * * *'
    String harmoniaGitUrl = 'https://github.com/jboss-set/harmonia.git'
    String harmoniaBranch = 'main'
    String jobPrefix = ''
    String jobSuffix = ''
    String pipelineFile = 'pipelines/ansible-pipeline'
    String podmanImage
    String pathToScript
    String downloadServerUrl
    String copyFromParentJob = "False"

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
                    JobSharedUtils.gitParameters(params: delegate, gitRepositoryUrl: gitUrl, branch: branch)
                    stringParam {
                      name("CHECKOUT_GIT_PROJECT")
                      defaultValue(checkoutProject)
                    }
                    stringParam {
                      name("COPY_FROM_PARENT_JOB")
                      defaultValue(copyFromParentJob)
                    }
                    JobSharedUtils.harmoniaParameters(params: delegate)
                    JobSharedUtils.podmanImageParameter(params: delegate, imageName: podmanImage)
                    stringParam {
                      name("MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL")
                      defaultValue(downloadServerUrl != null ? downloadServerUrl : MIDDLEWARE_DOWNLOAD_RELEASE_SERVER_URL)
                    }
                    stringParam {
                      name ("JENKINS_JOBS_VOLUME_ENABLED")
                      defaultValue('True')
                    }
                    JobSharedUtils.toolsDirParameters(params: delegate)
                }
            }
        }
    }
}

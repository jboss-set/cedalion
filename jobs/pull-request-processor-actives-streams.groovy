import util.JobSharedUtils

pipelineJob(ITEM_NAME) {

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/pr-processor-pipeline'))
            sandbox()
        }
    }
    properties {
        JobSharedUtils.doDisableConcurrentBuilds(delegate)
    }
    JobSharedUtils.defaultBuildDiscarder(delegate)
    triggers {
        scm (JobSharedUtils.DEFAULT_SCHEDULE)
        cron('@hourly')
    }
    parameters {
        JobSharedUtils.mavenParameters(params: delegate)

        stringParam {
            name ("PULL_REQUEST_PROCESSOR_HOME")
            defaultValue('/opt/tools/pr-processor/')
        }
        stringParam {
            name ("HARMONIA_SCRIPT")
            defaultValue('pr-processor.sh')
        }
        stringParam {
            name ("VERSION")
            defaultValue("0.8.13.Final")
        }
    }
}

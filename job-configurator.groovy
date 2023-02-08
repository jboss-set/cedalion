JOBDSL_INCLUDE = binding.hasVariable("JOBDSL_INCLUDE") ? JOBDSL_INCLUDE : ".*"

pipelineJob("job-configurator") {
    properties {
        disableConcurrentBuilds()
        durabilityHint {
            hint('PERFORMANCE_OPTIMIZED')
        }
        pipelineTriggers {
            triggers {
                // https://issues.jenkins.io/browse/JENKINS-61463
                // scm('@daily')
            }
        }
    }
    parameters {
        stringParam('JOBS_DECLARATION_REPO', 'https://github.com/jboss-set/cedalion', 'Git cloneable URL of declaration repository (mapped volume in development mode)')
        stringParam('JOBS_DECLARATION_REPO_BRANCH', 'main', 'Branch used for Git declaration repository (inactive in development mode)')
        stringParam('JOBDSL_INCLUDE', JOBDSL_INCLUDE, "Process only Job DSL files that are matching the regex")
    }
   
    // Those deprecated 'triggers' are needed until fixed https://issues.jenkins.io/browse/JENKINS-61463 
    triggers {
        scm('H/15 * * * *')
        hudsonStartupTrigger {
            nodeParameterName("built-in")
            label("built-in")
            quietPeriod("0")
            runOnChoice("False")
          }
    }

    definition {
        cps {
            script(new URL("https://raw.githubusercontent.com/jboss-set/cedalion/master/job-configurator.jenkinsfile").text)
            sandbox(true)
        }
    }
}

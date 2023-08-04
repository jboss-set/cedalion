package ansible

class JanusBuilder extends AbstractAnsibleBuilder {

  String playbook = 'playbooks/job.yml'
  String upstreamCollectionName = ""

  def build(factory) {
    super.build(factory).parameters {
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
      stringParam {
        name("UPSTREAM_COLLECTION_NAME")
        defaultValue(upstreamCollectionName)
      }
    }.triggers {
      upstream threshold: 'FAILURE', upstreamProjects: 'ansible-ci-' + (upstreamCollectionName ?: projectUpstreamName)
    }
  }
}

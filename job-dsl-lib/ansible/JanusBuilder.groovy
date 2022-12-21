package ansible

class JanusBuilder extends AbstractAnsibleBuilder {

  String playbook = 'playbooks/playbook.yml'

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
    }
  }
}

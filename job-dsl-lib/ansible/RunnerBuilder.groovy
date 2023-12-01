package ansible

class RunnerBuilder extends AbstractAnsibleBuilder {

  String playbook = 'playbook'
  String collections
  String products_paths

  def build(factory) {
    super.build(factory).parameters {
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
        name("PLAYBOOK")
        defaultValue(playbook)
      }
    }
  }
}

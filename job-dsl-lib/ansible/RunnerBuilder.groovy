package ansible

class RunnerBuilder extends AbstractAnsibleBuilder {

  String playbook = 'playbooks/playbook.yml'
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
        name ("PATHS_TO_PRODUCTS_TO_DOWNLOAD")
        defaultValue(products_paths)
        description("A comma separated set of paths specifying where the product archive is located on the middleware release download server.")
      }
      stringParam {
        name ("PLAYBOOK")
        defaultValue(playbook)
      }
      stringParam {
        name ("COLLECTIONS_TO_INSTALL")
        defaultValue(collections)
        description("A comma separated list of the Red Hat collections to install. Ex: 'redhat_csp_download,jboss_eap'. Note that non Redhat collection will be installed automatically when the requirements.yml is processed.")
      }
    }
  }
}

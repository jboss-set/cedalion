package ansible

class MoleculeBuilder extends AbstractAnsibleBuilder {

    String scenarioName = "--all"
    String moleculeBuildId

    def build(factory) {
      super.build(factory).parameters {
        stringParam {
          name("SCENARIO_NAME")
          defaultValue(scenarioName)
          description("name(s) [comma separated] of the molecule scenario(s) to run, default: --all")
        }
        stringParam {
          name("BUILD_MOLECULE_SLAVE_SSHD_PORT")
          defaultValue(moleculeBuildId)
          description("Unique id attached to the job, used as port number to connect to molecule slave instance")
        }
      }
    }
}

def buildGitUrl(projectName) {
  return 'https://github.com/ansible-middleware/' + projectName + '.git'
}

def upstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all") {
  new ansible.MoleculeBuilder(
        projectName: projectName,
        moleculeBuildId: moleculeBuildId,
        scenarioName: scenarioName,
        gitUrl: buildGitUrl(projectName),
        jobPrefix: "ansible-ci-",
        pathToScript: 'molecule.sh',
        podmanImage: 'localhost/molecule-runner',
    ).build(this)
}

def moleculeJobWithGitUrl(projectName, moleculeBuildId, gitUrl, branch = "main", scenarioName = "--all") {
  new ansible.MoleculeBuilder(
        projectName: projectName,
        moleculeBuildId: moleculeBuildId,
        scenarioName: scenarioName,
        gitUrl: gitUrl,
        branch: branch,
        jobPrefix: "ansible-ci-",
        pathToScript: 'molecule.sh',
        podmanImage: 'localhost/molecule-runner',
    ).build(this)
}

def downstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all", projectUpstreamName = projectName) {
  new ansible.MoleculeBuilder(
        projectName: projectName,
        projectUpstreamName: projectUpstreamName,
        moleculeBuildId: moleculeBuildId,
        scenarioName: scenarioName,
        gitUrl: buildGitUrl(projectName),
        jobPrefix: "ansible-downstream-ci-",
        podmanImage: 'localhost/molecule-runner',
        pathToScript: "molecule-downstream.sh",
        copyFromParentJob: "True",
        checkoutProject: "False"
    ).build(this)
}

def janusDefaultPlaybook(projectName) {
  return "playbooks/" + projectName + ".yml"
}

def janusJob(projectName, projectUpstreamName = projectName, gitUrl = buildGitUrl(projectName), playbook = "") {
  if ( playbook.equals("") ) playbook = janusDefaultPlaybook(projectName)
  new ansible.JanusBuilder(
        projectName: projectName,
        projectUpstreamName: projectUpstreamName,
        playbook: playbook,
        gitUrl: gitUrl,
        jobPrefix: 'ansible-janus-',
        pathToScript: 'ansible-janus.sh',
        podmanImage: 'localhost/ansible',
        checkoutProject: "False"
    ).build(this)
}


def janusAmqJob() {
  new ansible.JanusBuilder(
        projectName: 'amq_broker',
        projectUpstreamName: 'activemq',
        upstreamCollectionName: 'amq',
        playbook: "playbooks/amq_broker.yml",
        gitUrl: 'https://github.com/ansible-middleware/amq',
        jobPrefix: 'ansible-janus-',
        pathToScript: 'ansible-janus.sh',
        podmanImage: 'localhost/ansible',
        checkoutProject: "False"
    ).build(this)
}

def dotJob(projectName, dotJobsPrefix, portOffset) {
   String dotProjectName = projectName + "-dot"
   new ansible.MoleculeBuilder(
     projectName: dotProjectName,
     projectUpstreamName: projectName,
     jobPrefix: dotJobsPrefix,
     moleculeBuildId: portOffset,
     checkoutProject: "True",
     gitUrl: "git@gitlab:ansible-middleware/" + dotProjectName + ".git",
     copyFromParentJob: "True",
     pathToScript: "molecule-downstream.sh",
     podmanImage: 'localhost/molecule-runner'
    ).build(this)
}

def demoJob(projectName, portOffset, jobPrefix = "ansible-") {
  new ansible.MoleculeBuilder(
      projectName: projectName,
      moleculeBuildId: portOffset,
      jobPrefix: jobPrefix,
      pathToScript: "molecule.sh",
      gitUrl: buildGitUrl(projectName),
      podmanImage: 'localhost/molecule-runner'
  ).build(this)
}

def downstreamRunnerJob(projectName, playbook, collections, productPaths) {
  new ansible.RunnerBuilder(
        projectName: projectName,
        playbook: playbook,
        collections: collections,
        products_paths: productPaths,
        podmanImage: 'localhost/molecule-runner',
        pathToScript: 'ansible-validate-downstream-collection.sh',
        jobPrefix: 'ansible-downstream-runner-',
        checkoutProject: "False"
    ).build(this)
}

def releaseCollection(projectName, projectUpstreamName = projectName) {
  new ansible.AnsibleReleaseBuilder(
        projectName: projectName,
        projectUpstreamName: projectUpstreamName
  ).build(this)
}
releaseCollection("eap","wildfly")
releaseCollection("jws","jws")
releaseCollection("data_grid", "infinispan")
releaseCollection("sso","keycloak")
releaseCollection("amq_broker","amq")
releaseCollection("runtimes_common","common")
EapView.jobList(this, 'Ansible Release', 'ansible-release.*')
//
// CI Jobs for Ansible Middleware
//
int upstreamProjectsPortOffsetstart = 22000
[ 'jws', 'wildfly', 'infinispan'].each { project -> upstreamCIJob(project, upstreamProjectsPortOffsetstart++) }
// upstreamCIJob('jbcs',upstreamProjectsPortOffsetstart++)
upstreamCIJob('keycloak', upstreamProjectsPortOffsetstart++, "default,overridexml")
upstreamCIJob('amq', upstreamProjectsPortOffsetstart++ , "default,amq_upgrade")
moleculeJobWithGitUrl('zeus', upstreamProjectsPortOffsetstart++, 'https://github.com/jboss-set/zeus.git', 'olympus')
moleculeJobWithGitUrl('common-criteria', upstreamProjectsPortOffsetstart++, 'https://github.com/ansible-middleware/common_criteria.git')
EapView.jobList(this, 'Ansible CI', 'ansible-ci.*')
//
// CI jobs for downstream (Janus generated) collections
//
int downstreamProjectsPortOffsetstart = 23000
['jws', 'eap', 'data_grid','sso'].each { project -> downstreamCIJob(project, downstreamProjectsPortOffsetstart++) }
downstreamCIJob('sso', downstreamProjectsPortOffsetstart++, "default,overridexml")
downstreamCIJob('amq_broker', downstreamProjectsPortOffsetstart++, "default,amq_upgrade")
EapView.jobList(this, 'Ansible Downstream CI', 'ansible-downstream-ci.*$')

//
// DOT jobs
//
String dotJobsPrefix = "ansible-downstream-tests-"
int dotPortOffsetstart = 24000
['jws', 'eap', 'sso', 'amq_broker'].each {  projectName -> dotJob(projectName, dotJobsPrefix, dotPortOffsetstart++) }
EapView.jobList(this, 'Ansible DOT', dotJobsPrefix + '.*$')

//
// CI Jobs for demos
//
int demoPortOffsetstart = 25000
[ 'wildfly-cluster-demo', 'flange-demo', 'eap-migration-demo', 'jws-app-update-demo'].each {  projectName -> demoJob(projectName, demoPortOffsetstart++) }
EapView.jobList(this, 'Ansible Demos', '^.*-demo')

//
// Janus jobs - generating downstream collections
//
janusJob('redhat_csp_download','redhat-csp-download', buildGitUrl('redhat-csp-download'), "playbooks/janus.yml")
janusJob('jws','jws', buildGitUrl('jws'), "playbooks/janus.yml")
janusJob('eap', 'wildfly', buildGitUrl('wildfly'))
janusJob('data_grid', 'infinispan', buildGitUrl('infinispan'))
janusJob('sso', 'keycloak', buildGitUrl('keycloak'))
janusJob('runtimes_common', 'common', buildGitUrl('common'))
janusAmqJob()
janusJob('openshift', 'okd', 'https://github.com/openshift/community.okd.git')
EapView.jobList(this, 'Ansible Janus', '^ansible-janus.*$')
//
// Job testing the default playbook of the downstream (Janus generated) collection
//
downstreamRunnerJob('jws','playbooks/playbook.yml', 'redhat_csp_download', '/webserver/5.6.0/jws-5.6.0-application-server.zip,/webserver/5.6.0/jws-5.6.0-application-server-RHEL8-x86_64.zip')
downstreamRunnerJob('eap', 'playbooks/playbook.yml', 'redhat_csp_download', '/eap7/7.4.0/jboss-eap-7.4.0.zip')
downstreamRunnerJob('sso', 'playbooks/keycloak.yml', 'runtimes_common', '/sso/7.6.0/rh-sso-7.6.0-server-dist.zip')
downstreamRunnerJob('amq_broker', 'playbooks/activemq.yml', 'runtimes_common', '/amq/broker/7.9.4/amq-broker-7.9.4-bin.zip')
EapView.jobList(this, 'Ansible Downstream Runner', '^ansible-downstream-runner-.*$')

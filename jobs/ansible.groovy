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

def janusJob(projectName, projectUpstreamName = projectName, playbook = "playbooks/job.yml") {
  new ansible.JanusBuilder(
        projectName: projectName,
        projectUpstreamName: projectUpstreamName,
        playbook: playbook,
        gitUrl: buildGitUrl(projectUpstreamName),
        jobPrefix: 'ansible-janus-',
        pathToScript: 'ansible-playbook.sh',
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
     checkoutProject: "False",
     gitUrl: "git@gitlab:ansible-middleware/" + dotProjectName,
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
//
// CI Jobs for Ansible Middleware
//
int upstreamProjectsPortOffsetstart = 22000
[ 'jws', 'wildfly', 'infinispan'].each { project -> upstreamCIJob(project, upstreamProjectsPortOffsetstart++) }
// upstreamCIJob('jbcs',upstreamProjectsPortOffsetstart++)
upstreamCIJob('keycloak', upstreamProjectsPortOffsetstart++, "default,overridexml")
upstreamCIJob('amq', upstreamProjectsPortOffsetstart++ , "default,amq_upgrade")
//new ansibleCi.Builder(projectName:'zeus', moleculeBuildId: 29001, gitUrl: "https://github.com/jboss-set/", branch: 'olympus').build(this)
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
['jws', 'eap'].each {  projectName -> dotJob(projectName, dotJobsPrefix, dotPortOffsetstart++) }
EapView.jobList(this, 'Ansible DOT', dotJobsPrefix + '.*$')

//
// CI Jobs for demos
//
int demoPortOffsetstart = 25000
// To fix : ['eap-migration-demo', 'jws-app-update-demo']
[ 'wildfly-cluster-demo', 'flange-demo'].each {  projectName -> demoJob(projectName, demoPortOffsetstart++) }
EapView.jobList(this, 'Ansible Demos', '^.*-demo')

//
// Janus jobs - generating downstream collections
//
janusJob('redhat_csp_download','redhat-csp-download')
janusJob('jws')
janusJob('eap', 'wildfly')
janusJob('data_grid', 'infinispan')
janusJob('sso', 'keycloak')
janusJob('amq_broker', 'amq')
EapView.jobList(this, 'Ansible Janus', '^ansible-janus.*$')
//
// Job testing the default playbook of the downstream (Janus generated) collection
//
downstreamRunnerJob('jws','playbooks/playbook.yml', 'redhat_csp_download', '/webserver/5.6.0/jws-5.6.0-application-server.zip,/webserver/5.6.0/jws-5.6.0-application-server-RHEL8-x86_64.zip')
downstreamRunnerJob('eap', 'playbooks/playbook.yml', 'redhat_csp_download', '/eap7/7.4.0/jboss-eap-7.4.0.zip')
downstreamRunnerJob('sso', 'playbooks/keycloak.yml', 'redhat_csp_download', '/sso/7.6.0/rh-sso-7.6.0-server-dist.zip')
EapView.jobList(this, 'Ansible Downstream Runner', '^ansible-downstream-runner-.*$')

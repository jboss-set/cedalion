def upstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all") {
  new ansible.MoleculeBuilder(
        projectName: projectName,
        moleculeBuildId: moleculeBuildId,
        scenarioName: scenarioName,
        jobPrefix: "ansible-ci-",
        pipelineFile: "pipelines/ansible-ci-pipeline",
        pathToScript: 'molecule.sh',
        podmanImage: 'localhost/molecule-runner',
    ).build(this)
}
def downstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all") {
  new ansible.MoleculeBuilder(
        projectName: projectName,
        moleculeBuildId: moleculeBuildId,
        scenarioName: scenarioName,
        jobPrefix: "ansible-downstream-ci-",
        pipelineFile: "pipelines/ansible-downstream-ci-pipeline",
        podmanImage: 'localhost/molecule-runner',
        pathToScript: "molecule-downstream.sh",
        checkoutProject: "False"
    ).build(this)
}

def janusJob(projectName, projectUpstreamName = projectName, playbook = "playbooks/job.yml") {
  new ansible.JanusBuilder(
        projectName: projectName,
        projectUpstreamName: projectUpstreamName,
        playbook: playbook,
        gitUrl: 'https://github.com/ansible-middleware/' + projectUpstreamName + '.git',
        jobPrefix: 'ansible-janus-',
        pipelineFile: 'pipelines/ansible-janus-pipeline',
        pathToScript: 'ansible-playbook.sh',
        podmanImage: 'localhost/ansible'
    ).build(this)
}

def dotJob(projectName, dotJobsPrefix, portOffset) {
   new ansible.MoleculeBuilder(
     projectName: projectName + "-dot",
     projectUpstreamName: projectName,
     jobPrefix: dotJobsPrefix,
     moleculeBuildId: portOffset,
     checkoutProject: "False",
     gitUrl: 'https://github.com/ansible-middleware/' + projectName + '.git',
     pipelineFile: 'pipelines/ansible-downstream-ci-pipeline',
     pathToScript: "molecule-downstream.sh",
     podmanImage: 'localhost/molecule-runner'
    ).build(this)
}

def demoJob(projectName, portOffset, jobPrefix = "ansible-") {
  new ansible.MoleculeBuilder(
      projectName: projectName,
      moleculeBuildId: portOffset,
      jobPrefix: jobPrefix,
      pipelineFile: "pipelines/ansible-ci-pipeline",
      pathToScript: "molecule.sh",
      podmanImage: 'localhost/molecule-runner'
  ).build(this)
}

def downstreamRunnerJob(projectName, playbook, collections, productPaths) {
  new ansible.RunnerBuilder(
        projectName: projectName,
        playbook: playbook,
        collections: collections,
        products_paths: productPaths,
        pipelineFile: 'pipelines/ansible-downstream-runner-pipeline',
        podmanImage: 'localhost/molecule-runner',
        pathToScript: 'ansible-validate-downstream-collection.sh',
        jobPrefix: 'ansible-downstream-runner-'
    ).build(this)
}
//
// CI Jobs for Ansible Middleware
//
int upstreamProjectsPortOffsetstart = 22000
[ 'jws', 'wildfly', 'infinispan', 'jbcs'].each { project -> upstreamCIJob(project, upstreamProjectsPortOffsetstart++) }
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
downstreamCIJob('amq', downstreamProjectsPortOffsetstart++, "default,amq_upgrade")
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
janusJob('redhat_csp_download')
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
downstreamRunnerJob('sso', 'playbooks/playbook.yml', 'redhat_csp_download', '/sso/7.6.0/rh-sso-7.6.0-server-dist.zip')
EapView.jobList(this, 'Ansible Downstream Runner', '^ansible-downstream-runner-.*$')

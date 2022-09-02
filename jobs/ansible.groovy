def upstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all") {
  new ansibleCi.Builder(projectName: projectName, moleculeBuildId: moleculeBuildId, scenarioName: scenarioName, downloadServerUrl: '').build(this)
}
def downstreamCIJob(projectName, moleculeBuildId, scenarioName = "--all") {
  new ansibleCi.Builder(projectName: projectName, moleculeBuildId: moleculeBuildId, scenarioName: scenarioName, projectPrefix: "ansible-downstream-ci", pipelineFile: "pipelines/ansible-downstream-ci-pipeline", pathToScript: "molecule-downstream.sh").build(this)
}
// CI Jobs for Ansible Middleware
//   Note that each CI job needs to increment the moleculeBuildId as
//   this translate into a port number for SSHd running on the slave
//   container (and thus, needs to be unique).
upstreamCIJob('jws', 22001)
upstreamCIJob('wildfly', 23001)
upstreamCIJob('infinispan', 25001)
upstreamCIJob('keycloak', 26001)
upstreamCIJob('amq', 27001, "default,amq_upgrade")
//new ansibleCi.Builder(projectName:'zeus', moleculeBuildId: 29001, gitUrl: "https://github.com/jboss-set/", branch: 'olympus').build(this)
EapView.jobList(this, 'Ansible CI', 'ansible-ci.*')
// CI jobs for downstream (Janus generated) collections
downstreamCIJob('jws', 30001)
downstreamCIJob('eap', 30002)
downstreamCIJob('jws-dot', 30003)
downstreamCIJob('amq', 30004, "default,amq_upgrade")
EapView.jobList(this, 'Ansible Downstream CI', 'ansible-downstream-ci.*$')
// DOT jobs
String dotJobsPrefix = "ansible-downstream-tests"
new ansibleCi.Builder(projectName: "jws-dot", projectUpstreamName: "jws", projectPrefix: dotJobsPrefix, pipelineFile: "pipelines/ansible-downstream-dot-pipeline", pathToScript: "molecule-downstream.sh", gitUrl: 'git@gitlab:ansible-middleware/', moleculeBuildId: 31001).build(this)
EapView.jobList(this, 'Ansible DOT CI', dotJobsPrefix + '.*$')
// CI Jobs for demos
new ansibleCi.Builder(projectName:'wildfly-cluster-demo', projectPrefix: 'ansible', moleculeBuildId: 40001).build(this)
new ansibleCi.Builder(projectName:'flange-demo', branch: 'master', projectPrefix: 'ansible', moleculeBuildId: 40002).build(this)
//new ansibleCi.Builder(projectName:'eap-migration-demo', branch: 'main', projectPrefix: 'ansible', moleculeBuildId: 41003).build(this)
//new ansibleCi.Builder(projectName:'jws-app-update-demo', branch: 'main', projectPrefix: 'ansible', moleculeBuildId: 42003).build(this)
EapView.jobList(this, 'Ansible Demos', '^.*-demo')
// Janus jobs - generating downstream collections
new ansible.Builder(projectName:'janus', jobSuffix: '-redhat_csp_download', playbook: 'playbooks/redhat_csp_download.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-jws', playbook: 'playbooks/jws.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-eap', playbook: 'playbooks/eap.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-jboss_data_grid', playbook: 'playbooks/jboss_data_grid.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-rh_sso', playbook: 'playbooks/rh_sso.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-amq_broker', playbook: 'playbooks/amq_broker.yml').build(this)
EapView.jobList(this, 'Ansible Janus', '^ansible-janus.*$')
// Job testing the default playbook of the downstream (Janus generated) collection
new ansibleDownstreamRunner.Builder(
  projectName: 'jws',
  playbook: 'playbooks/playbook.yml',
  collections: 'redhat_csp_download',
  products_paths: '/webserver/5.6.0/jws-5.6.0-application-server.zip,/webserver/5.6.0/jws-5.6.0-application-server-RHEL8-x86_64.zip'
  ).build(this)
new ansibleDownstreamRunner.Builder(
  projectName: 'eap',
  playbook: 'playbooks/playbook.yml',
  collections: 'redhat_csp_download',
  products_paths: '/eap7/7.4.5/jboss-eap-7.4.5.zip'
  ).build(this)
EapView.jobList(this, 'Ansible Downstream Runner', '^ansible-downstream-runner-.*$')

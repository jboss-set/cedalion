def downstreamCIJob(projectName, moleculeBuildId, projectPrefix = "ansible-downstream-ci", pipelineFile = "pipelines/ansible-downstream-ci-pipeline", pathToScript  = "molecule-downstream.sh") {
  new ansibleCi.Builder(projectName: projectName, projectPrefix: projectPrefix, pipelineFile: pipelineFile, pathToScript: pathToScript, moleculeBuildId: moleculeBuildId).build(this)
}
// Jobs to release productized version of Ansible Collection
new ansibleCollection.Builder(collectionName:'redhat-csp-download').build(this)
new ansibleCollection.Builder(collectionName:'jws-ansible-playbook').build(this)
new ansibleCollection.Builder(collectionName:'ansible_collections_jcliff').build(this)
new ansibleCollection.Builder(collectionName:'wildfly', downstreamName: 'jboss_eap').build(this)
new ansibleCollection.Builder(collectionName:'infinispan', downstreamName: 'jboss_data_grid').build(this)
new ansibleCollection.Builder(collectionName:'keycloak', downstreamName: 'rh_sso').build(this)
new ansibleCollection.Builder(collectionName:'amq', downstreamName: 'amq').build(this)
EapView.jobList(this, 'Ansible Collections', 'ansible-collection.*')
// CI Jobs for Ansible Middleware
//   Note that each CI job needs to increment the moleculeBuildId as
//   this translate into a port number for SSHd running on the slave
//   container (and thus, needs to be unique).
new ansibleCi.Builder(projectName:'jws-ansible-playbook', moleculeBuildId: 22001).build(this)
new ansibleCi.Builder(projectName:'wildfly', moleculeBuildId: 23001).build(this)
new ansibleCi.Builder(projectName:'infinispan', moleculeBuildId: 25001).build(this)
new ansibleCi.Builder(projectName:'keycloak', moleculeBuildId: 26001).build(this)
new ansibleCi.Builder(projectName:'amq', scenarioName: 'default,amq_upgrade', moleculeBuildId: 27001).build(this)
new ansibleCi.Builder(projectName:'jws-dot', moleculeBuildId: 28001, gitUrl: "git@gitlab:ansible-middleware/").build(this)
//new ansibleCi.Builder(projectName:'zeus', moleculeBuildId: 29001, gitUrl: "https://github.com/jboss-set/", branch: 'olympus').build(this)
EapView.jobList(this, 'Ansible CI', 'ansible-ci.*')
// CI jobs for downstream (Janus generated) collections
downstreamCIJob(projectName: 'jws-ansible-playbook', moleculeBuildId: 50001)
downstreamCIJob(projectName:  'jboss_eap', moleculeBuildId: 50002)
downstreamCIJob(projectName:'jws-dot', moleculeBuildId: 50003)
EapView.jobList(this, 'Ansible Downstream CI', 'ansible-downstream-ci.*$')
// CI Jobs for demos
new ansibleCi.Builder(projectName:'wildfly-cluster-demo', projectPrefix: 'ansible', moleculeBuildId: 40001).build(this)
new ansibleCi.Builder(projectName:'flange-demo', branch: 'master', projectPrefix: 'ansible', moleculeBuildId: 40002).build(this)
//new ansibleCi.Builder(projectName:'eap-migration-demo', branch: 'main', projectPrefix: 'ansible', moleculeBuildId: 41003).build(this)
//new ansibleCi.Builder(projectName:'jws-app-update-demo', branch: 'main', projectPrefix: 'ansible', moleculeBuildId: 42003).build(this)
EapView.jobList(this, 'Ansible Demos', '^.*-demo')
// Janus jobs - generating downstream collections
new ansible.Builder(projectName:'janus', jobSuffix: '-redhat_csp_download', playbook: 'playbooks/redhat_csp_download.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-jws-ansible-playbook', playbook: 'playbooks/jws.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-jboss_eap', playbook: 'playbooks/jboss_eap.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-jboss_data_grid', playbook: 'playbooks/jboss_data_grid.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-rh_sso', playbook: 'playbooks/rh_sso.yml').build(this)
new ansible.Builder(projectName:'janus', jobSuffix: '-amq', playbook: 'playbooks/activemq.yml').build(this)
EapView.jobList(this, 'Ansible Janus', '^ansible-janus.*$')
// Job testing the default playbook of the downstream (Janus generated) collection
new ansibleDownstreamRunner.Builder(
  projectName: 'jws',
  playbook: 'playbooks/playbook.yml',
  collections: 'redhat_csp_download',
  products_paths: '/webserver/5.6.0/jws-5.6.0-application-server.zip,/webserver/5.6.0/jws-5.6.0-application-server-RHEL8-x86_64.zip'
  ).build(this)
new ansibleDownstreamRunner.Builder(
  projectName: 'jboss_eap',
  playbook: 'playbooks/playbook.yml',
  collections: 'redhat_csp_download',
  products_paths: '/eap7/7.4.5/jboss-eap-7.4.5.zip'
  ).build(this)
EapView.jobList(this, 'Ansible Downstream Runner', '^ansible-downstream-runner-.*$')

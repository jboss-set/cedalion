// Jobs to release productized version of Ansible Collection
new ansibleCollection.Builder(collectionName:'redhat-csp-download').build(this)
new ansibleCollection.Builder(collectionName:'jws-ansible-playbook').build(this)
new ansibleCollection.Builder(collectionName:'ansible_collections_jcliff').build(this)
new ansibleCollection.Builder(collectionName:'wildfly', downstreamName: 'jboss_eap').build(this)
new ansibleCollection.Builder(collectionName:'infinispan', downstreamName: 'jboss_data_grid').build(this)
new ansibleCollection.Builder(collectionName:'keycloak', downstreamName: 'rh_sso').build(this)
EapView.jobList(this, 'Ansible Collections', 'ansible-collection.*')
// CI Jobs for Ansible Middleware
//   Note that each CI job needs to increment the moleculeBuildId as
//   this translate into a port number for SSHd running on the slave
//   container (and thus, needs to be unique).
new ansibleCi.Builder(projectName:'jws-ansible-playbook', moleculeBuildId: 22001).build(this)
new ansibleCi.Builder(projectName:'wildfly', moleculeBuildId: 23001).build(this)
new ansibleCi.Builder(projectName:'ansible_collections_jcliff', moleculeBuildId: 24001).build(this)
new ansibleCi.Builder(projectName:'infinispan', moleculeBuildId: 25001).build(this)
new ansibleCi.Builder(projectName:'keycloak', moleculeBuildId: 26001).build(this)
EapView.jobList(this, 'Ansible CI', 'ansible-ci.*')
new ansibleCi.Builder(projectName:'wildfly-cluster-demo', projectPrefix: 'ansible', moleculeBuildId: 27001).build(this)
new ansibleCi.Builder(projectName:'flange-demo', branch: 'master', projectPrefix: 'ansible', moleculeBuildId: 28001).build(this)
EapView.jobList(this, 'Ansible Demos', '^.*-demo')
// Job running an Ansible Playbook
new ansible.Builder(projectName:'janus', jobPrefix: '', jobSuffix: '-rh-csp-download', playbook: 'playbooks/redhat_csp_download.yml').build(this)
new ansible.Builder(projectName:'janus', jobPrefix: '', jobSuffix: '-jws', playbook: 'playbooks/jws.yml').build(this)
new ansible.Builder(projectName:'janus', jobPrefix: '', jobSuffix: '-eap', playbook: 'playbooks/jboss_eap.yml').build(this)
new ansible.Builder(projectName:'janus', jobPrefix: '', jobSuffix: '-jdg', playbook: 'playbooks/jboss_data_grid.yml').build(this)
new ansible.Builder(projectName:'janus', jobPrefix: '', jobSuffix: '-rhsso', playbook: 'playbooks/rh_sso.yml').build(this)
EapView.jobList(this, 'Ansible Janus', '^janus-.*$')

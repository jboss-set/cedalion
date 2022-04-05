// Jobs to release productized version of Ansible Collection
new ansible.Builder(collectionName:'redhat-csp-download').build(this)
new ansible.Builder(collectionName:'jws-ansible-playbook').build(this)
new ansible.Builder(collectionName:'ansible_collections_jcliff').build(this)
new ansible.Builder(collectionName:'wildfly', downstreamName: 'jboss_eap').build(this)
new ansible.Builder(collectionName:'infinispan', downstreamName: 'jboss_data_grid').build(this)
new ansible.Builder(collectionName:'keycloak', downstreamName: 'rh_sso').build(this)
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
new ansibleCi.Builder(projectName:'zeus', moleculeBuildId: 29001, gitUrl:'https://github.com/jboss-set/', branch: 'olympus').build(this)
EapView.jobList(this, 'Ansible CI', 'ansible-ci.*')
new ansibleCi.Builder(projectName:'wildfly-cluster-demo', projectPrefix: 'ansible', moleculeBuildId: 27001).build(this)
new ansibleCi.Builder(projectName:'flange-demo', branch: 'master', projectPrefix: 'ansible', moleculeBuildId: 28001).build(this)
EapView.jobList(this, 'Ansible Demos', '^.*-demo')

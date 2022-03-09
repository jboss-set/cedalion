import alignment.Builder

new alignment.Builder(id: 'component-alignment-eap-73', rule: 'eap-73', reportTitle: '[Olympus] JBoss EAP 7.3.x', logger_project_code: 'olympus-eap-7.3.x', jobSuffix: 'eap-73', repoUrl: 'git@github.com:jbossas/jboss-eap7.git', branch: '7.3.x', toAddress: EAP_COMP_ALIGMENT_TO_ADDRESS).build(this)
new alignment.Builder(id: 'component-alignment-eap-74', rule: 'eap-74', reportTitle: '[Olympus] JBoss EAP 7.4.x', logger_project_code: 'olympus-eap-7.4.x', jobSuffix: 'eap-74', repoUrl: 'git@github.com:jbossas/jboss-eap7.git', branch: '7.4.x', toAddress: EAP_COMP_ALIGMENT_TO_ADDRESS).build(this)

new alignment.Builder(id: 'component-alignment-elytron-web', rule: 'elytron-1x', reportTitle: '[Olympus] Elytron Web', logger_project_code: 'olympus-elytron-web-master', jobSuffix: 'elytron-web', repoUrl: 'git@github.com:wildfly-security/elytron-web.git', branch: 'master', toAddress: WFLY_COMP_ALIGMENT_TO_ADDRESS).build(this)
new alignment.Builder(id: 'component-alignment-wildfly-elytron', rule: 'elytron-1x', reportTitle: '[Olympus] Wildfly Elytron', logger_project_code: 'olympus-elytron-1.x', jobSuffix: 'wildfly-elytron', repoUrl: 'git@github.com:wildfly-security/wildfly-elytron.git', branch: '1.x', toAddress: WFLY_COMP_ALIGMENT_TO_ADDRESS).build(this)

new alignment.Builder(id: 'component-alignment-wildfly-core-74', rule: 'eap-74', reportTitle: '[Olympus] Wildfly Core 15.0.x (EAP 7.4)', logger_project_code: 'olympus-wildfly-core-eap-7.4.x', jobSuffix: 'wildfly-core-74', repoUrl: 'git@github.com:jbossas/wildfly-core-eap.git', branch: '15.0.x', toAddress: EAP_COMP_ALIGMENT_TO_ADDRESS).build(this)
new alignment.Builder(id: 'component-alignment-wildfly-core-73', rule: 'eap-73', reportTitle: '[Olympus] Wildfly Core 10.1.x (EAP 7.3)', logger_project_code: 'olympus-wildfly-core-eap-7.3.x', jobSuffix: 'wildfly-core-73', repoUrl: 'git@github.com:jbossas/wildfly-core-eap.git', branch: '10.1.x', toAddress: EAP_COMP_ALIGMENT_TO_ADDRESS).build(this)

new alignment.Builder(id: 'component-alignment-Wildfly', rule: 'wildfly-master', reportTitle: '[Olympus] Wildfly', logger_project_code: 'olympus-wildfly-master', jobSuffix: 'wildfly', repoUrl: 'git@github.com:wildfly/wildfly.git', branch: 'main', toAddress: WFLY_COMP_ALIGMENT_TO_ADDRESS).build(this)
new alignment.Builder(id: 'component-alignment-wildfly-core', rule: 'wildfly-master', reportTitle: '[Olympus] Wildfly Core', logger_project_code: 'olympus-wildfly-core-master', jobSuffix: 'wildfly-core', repoUrl: 'git@github.com:wildfly/wildfly-core.git', branch: 'main', toAddress: WFLY_COMP_ALIGMENT_TO_ADDRESS).build(this)

EapView.jobList(this, 'Component Alignment', 'component-alignment.*')

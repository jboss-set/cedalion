//new vbe.Builder(branch: '7.4.x', javaHome: "/opt/oracle/openjdk-11.0.14.1_1").buildAndTest(this)
//Disable 8, since its set up from custom branch for now
//new vbe.Builder(branch: '8.0.0.dev', javaHome: "/opt/oracle/openjdk-11.0.14.1_1", gitRepositoryUrl: 'git@github.com:jbossas/jboss-eap8.git',vbeRepositoryNames: '', vbeChannels: '').buildAndTest(this)

EapView.jobList(this, 'vbe', 'vbe-eap.*')

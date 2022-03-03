for (version in ["7.4.x", "7.3.x"]) {

    for (branch_suffix in ["", "-proposed", "-future"]) {
        new eap7.Builder(branch:version + branch_suffix).buildAndTest(this)
    }

    EapView.jobList(this, 'eap-' + version, 'eap-' + version.replace('x','*'))
}

new eap7.Builder(branch:'7.4.x', jobName:'eap-7.4.x-jdk17', parentJobname:'eap-7.4.x-build').test(this)

for (version in ["7.2.x", "7.1.x", "7.0.x"]) {

    new eap7.Builder(branch:version).buildAndTest(this)

    EapView.jobList(this, 'eap-' + version, 'eap-' + version.replace('x','*'))
}

new eap7.Builder(branch:'6.4.x',
                 mavenSettingsXml:'/opt/tools/settings-64.xml',
                 harmoniaScript: 'eap-job-64.sh',
                 gitRepositoryUrl: "git@github.com:jbossas/jboss-eap.git"
                ).buildAndTest(this)
EapView.jobList(this, 'eap-6.4.x', 'eap-6.4.*')

new eap7.Builder(branch:'main',
                 jobName: 'wildfly',
                 gitRepositoryUrl: "git@github.com:wildfly/wildfly.git",
                 javaHome: "/opt/oracle/openjdk-11.0.14.1_1"
                ).buildAndTest(this)
EapView.jobList(this, 'wildfly', 'wildfly.*')

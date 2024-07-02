for (version in ["7.4.x", "7.3.x"]) {

    for (branch_suffix in ["", "-proposed", "-future"]) {
        new eap7.Builder(branch:version + branch_suffix).buildAndTest(this)
    }

    EapView.jobList(this, 'eap-' + version, 'eap-' + version.replace('x','*'))
}

// EAP 7.4.x builds on JDK 8, tests on JDK 17
new eap7.Builder(branch:'7.4.x', jobName:'eap-7.4.x-jdk17',
        customParams: {
            stringParam {
                name("BUILD_OPTS")
                defaultValue("-Drelease -DallTests -Delytron -DskipTests")
            }
        }).build(this)

new eap7.Builder(branch:'7.4.x', jobName:'eap-7.4.x-jdk17', parentJobname:'eap-7.4.x-jdk17-build', javaHome: "/opt/oracle/jdk17-latest",
        customParams: {
            stringParam {
                name("TESTSUITE_OPTS")
                defaultValue("-Delytron -DnoCompile")
            }
            stringParam {
                name("CGROUP_MOUNT_ENABLED")
                defaultValue("true")
            }
        }).test(this)

for (version in ["7.2.x", "7.1.x", "7.0.x"]) {

    new eap7.Builder(branch:version).buildAndTest(this)
    new eap7.Builder(branch:version + "-proposed").buildAndTest(this)

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
                 javaHome: "/opt/oracle/jdk17-latest"
                ).buildAndTest(this)
EapView.jobList(this, 'wildfly', 'wildfly.*')

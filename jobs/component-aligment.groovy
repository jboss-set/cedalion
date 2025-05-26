import alignment.Builder
import alignment.channel.Builder as ChannelBuilder

def defaultBinaryVersion = '1.0.5'
def defaultFromAddr = 'thofman@redhat.com'
def defaultLoggerUri = 'https://component-upgrade-logger.apps.int.gpc.ocp-hub.prod.psi.redhat.com/api'

new Builder(jobName: 'component-alignment-wildfly-main',
        projectRepositoryUrl: 'https://github.com/wildfly/wildfly.git',
        projectRepositoryBranch: 'main',
        configRepositoryUrl: 'https://github.com/jboss-set/alignment-report-configs.git',
        configRepositoryBranch: 'main',
        configFile: 'rules-wildfly-main.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'wildfly-main',
        subject: 'Possible component upgrades report - wildfly:main',
        fromAddr: defaultFromAddr,
        toAddr: 'wildfly-dev@lists.jboss.org,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-wildfly-core-main',
        projectRepositoryUrl: 'https://github.com/wildfly/wildfly-core.git',
        projectRepositoryBranch: 'main',
        configRepositoryUrl: 'https://github.com/jboss-set/alignment-report-configs.git',
        configRepositoryBranch: 'main',
        configFile: 'rules-wildfly-main.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'wildfly-core-main',
        subject: 'Possible component upgrades report - wildfly-core:main',
        fromAddr: defaultFromAddr,
        toAddr: 'wildfly-dev@lists.jboss.org,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-elytron',
        projectRepositoryUrl: 'https://github.com/wildfly-security/wildfly-elytron.git',
        projectRepositoryBranch: '2.x',
        configRepositoryUrl: 'https://github.com/jboss-set/alignment-report-configs.git',
        configRepositoryBranch: 'main',
        configFile: 'rules-elytron-1.x.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'elytron-2.x',
        subject: 'Possible component upgrades report - wildfly-elytron:2.x',
        fromAddr: defaultFromAddr,
        toAddr: 'wildfly-dev@lists.jboss.org,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-elytron-web',
        projectRepositoryUrl: 'https://github.com/wildfly-security/elytron-web.git',
        projectRepositoryBranch: '4.x',
        configRepositoryUrl: 'https://github.com/jboss-set/alignment-report-configs.git',
        configRepositoryBranch: 'main',
        configFile: 'rules-elytron-1.x.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'elytron-web-4.x',
        subject: 'Possible component upgrades report - elytron-web:4.x',
        fromAddr: defaultFromAddr,
        toAddr: 'wildfly-dev@lists.jboss.org,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-jboss-eap-7.4.x',
        projectRepositoryUrl: 'git@github.com:jbossas/jboss-eap7.git',
        projectRepositoryBranch: '7.4.x',
        configRepositoryUrl: 'https://gitlab.cee.redhat.com/jboss-set/dependency-alignment-configs.git',
        configRepositoryBranch: 'master',
        configFile: 'rules-eap-74.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'eap-7.4.x',
        subject: 'Possible component upgrades report - jboss-eap:7.4.x',
        fromAddr: defaultFromAddr,
        toAddr: 'jboss-set-ops@redhat.com,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-wildfly-core-eap-15.0.x',
        projectRepositoryUrl: 'git@github.com:jbossas/wildfly-core-eap.git',
        projectRepositoryBranch: '15.0.x',
        configRepositoryUrl: 'https://gitlab.cee.redhat.com/jboss-set/dependency-alignment-configs.git',
        configRepositoryBranch: 'master',
        configFile: 'rules-eap-74.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'wildfly-core-eap-15.0.x',
        subject: 'Possible component upgrades report - wildfly-core-eap:15.0.x (EAP 7.4)',
        fromAddr: defaultFromAddr,
        toAddr: 'jboss-set-ops@redhat.com,thofman@redhat.com'
).build(this)

new ChannelBuilder(jobName: 'component-alignment-jboss-eap-8.0.x',
        subject: 'Possible component upgrades report - jboss-eap:8.0.x',
        fromAddr: defaultFromAddr,
        toAddr: 'jboss-set-ops@redhat.com,thofman@redhat.com'
).build(this)

new Builder(jobName: 'component-alignment-wildfly-core-eap-21.0.x',
        projectRepositoryUrl: 'git@github.com:jbossas/wildfly-core-eap.git',
        projectRepositoryBranch: '21.0.x',
        configRepositoryUrl: 'https://gitlab.cee.redhat.com/jboss-set/dependency-alignment-configs.git',
        configRepositoryBranch: 'master',
        configFile: 'rules-eap-80.json',
        binaryVersion: defaultBinaryVersion,
        loggerUri: defaultLoggerUri,
        loggerCode: 'wildfly-core-eap-21.0.x',
        subject: 'Possible component upgrades report - wildfly-core-eap:21.0.x (EAP 8.0)',
        fromAddr: defaultFromAddr,
        toAddr: 'jboss-set-ops@redhat.com,thofman@redhat.com'
).build(this)

EapView.jobList(this, 'Component Alignment', 'component-alignment.*')

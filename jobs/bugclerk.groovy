new bugclerk.Builder(
            jobName: 'bugclerk-reports-jira-eap740-unresolved',
            branch: 'EAP_740'
        ).build(this)

EapView.jobList(this, 'bugclerk', 'bugclerk.*')

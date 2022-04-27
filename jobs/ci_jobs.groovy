new ci_jobs.Builder(repoName: 'aphrodite').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'assistant').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'bug-clerk').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'cryo').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'mjolnir', javaHome: "/opt/oracle/openjdk-11.0.14.1_1").buildMvnJob(this)

new ci_jobs.Builder(repoName: 'prbz-overview').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'harmonia', branch: 'olympus').buildBashJob(this)

new ci_jobs.Builder(jobName: 'ci-harmonia-cci', repoName: 'harmonia', branch: 'olympus-cci').buildBashJob(this)

new ci_jobs.Builder(repoName: 'maven-vbe').buildMvnJob(this)

EapView.jobList(this, 'SET CI', 'ci.*')

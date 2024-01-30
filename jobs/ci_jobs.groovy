new ci_jobs.Builder(repoName: 'aphrodite').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'bug-clerk').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'cryo').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'mjolnir', javaHome: "/opt/oracle/openjdk11-latest").buildMvnJob(this)

new ci_jobs.Builder(repoName: 'prbz-overview').buildMvnJob(this)

new ci_jobs.Builder(repoName: 'harmonia', branch: 'main').buildBashJob(this)

new ci_jobs.Builder(jobName: 'ci-harmonia-cci', repoName: 'harmonia', branch: 'olympus-cci').buildBashJob(this)

new ci_jobs.Builder(repoName: 'maven-vbe', javaHome: "/opt/oracle/openjdk11-latest").buildMvnJob(this)

EapView.jobList(this, 'SET CI', 'ci.*')

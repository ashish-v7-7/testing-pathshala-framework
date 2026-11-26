# Local Jenkins for the demo (Docker)

A throwaway Jenkins on your own computer that runs the repo's `Jenkinsfile`. Everything is configured as code:

| File | What it does |
|---|---|
| `Dockerfile` | `jenkins/jenkins:lts-jdk21` + Maven, Chromium + chromedriver, Firefox ESR, browser libraries, plugins |
| `plugins.txt` | plugins (pinned versions) installed by `jenkins-plugin-cli` |
| `casc.yaml` | Jenkins Configuration as Code: no setup wizard, Allure tool 2.46.2, the pipeline job |

The job `testing-pathshala-framework` is "Pipeline script from SCM": it clones `file:///repo` (this repo,
mounted read-only into the container), branch `main`, and runs `Jenkinsfile`.

**Security:** this Jenkins has NO login (`authorizationStrategy: unsecured`). That is only OK because it listens on
`127.0.0.1` (your own computer) and is deleted after the demo. Never expose it to a network and never put real
credentials in it.

## Start

Run from the repo root (the folder with `Jenkinsfile`). The repo needs at least one git commit on `main`.

```bash
docker build -t pathshala-jenkins:local ci/jenkins

docker run -d --name pathshala-jenkins --shm-size=1g \
  -p 127.0.0.1:8080:8080 \
  -v pathshala-jenkins-home:/var/jenkins_home \
  -v "$(pwd):/repo:ro" \
  pathshala-jenkins:local
```

Open http://localhost:8080 (about 20 seconds after start). If port 8080 is already used on your computer, use
for example `-p 127.0.0.1:8088:8080 -e JENKINS_URL=http://localhost:8088/` and open http://localhost:8088.

## Run the job

In the UI: **testing-pathshala-framework** -> **Build Now**. The very first build runs with the default values
(`MODULE=all`) and registers the parameters from the `Jenkinsfile`; after that the button is
**Build with Parameters** (MODULE, BROWSER, HEADLESS).

From the command line (Jenkins wants a CSRF "crumb" for POST requests):

```bash
J=http://localhost:8080
CRUMB=$(curl -s -c /tmp/cj $J/crumbIssuer/api/json | python3 -c 'import sys,json;d=json.load(sys.stdin);print(d["crumbRequestField"]+":"+d["crumb"])')
curl -b /tmp/cj -H "$CRUMB" -X POST "$J/job/testing-pathshala-framework/buildWithParameters?MODULE=all&BROWSER=chrome&HEADLESS=true"
curl -s "$J/job/testing-pathshala-framework/lastBuild/api/json?tree=number,building,result"
```

After a build: **Allure Report** (left menu of the build) shows the combined report of all modules, **Test Result**
shows the JUnit view, **Pipeline Overview / Stage View** shows the stages.

## Stop, restart, clean up

```bash
docker stop pathshala-jenkins          # stop (history kept in the volume)
docker start pathshala-jenkins         # start again
docker rm -f pathshala-jenkins         # remove the container (volume and image stay)
docker volume rm pathshala-jenkins-home   # forget all builds and history
docker rmi pathshala-jenkins:local     # remove the image
```

After changing `casc.yaml` or `plugins.txt`, rebuild the image and start a new container.

## Notes

- **Chromium in Docker needs `--no-sandbox`**: Docker's default seccomp profile blocks the user namespaces the
  Chromium sandbox uses ("Chrome instance exited"). The Dockerfile adds the flag in `/etc/chromium.d/`, which the
  Debian `chromium` launcher reads, so the test code stays the same as on a laptop.
- `--shm-size=1g`: Docker gives containers only 64 MB of `/dev/shm`; browsers like more.
- Selenium Manager finds the Debian `chromium` + `chromedriver` by itself (also on ARM64 Macs, where Google Chrome
  for Linux does not exist).
- Playwright downloads its own Chromium in the pipeline (`install chromium`); the test step sets
  `PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1` so it does not download Firefox and WebKit as well.
- `-Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true` allows the `file:///repo` URL;
  `git config --system safe.directory '*'` stops git complaining that `/repo` belongs to another user.

## Versions (2026-11-26)

| Item | Version |
|---|---|
| Jenkins (jenkins/jenkins:lts-jdk21) | 2.580.3, Java 21.0.13 |
| Maven (Debian 13 package) | 3.9.9 |
| Chromium / chromedriver (Debian) | 156.0.8078.108 |
| Firefox ESR (Debian) | 153.8.0esr |
| Allure commandline (tool, from Maven Central) | 2.46.2 |
| workflow-aggregator (Pipeline) | 608.v67378e9d3db_1 |
| pipeline-stage-view | 2.41 |
| git | 5.11.0 |
| junit | 1458.1460.vd53b_087eeb_2c (newest for this Jenkins LTS) |
| allure-jenkins-plugin | 3.0.0 |
| timestamper | 1.30 |
| configuration-as-code | 2150.v9cb_6a_756737e |
| job-dsl | 3752.vb_199526b_20c3 |

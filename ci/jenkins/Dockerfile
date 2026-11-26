# Local Jenkins for the Testing Ki Pathshala demo (NOT for production).
# Jenkins LTS + JDK 21 + Maven + Chromium + Firefox ESR + the plugins we need,
# configured as code (casc.yaml): no setup wizard, Allure tool and the pipeline job are ready.
FROM jenkins/jenkins:lts-jdk21

USER root
# Maven, browsers for Selenium/Cucumber (chromium + chromedriver, firefox-esr),
# fonts and libraries that Playwright's own Chromium also needs.
RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      maven chromium chromium-driver firefox-esr \
      fonts-liberation fonts-noto-color-emoji \
      libnss3 libatk-bridge2.0-0 libgbm1 libxkbcommon0 libxcomposite1 libxdamage1 \
      libxrandr2 libasound2 libpango-1.0-0 libcairo2 libcups2 \
 && rm -rf /var/lib/apt/lists/* \
 && echo 'export CHROMIUM_FLAGS="$CHROMIUM_FLAGS --no-sandbox"' > /etc/chromium.d/no-sandbox \
 && git config --system --add safe.directory '*'
# --no-sandbox: Docker's default seccomp profile blocks the user namespaces that the Chromium sandbox needs.
# The Debian /usr/bin/chromium launcher reads /etc/chromium.d/*, so the tests' code stays unchanged.

USER jenkins
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY --chown=jenkins:jenkins casc.yaml /usr/share/jenkins/ref/casc.yaml
ENV CASC_JENKINS_CONFIG=/usr/share/jenkins/ref/casc.yaml
# skip the setup wizard; allow the pipeline job to clone the repo mounted at /repo (file:///repo)
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true"

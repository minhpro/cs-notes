## How to use Gitlab CI

### Clone your repository

Go to your staging server and clone your project to it, assume that your project places on the $PROJECT_HOME (path to your project).

Before cloning, you need to add your staging ssh-key to your repository.

### Install Gitlab runner

Install Gitlab runner on your deployment server, see the below guideline

https://docs.gitlab.com/runner/install/

### Register Gitlab runner

After installing Gitlab runner, you have to register it to your Gitlab.

https://docs.gitlab.com/runner/register/

### Setup CICD environment variables

Go to your repository on your Gitlab, then go to Settings > CICD

In the Variables section, adding following variables:

* PROJECT_HOME: path to your project on the staging server (have cloned)
* STAGING_USER: user to ssh to the staging server
* STAGING_PASSWORD: password to ssh to the staging server
* SSH_PRIVATE_KEY: private ssh key of your STAGING_USER

### Run gitlab pipline

The pipeline to build your project on the staging server is configured in the file `.gitlab-ci.yml`

Whenever the `develop` branch have been updated, the Gitlab will run a CICD pipeline (configured in the `.gitlab-ci.yml`) to build and deploy your project into the staging server.

#!/bin/sh
set -e

SONAR_HOST_URL = https://sonarcloud.io
SONAR_ORGANIZATION = twiewiora-github
SONAR_TOKEN = dba89d9bda7bbf2e2499e6378d4414bfd2164004

# And run the analysis
# It assumes that the project uses Maven and has a POM at the root of the repo
if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
	mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
		-Dsonar.host.url=$SONAR_HOST_URL \
		-Dsonar.organization=twiewiora-github \
		-Dsonar.login=$SONAR_TOKEN
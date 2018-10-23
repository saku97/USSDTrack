#!/bin/sh
set -e

export PUBLISH_BRANCH=${PUBLISH_BRANCH:-master}

#setup git
git config --global user.email "noreply@travis.com"
git config --global user.name "Travis CI"

#clone the repository
git clone --quiet --branch=apk https://CloudyPadmal:$GITHUB_API_KEY@github.com/CloudyPadmal/USSDTrack apk > /dev/null

echo "List of files in current directory"
ls -la

cd apk

\cp -r ../app/build/outputs/apk/*/**.apk .
\cp -r ../app/build/outputs/apk/debug/output.json debug-output.json
\cp -r ../app/build/outputs/apk/release/output.json release-output.json

# Signing Apps

if [ "$TRAVIS_BRANCH" == "$PUBLISH_BRANCH" ]; then
    echo "Push to main branch detected, generating apk..."
    # Rename apks with dev prefixes
    mv app-debug.apk app-dev-debug.apk
    mv app-release-unsigned.apk app-dev-release.apk
    # Push generated apk files to apk branch
    git checkout apk
    git add -A
    git commit -am "Travis build pushed [Padmal]"
    git push origin apk --force --quiet> /dev/null
fi

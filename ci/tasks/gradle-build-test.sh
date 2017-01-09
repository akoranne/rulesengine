#!/bin/sh

set -e # fail fast
set -x # print commands

# The src from the 'develop' branch, injected by concourse
srcdir=`pwd`/source-code-develop

# change to the code repo (injected)
cd source-code

gradle -v

# do a clean build using gradle, and execute all tests.
gradle clean test assemble;


# if everything succeeds, assemble the archive (will be in the build/libs)
#	gradle assemble;


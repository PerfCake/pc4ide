#!/bin/bash
#
# set-version.sh
# ==============
# Sets version of the project.
#
# usage: set-version.sh <new-version> <intellij-home>

function print_usage {
    echo "usage: $0 <new-version> <intellij-home>"
}

if [[ $# -ne 2 ]]; then
    >&2 print_usage
    exit 1
fi


if ! which mvn > /dev/null 2>&1 ; then
    >&2 echo "ERROR: maven not installed. Please install maven"
    exit 2
fi



REPO_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $REPO_DIR && cd .. # set project root as working directory

# set maven modules versions
mvn versions:set -DnewVersion="$1" -Dintellij.home="$2"

# set intllij plugin version
sed -i "s:<version>.*</version>:<version>$1</version>:" pc4ide-intellij/src/main/resources/META-INF/plugin.xml



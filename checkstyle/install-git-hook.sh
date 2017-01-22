#!/bin/bash
#
# This scripts install git pre-commit hook that checks code
# conventions using checkstyle.

CHECKSTYLE_URL="https://sourceforge.net/projects/checkstyle/files/checkstyle/7.4/checkstyle-7.4-all.jar/download"

# get path absolute path to repository
CHECKSTYLE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
REPO_DIR="$(realpath $CHECKSTYLE_DIR/..)"
CHECKSTYLE_JAR_NAME="checsktyle-all.jar"
CHECKSTYLE_CONFIG="checks.xml"
HOOK_NAME="pre-commit-hook"

# check if the checstyle jar exists in the repository
if [ ! -f $CHECKSTYLE_DIR/$CHECKSTYLE_JAR_NAME ]; then
	echo "Downloading checkstyle jar..."
	curl -L -# -o "$CHECKSTYLE_DIR/$CHECKSTYLE_JAR_NAME" "$CHECKSTYLE_URL"
fi


PRE_COMMIT_HOOK="$REPO_DIR/.git/hooks/pre-commit"

if [ -f $PRE_COMMIT_HOOK ]; then
	if ! cmp "$PRE_COMMIT_HOOK" "$CHECKSTYLE_DIR/$HOOK_NAME" 2>/dev/null ; then
		echo "Different commit hook already exists. If you will continue then it will be overwritten."
		cp -i "$CHECKSTYLE_DIR/$HOOK_NAME" "$PRE_COMMIT_HOOK"
		if ! cmp "$PRE_COMMIT_HOOK" "$CHECKSTYLE_DIR/$HOOK_NAME" 2>/dev/null ; then
			echo "Aborting..."
			exit 1
		fi
	fi
else
	cp "$CHECKSTYLE_DIR/$HOOK_NAME" "$PRE_COMMIT_HOOK"
	chmod +x $PRE_COMMIT_HOOK
fi



cd $REPO_DIR

git config --local checkstyle.jar "$CHECKSTYLE_DIR/$CHECKSTYLE_JAR_NAME"
git config --local checkstyle.config "$CHECKSTYLE_DIR/$CHECKSTYLE_CONFIG"

# check if java exists
if ! command -v java > /dev/null; then
	echo "Hook was successfully installed, but Java was not found on your path."
	echo "Either add java to path or use following command:"
	echo " $ git config --add java.command <path to java executale>"
fi


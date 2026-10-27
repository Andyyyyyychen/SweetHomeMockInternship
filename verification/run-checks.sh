#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
: "${JAVA_HOME:?Set JAVA_HOME to a Java 8 JDK}"
check_dir=$(mktemp -d "${TMPDIR:-/tmp}/sweethome-checks.XXXXXX")
trap 'rm -rf "$check_dir"' EXIT
"$JAVA_HOME/bin/javac" -encoding UTF-8 -cp 'target/classes:SweetHome3D/lib/*' -d "$check_dir" verification/PrintStateCheck.java verification/AcceptanceCheck.java
"$JAVA_HOME/bin/java" -Djava.awt.headless=true -cp "$check_dir:target/classes:SweetHome3D/lib/*" PrintStateCheck
"$JAVA_HOME/bin/java" -cp "$check_dir:target/classes:SweetHome3D/lib/*" AcceptanceCheck print
python3 test_submission.py

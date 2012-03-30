#!/bin/bash

cdir=`pwd`
JAVA_HOME=/usr/java/j2sdk
srcdir=$cdir/src
classdir=$cdir/classes/

export CLASSPATH=.:$classdir
libfilelist=`find $cdir/lib -name "*.jar" -print`
for lf in $libfilelist
do
  export CLASSPATH=$CLASSPATH:$lf
done
echo $CLASSPATH
$JAVA_HOME/bin/java -jar dist/arrangement.jar

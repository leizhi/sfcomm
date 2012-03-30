@echo off

set PROPJECT_HOME=%cd%
set SRC_HOME=%cd%\src
set CLASSES_HOME=%cd%\classes
set CLASSPATH=.;%CLASSES_HOME%

for /r %PROPJECT_HOME%\lib %%i in (*.jar) do call set CLASSPATH=%%CLASSPATH%%;%%i

echo %CLASSPATH%

java -jar dist/arrangement.jar

@echo on
@pause

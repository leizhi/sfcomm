@echo off

set PROPJECT_HOME=%cd%
set CATALINA_HOME=D:\apache-tomcat-6.0.20
set SRC_HOME=%cd%\src

set CLASSES_HOME=%cd%\WEB-INF/classes
set CLASSPATH=.;%CLASSES_HOME%

for /r %CATALINA_HOME%\lib %%i in (*.jar) do call set CLASSPATH=%%CLASSPATH%%;%%i

echo %CLASSPATH%

for /r %PROPJECT_HOME%\WEB-INF\lib %%i in (*.jar) do call set CLASSPATH=%%CLASSPATH%%;%%i

echo %CLASSPATH%

for /r %PROPJECT_HOME%\WEB-INF\src %%i in (*.java) do (

@echo javac %%i to %CLASSES_HOME%
javac -encoding UTF-8 -d %CLASSES_HOME% %%i
)

@echo on
@pause

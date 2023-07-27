@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  retro-converter startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and RETRO_CONVERTER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED" "--illegal-access=deny"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\cfg;%APP_HOME%\lib\retro-converter-5.3.4.7.jar;%APP_HOME%\lib\confluence-rest-client-7.0.1-m126.jar;%APP_HOME%\lib\confluence-rest-api-7.0.1-m126.jar;%APP_HOME%\lib\confluence-java-api-7.0.1-m126.jar;%APP_HOME%\lib\json-parser-1.1.jar;%APP_HOME%\lib\MetaModel-csv-5.3.0.jar;%APP_HOME%\lib\opencsv-5.6.jar;%APP_HOME%\lib\commons-text-1.9.jar;%APP_HOME%\lib\commons-lang3-3.12.0.jar;%APP_HOME%\lib\jackson-dataformat-csv-2.13.3.jar;%APP_HOME%\lib\jackson-databind-2.13.3.jar;%APP_HOME%\lib\cxf-rt-transports-http-3.5.2.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.9.jar;%APP_HOME%\lib\jcl-over-slf4j-1.7.9.jar;%APP_HOME%\lib\log4j-over-slf4j-1.7.9.jar;%APP_HOME%\lib\log4j-slf4j-impl-2.11.0.jar;%APP_HOME%\lib\MetaModel-core-5.3.0.jar;%APP_HOME%\lib\json-path-2.4.0.jar;%APP_HOME%\lib\slf4j-api-1.8.0-alpha2.jar;%APP_HOME%\lib\jackson-annotations-2.13.3.jar;%APP_HOME%\lib\jackson-core-2.13.3.jar;%APP_HOME%\lib\commons-beanutils-1.9.4.jar;%APP_HOME%\lib\commons-collections4-4.4.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\cxf-core-3.5.2.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\confluence-rest-serialization-7.0.1-m126.jar;%APP_HOME%\lib\atlassian-util-concurrent-3.0.0.jar;%APP_HOME%\lib\jersey-client-1.19.4.jar;%APP_HOME%\lib\jersey-multipart-1.19.4.jar;%APP_HOME%\lib\future-converter-java8-guava-1.2.0.jar;%APP_HOME%\lib\jackson-jaxrs-1.9.13-atlassian-3.jar;%APP_HOME%\lib\jackson-xc-1.9.13-atlassian-3.jar;%APP_HOME%\lib\jackson-mapper-asl-1.9.13-atlassian-3.jar;%APP_HOME%\lib\jackson-core-asl-1.9.13-atlassian-3.jar;%APP_HOME%\lib\joda-time-2.9.9.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\fugue-2.7.0.jar;%APP_HOME%\lib\fugue-4.7.2.jar;%APP_HOME%\lib\future-converter-guava-common-1.2.0.jar;%APP_HOME%\lib\guava-26.0-jre.jar;%APP_HOME%\lib\atlassian-annotations-2.2.1.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\sal-api-4.0.0-e057c61.jar;%APP_HOME%\lib\atlassian-graphql-annotations-1.3.7.jar;%APP_HOME%\lib\checker-qual-2.8.1.jar;%APP_HOME%\lib\error_prone_annotations-2.3.2.jar;%APP_HOME%\lib\log4j-core-2.11.0.jar;%APP_HOME%\lib\log4j-api-2.11.0.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\json-smart-2.3.jar;%APP_HOME%\lib\jaxb-runtime-2.3.5.jar;%APP_HOME%\lib\woodstox-core-6.2.7.jar;%APP_HOME%\lib\xmlschema-core-2.3.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jersey-core-1.19.4.jar;%APP_HOME%\lib\jsr311-api-1.1.1.jar;%APP_HOME%\lib\mimepull-1.9.3.jar;%APP_HOME%\lib\future-converter-java8-common-1.2.0.jar;%APP_HOME%\lib\future-converter-common-1.2.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.14.jar;%APP_HOME%\lib\accessors-smart-1.2.jar;%APP_HOME%\lib\txw2-2.3.5.jar;%APP_HOME%\lib\istack-commons-runtime-3.0.12.jar;%APP_HOME%\lib\jakarta.activation-1.2.2.jar;%APP_HOME%\lib\stax2-api-4.2.1.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\asm-5.0.4.jar

@rem Execute retro-converter
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %RETRO_CONVERTER_OPTS%  -classpath "%CLASSPATH%" com.smarttrade.retro_converter.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable RETRO_CONVERTER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%RETRO_CONVERTER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

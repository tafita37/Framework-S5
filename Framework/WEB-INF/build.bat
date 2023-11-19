cd "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Framework\Framework\WEB-INF"
javac -d . src/annotation/*.java
javac -d . src/upload/*.java
javac -d . src/url/*.java
javac -d . src/ETU1863/framework/*.java
javac -d . src/ETU1863/framework/servlet/*.java
rmdir /s /q classes
mkdir classes
move /Y annotation classes/
move /Y ETU1863 classes/
move /Y url classes/
move /Y upload classes/
@REM jar cvf ETU1863.jar -C classes/ .
jar cvf Framework.jar -C classes/ .
set CLASSPATH=%CLASSPATH%;C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Framework\Framework\WEB-INF\Framework.jar
del ..\..\Test-Framework\WEB-INF\lib\Framework.jar
copy Framework.jar ..\..\Test-Framework\WEB-INF\lib\Framework.jar
cd ../../Test-Framework/WEB-INF
javac -d . src/controllers/*.java
rmdir /s /q classes
mkdir classes
move /Y controllers classes/
cd ..
jar -cvf Test-Framework.war WEB-INF/*
move /Y Test-Framework.war "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps"
move /Y *.jsp "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Test-Framework"
cd "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Framework\Framework\WEB-INF"
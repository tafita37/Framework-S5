javac -d . src/annotation/*.java
javac -d . src/ETU1863/framework/*.java
javac -d . src/url/*.java
javac -d . src/ETU1863/framework/servlet/*.java
javac -d . src/model/*.java
rmdir /s /q classes
mkdir classes
move /Y annotation classes/
move /Y ETU1863 classes/
move /Y model classes/
move /Y url classes/
jar cvf Framework.jar -C classes/ .
set CLASSPATH=%CLASSPATH%;C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Framework\Framework\WEB-INF\Framework.jar
del ..\Test-Framework\WEB-INF\lib\*.jar
copy Framework.jar ..\Test-Framework\WEB-INF\lib\*.jar
cd ../Test-Framework/WEB-INF
javac -d . src/controllers/*.java
rmdir /s /q classes
mkdir classes
move /Y controllers classes/
cd ..
jar -cvf Test-Framework.war WEB-INF/*
move /Y Test-Framework.war "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps"
cd "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\Framework\Framework\WEB-INF>"
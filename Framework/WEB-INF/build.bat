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
tar xvf launch4j.tar.xz
mvn install:install-file -Dfile=lib/activation.jar -DgroupId=activation -DartifactId=activation -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/bridj-0.6.2.jar -DgroupId=bridj-0.6.2 -DartifactId=bridj-0.6.2 -Dversion=0.6.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/commons-email-1.5.jar -DgroupId=commons-email-1.5 -DartifactId=commons-email-1.5 -Dversion=1.5 -Dpackaging=jar
mvn install:install-file -Dfile=lib/commons-email-1.5-javadoc.jar -DgroupId=commons-email-1.5-javadoc -DartifactId=commons-email-1.5-javadoc -Dversion=1.5 -Dpackaging=jar
mvn install:install-file -Dfile=lib/dsn.jar -DgroupId=dsn -DartifactId=dsn -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/imap.jar -DgroupId=imap -DartifactId=imap -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jnativehook.jar -DgroupId=jnativehook -DartifactId=jnativehook -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/mailapi.jar -DgroupId=mailapi -DartifactId=mailapi -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/pop3.jar -DgroupId=pop3 -DartifactId=pop3 -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/slf4j-api-1.7.2.jar -DgroupId=slf4j-api-1.7.2 -DartifactId=slf4j-api-1.7.2 -Dversion=1.7.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/webcam-capture-0.3.10.jar -DgroupId=webcam-capture-0.3.10 -DartifactId=webcam-capture-0.3.10 -Dversion=0.3.10 -Dpackaging=jar
mvn install:install-file -Dfile=lib/smtp.jar -DgroupId=smtp -DartifactId=smtp -Dversion=0.1 -Dpackaging=jar
mvn clean compile assembly:single

FROM openjdk:8
VOLUME /tmp
COPY sql-boot.zip sql-boot.zip
RUN sh -c 'mkdir sql-boot'
RUN sh -c 'unzip sql-boot.zip -d sql-boot'
RUN sh -c 'touch /sql-boot/sql-boot.jar'
ENV JAVA_OPTS=""
WORKDIR /sql-boot
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dloader.path='.' -jar ./sql-boot.jar" ]
MAINTAINER "mgramin@gmail.com"

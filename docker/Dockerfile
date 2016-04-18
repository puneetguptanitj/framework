FROM isuper/java-oracle 

RUN echo deb http://repos.mesosphere.io/ubuntu trusty main > /etc/apt/sources.list.d/mesosphere.list && \
apt-get update && \
apt-get -y --force-yes install mesos=0.24.1-0.2.35.ubuntu1404

ADD ./mesos-framework-1-SNAPSHOT.jar /bin/mesos-framework-1-SNAPSHOT.jar

CMD ["java", "-Xms1024m", "-Xmx1024m", "-jar",  "/bin/mesos-framework-1-SNAPSHOT.jar"]

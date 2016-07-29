Camel Router Project for Blueprint (OSGi)
=========================================

To run this project with in a JBoss Fuse 6.2.1 container requires some additional
steps to normal due to the version of the Cassandra client drivers that the
camel-cassandraql component ships with which means that the shipped camel-cassandraql 
component is only compatible with Apache Cassandra 2.x .

Build
=====

mvn clean install -DskipTests


Additional Steps
================

Prior to starting Fuse (THIS IS VERY IMPORTANT) copy the file

system/org/apache/camel/camel-cassandraql/2.15.1.redhat-621084/camel-cassandraql-2.15.1.redhat-621084.jar

to a working directory say /tmp/cassandra then:

cd /tmp/cassandra
unzip camel-cassandraql-2.15.1.redhat-621084.jar
vi META-INF/MANIFEST.MF 

at this point replace any imported dependencies on the Datastax version 2.x drivers with 3.x dependencies i.e.

Import-Package: com.datastax.driver.core;version="[3,4)",com.datastax.
 driver.core.querybuilder;version="[3,4)",org.apache.camel;version="[2
 .15,2.16)",org.apache.camel.impl;version="[2.15,2.16)",org.apache.camel
 .processor.aggregate.cassandra,org.apache.camel.processor.idempotent.ca
 ssandra,org.apache.camel.spi;version="[2.15,2.16)",org.apache.camel.sup
 port;version="[2.15,2.16)",org.apache.camel.util;version="[2.15,2.16)",
 org.apache.camel.utils.cassandra;version="[2.15,3)",org.slf4j;version="
 [1.6,2)"

then execute

zip -r camel-cassandraql-2.15.1.redhat-621084.jar .
cp camel-cassandraql-2.15.1.redhat-621084.jar FUSE_HOME/system/org/apache/camel/camel-cassandraql/2.15.1.redhat-621084/

Fuse may be started.

Then add a feature to you feature respository file:

<feature name='camel-cassandraql3' version='2.15.1.redhat-621084' resolver='(obr)' start-level='50'>
    <feature version='2.15.1.redhat-621084'>camel-core</feature>
    <bundle dependency='true'>mvn:com.google.guava/guava/19.0</bundle>
    <bundle dependency='true'>mvn:io.netty/netty-buffer/4.0.37.Final</bundle>
    <bundle dependency='true'>mvn:io.netty/netty-transport/4.0.37.Final</bundle>
    <bundle dependency='true'>mvn:io.netty/netty-codec/4.0.37.Final</bundle>
    <bundle dependency='true'>mvn:io.netty/netty-handler/4.0.37.Final</bundle>
    <bundle dependency='true'>mvn:io.netty/netty-common/4.0.37.Final</bundle>
    <bundle dependency='true'>mvn:io.dropwizard.metrics/metrics-core/3.1.2</bundle>
    <bundle dependency='true'>mvn:io.dropwizard.metrics/metrics-json/3.1.1</bundle>
    <bundle dependency='true'>mvn:com.fasterxml.jackson.core/jackson-core/2.6.3</bundle>
    <bundle dependency='true'>mvn:com.fasterxml.jackson.core/jackson-databind/2.6.3</bundle>
    <bundle dependency='true'>mvn:com.fasterxml.jackson.core/jackson-annotations/2.6.3</bundle>
    <bundle dependency='true'>mvn:com.datastax.cassandra/cassandra-driver-core/3.0.3</bundle>
    <bundle>mvn:org.apache.camel/camel-cassandraql/2.15.1.redhat-621084</bundle>
  </feature>

Then make sure the feature repository has been added to the running fuse instance via

features:addurl mvn:....

then install the feature

features:install camel-cassandraql3

Deployment
==========

Start Cassandra with the default configuration

$CASSANDRA_HOME/bin/cassandra
$CASSANDRA_HOME/bin/cqlsh
/bin/cqlsh> CREATE KEYSPACE camel WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
/bin/cqlsh> USE camel;
/bin/cqlsh> CREATE TABLE emp(emp_id int PRIMARY KEY, emp_name text);
/bin/cqlsh> insert into emp values (1, 'Julian Barnes');

We are now ready to install the application:

    osgi:install -s mvn:org.pfry/cassandra/1.0-SNAPSHOT



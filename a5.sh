#!/usr/bin/env bash

ELASTICSEARCH_HOME=https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/zip/elasticsearch
ELASTICSEARCH_RELEASE=${ELASTICSEARCH_HOME}/2.3.3/elasticsearch-2.3.3.zip
TEST_DATA=https://uni-koeln.sciebo.de/s/RnL6h6q4nVZ3i24/download
TOMCAT_HOME=https://www-us.apache.org/dist/tomcat
TOMCAT_RELEASE=${TOMCAT_HOME}/tomcat-8/v8.5.40/bin/apache-tomcat-8.5.40.zip

build_a5_core()
{
    gradle a5-core:war
}

build_a5_slave_ffmpeg()
{
    gradle a5-slave-ffmpeg:war
}

configure()
{
    sed "s|%PWD%|$PWD|g" ./a5.groovy | tee ~/a5.groovy
}

deploy_a5_core()
{
    mv ./a5-core/build/libs/a5-core.war ./dist/tomcat/webapps/a5-core.war
}

deploy_a5_slave_ffmpeg()
{
    mv ./a5-slave-ffmpeg/build/libs/a5-slave-ffmpeg.war ./dist/tomcat/webapps/a5-slave-ffmpeg.war
}

display_usage()
{
  echo
  echo "Usage: $0"
  echo
  echo " ingest       Ingest test data"
  echo " install      Install all services"
  echo " restart      Restart all services"
  echo " start        Start all services"
  echo " stop         Stop all services"
  echo
}

ingest()
{
    curl http://localhost:8080/a5-core/api/admin/a5-1/reindex
    curl http://localhost:8080/a5-core/api/admin/a5-2/reindex
}

install()
{
    sudo apt-get install -y zip
    mkdir -p ./dist
    cd ./dist/
    install_elasticsearch
    install_tomcat
    install_sdkman
    install_gradle
    install_grails
    install_test_data
    cd ..
    build_a5_core
    build_a5_slave_ffmpeg
    deploy_a5_core
    deploy_a5_slave_ffmpeg
    configure
}

install_elasticsearch()
{
    curl -o elasticsearch.zip ${ELASTICSEARCH_RELEASE}
    unzip elasticsearch.zip
    rm elasticsearch.zip
    mv elasticsearch-* elasticsearch
    rm -rf elasticsearch-*
}

install_gradle()
{
    sdk selfupdate force
    sdk install gradle 3.0
    sdk use gradle 3.0
}

install_grails()
{
    sdk selfupdate force
    sdk install grails 3.2.4
    sdk use grails 3.2.4
}

install_sdkman()
{
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
}

install_test_data()
{
    curl -o testdata.zip ${TEST_DATA}
    unzip testdata.zip
    rm testdata.zip
}

install_tomcat()
{
    curl -o tomcat.zip ${TOMCAT_RELEASE}
    unzip tomcat.zip
    rm tomcat.zip
    mv apache-tomcat-* tomcat
    rm -rf apache-tomcat-*
    sudo sh -c 'chmod +x ./tomcat/bin/*.sh'
}

start()
{
    start_elasticsearch
    start_tomcat
}

start_elasticsearch()
{
    cd ./dist/elasticsearch
    ./bin/elasticsearch -p pid -d
    cd ../../
}

start_tomcat()
{
    cd ./dist/tomcat/bin
    ./startup.sh
    cd ../../../
}

stop()
{
    stop_elasticsearch
    stop_tomcat
}

stop_elasticsearch()
{
    kill -9 `cat ./dist/elasticsearch/pid`
}

stop_tomcat()
{
    cd ./dist/tomcat/bin
    ./shutdown.sh
    cd ../../../
}

argument="$1"
if [[ -z ${argument} ]] ; then
  raise_error "Expected argument to be present"
  display_usage
else
  case ${argument} in
    install)
      install
      ;;
    configure)
      configure
      ;;
    restart)
      stop
      start
      ;;
    start)
      start
      ;;
    ingest)
      ingest
      ;;
    stop)
      stop
      ;;
    *)
      raise_error "Unknown argument: ${argument}"
      display_usage
      ;;
  esac
fi


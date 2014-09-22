FROM ubuntu:12.04

MAINTAINER yegor256 "yegor@teamed.io"

RUN apt-get update -qq

# Common dependencies
RUN apt-get install -y sudo git

# Dependencies to execute android
RUN apt-get install -y --no-install-recommends openjdk-7-jdk libgd2-xpm ia32-libs ia32-libs-multiarch

# Main Android SDK
RUN apt-get install -y --no-install-recommends wget
RUN cd /opt && wget -q http://dl.google.com/android/android-sdk_r23-linux.tgz
RUN cd /opt && tar xzf android-sdk_r23-linux.tgz
RUN cd /opt && rm -f android-sdk_r23-linux.tgz

# Other tools and resources of Android SDK
ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools
RUN echo y | android update sdk --filter platform-tools,build-tools-19.0.3,android-17,extra-android-support --no-ui --force

# Git to pull external repositories of Android app projects
RUN apt-get install -y --no-install-recommends git

# Download and install latest Maven
ENV MAVEN_MAJOR 3
ENV MAVEN_VERSION 3.2.3
RUN wget http://www.us.apache.org/dist/maven/maven-$MAVEN_MAJOR/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN tar -xzf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /usr/local
RUN mv /usr/local/apache-maven-$MAVEN_VERSION /usr/local/maven
RUN rm apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN ln -s /usr/local/maven/bin/mvn /usr/local/bin/mvn

# Warming up Maven local repo
RUN git clone https://github.com/aintshy/android /tmp/android
RUN cd /tmp/android && mvn clean install site -Pqulice -Psite
RUN rm -rf /tmp/android

# Cleaning
RUN apt-get clean

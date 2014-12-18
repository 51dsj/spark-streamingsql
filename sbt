#!/bin/bash

# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

root=$(
  cd $(dirname $(readlink $0 || echo $0))
  /bin/pwd
)

sbtjar=sbt-launch.jar

if [ ! -f $root/lib/$sbtjar ]; then
  echo 'downloading '$sbtjar 1>&2
  curl -O http://typesafe.artifactoryonline.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.6/$sbtjar
  mv $sbtjar $root/lib
fi

java -ea                          \
  $JAVA_OPTS                      \
  -Djava.net.preferIPv4Stack=true \
  -DsocksProxyHost=proxy.jf.intel.com -DsocksProxyPort=1080 \
  -XX:+AggressiveOpts             \
  -XX:+UseParNewGC                \
  -XX:+UseConcMarkSweepGC         \
  -XX:+CMSClassUnloadingEnabled   \
  -XX:MaxPermSize=1024m           \
  -Xss8M                          \
  -Xms512M                        \
  -Xmx1G                          \
  -server                         \
  -jar $root/lib/$sbtjar "$@"

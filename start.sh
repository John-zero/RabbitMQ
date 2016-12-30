#!/bin/sh
##########################################################
# Write date  : 2016/12/30
# Written by  : John_zero <2625210655@qq.com>
# Written des : Java control of shell script
# Call		  : ./start.sh start
##########################################################
. ~/.bash_profile

JAVA_HOME="/usr/java/jdk"

RUNNING_USER=root

APP_HOME=/opt/tudaxia/test/WEB-INF

APP_MAINCLASS=com.learning.helloWorld.Recv

CLASSPATH=$APP_HOME/classes
for i in "$APP_HOME"/lib/*.jar; do
   CLASSPATH="$CLASSPATH":"$i"
done

JAVA_OPTS="-Xmx512m -Xms256m -Xss1m -XX:NewRatio=1 -XX:NewSize=192m -XX:MaxNewSize=700m -XX:OldSize=512m -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=30 -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSCompactAtFullCollection"

tag=$(pwd |cut -d/ -f5)
chmod 777 ../$tag

real_path=$(cd `dirname $0`; pwd)
echo  "本脚本所在目录路径是: $real_path "
echo 系统时间 `date +%F\ %T`
cd $real_path

##########################################################
psid=0

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

##########################################################
start() {
	checkpid

	if [ $psid -ne 0 ]; then
		echo "================================================================"
		echo -e  "\e[31m 警告: $APP_MAINCLASS 已经启动! (pid=$psid) \e[0m"
		echo -en "是否需要关闭? [y/n]"
		read answer
		if [ $answer == y ]; then
			/bin/sh $real_path/stop.sh stop
		else
			exit 0
		fi
	fi

	echo -n "Starting $APP_MAINCLASS ..."
	JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS >/dev/null 2>&1 &"
	su - $RUNNING_USER -c "$JAVA_CMD"
	checkpid
	if [ $psid -ne 0 ]; then
		echo "$APP_MAINCLASS (pid=$psid) [OK]"
		echo ----------------------------
	else
		echo "$APP_MAINCLASS [Failed]"
		echo "请手动检查."
		echo ----------------------------
		exit 0
	fi
}

##########################################################
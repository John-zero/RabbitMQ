#!/bin/sh
##########################################################
# Write date  : 2016/12/30
# Written by  : John_zero <2625210655@qq.com>
# Written des : Java control of shell script
# Call		  : ./stop.sh stop
##########################################################
. ~/.bash_profile

JAVA_HOME="/usr/java/jdk"

RUNNING_USER=root

APP_MAINCLASS=com.learning.helloWorld.Recv

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
# echo -n 不换行
# $? 表示上一句命令或者函数的返回值
# -ne 非等于; -eq 等于
##########################################################
stop() {
   checkpid

   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_MAINCLASS ... (pid=$psid) "
      su - $RUNNING_USER -c "kill -9 $psid"
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi

      checkpid
      if [ $psid -ne 0 ]; then
         stop
      fi
   else
      echo "================================================================"
      echo "警告: $APP_MAINCLASS 还没有运行, 无须 Stop ..."
      echo "================================================================"
	  exit 0
   fi
}

##########################################################
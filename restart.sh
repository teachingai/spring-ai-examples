#!/usr/bin/env bash
#source /etc/profile
export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=

User=uzx
cur_dir=$(pwd)
cur_env="dev"

#判断脚本执行用户
if [ $(whoami) != "$User" ]; then
        echo -e "\e[1;31m 仅 $User 用户允许运行本脚本！\e[0m"
        exit
fi
#判断参数及配置文件是否正确可读（参数是否为当前目录下jar包目录）
if [ $# -ne 1 ]; then
        echo -e "\e[1;33m请输入一个要启动的服务\n例如：sh $0 market \e[0m"
        exit
fi
if [ ! -d $1 ]; then
        echo -e "\e[1;33m$1服务不存在\e[0m"
        exit
else
        Config=$1/application-dev.yml
        if [ ! -f $Config ]; then
                echo -e "\e[1;33m$1服务配置文件不存在，请核实。\e[0m"
                exit
        fi
fi

echo "开始重新启动 $1 项目脚本"
echo 检查 $1 服务端口是否被占用...
pid_blog=$(ps -ef | grep -v grep | grep $1.jar | awk '{print $2}')
if [ "$pid_blog" != "" ]; then
        kill -9 "$pid_blog" && echo "$pid_blog 进程已被杀死"
else
        echo "端口未被占用"
fi

echo "--------------------------------------------------------------------------"
pid_blog=$(ps -ef | grep -v grep | grep $1.jar | awk '{print $2}')
if [ "$pid_blog" != "" ]; then
        echo "$1 服务已经在运行中 PID: $pid_blog"
else
        nohup java -Xms256m -Xmx512m -jar ${cur_dir}/$1/$1.jar --spring.config.location=$Config --spring.profiles.active=${cur_env} >/dev/null 2>&1 &
#       java -Xms512m -Xmx768m -jar ${cur_dir}/$1/$1.jar --spring.config.location=$Config --spring.profiles.active=${cur_env}
#       nohup java -Xms256m -Xmx384m -jar ${cur_dir}/$1/$1.jar --spring.config.location=$Config --spring.profiles.active=${cur_env} >/dev/null 2>&1 &
#       nohup java -Xms512m -Xmx512m -jar ${cur_dir}/$1/$1.jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 --spring.config.location=$Config --spring.profiles.active=${cur_env} >/dev/null 2>&1 &
        sleep 2
        pid_start=$(ps -ef | grep -v grep | grep $1.jar | awk '{print $2}')
        echo "服务 $1 已启动>>>>>>>>>>>>>>PID:$pid_start"
        sleep 2
        echo -----------------------------------------------------------------------
        echo '删除配置文件'
        find ./$1 -name application-dev.yml | xargs rm && echo "已删除"
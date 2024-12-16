#!/bin/bash

netbeans19=apache-netbeans_19-1_all.deb 
ans=y

if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

clear

sudo apt update

echo "******************************************************************************
*                  встановлення Java JDK 21
******************************************************************************"
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "System has no java"
    sudo apt install openjdk-21-jdk -y
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" > "17" ]]; then
        echo version is higher than 17
    else         
        echo version is 17 or less, updating to 21
	sudo apt install openjdk-21-jdk -y
    fi
fi


echo "******************************************************************************
*                  встановлення curl
******************************************************************************"

sudo apt install curl -y

echo "******************************************************************************
*                  завантаження Apache NetBeans 19
******************************************************************************"
if [ -f ./$netbeans19 ]; then
	echo "середовище вже завантажено"
else 
	curl https://archive.apache.org/dist/netbeans/netbeans-installers/19/$netbeans19 --output $netbeans19
fi

echo "******************************************************************************
*                  встановлення Apache NetBeans 19
******************************************************************************"

sudo dpkg -i $netbeans19

echo "******************************************************************************
*                  встановлення Github client
******************************************************************************"

sudo apt install git -y

echo "******************************************************************************
*                  Клонування Spring Web-Client із Github
******************************************************************************"
if [ -e ./SpringClientDemo ]; then
	echo "проєкт SpringWClient вже існує, клонування пропущено"
else 
	git clone https://github.com/Wishmaster-sa/SpringClentDemo.git

	sudo chown -R $currentuser:$currentuser ./SpringClientDemo
fi


autostartFile="./SpringClientDemo/springwc.service" 
sed -i "s/User=sa/User=$currentuser/g" $autostartFile

sudo chown -R $currentuser:$currentuser ./SpringClientDemo

cd ./SpringClientDemo

/usr/lib/apache-netbeans/java/maven/bin/mvn package

sudo chown +x ./start-client.sh


echo "******************************************************************************
*                  ВІТАЄМО! РОЗГОРТАННЯ СЕРЕДОВИЩА ПРОЄКТУ ЗАВЕРШЕНО!
******************************************************************************"

echo "**************************************************************************************
    * Щоб запустити клієнт перейдить в папку проекта (SpringClientDemo) 
    * Отредагуйте конфіг файл за допомогою nano ./config/config.properties
    * Вам треба вказати адрес сервіса (server-path)
    * порт клієнта (port)
    * Встановити або змінити файл логування (logfilename)
    * Встановити або змінити рівень логування. (0 - немає логування, 2 - є повне логування) 
    * У разі якщо ви бажаєте використовувати SSL то треба встановити:
    * Потреба використовувати SSL (ssl=true)
    * Шлях до файлів сертіфікатів (certs-path)
    * Шлях до сховища довіри (trust-store-path та trust-store-password)
    * Шлях до збереження ASIC контейнерів (asic-store)
    * Також у разі використовування сервіса разом Трембітою треба налаштувати хедери (headers)
    * 
    * Зберегите файл налаштувань та виконайте наступну команду:  bash start-client.sh
    * Кліент буде доступний за адресою http[s]://localhost:[port]
    * також у вас повинен бути вже розгорнутий та налаштований SpringWSrest сервіс до якого цей
    * клієнт буде звертатись.
    ****************************************************************************************"
     


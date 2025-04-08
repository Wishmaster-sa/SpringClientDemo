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
*                  Встановлення Java JDK 21
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
*                  Встановлення curl
******************************************************************************"

sudo apt install curl -y

#echo "******************************************************************************
#*                  завантаження Apache NetBeans 19
#******************************************************************************"
#if [ -f ./$netbeans19 ]; then
#	echo "середовище вже завантажено"
#else 
#	curl https://archive.apache.org/dist/netbeans/netbeans-installers/19/$netbeans19 --output $netbeans19
#fi

#echo "******************************************************************************
#*                  встановлення Apache NetBeans 19
#******************************************************************************"

#sudo dpkg -i $netbeans19

echo "******************************************************************************
*                  Встановлення Maven
******************************************************************************"
sudo apt install -y maven 




echo "******************************************************************************
*                  Встановлення Github client
******************************************************************************"

sudo apt install git -y

echo "******************************************************************************
*                  Клонування Spring Web-Client із Github
******************************************************************************"
currentuser=$(stat -c "%G" .)

if [ -e ./SpringClientDemo ]; then
	echo "проєкт SpringWClient вже існує, клонування пропущено"
else 
	git clone https://github.com/Wishmaster-sa/SpringClientDemo.git

	sudo chown -R $currentuser:$currentuser ./SpringClientDemo
fi


autostartFile="./SpringClientDemo/springwc.service" 
sed -i "s/User=sa/User=$currentuser/g" $autostartFile

sudo chown -R $currentuser:$currentuser ./SpringClientDemo

cd ./SpringClientDemo

#/usr/lib/apache-netbeans/java/maven/bin/mvn package
/usr/bin/mvn package

sudo chmod +x ./start-client.sh


echo "******************************************************************************
*                  ВІТАЄМО! РОЗГОРТАННЯ СЕРЕДОВИЩА ПРОЄКТУ ЗАВЕРШЕНО!
******************************************************************************"

echo "**************************************************************************************
    * Щоб запустити клієнт необхідно:
    * - перейти в папку проекта (SpringClientDemo) 
    * - відредагувати конфіг файл за допомогою nano ./config/config.properties а саме:
    * - вказати адрес сервіса (server-path)
    * - порт клієнта (port)
    * - задати місце розташування файлу логування (logfilename)
    * - задати рівень логування. (0 - немає логування, 2 - є повне логування) 
    * - у випадку використання SSL необхідно:
    * - ввімкнути використовувати SSL (ssl=true)
    * - вказати шлях до файлів сертіфікатів (certs-path)
    * - вказати шлях до сховища довіри (trust-store-path та trust-store-password)
    * - вказати шлях до збереження ASIC контейнерів (asic-store)
    *- при використовування сервіса разом Трембітою необхідно налаштувати хедери (headers)
    * 
    * Після цього необхідно зберегти файл налаштувань та виконати команду:  bash start-client.sh
    * Кліент буде доступний за адресою http[s]://localhost:[port]
    ****************************************************************************************"
     


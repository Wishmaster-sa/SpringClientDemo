#!/bin/sh


function installService() {
	echo 'Додаю сервіс до автозапуску...'
 	#modify service daemon for current user
	autostartFile="./springwc.service"
 	currentuser=$(stat -c "%G" .)
	sed -i "s/User=sa/User=$currentuser/g" $autostartFile
	sudo mkdir /opt/SpringWClient
	sudo mkdir /opt/SpringWClient/config
	sudo mkdir /opt/SpringWClient/certs
	sudo cp ./target/SpringClientDemo-1.0-SNAPSHOT.jar /opt/SpringWClient
        sudo cp -R config/* /opt/SpringWClient/config 
        sudo cp -R certs/* /opt/SpringWClient/certs 
	#sudo cp ./webservice.settings /opt/SpringWClient
	sudo cp ./springwc.service /etc/systemd/system
	sudo systemctl daemon-reload
	sudo systemctl enable springwc.service
	sudo systemctl start springwc.service
	sudo systemctl status springwc.service
}

function removeService() {
	echo "Видаляю SpringWClient сервіс..."
	sudo systemctl stop springwc.service
	sudo systemctl disable springwc.service
	sudo rm /etc/systemd/system/springwc.service
	sudo sudo systemctl daemon-reload
	
	sudo rm -R /opt/SpringWClient
}

function startService() {
	echo "Запускаю SpringWClient сервіс..."
	sudo systemctl start springwc.service
	sudo systemctl status springwc.service
}

function stopService() {
	echo "Зупиняю SpringWClient сервіс..."
	sudo systemctl stop springwc.service
	sudo systemctl status springwc.service
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Додати сервіс до автозапуску" "Запустити сервіс" "Зупинити сервіс" "Видалити сервіс" "Вихід"
	do
	    case $option in
		"Додати сервіс до автозапуску")
		    installService
		    break
		    ;;
		"Запустити сервіс")
		    startService
		    break
		    ;;
		"Зупинити сервіс")
		    stopService
		    break
		    ;;
		"Видалити сервіс")
		    removeService
		    break
		    ;;
		"Вихід")
		    break
		    ;;
		*)
		    echo 'Invalid option.'
		    ;;
	    esac
	done	
	exit 1
fi


case $1 in
    'install')
        installService
        ;;
    'start')
        startService
        ;;
    'stop')
        stopService
        ;;
    'remove')
        removeService
        ;;
    *)
        echo 'Usage: bash springwc-service.sh install|start|stop|remove'
        ;;
esac


#!/bin/sh

function installDocker() {
	echo "Встановлюємо Docker"
	ISPRESENT=$(docker version)

	if [ "$ISPRESENT" = '' ]; then
		echo "Продовжуємо інсталяцію..."
		sudo apt update
		sudo apt install apt-transport-https ca-certificates curl software-properties-common
		curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
		echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
		sudo apt update
		sudo apt install docker-ce
		sudo systemctl status docker
		exit 0
	else
		echo "docker вже встановлено на компьютері"
	fi
	
}

function buildImage() {
	echo "Створюю Docker образ springrestclient ..."
	sudo docker build -t springrestclient:latest .
}

function setParameters() {
	echo "Налаштовуємо параметри..."
	nano ./service.env
}

function startImage() {
	echo "Запускаю Docker образ springrestclient ..."
	sudo docker run -it --rm -p 8050:8050 --env-file ./service.env springrestclient:latest
	
}

function removeImage() {
	echo "Видаляю Docker образ springrestclient ..."
	sudo docker rmi springrestclient:latest
}


if [[ $EUID -ne 0 ]]; then
    echo "Скрипт потрібно запускати з правами root (додайте sudo перед командою)"
    exit 1
fi

if [ -z $1 ]; then
	PS3='Будь-ласка, зробіть вибір: '
	select option in "Встановити Docker" "Створити Docker образ" "Налаштувати Docker образ" "Запустити Docker образ" "Видалити Docker образ" "Вихід"
	do
	    case $option in
		"Встановити Docker")
		    installDocker
		    break
		    ;;
		"Створити Docker образ")
		    buildImage
		    break
		    ;;
		"Запустити Docker образ")
		    startImage
		    break
		    ;;
		"Налаштувати Docker образ")
		    setParameters
		    break
		    ;;
		"Видалити Docker образ")
		    removeImage
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
    'build')
        buildImage
        ;;
    'start')
        startImage
        ;;
    'set')
        setParameters
        ;;
    'remove')
        removeImage
        ;;
    *)
        echo 'Usage: bash docker.sh build|start|set|remove'
        ;;
esac


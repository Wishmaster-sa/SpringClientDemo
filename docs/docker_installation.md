# Розгортання вебклієнта в Docker

## Вимоги

| ПЗ             |   Версія   | Примітка                     |
|:---------------|:----------:|------------------------------|
| Docker         | **20.10+** |                              |
| Docker Compose |   10.5+    | Якщо планується використання |
| Git            |            | Для клонування репозиторію   |

## Змінні оточення

Вебклієнт підтримує конфігурацію через змінні оточення. 
Нижче наведено основні параметри:
- `SPRING_APPLICATION_NAME`: Назва Spring Boot застосунку (відображається у логах та активах).
- `SERVER_PORT`: Порт, на якому запускається застосунок (типово `8050`).

- `WEBCLIENT_SETTINGS_LOGFILENAME`: Ім’я файлу для логування клієнта.
- `WEBCLIENT_SETTINGS_LOGLEVEL`: Рівень логування клієнта (0 — вимкнено, 1 — помилки, 2 — повний лог).
- `WEBCLIENT_SETTINGS_SERVER-PATH`: URL сервера, до якого клієнт відправляє HTTP-запити.
- `WEBCLIENT_SETTINGS_CERTS-PATH`: Шлях до каталогу з сертифікатами для SSL-з’єднання.
- `WEBCLIENT_SETTINGS_ASIC-STORE`: Шлях до папки, де зберігається тимчасовий ASiC архів.
- `WEBCLIENT_SETTINGS_SSL`: Увімкнення або вимкнення SSL (`true` або `false`).
- `WEBCLIENT_SETTINGS_TRUST-STORE-PATH`: Шлях до trust-store файлу для перевірки SSL-сертифікатів.
- `WEBCLIENT_SETTINGS_TRUST-STORE-PASSWORD`: Пароль до trust-store.
- `WEBCLIENT_SETTINGS_HEADERS`: Додаткові HTTP-заголовки у форматі JSON (наприклад: `{'UXP-CLIENT': '...', 'UXP-SERVICE': '...'}`).

- `TREMBITA_SERVICE_XROADINSTANCE`: Назва X-Road інстанції для сервісу.
- `TREMBITA_SERVICE_MEMBERCLASS`: Клас учасника Trembita (наприклад, `GOV`).
- `TREMBITA_SERVICE_MEMBERCODE`: Код учасника Trembita.
- `TREMBITA_SERVICE_SUBSYSTEMCODE`: Код підсистеми сервісу.

- `TREMBITA_CLIENT_XROADINSTANCE`: Назва X-Road інстанції для клієнта.
- `TREMBITA_CLIENT_MEMBERCLASS`: Клас клієнта Trembita.
- `TREMBITA_CLIENT_MEMBERCODE`: Код клієнта Trembita.
- `TREMBITA_CLIENT_SUBSYSTEMCODE`: Код підсистеми клієнта.
- `TREMBITA_CLIENT_ENDPOINT`: Назва кінцевої точки (endpoint), яку викликає клієнт.
## Збір Docker-образу

Для того, щоб зібрати Docker-образ, необхідно:

1. Клонувати репозиторій:

```bash
git clone https://github.com/Wishmaster-sa/SpringClientDemo.git
```

2. Перейти до директорії з вебклієнтом:

```bash
cd SpringClientDemo
```

3. Виконати наступну команду в кореневій директорії проєкту:

```bash
sudo docker build -t SpringClientDemo-app .
```

Дана команда створить Docker-образ з іменем `SpringClientDemo-app`, використовуючи Dockerfile, який знаходиться в поточній директорії.

## Запуск та використання контейнера зі змінними оточення

Щоб запустити контейнер з додатком, виконайте команду:

```bash
sudo docker run -it --rm -p 8050:8050 \
  -e SPRING_APPLICATION_NAME="Spring Client Demo" \
  -e SERVER_PORT=8050 \
  -e WEBCLIENT_SETTINGS_LOGFILENAME="client.log" \
  -e WEBCLIENT_SETTINGS_LOGLEVEL=2 \
  -e WEBCLIENT_SETTINGS_SERVER-PATH="http://10.211.55.5:8080/api/v1/persons" \
  -e WEBCLIENT_SETTINGS_CERTS-PATH="./certs" \
  -e WEBCLIENT_SETTINGS_ASIC-STORE="./asic" \
  -e WEBCLIENT_SETTINGS_SSL=false \
  -e WEBCLIENT_SETTINGS_TRUST-STORE-PATH="keystore/keystore.p12" \
  -e WEBCLIENT_SETTINGS_TRUST-STORE-PASSWORD="localhost" \
  -e WEBCLIENT_SETTINGS_HEADERS="{'UXP-CLIENT': 'test1/GOV/00000088/TEST_SUB888', 'UXP-SERVICE': 'test1/GOV/00000088/TEST_SUB888/springrest'}" \
  -e TREMBITA_SERVICE_XROADINSTANCE="test1" \
  -e TREMBITA_SERVICE_MEMBERCLASS="GOV" \
  -e TREMBITA_SERVICE_MEMBERCODE="00000088" \
  -e TREMBITA_SERVICE_SUBSYSTEMCODE="TEST_SUB888" \
  -e TREMBITA_CLIENT_XROADINSTANCE="test1" \
  -e TREMBITA_CLIENT_MEMBERCLASS="GOV" \
  -e TREMBITA_CLIENT_MEMBERCODE="00000088" \
  -e TREMBITA_CLIENT_SUBSYSTEMCODE="TEST_SUB888" \
  -e TREMBITA_CLIENT_ENDPOINT="springrest" \
  SpringClientDemo-app
```

де:
- параметр `-p 8050:8050` перенаправляє порт 8050 на локальній машині на порт 8050 всередині контейнера.
- інші змінні перелічені в пункті [Змінні оточення](#змінні-оточення)


## Перегляд журналів подій

Якщо виведення журналів подій налаштоване у консоль, переглядати їх можна за допомогою команди:

```bash
docker logs <container_id>
```

##
Матеріали створено за підтримки проєкту міжнародної технічної допомоги «Підтримка ЄС цифрової трансформації України (DT4UA)».

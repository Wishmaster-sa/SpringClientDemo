## Конфігурація вебклієнту

Для конфігурації вебклієнту необхідно:
1.	Відкрити файл конфігурації `config.properties` в директорії `/SpringClientDemo/config/`:

   ```bash
   nano ./SpringClientDemo/config/config.properties
   ```

та встановити коректні параметри підключення до сервіса

```
server.port=8050
webclient.settings.logfilename=/tmp/client.log # шлях до файлу логування
webclient.settings.loglevel=2 # шлях до файлу логування
webclient.settings.server-path=https://192.168.99.93/restapi # Адреса сервісу або локальна IP-адреса ШБО
webclient.settings.ssl=false # у випадку використання використання SSL встановити значення ssl=true
webclient.settings.certs-path=./certs # шлях до файлів сертіфікатів
webclient.settings.asic-store=asic # шлях до збереження ASIC контейнерів
webclient.settings.trust-store-path=keystore/keystore.p12 # шлях до сховища ключів та сертифікатів
webclient.settings.trust-store-password=localhost # пароль до сховища клчів та сертифікатів
webclient.settings.headers={'UXP-CLIENT': 'test1/GOV/00000088/TEST_SUB888', 'UXP-SERVICE': 'test1/GOV/00000088/TEST_SUB888/springrest'} # хедери (headers) системи "Трембіта"
```

##
Матеріали створено за підтримки проєкту міжнародної технічної допомоги «Підтримка ЄС цифрової трансформації України (DT4UA)».




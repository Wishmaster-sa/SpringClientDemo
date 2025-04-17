### Building and running your application

When you're ready, start your application by running:
`docker compose up --build`.

Your application will be available at http://localhost:8050.

### Deploying your application to the cloud

First, build your image, e.g.: `docker build -t myapp .`.
If your cloud uses a different CPU architecture than your development
machine (e.g., you are on a Mac M1 and your cloud provider is amd64),
you'll want to build the image for that platform, e.g.:
`docker build --platform=linux/amd64 -t myapp .`.

Then, push it to your registry, e.g. `docker push myregistry.com/myapp`.

Consult Docker's [getting started](https://docs.docker.com/go/get-started-sharing/)
docs for more detail on building and pushing

```env
SPRING_APPLICATION_NAME=Spring Client Demo
SERVER_PORT=8050

WEBCLIENT_SETTINGS_LOGFILENAME=client.log
WEBCLIENT_SETTINGS_LOGLEVEL=2
WEBCLIENT_SETTINGS_SERVER-PATH=http://10.211.55.5:8080/api/v1/persons
WEBCLIENT_SETTINGS_CERTS-PATH=./certs
WEBCLIENT_SETTINGS_ASIC-STORE=./asic
WEBCLIENT_SETTINGS_SSL=false
WEBCLIENT_SETTINGS_TRUST-STORE-PATH=keystore/keystore.p12
WEBCLIENT_SETTINGS_TRUST-STORE-PASSWORD=localhost
WEBCLIENT_SETTINGS_HEADERS={'UXP-CLIENT': 'test1/GOV/00000088/TEST_SUB888', 'UXP-SERVICE': 'test1/GOV/00000088/TEST_SUB888/springrest'}

TREMBITA_SERVICE_XROADINSTANCE=test1
TREMBITA_SERVICE_MEMBERCLASS=GOV
TREMBITA_SERVICE_MEMBERCODE=00000088
TREMBITA_SERVICE_SUBSYSTEMCODE=TEST_SUB888

TREMBITA_CLIENT_XROADINSTANCE=test1
TREMBITA_CLIENT_MEMBERCLASS=GOV
TREMBITA_CLIENT_MEMBERCODE=00000088
TREMBITA_CLIENT_SUBSYSTEMCODE=TEST_SUB888
TREMBITA_CLIENT_ENDPOINT=springrest
```
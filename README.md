# Shopbot

In this app i implement shop for [aquamarina](https://aquamarina-pool.ru/)
in telegram bot. 

## Set up and run

### Database

This app is supposed to use PostgreSQl 17 as the sql database.
It is mandatory dependency. Before run the app you should be
sure that you have [prepared db](https://www.postgresql.org/download/). 

### environment

Set this environment variable in your system before run the app. Like this:
```bash
export SB_SERVER_PORT=7474
```

| name                  | default value | description                                               |
|-----------------------|---------------|-----------------------------------------------------------|
| SB_DB_DEFAULT_SCHEMA  | public        | shema name which app will use in postgres db              |
| SB_DB_PASSWORD        |               | password for bd user                                      |
| SB_DB_USERNAME        |               | bd username which app will use in postgres db             |
| SB_DB_URL             |               | url for db. like: jdbc:postgresql://localhost:5432/dbname |
| SB_SERVER_PORT        | 8080          | port on which server will be running                      |
| SB_TELEGRAM_BOT_TOKEN |               | access token for telegram bot                             |

Then you can build and run it via docker:
```bash
dokcer build -t shopbot:latest .
docker run shopbot:latest
```
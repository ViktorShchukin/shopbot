## Micronaut 4.7.6 Documentation

- [User Guide](https://docs.micronaut.io/4.7.6/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.7.6/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.7.6/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

# Telegram ChatBot

Follow the instructions in the [Micronaut ChatBot Documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/) to create a Telegram ChatBot.

Once you have a username and HTTP auth key for your new bot, edit the application config in this project to set the bot username and make up a WEBHOOK_TOKEN so you can ensure it's Telegram that's calling your bot.

This project has a dependency on `micronaut-chatbots-telegram-http` which has added a controller to your application with the path `/telegram`.

When registering your bot with Telegram, you will need to provide the HTTPS URL of your application including this path.
If you are running your application locally, you can use a tool like [ngrok](https://ngrok.com/) to expose your application to the internet.

You can then set up the Telegram webhook by running the following command:

```bash
curl -X POST 'https://api.telegram.org/bot${HTTP_AUTH_KEY}/setWebhook?url=${YOUR_HTTP_TRIGGER_URL}&secret_token=${YOUR_SECRET_TOKEN}'
```

Where HTTP_AUTH_KEY is the key given to you by the BotFather, YOUR_HTTP_TRIGGER_URL is the URL of your HTTP function and YOUR_SECRET_TOKEN is the value you chose for the WEBHOOK_TOKEN in the configuration.


- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature flyway documentation

- [Micronaut Flyway Database Migration documentation](https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html)

- [https://flywaydb.org/](https://flywaydb.org/)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature chatbots-telegram-http documentation

- [Micronaut Telegram ChatBot as a controller documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)


## Feature validation documentation

- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)


## Feature data-jdbc documentation

- [Micronaut Data JDBC documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc)


## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)


## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)



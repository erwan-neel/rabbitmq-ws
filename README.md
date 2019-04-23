# Démarrer RabbitMQ en local

Démarrer un container RabbitMQ grâce à la commande suivante :

```
docker run -d -p 5672:5672 --name=rabbit rabbitmq
```

Vérifier que le container a correctement démarré :

```
docker exec -it rabbit bash
rabbitmqctl list_exchanges
```

Vous devriez voir des choses s'afficher dans votre console !

# Configurer le proxy de Gradle

Nous utiliserons Gradle dans nos TPs. Si vous êtes derrière un proxy, il faut créer un fichier **gradle.properties** dans votre dossier .gradle (dans votre dossier utilisateur) avec le contenu suivant : 

```
systemProp.http.proxyHost=127.0.0.1
systemProp.http.proxyPort=3128
systemProp.https.proxyHost=127.0.0.1
systemProp.https.proxyPort=3128
```

# TP n°1

Dans ce TP, nous aurons deux applications java distinctes :

- Un publisher
- Un receiver

Le publisher enverra des messages sur une queue.
Le receiver écoutera cette queue et affichera les messages qu'il recevra.

Démarrez le container RabbitMQ si cela n'a pas déjà été fait.

**Démarrez le publisher :**
```
cd path_to_rabbitmq-ws/rabbitmq-publisher
./gradlew clean assemble
java -jar build/libs/rabbitmq-publisher-0.0.1-SNAPSHOT.jar
```

**Démarrez enfin le receiver (dans une autre console) :**
```
cd path_to_rabbitmq-ws/rabbitmq-receiver
./gradlew clean assemble
java -jar build/libs/rabbitmq-receiver-0.0.1-SNAPSHOT.jar
```

Vous pouvez désormais envoyer des messages depuis le publisher et vérifier qu'ils sont bien affichés par le receiver.
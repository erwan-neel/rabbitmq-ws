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

# TP n°1 : "_Hello World_"

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

# TP n°2 : "_Work Queues_"

Dans ce TP, nous allons créer une _Work Queue_ qui va être utilisée pour distribuer des tâches chronophages entre de multiples workers.

L'idée principale derrière les _Work Queues_ est d'éviter de réaliser une tâche gourmande en ressources immédiatement et d'avoir à attendre qu'elle soit terminée. A la place on va planifier l'exécution de cette tâche à plus tard. On va encapsuler une tâche dans un message puis l'envoyer à une Queue. Un worker se chargera d'exécuter cette tâche. Si plusieurs workers sont à l'écoute de la même Queue, ils se partageront le travail.

Ce concept est très utile dans les applications web où il est impossible de prendre en charge une tâche complexe durant une requête HTTP courte.

**Démarrez le publisher**

```
cd path_to_rabbitmq-ws/rabbitmq-publisher
./gradlew clean assemble
java -jar build/libs/rabbitmq-publisher-0.0.1-SNAPSHOT.jar
```

**Démarrez plusieurs instances (2 ou 3) du receiver (dans différentes consoles) :**
```
cd path_to_rabbitmq-ws/rabbitmq-receiver
./gradlew clean assemble
java -jar build/libs/rabbitmq-receiver-0.0.1-SNAPSHOT.jar
```

Vous pouvez envoyer plusieurs messages depuis le publisher et regarder comment les messages sont répartis entre les différentes instances.


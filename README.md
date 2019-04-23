**Démarrer RabbitMQ en local**

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
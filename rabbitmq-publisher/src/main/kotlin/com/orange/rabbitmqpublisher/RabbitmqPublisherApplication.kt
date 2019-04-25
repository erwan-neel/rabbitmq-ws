package com.orange.rabbitmqpublisher

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class RabbitmqPublisherApplication : CommandLineRunner {

    companion object {
        const val EXCHANGE_NAME = "topic_logs"
    }

    override fun run(vararg args: String?) {
        val channel = prepareRabbitMqConnection()
        var routingKey = getRoutingKey()
        val message = "Hello World"

        while (routingKey != "exit") {
            channel.basicPublish(
                    EXCHANGE_NAME,
                    routingKey,
                    null,
                    message.toByteArray()
            )
            routingKey = getRoutingKey()
        }
    }

    private fun getRoutingKey(): String {
        val promptMessage = "Veuillez saisir clé de routage (toto.example.*) pour le message à envoyer (CTRL + C pour quitter) : "
        println(promptMessage)

        return readLine()!!
    }

    private fun prepareRabbitMqConnection(): Channel {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.TOPIC)

        return channel
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitmqPublisherApplication>(*args)
}

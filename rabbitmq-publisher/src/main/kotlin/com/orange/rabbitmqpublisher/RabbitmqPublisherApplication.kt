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
        const val EXCHANGE_NAME = "direct_logs"
    }

    override fun run(vararg args: String?) {
        val channel = prepareRabbitMqConnection()
        var severity = getSeverity()
        val message = "Hello World"

        while (severity != "exit") {
            channel.basicPublish(
                    EXCHANGE_NAME,
                    severity,
                    null,
                    message.toByteArray()
            )
            severity = getSeverity()
        }
    }

    private fun getSeverity(): String {
        val promptMessage = "Veuillez saisir une sévérité (info - warning - error) pour le message à envoyer (CTRL + C pour quitter) : "
        println(promptMessage)

        return readLine()!!
    }

    private fun prepareRabbitMqConnection(): Channel {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.DIRECT)

        return channel
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitmqPublisherApplication>(*args)
}

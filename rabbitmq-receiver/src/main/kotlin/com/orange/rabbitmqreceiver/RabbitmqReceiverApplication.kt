package com.orange.rabbitmqreceiver

import com.rabbitmq.client.ConnectionFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.rabbitmq.client.Delivery
import org.springframework.amqp.core.ExchangeTypes


@SpringBootApplication
class RabbitmqReceiverApplication : CommandLineRunner {
    companion object {
        const val EXCHANGE_NAME = "topic_logs"
        const val HOST = "localhost"
    }

    override fun run(vararg args: String?) {
        val factory = ConnectionFactory()
        factory.host = HOST
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.TOPIC)

        val queueName = channel.queueDeclare().queue
        val routingBinding = getRoutingBinding()

        channel.queueBind(queueName, EXCHANGE_NAME, routingBinding)

        println(" [*] Waiting for messages. To exit press CTRL+C")

        val deliverCallback = { consumerTag: String, delivery: Delivery ->
            val message = String(delivery.getBody(), charset("UTF-8"))
            println(" [x] Received '$message'")
        }

        val autoAck = true
        channel.basicConsume(queueName, autoAck, deliverCallback, { consumerTag -> })
    }

    private fun getRoutingBinding(): String {
        val promptMessage = "Veuillez saisir une clé de routage pour le binding de la queue (CTRL + C pour quitter) : "
        println(promptMessage)

        return readLine()!!
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitmqReceiverApplication>(*args)
}

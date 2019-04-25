package com.orange.rabbitmqreceiver

import com.rabbitmq.client.ConnectionFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.amqp.rabbit.core.RabbitAdmin.QUEUE_NAME
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import org.springframework.amqp.core.ExchangeTypes


@SpringBootApplication
class RabbitmqReceiverApplication : CommandLineRunner {
    companion object {
        const val EXCHANGE_NAME = "logs"
        const val HOST = "localhost"
    }

    override fun run(vararg args: String?) {
        val factory = ConnectionFactory()
        factory.host = HOST
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.FANOUT)

        val queueName = channel.queueDeclare().queue
        channel.queueBind(queueName, EXCHANGE_NAME, "")

        println(" [*] Waiting for messages. To exit press CTRL+C")

        val deliverCallback = { consumerTag: String, delivery: Delivery ->
            val message = String(delivery.getBody(), charset("UTF-8"))
            println(" [x] Received '$message'")
        }

        val autoAck = true
        channel.basicConsume(queueName, autoAck, deliverCallback, { consumerTag -> })
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitmqReceiverApplication>(*args)
}

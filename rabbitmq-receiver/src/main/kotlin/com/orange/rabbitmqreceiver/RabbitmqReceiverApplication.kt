package com.orange.rabbitmqreceiver

import com.rabbitmq.client.ConnectionFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.amqp.rabbit.core.RabbitAdmin.QUEUE_NAME
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery


@SpringBootApplication
class RabbitmqReceiverApplication: CommandLineRunner {
    companion object {
        const val QUEUE_NAME = "hello"
    }

    override fun run(vararg args: String?) {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println(" [*] Waiting for messages. To exit press CTRL+C")

        val deliverCallback = { consumerTag: String, delivery: Delivery ->
            val message = String(delivery.getBody(), charset("UTF-8"))
            println(" [x] Received '$message'")
            doWork(message)
        }

        val autoAck = true
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, { consumerTag -> })
    }

    private fun doWork(task: String) {
        for (ch: Char in task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RabbitmqReceiverApplication>(*args)
}

package com.orange.rabbitmqpublisher

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class RabbitmqPublisherApplication: CommandLineRunner {

	companion object {
		const val QUEUE_NAME = "hello"
	}

	override fun run(vararg args: String?) {
		val channel = prepareRabbitMqConnection()
		var inputMessage = getUserInputMessage()

		while (inputMessage != "exit") {
			channel.basicPublish(
					"",
					QUEUE_NAME,
					MessageProperties.PERSISTENT_TEXT_PLAIN,
					inputMessage.toByteArray()
			)
			inputMessage = getUserInputMessage()
		}
	}

	private fun getUserInputMessage(): String {
		val promptMessage = "Veuillez saisir un message à envoyer (CTRL + C pour quitter) : "
		println(promptMessage)
		return readLine()!!
	}

	private fun prepareRabbitMqConnection(): Channel {
		val factory = ConnectionFactory()
		factory.host = "localhost"
		val connection = factory.newConnection()
		val channel = connection.createChannel()
		val durable = true
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null)

		return channel
	}
}

fun main(args: Array<String>) {
	runApplication<RabbitmqPublisherApplication>(*args)
}

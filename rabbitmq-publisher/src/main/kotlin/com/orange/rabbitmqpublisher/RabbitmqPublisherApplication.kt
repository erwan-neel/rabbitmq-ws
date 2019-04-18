package com.orange.rabbitmqpublisher

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
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
			channel.basicPublish("", QUEUE_NAME, null, inputMessage.toByteArray())
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
		channel.queueDeclare(QUEUE_NAME, false, false, false, null)

		return channel
	}
}

fun main(args: Array<String>) {
	runApplication<RabbitmqPublisherApplication>(*args)
}

package com.stocktrader.service.notification;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

@Component
public class QueueConsumer implements Consumer {
	@Autowired
	PushNotification pushNotification;

	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available.
	 */
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		String message = new String(body, "UTF-8");
		pushNotification.sendPushNotificationMessage(message);
	}

	public void handleCancel(String consumerTag) {
	}

	public void handleCancelOk(String consumerTag) {
	}

	public void handleRecoverOk(String consumerTag) {
	}

	public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
	}

}
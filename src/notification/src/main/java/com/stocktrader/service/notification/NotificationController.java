package com.stocktrader.service.notification;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Component
@PropertySource(value = { "classpath:application.properties" })
public class NotificationController {
	public static Logger LOGGER = Logger.getLogger(NotificationController.class.getName());

	@Value("${rabbitmq.userName}")
	private String mqUserName;
	@Value("${rabbitmq.hostName}")
	private String mqHost;
	@Value("${rabbitmq.virtualHost:/}")
	private String mqVirtualHost;
	@Value("${rabbitmq.password}")
	private String mqPassword;
	@Value("${rabbitmq.portNumber:5672}")
	private Integer mqPort;
	@Value("${rabbitmq.queue}")
	private String queue;

	private Connection conn;
	private Channel channel;
	private @Autowired QueueConsumer queueConsumer;

	ExecutorService ex = Executors.newFixedThreadPool(1);
	private boolean initialized;

	@PostConstruct
	public void publ() {
		if (isSocketAlive())
			ex.submit(() -> init(this));
	}

//	@PostConstruct
	public void init(NotificationController obj) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(obj.mqUserName);
			factory.setPassword(obj.mqPassword);
			factory.setVirtualHost(obj.mqVirtualHost);
			factory.setHost(obj.mqHost);
			factory.setPort(obj.mqPort);
			factory.setAutomaticRecoveryEnabled(true);

			obj.conn = factory.newConnection();

			obj.channel = obj.conn.createChannel();

			obj.channel.queueDeclare(obj.queue, false, false, false, null);

			LOGGER.info("The MQ client is ready. [*] Waiting for messages");
			obj.channel.basicConsume(obj.queue, true, obj.queueConsumer);
			initialized = true;
		} catch (Exception e) {
			initialized = false;
			LOGGER.log(Level.WARNING, "Exception while publish message", e);
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e1) {
					LOGGER.log(Level.WARNING, "Exception while publish message", e);
				}
			}
		}
	}

	/**
	 * To check the connection after every 1 minute to ensure that MQ is connected always
	 */
	@Scheduled(fixedDelay = 60000, initialDelay = 60000)
	public void testConnection() {
		if (!isSocketAlive())
			return;
		if (initialized == false)
			init(this);
		if (conn == null || !conn.isOpen())
			init(this);
 
	}

	@PreDestroy
	public void shutdown() throws IOException, TimeoutException {
		System.out.println("Closing channel.....");
		channel.close();
		System.out.println("Closing connection.....");
		conn.close();
	}

	/**
	 * Check the port and host is accessible
	 * 
	 * @param hostName
	 * @param port
	 * @return boolean - true/false
	 */
	public boolean isSocketAlive() {
		if (conn != null && conn.isOpen()) // already connected
			return true;
		boolean isAlive = false;

		// Creates a socket address from a hostname and a port number
		SocketAddress socketAddress = new InetSocketAddress(mqHost, mqPort);
		Socket socket = new Socket();

		// Timeout required - it's in milliseconds
		int timeout = 2000;
		try {
			socket.connect(socketAddress, timeout);
			socket.close();
			isAlive = true;

		} catch (SocketTimeoutException exception) {
			LOGGER.log(Level.WARNING,
					"SocketTimeoutException " + mqHost + ":" + mqPort + ". " + exception.getMessage());
		} catch (IOException exception) {
			LOGGER.log(Level.WARNING,
					"IOException - Unable to connect to " + mqHost + ":" + mqPort + ". " + exception.getMessage());
		}
		return isAlive;
	}

//	public static void main(String args[]) throws IOException, TimeoutException {
//		NotificationController nController = new NotificationController();
//
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setUsername("user");
//		factory.setPassword("bitnami");
//		factory.setVirtualHost("/");
//		factory.setHost("172.17.76.28");
//		factory.setPort(5672);
//		Connection conn = factory.newConnection();
//		Channel channel = conn.createChannel();
//		channel.queueDeclare("TESTQ", false, false, false, null);
//		for (int i = 5; i < 10; i++)
//			channel.basicPublish("", "TESTQ", null,
//					new String("Deleted portfolio by user 1 with name IBM" + i).getBytes("UTF-8"));
//
//		System.out.println("Success");
//		conn.close();
//
//	}
}

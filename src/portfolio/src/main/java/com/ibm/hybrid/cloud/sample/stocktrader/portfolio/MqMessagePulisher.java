package com.ibm.hybrid.cloud.sample.stocktrader.portfolio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqMessagePulisher {
	private static MqMessagePulisher object = null;
	private static Logger logger = Logger.getLogger(MqMessagePulisher.class.getName());

	public static String MESSAGE_FORMAT = "OWNER:%s, PERFORMED OPERATION:%s, DETAILS:%s";

	private String mqUserName = System.getenv("MQ_USR");
	private String mqHost = System.getenv("MQ_HOST_NAME");
	private String mqVirtualHost = System.getenv("MQ_VIRTUAL_HOST");
	private String mqPassword = System.getenv("MQ_PWD");
	private Integer mqPort = Integer.valueOf(System.getenv("MQ_PORT"));
	private String queue = System.getenv("MQ_QUEUE");

	private Connection conn;
	private Channel channel;
	private boolean initialized;

	private MqMessagePulisher() {
	}

	public static MqMessagePulisher getInstance() {
		if (object == null) {
			object = new MqMessagePulisher();
		}
		return object;
	}

	public void init() {

		if (mqPassword == null || mqPort == null || mqVirtualHost == null || mqUserName == null || queue == null
				|| mqHost == null) {
			initialized = false;
			return;
		}

		if (!isSocketAlive()) {
			logger.info("Cant connect to the Rabbit MQ host and port");
			initialized = false;
			return;
		}

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(mqUserName);
			factory.setPassword(mqPassword);
			factory.setVirtualHost(mqVirtualHost);
			factory.setHost(mqHost);
			factory.setPort(mqPort);
			conn = factory.newConnection();
			logger.info("Initializing the Rabbit MQ conniction Done");
			initialized = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception while init Rabbit MQ connection", e);
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			initialized = false;
		}
	}

	public void sendMessage(String user, String operation, String details) {
		if (!initialized)
			init();

		if (initialized)
			try (Channel channel = conn.createChannel()) {
				try {
					channel.queueDeclare(queue, false, false, false, null);
					channel.basicPublish("", queue, null,
							new String(String.format(MESSAGE_FORMAT, user, operation, details)).getBytes("UTF-8"));
				} catch (IOException e) {
					logger.log(Level.WARNING, "Exception while publish message", e);
				}
			} catch (IOException | TimeoutException e) {
				logger.log(Level.WARNING, "Exception while publish message", e);
			}
	}

	public void shutdown() throws IOException, TimeoutException {
		logger.info("Closing channel.....");
		channel.close();
		logger.info("Closing connection.....");
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
			logger.info("SocketTimeoutException " + mqHost + ":" + mqPort + ". " + exception.getMessage());
		} catch (IOException exception) {
			logger.info("IOException - Unable to connect to " + mqHost + ":" + mqPort + ". " + exception.getMessage());
		}
		return isAlive;
	}

}

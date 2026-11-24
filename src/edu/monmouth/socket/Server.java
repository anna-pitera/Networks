package edu.monmouth.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	// designate flag variable to avoid infinite looping
	private static boolean running = true;

	private static ServerSocket serverSocket;
	private static Socket client;
	private static String[] inputFromClient;
	private static InputStream is;
	private static BufferedReader br;
	private static PrintWriter writer;
	private static OutputStream os;
	private static String received;

	public static void main(String[] args) {
		initialize();

		try {
			is = client.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		while (running) {
			try {
				// create array of strings by reading a line from the client with defined delimiter
				inputFromClient = br.readLine().split(SocketConstants.DELIMITER, SocketConstants.SPLITLIMIT);

				String command = inputFromClient[SocketConstants.COMMANDINDEX];

				switch (command) {
				case "time" -> respondWithTime();
				case "dns" -> respondWithDNS();
				case "echo" -> respondWithEcho();
				default -> System.out.println("Invalid option. Please try again.");
				}
			} catch (IOException e) {
				System.err.println("Client disconnected. System terminating.");
				running = false;
				System.exit(SocketConstants.CLIENTDISCONNECTED);
			}
		}
	}

	public static void initialize() {
		boolean initializing = true;

		try {
			serverSocket = new ServerSocket(SocketConstants.PORTNUMBER);
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		while (initializing) {
			try {
				client = serverSocket.accept();
				os = client.getOutputStream();
				writer = new PrintWriter(os, true);

				String ip = client.getInetAddress().toString();
				int remotePort = client.getPort();
				int localPort = client.getLocalPort();

				writer.println("Hi this is Anna. Pleased to meet you. I see you are coming from " + ip + " using your remote port " + remotePort + " to access my port " + localPort + ".");

				initializing = false;
			} catch (IOException e) {
				System.err.println("IO Error. Please try again.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void respondWithTime() {
		Socket timeSocket = null;
		InputStream timeStream = null;
		BufferedReader timeReader = null;

		try { 
			timeSocket = new Socket(SocketConstants.NIST, SocketConstants.TIMEPORTNUMBER);

			timeStream = timeSocket.getInputStream();
			timeReader = new BufferedReader(new InputStreamReader(timeStream));

			while ((received = timeReader.readLine()) != null) {
				writer.println(received);
			}

			// close NIST socket to prevent resource leaks
			timeSocket.close();
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void respondWithDNS() {
		String domainName = inputFromClient[SocketConstants.DOMAINNAMEINDEX];

		InetAddress ipClient = null;

		try {
			ipClient = InetAddress.getByName(domainName);
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host Exception. Please try again.");
		}

		String ipResponse = ipClient.toString();
		String hostNameResponse = ipClient.getHostName();
		String fqdnResponse = ipClient.getCanonicalHostName();

		writer.println("IP: " + ipResponse);
		writer.println("Host Name: " + hostNameResponse);
		writer.println("FQDN: " + fqdnResponse);
	}

	public static void respondWithEcho() {
		String echoContent = inputFromClient[SocketConstants.ECHOCONTENTINDEX];
		writer.println(echoContent);
	}


}
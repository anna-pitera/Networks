package edu.monmouth.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static Socket server;
	private static BufferedReader br;
	private static PrintWriter writer;
	private static OutputStream os;
	private static String received;

	public static void main(String[] args) {
		server = initialize(); // connect to server

		while (true) {
			printMenu();
			int choice = getUserChoice();

			// based on their choice execute appropriate method which handles all exceptions 
			switch (choice) {
			case 1 -> askServerForTime();
			case 2 -> sendCommandToServer();
			case 3 -> getDNSnameForIP();
			case 4 -> {
				System.out.println("Exiting program");
				return;
			}
			default -> System.out.println("Invalid option. Please try again.");
			}
		}

	}

	private static Socket initialize() {
		boolean initializing = true;
		
		while (initializing) {
			System.out.print("\nEnter IP of server: ");
			String userInput = SocketConstants.SCANNER.nextLine().trim();
			InetAddress ip = null;
			InputStream is = null;

			try {
				ip = InetAddress.getByName(userInput);

				server = new Socket(ip, SocketConstants.PORTNUMBER);

				is = server.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				
				os = server.getOutputStream();
				writer = new PrintWriter(os, true);

				// read server's response messages until it returns null, indicating there are no messages left to be read
				received = br.readLine();
				System.out.println(received);

				initializing = false;
			} catch (IOException e ) {
				System.err.println("Invalid input. Please try again.");
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}

		return server;
	}

	private static void printMenu() {
		System.out.println("\n--- Network Client Tool ---");
		System.out.println("1. What time is it?");
		System.out.println("2. Echo");
		System.out.println("3. Resolve DNS name to IP");
		System.out.println("4. Exit");
		System.out.print("Choose an option: ");
	}

	private static int getUserChoice() {
		try {
			return Integer.parseInt(SocketConstants.SCANNER.nextLine().trim());
		} catch (NumberFormatException e) {
			return SocketConstants.INVALIDCHOICE;
		}
	}

	private static void askServerForTime() {
		try {
			writer.println("time");
			
			for (int i = 0; i < SocketConstants.TIMERESPONSELINES; i++) {
				received = br.readLine();
				System.out.println(received);
			}
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void getDNSnameForIP() {
		System.out.print("Enter DNS name to resolve: "); 
		String dnsName = "dns " + SocketConstants.SCANNER.nextLine().trim();
		
		try {
			writer.println(dnsName);
			
			for (int i = 0; i < SocketConstants.DNSRESPONSELINES; i++) {
				received = br.readLine();
				System.out.println(received);
			}
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void sendCommandToServer() {
		System.out.print("Enter string to be echoed back from server: "); 
		String stringToEcho = "echo " + SocketConstants.SCANNER.nextLine().trim();
		
		try {
			writer.println(stringToEcho);
			
			for (int i = 0; i < SocketConstants.ECHORESPONSELINES; i++) {
				received = br.readLine();
				System.out.println(received);
			}
		} catch (IOException e) {
			System.err.println("IO Error. Please try again.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}

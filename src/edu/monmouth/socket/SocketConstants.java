package edu.monmouth.socket;

import java.util.Scanner;

public class SocketConstants {
	// network utility constants
	public static final Scanner SCANNER = new Scanner(System.in);
	public static final int PORTNUMBER = 3000;
	public static final String DELIMITER = " ";
	public static final int SPLITLIMIT = 2;
	public static final int TIMEPORTNUMBER = 13;
	public static final String NIST = "ntp-d.nist.gov";
	
	// indices
	public static final int COMMANDINDEX = 0;
	public static final int DOMAINNAMEINDEX = 1;
	public static final int ECHOCONTENTINDEX = 1;
	
	// response lines
	public static final int TIMERESPONSELINES = 2;
	public static final int DNSRESPONSELINES = 3;
	public static final int ECHORESPONSELINES = 1;
	
	// user inputs
	public static final int INVALIDCHOICE = -1;
	
	// exit codes
	public static final int CLIENTDISCONNECTED = -1;
	
	// private constructor - nobody needs to instantiate SocketConstants
	private SocketConstants() {}
}

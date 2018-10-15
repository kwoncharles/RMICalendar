package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import server.CalendarServerIntf;

// CalendarClientDriver looks up the Remote server by preset name.
// And then, makes thread to start the interacting.

public class CalendarClientDriver {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		String calendarServerURL = "rmi://localhost/RMICalendarServer";
		CalendarServerIntf calendarServer = (CalendarServerIntf) Naming.lookup(calendarServerURL);
		System.out.println(calendarServer.getMessage());
		
		new Thread(new CalendarClient(calendarServer)).start();
	}
}

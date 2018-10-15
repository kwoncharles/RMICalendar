package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

// CalendarServerDriver registers a name for a remote object that can be used at a later time to look up that remote object
public class CalendarServerDriver {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Naming.rebind("RMICalendarServer", new CalendarServer());
	}
}
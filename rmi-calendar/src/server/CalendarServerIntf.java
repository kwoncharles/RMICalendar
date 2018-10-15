package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;
import java.util.Vector;

import schedule.Schedule;

public interface CalendarServerIntf extends Remote {
	
	public String getMessage() throws RemoteException;
	public int addSchedule(GregorianCalendar from, GregorianCalendar to, String desc) throws RemoteException;
	public int deleteSchedule(int id) throws RemoteException;
	public Vector<Schedule> retrieveSchedules(GregorianCalendar from, GregorianCalendar to) throws RemoteException;
	public Vector<Schedule> getSchedules() throws RemoteException;
	
}


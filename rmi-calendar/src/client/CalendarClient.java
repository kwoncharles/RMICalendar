package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import schedule.Schedule;
import server.CalendarServerIntf;

/*
 * 	Extends		-	UnicastRemoteObject	: Used for exporting a remote object with JRMP and obtaining a stub that 
 * 					communicates to the remote object. Stubs are either generated at runtime 
 *   				using dynamic proxy objects, or they are generated statically at build time, 
*					typically using the rmic tool. 
 * 	Implements 	-	CalendarClientIntf	:
 * 				-	Runnable			: Can be put into Thread
 */	  	
public class CalendarClient extends UnicastRemoteObject implements CalendarClientIntf, Runnable {
	/*
	 * 
	 * CalendarClient can connect with CalendarServer remotely by threading. 
	 * It calls the method of Remote server and gets the result.
	 */
	
	private static final long serialVersionUID = 1L;
	private CalendarServerIntf calendarServer;
	
	
	// Initialize Client
	protected CalendarClient(CalendarServerIntf calendarServer) throws RemoteException {
		this.calendarServer = calendarServer;
	}
	
	@SuppressWarnings("resource")
	public void run() {
		/*
		 *  Method that ran when thread starts.
		 * 			
		 * 		scanner	: Get the message from console line
		 * 		message : Store the message comes from scanner
		 */
		
		Scanner scanner = new Scanner(System.in); 
		String message;

		while(true) {
			System.out.println("What do you want to do?\n1. add 2. delete 3. retrieve 4. all\n");
			
			message = scanner.nextLine();
			GregorianCalendar from;
			GregorianCalendar to;
			String desc;
			
			Vector<Schedule> result = new Vector<Schedule>();
			
			int id = 0;
			
			
			if(message.equals("add") || message.equals("1")) {
				/*
				 * If user select 'add', It calls addSchedule method and pass the datetimes of start and end point and description.
				 * As a result, it takes the Id of schedule that registered in remote server.
				 * If it was not able to be registered, remote server returns -1 and it prints error message.
				 */
				
				try {
					System.out.println("From when? (yy.MM.dd.hh.mm) ");
					from = convertStringToGregorian(scanner.nextLine());
					System.out.println("When it finish? (yy.MM.dd.hh.mm) ");
					to = convertStringToGregorian(scanner.nextLine());
					System.out.println("Please describe about this schedule: ");
					desc = scanner.nextLine();
					
					if(from.after(to)) {
						System.out.println("\n#Error: Please type the datetime correctly.\n");
						continue;
					}
					
					id = calendarServer.addSchedule(from, to, desc );
					if(id == -1)
						System.out.println("\n#Error: It conflicts with existing schedules\n");
					else
						System.out.println("\nYour schedule is added to your Calendar with Id " + id + " !\n");
						
				} catch (ParseException | RemoteException e) {
					e.printStackTrace();
				}
			} else if(message.equals("delete") || message.equals("2")) {
				/*
				 * If user select 'delete', calls deleteSchedule method and pass the Id of schedule that wants to be removed from remote server.
				 * If server deletes the specified schedule successfully, client gets the same Id as a return value.
				 * But If server was not able to find the schedule with the Id, client gets -1 and prints the error message.
				 */
				
				int deleteResult = 0;
				
				try {
					System.out.println("Please input the ID number: ");
					id = Integer.parseInt(scanner.nextLine());
					deleteResult = calendarServer.deleteSchedule(id);
				} catch (NumberFormatException | RemoteException e) {
					e.printStackTrace();
				}
				
				if(deleteResult == -1) {
					System.out.println("\n#Error: #" + id + " does not exist.\n");
				} else {
					System.out.println("\n#" + id + " is deleted successfully.\n");
				}
			} else if(message.equals("retrieve") || message.equals("3")) {		
				/*
				 * If user selects 'retrieve', calls retrieveSchedule method and pass the period. 
				 * It takes the result vector array as a return value.
				 * If there is no schedule in that period, result will be empty and prints the result.
				 */
				
				try {
					System.out.println("From when? (yy.MM.dd.hh.mm) ");
					from = convertStringToGregorian(scanner.nextLine());
					System.out.println("Until when? (yy.MM.dd.hh.mm) ");
					to = convertStringToGregorian(scanner.nextLine());
					
					if(from.after(to)) {
						System.out.println("\n#Error: Please type the datetime correctly.\n");
						continue;
					}
					
					result = calendarServer.retrieveSchedules(from, to);
				} catch (ParseException | RemoteException e) {
					e.printStackTrace();
				}		
				
				if(!result.isEmpty()) {
					Iterator<Schedule> iterator = result.iterator();
					
					while(iterator.hasNext()) {
						Schedule cur = iterator.next();
						System.out.println("\nId\t\t: " + cur.getId() + "\n" +
										   "From\t\t: " + cur.getFrom().getTime() + "\n" + 
										   "To\t\t: " + cur.getTo().getTime() + "\n" + 
										   "Description\t: " + cur.getDesc() + "\n\n");
					}
				} else {
					System.out.println("There are no Schedules");
				}
			} else if(message.equals("all") || message.equals("4")) {
				// If user select 'all', it calls getSchedules. Then Remote server will pass the whole list of registered schedules.
				
				Vector<Schedule> schedules = new Vector<Schedule>();
				
				try {
					schedules = calendarServer.getSchedules(); 
				} catch (RemoteException e) {
					System.out.println("#Error: Something went wrong");
					e.printStackTrace();
				}
				
				Iterator<Schedule> iterator = schedules.iterator();
				if(!schedules.isEmpty()) {
					while(iterator.hasNext()) {
						Schedule cur = iterator.next();
						System.out.println("\nId\t\t: " + cur.getId() + "\n" +
										   "From\t\t: " + cur.getFrom().getTime() + "\n" + 
										   "To\t\t: " + cur.getTo().getTime() + "\n" + 
										   "Description\t: " + cur.getDesc() + "\n\n");
					}
				} else {
					System.out.println("There are no Schedules");
				}				
			} else {
				System.out.println("\n#Error: Invalid command.\n You can use \'add\', \'delete\', \'retreive\'");
			}			
			
		}
	}
	
	public GregorianCalendar convertStringToGregorian(String str) throws ParseException {
		/*
		 * This method takes a string and returns GregorianCalendar type value.
		 * Its input format must to be like "yy.MM.dd.hh.mm".
		 */
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
		java.util.Date date = sdf.parse(str.replace(".", ""));
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(date);
		
		return calender;
	}
}

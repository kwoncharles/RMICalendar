package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import schedule.Schedule;

/*
 * 	Extends		-	UnicastRemoteObject	: Used for exporting a remote object with JRMP and obtaining a stub that 
 * 					communicates to the remote object. Stubs are either generated at runtime 
 * 					using dynamic proxy objects, or they are generated statically at build time, 
 *					typically using the rmic tool. 
 * 	Implements 	-	CalendarServerIntf	:
 * 				-	Serializable		: To be marshalled
 */	  	
public class CalendarServer extends UnicastRemoteObject implements CalendarServerIntf, Serializable {
	
	/*
	 * CalendarServer class has informations of a schedule.
	 *
	 *	seqId			: ID sequence of schedules
	 *	schedules		: List of schedules
	 */
	
	private static final long serialVersionUID = 1L;
	private int seqId = 0;
	private String MESSAGE = "\nWELCOME!\n";
	
	private Vector<Schedule> schedules = new Vector<Schedule>();
	
	protected CalendarServer() throws RemoteException {
		super();
	}

	//experiment
	@Override
	public String getMessage() throws RemoteException {
        return MESSAGE;
    }

	@Override
	public int addSchedule(GregorianCalendar from, GregorianCalendar to, String desc) throws RemoteException {
		
		/*
		 * Save schedule in a chronological sequence.
		 *	
		 *	uuid	: Id of current schedule
		 *	i		: Index for loop
		 *	len		: Number of existing schedules
		 */
		
		Schedule schedule = new Schedule(from, to, desc);
		int uuid = seqId++;
		int i = 0;
		int len = schedules.size();
		
		schedule.setId(uuid);
		
		if(len != 0 && schedules.get(len-1).getTo().before(from)) {
			// If new schedule is the latest one, add new schedule at the end.
			schedules.add(len, schedule);
			
			return uuid;
		} else if(len == 0 || schedules.get(0).getFrom().after(to)) {
			// Else If new schedule is the earliest or the first one, add new schedule at the beginning.
			schedules.add(0, schedule);
			
			return uuid;
		}
		
		while(i < len-1) {
			// Loop until find the correct position.
			if(from.after(schedules.get(i++).getTo()) &&
					to.before(schedules.get(i).getFrom())) {
				schedules.add(i, schedule);
				
				return uuid;
			} 			
		}
		
		// New schedule conflicts with existing schedules. Decrease "this.seqId"
		seqId--;
		
		return -1;
	}

	@Override
	public int deleteSchedule(int id) throws RemoteException {
		
		/*
		 * Delete schedule that corresponding with specified Id
		 * 	
		 * 	iterator	: Iterator of "this.schedules"
		 */
		
		Iterator<Schedule> iterator = this.schedules.iterator();
		
		while(iterator.hasNext()) {
			if(iterator.next().getId() == id) {
				iterator.remove();
				
				return id;
			}
 		}
		
		return -1;
	}

	@Override
	public Vector<Schedule> retrieveSchedules(GregorianCalendar from, GregorianCalendar to) throws RemoteException {

		/*
		 * Retrieve schedules that corresponding with specified from, to
		 * 
		 * 	result 	: Vector list storing result
		 * 	saving	: Boolean flag
		 * 	iterator: Iterator of "this.schedules"
		 */
		
		Vector<Schedule> result = new Vector<Schedule>();
		boolean saving = false;
		
		Iterator<Schedule> iterator = this.schedules.iterator();
		
		while(iterator.hasNext()) {
			Schedule cur = iterator.next();
			
			
			if(!saving) {
				// If it doesn't find the starting point yet
				if(cur.getFrom().after(from)) {
					saving = !saving;
					result.add(cur);
				}
			} else {
				// If it found the starting point and on going to save
				if(cur.getTo().after(to)) {
					// If it finds the stop point
					if(cur.getFrom().before(to)) {
						result.add(cur);
					}
					break;
				}
			}
		}
		
		return result;
	}	
	
	@Override
	public Vector<Schedule> getSchedules() throws RemoteException {
		// Method returns whole "this.schedules"
		
		return this.schedules;
	}
}

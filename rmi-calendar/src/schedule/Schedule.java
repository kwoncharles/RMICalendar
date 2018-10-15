package schedule;

import java.io.Serializable;
import java.util.GregorianCalendar;

// Implements 'Serializable' to be marshalled
public class Schedule implements Serializable {  
	
	/*
	 
	Schedule class has informations of a schedule.
	 
	 id		: Identifier number of schedule
	 from	: Datetime of when schedule starts
	 to		: Datetime of when schedule ends
	 desc	: Description of schedule
	 
	*/
	
	private static final long serialVersionUID = 1L;
	private int id;
    private GregorianCalendar from;
    private GregorianCalendar to;
    private String desc;
    
	public Schedule(GregorianCalendar from, GregorianCalendar to, String desc) {
		super();
		this.from = from;
		this.to = to;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}   
	
	public GregorianCalendar getFrom() {
		return from;
	}
	
	public void setFrom(GregorianCalendar from) {
		this.from = from;
	}
	
	public GregorianCalendar getTo() {
		return to;
	}
	
	public void setTo(GregorianCalendar to) {
		this.to = to;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	} 
} 
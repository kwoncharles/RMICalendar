# RMICalendar
Implementing RMI(Remote Method Invocation) with JAVA


•	Language   	- Java 10
•	Environment	- Eclipse Java EE IDE (Photon Release 4.8.0)
•	OS		      - macOS Mojave(10.14)


# How to compile?

1) Open command line window. 

2) Change the current working directory to parent of server,client,schedule package.

3) Precompile/generate server skeleton & client proxy
  ### $ rmic server.CalendarServer
  ### $ rmic client.CalendarClient

4) Create RMI registry
  ### $ rmiregistry 
  
5) Open another command line window. In the same working directory, register the Server
  ### $ java server.CalendarServerDriver

6) In another command line window, Execute client and implement invocation
  ### $ java client.CalendarClientDriver


## Warning

1) Input datetime format have to be like "yy.MM.dd.hh.mm"

2) New schedule is added in sequence of date. And it won't be added if it conflicts with existing schedules.

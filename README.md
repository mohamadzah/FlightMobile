# FlightMobile
The FlightMobile Android Application is a project that I created while attending the Advanced Programming 2 class at Bar-Ilan University
## Description and Design
In this project we developed an Android mobile application using Kotlin, and a proxy server using ASP.NET Core C# technology

The Applicaton allows the user to control and fly an aeroplane within the Flight Simulator, by using HTTP Protocol the application communicates with the proxy server that sends real-time data and values to the simulator. The Proxy server can connect to up to one client only. Similiar to other projects in this class, we made sure to use and implement the MVVM architecture and also take into consideration the coding conventions of the Kotlin language.

**_Design and visuals_**:
We have both a main screen, and an activity screen,
- *Main screen*: In the main screen the user will be shown their 5 most recently used URLs, that the user used for connecting. Clicking on one of the links will immediately cause it to be shown in the textbox below for the user's convenience. The CONNECT button can be used to connect to the typed URL
![main login screen app github11](https://github.com/user-attachments/assets/34c9edda-d759-4c44-99af-b3161f796e34)

- *Activity screen*: In the activity screen the user can see the Joystick and sliders used to control the Flight (Throttle, Rudder, Aileron, Elevator), and a view of the airplane's cockpit. The view is updated consistently by displaying updated screenshots of the airplane's state
  ![mobile app github111](https://github.com/user-attachments/assets/3ddb399d-f56c-4674-b3ae-52302c9e2565)

#### Support

FlightGear simulator: https://www.flightgear.org/
Kotlin coding conventions: https://kotlinlang.org/docs/coding-conventions.html

###### Authors

***The author(s) of this program is Mohamad Zahalka***

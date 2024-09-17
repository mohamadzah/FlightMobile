# FlightMobile
The FlightMobile Android Application is a project that I created while attending the Advanced Programming 2 class at Bar-Ilan University
## Description and Design
In this project we developed an Android mobile application using Kotlin, and a proxy server using ASP.NET Core C# technology

The Applicaton allows the user to control and fly an aeroplane within the Flight Simulator, by using HTTP Protocol the application communicates with the proxy server that sends real-time data and values to the simulator. The Proxy server can connect to up to one client only. Similiar to other projects in this class, we made sure to use and implement the MVVM architecture and also take into consideration the coding conventions of the Kotlin language.

**_Design and visuals_**:
We have both a main screen, and an activity screen,
- *Main screen*: In the main screen the user will be shown their 5 most recently used URLs, that the user used for connecting. Clicking on one of the links will immediately cause it to be shown in the textbox below for the user's convenience. The CONNECT button can be used to connect to the typed URL
- *Activity screen*: In the activity screen the user can see the Joystick and sliders used to control the Flight (Throttle, Rudder, Aileron, Elevator), and a view of the airplane's cockpit. The view is updated consistently by displaying updated screenshots of the airplane's state
- The user can use the application in both Portrait and Landscape mode, whichever is more comfortable.
  
![main login screen app github11](https://github.com/user-attachments/assets/34c9edda-d759-4c44-99af-b3161f796e34)     - - - - - - - - - - - - - - - -     ![mobile app github111](https://github.com/user-attachments/assets/3ddb399d-f56c-4674-b3ae-52302c9e2565)

#### Compiling and running
To run this program, there are a couple of things that need to be in place:
- 1: First download all the files of this project
- 2: Ensure you have flask and python installed in order to run both the dummyflight.py and app.py (found in the folder dummy_server), together they will simulate a dummy simulator
- 3: Open the FlightMobileApp using Android studio, and run an Emulator to launch the app.
- 4: Open FlightMobileServer, go to Out, there you can find an exe file called FlightMobileWeb.exe (run this only after you've launched both dummyflight.py and app.py)
- 5: In the app, enter http://10.0.2.2:5002/, connect and enjoy!

**_Note_**: We used a dummy flightgear server to speed up the process of development and for the convenience, It imitates a server which sends and accepts the relevant data, you can use the FlightGear flight simulator instead, but in order to to that, you have to open FlightGear, go to settings and then scroll down and there you will find Additional settings, there you should add these two lines

**--generic=socket,out,10,127.0.0.1,5400,tcp,generic_small**

**--telnet=socket,in,10,127.0.0.1,5402,tcp**

Launch and enjoy! (Note: At the time we did not take into consideration running the plane engine and simulating a full on real flight experience, the main goal was to establish the communication and the back and forth exchange of data and experiencing a real client-server environment)

##### Support

FlightGear simulator: https://www.flightgear.org/

Kotlin coding conventions: https://kotlinlang.org/docs/coding-conventions.html

###### Authors

***The author(s) of this program is Mohamad Zahalka***

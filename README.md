# ArduCV

Controlling Arduino RC car using gestures.

This project consists of 3 different modules
separated in different folders. The __ArduActuator__ is the code that
is running on Arduino car, which just receives signals from Serial device
(in this case, an Android device) and performs a specific action. The 
__MobileController__ is an Android application that receives signals from WiFi
and send data to Arduino (it is done like this because the phone is intended to
be on the car). The __RobotManagement__ module captures commands from webcam and
sends the information via TCP/IP protocol (intended to reach the 
MobileController module). 
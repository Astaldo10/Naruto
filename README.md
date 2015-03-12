Naruto: Living the Ninja Way
======

This Android application complements http://astaldo.noip.me/rol/naruto/naruto.php forum rol game. It is used to establish a connection to the server by a Socket connection. By a sequence of requests, this app is able to let a user log in the app, to get all the posts a user can access to, to get all groups an user can access to and to reply to that post group. It is still in developement.

It has 3 main Activities (well, it only has 3 activities at the moment): 

  1. "Identificacion" lets the user to authenticate at the server, sending a login to the server where its processed and determined if the login is correct (also this Activity saves the user login info as a Preference, to let the user login without typing everytime this login, but it is still in developement). This activity is the responsible for the login request. 
  
  2. "Foro" shows the user the posts he has access to at the rol. This is determined by the groups he can access to. This activity is the responsible for the get requests, i.e. the post get request and the group get request to the server. At this activity the user can also access to a simple menu, where he can close the app, close his session and go back for another login, reply to a post group or refresh the groups (looking for new posts).
  
  3. "Responder" allows the user to reply to a group. This activity is the responsible for the post request. This request will contain the user's login username, his color, the group he is replying to and the message to send. Later, the date and time will be added to this request.
  
All the connections to the server are made through the "Conexion" class. This class is the real responsive of all connection to the server. The activities only create secondary threads to run on background the request to the server, while this class contains all the necesary to make the application works.

Finally, all necesary mapping keys (Preferences, JSON, Intent extras), encoding (UTF8, ISO) and request keys to the server (ACCESS, GET, GROUP, POST) are contained in "Constantes" class, to make the access to this keys as easier as posible.

Enjoy the app.

Andrés López Díaz

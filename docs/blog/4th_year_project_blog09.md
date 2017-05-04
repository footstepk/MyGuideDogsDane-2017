# Blog: MyGuideDogsDane | 4th Year Project blog/09

**Kok Heng Chan**

## My blog for 04 May 2017


# The concern use of Google Place APIs

   Just recall back to a discussion with Mark(Supervisor) on the Location and Map to obtain the last movement location of  my position. I was worrying about the accuracy of the information that display to me about my location name, and its battery consuming.  After a lenghty of test and trial, i am pretty sure now this is what I want from my location activity. To retrieve the device's last known location [1].
The fused location provider is one of the location APIs in Google Play services. It manages the underlying location technology and provides a simple API so that you can specify requirements at a high level, like high accuracy or low power. It also optimizes the device's use of battery power.
https://developer.android.com/training/location/retrieve-current.html

   Further more, the use of Google Place APIs wasn't a clear at the moment, however, this has been resolved after couple of extra coding to assist the routing parameter and it worked fine for the app.  I just want bring my phone to test it out this faternoon.

Adding to the suggestion by Mark, overlapping the use of the route to detect my current position to alert user(s) whether user(s) want select from a suggestion point or keep using the saving route!
I had done some research, it is possible, so I am now entering to change some design and some code to ficilatate the suggestion. 
http://developer.android.com/helps
This overlapping coding is completed and now it has the alert capability to alert me whether I want to continue to record or use the existing route while performing the recording route.

# Implementation the Explore activity

   I supposed after reading on the Google's Map APIs and the GeoLocation Map APIs, you wonder the best practise use of the Map's library to implement my "Explore" activity and connect them with Google Map (like embedded Map) and and then provide all the source such as the route details and it should provide me step-by-step instruction in text to help me to locate the location that I wish to go.
   I have had coded my Map xml file to simulate the use of the Google's Location Map, it worked, now I need read other materials to assist my explore activity to get the instruction to connect them together.
https://developers.google.com/maps/documentation/android-api/start

Meannwhile, there are one particular issue here for me which is the linking to the dababase that I just created and trying to obtain the data and release it to my activity request.
At the moment, the use of the navagation step is completed and it work fine I thought! I will test it out this afternoon.
There are many suggestion over the internet, perhaps, I thought the best practise is using "one touch" gesture rather than create an "Yes" or "No" interface to facilitate user input request because it would make sense for me that reducing interaction on the screen while user moving around.

# Trying the activity

   The location activity is done, the saving activity is done, route selection action(class) is done, just the exploring activity and the overlapping the route algorithm and then I am finish!
The overlapping algorithm is completed, after a discussion with expertee (Online resource), finally, I got the overlaping method implemented into the "recording" activity.

# Preparation May's exam

   I have been busying on the 4th project and have not much time to study, and now I am much relief and I just want to start the exam revission tonight onward! I don't want stuck here any more DCU!

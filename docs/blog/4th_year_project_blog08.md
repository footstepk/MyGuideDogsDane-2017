# Blog: MyGuideDogsDane | 4th Year Project blog/08

**Kok Heng Chan**

## My blog for 24 April 2017


# Understanding use of Google's Location and Map in Android

   Just recall back to a discussion with Mark(Supervisor) on the Location and Map to obtain the last movement location of  my position. I was worrying about the accuracy of the information that display to me about my location name, and its battery consuming.  After a lenghty of test and trial, i am pretty sure now this is what I want from my location activity. To retrieve the device's last known location [1].
The fused location provider is one of the location APIs in Google Play services. It manages the underlying location technology and provides a simple API so that you can specify requirements at a high level, like high accuracy or low power. It also optimizes the device's use of battery power.
https://developer.android.com/training/location/retrieve-current.html

Adding to the suggestion by Mark, overlapping the use of the route to detect my current position to alert user(s) whether user(s) want select from a suggestion point or keep using the saving route!
I had done some research, it is possible, so I am now entering to change some design and some code to ficilatate the suggestion. 
http://developer.android.com/helps

# Implementation the Explore activity

   I supposed after reading on the Google's Map APIs, you wonder the best practise use of the Map's library to implement my "Explore" activity and connect them with Google Map (like embedded Map) and and then provide all the source such as the route details and it should provide me step-by-step instruction in text to help me to locate the location that I wish to go.
   I have had coded my Map xml file to simulate the use of the Google's Location Map, it worked, now I need read other materials to assist my explore activity to get the instruction to connect them together.
https://developers.google.com/maps/documentation/android-api/start

Meannwhile, there are one particular issue here for me which is the linking to the dababase that I just created and trying to obtain the data and release it to my activity request.

# Trying the activity

   The location activity is done, the saving activity is done, route selection action(class) is done, just the exploring activity and the overlapping the route algorithm and then I am finish!
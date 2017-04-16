# Blog: MyGuideDogsDane | 4th Year Project blog/06

**Kok Heng Chan**

## My blog for 16 April 2017

# Understanding use of SQLLite in Android

   Just recall back to a discussion with Mark(Supervisor) on the SQLLite to stor the recording route data.  There is a simple way for me to create and insert(recorded data) into the SQLLite database, I have done the database and I am trying to connect it to my record route activity and it looked/sound okay to me! I been watching(listening for me) in youtube for this:
https://www.youtube.com/watch?v=cp2rL3sAFmI

# The My Location layout images

   I have been trying to insert into this  repo, I wish I got it right!
   This activity will access the location information and it will provide me the name of the current location. Sound Great!

![App_Layout_Demo1](https://gitlab.computing.dcu.ie/chankok2/2017-ca400-chankok2/raw/master/docs/blog/images/App_Layout_Demo1.jpg)

    <pre>![alternative text](https://gitlab.computing.dcu.ie/chankok2/2017-ca400-chankok2/raw/master/docs/blog/images/App_Layout_Demo1.jpg)</pre>


# Designing the Explore activity

			  According to Geo Location Map APIs, I need a GeoMap library to simulate the current location and compare the geographical longtitute and latitute to map the surrounding areas which have a address data in order to have the actual grid accordingly and extract it to my activity onCreate()...
https://developers.google.com/maps/android/
# genesis-android
This Android App is a modern, mobile-friendly version of the Genesis Parent Portal, which is a web service 
that many school districts in NJ use to keep track of student attendance, grades, contact info, etc. The 
app works by sending HTTP requests to the Parent Portal's site with [JSoup](https://jsoup.org/), and 
parsing the resulting HTML pages to extract a student's basic info, schedule, grades, assignments, and so on.

## Features
* A full class schedule & overview page
* Lists of past & upcoming assignments for each class
* Automatic grade calculation based on your past assignments
* Notifications for when new grades are released

## Building & Installing
Building & installing genesis-android on your phone should mostly be the same process as building any
development app with the Android SDK and ADB. One key detail to note is that in [GenesisHTTP.java](
https://github.com/devonulrich/genesis-android/blob/master/app/src/main/java/com/devonulrich/genesisclient/network/GenesisHTTP.java),
all URL constants are currently for Cresskill's school district. If you want to use this app for your 
school's Parent Portal, you **must** replace all of these string constants with their respective URLs
for your own school's parent portal.

Also, this app has gone unmaintained, so you may need to modify/fix some of the HTML-parsing code if
the Gradebook's HTML has changed since the last time I worked on this app. It's also designed for an
older version of Android, so while it should still work just fine, it may use older features that have
become deprecated in recent API updates.

## Other Info
This app is an unofficial client for Genesis Parent Portal, and is not affiliated with Genesis
Educational Services. It was created by me as a side project in high school, mainly because Genesis'
website didn't work well on phones until around 2017.

# Privacy-Lept System

Privacy-Lept System is a play on words where the system will literally zero your privacy where it will show when you are
online and from which device you are connecting to the internet. Why I built this? No clue; I just wanted to show anyone
who visits my personal website https://niweera.gq that I am online or offline. Also, I just wanted to try to do
something I've never done 😂.

Privacy-Lept System is consisted with three components.

![image](https://i.imgur.com/5FpSJKb.jpg)

1. [Privacy-Lept-Desktop](https://github.com/Niweera/privacy-lept-desktop) (Windows service)

2. [Privacy-Lept-Mobile](https://github.com/Niweera/privacy-lept-app) (Android application)

3. [Privacy-Lept-API](https://github.com/Niweera/privacy-lept-api) (Socket.io NodeJS Express App)

When I'm online, from Desktop and Mobile, it will notify the Privacy-Lept-API and the Privacy-Lept-API will notify my
website using [socket.io](https://socket.io/).

## Privacy-Lept-Mobile

This is the Android mobile application for the Privacy-Lept system. In this Android application, it runs in the
background and notify the [Privacy-Lept-API](https://github.com/Niweera/privacy-lept-api) when the user has unlocked
their phone and currently the screen is on. (If the device has internet connection, the device is unlocked and the
screen is on, it is considered that the user is online.)

The user has the ability to turn on the privacy mode where it will stop notifying the API whether the user is online.
This is on by default (by adhering to privacy by design principles 😂).

| Now the Android app will notify the API whether the user is online. |  Now the Android app will stop notifying the API whether the user is online.|
|:-------------------------------------------------------------------:|:-------------------------:|
|              ![image](https://i.imgur.com/uLG37gx.jpg)              |  ![image](https://i.imgur.com/CxgjWFW.jpg)|







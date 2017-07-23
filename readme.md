#Camera Notifier
This side project started out as a way for me to get my old home security cameras to send me email when they saw motion.  As I started digging through the old iControl cameras, I found other neat things I could possibly do;

1. Administrate the camera remotely(see; [Sercomm API findings here](https://github.com/edent/Sercomm-API))
2. Use a PIR(passive IR) sensor on the camera
3. Trigger the camera to capture video on events other than motion(like a door opening)

This lead me to want to to continue further and see if I would be able to build out my own open source security solution and home automation while running it off a Raspberry Pi. So some of the goals of the project;

1. Implement a centralized server using open source technologies(in progress)
2. Implement a "hub" based on an Raspberry Pi(depends on 1)
3. Implement a way to talk to zigbee and / or zwave devices and have them be able to trigger events on the cameras(depends on 1, is part of 2 probably)
4. Implement mobile apps

I will distribute this with the GPLv3 license.  If you're redistributing this software and selling that redistribution I ask one thing first email me and let me know, beyond that, this software is free to use for anything and any project you want to do to build on top of it.

##Other little notes
I'm usually updating or adding to this on a nightly basis.  Usually during the week I add one feature/endpoint to accomplish something useful.  I won't always note in the readme what was added, but github's commit comments will usually serve to indicate what is new/changed.

##Status
* Build: [![Build Status](https://travis-ci.org/djr4488/camera-notifier.svg?branch=master)](https://travis-ci.org/djr4488/camera-notifier)
* * Coverage: [![Coverage Status](https://coveralls.io/repos/github/djr4488/camera-notifier/badge.svg?branch=master)](https://coveralls.io/github/djr4488/camera-notifier?branch=master)  [![codecov](https://codecov.io/gh/djr4488/camera-notifier/branch/master/graph/badge.svg)](https://codecov.io/gh/djr4488/camera-notifier)

## How to use this software
1. You will need administrator access to your camera
2. You need to mark what you want for motion detection
3. You need to go to HTTP page under the events subsection
4. You need to provide the URL and port that this software is running at(e.g. http://a.b.c.d:8080/api/camera/post/<your camera name no spaces>)
5. ~~Eventually, if there is enough support for this software maybe I will centralize where it runs at and require a user name and password~~ You will need to create a user through the addUser endpoint of the app but first;
6. Once your camera is setup, you will need TomEE 7.0.x and configure the tomee.xml adding a <Resource> section for your email see below in the tomee config section.
7. Compile the war file(eventually I will provide a self executable jar, this is still an app in very early development)
8. Deploy the app to TomEE
9. To add a user, you can use the swagger interface(I don't yet have a UI, a sample request is provided below)
10. You will also need to add your camera via the swagger interface, and a sample request is also provided below
11. The swagger interface can be found if running on a local machine: http://localhost:8080/

## TODOs
1. ~~Add database configuration to tomee.xml~~
2. ~~Add entities for users, cameras~~
3. ~~Finish initial authorization endpoints, filters, services and utilities~~ 
4. ~~Add swagger ui for rest documentation~~
5. Add front end so nobody has to call rest endpoints directly
6. Build "hub" software
7. Build mobile software
8. Consider making this a fatjar for easier deployment

* filters and endpoints and services
* user management: ~~add~~, ~~login~~, ~~change password~~, ~~reset password~~
* camera management: ~~add~~, ~~delete~~, ~~email captures~~, ~~notify of captures~~
* trigger management: ~~trigger~~, ~~record event~~

All of the above depends on level of interest in the application, and how much free time I continue to have and whether ultimately, anybody wishes to donate time, talent, or the root of all evils money to help further this along.

## Contact
I can be contacted via github, and the issues tabs I believe.  Or you can email me, promise it isn't hard to find that email I'm sure.

##TomEE configuration
```
<?xml version="1.0" encoding="UTF-8"?>
<tomee>
  <!-- see http://tomee.apache.org/containers-and-resources.html -->

  <!-- activate next line to be able to deploy applications in apps -->
  <!-- <Deployments dir="apps" /> -->
  	<Resource id="mail" type="javax.mail.Session">
            mail.smtp.host=smtp.gmail.com
            mail.smtp.starttls.enable=true
            mail.smtp.port=587
            mail.transport.protocol=smtp
            mail.smtp.auth=true
            mail.smtp.user=your email address
            mail.smtp.password=your password
            password=your password again?  not sure this is needed
	</Resource>
	<Resource id="jdbc/camera_notifier" type="javax.sql.DataSource">
            JdbcUrl=jdbc:mysql://localhost:3306/camera_notifier
            UserName=********
            Password=********
            JdbcDriver=com.mysql.jdbc.Driver
            MinIdle=5
            MaxWait=2500
            InitialSize=5
            ValidationQuery=SELECT 1
            TestOnBorrow=true
    </Resource>
</tomee>
```

##Sample Add User Request
```
{
  "userName" : "your user name",
  "password" : "your password",
  "emailAddress" : "an email address you would to forward video to",
  "confirmPassword" : "repeat your password"
}
```
##Sample Add Camera Request
```
{
  "cameraName" : "your camera name",
  "cameraAdministrator" : "your camera admin user name(used for event notifications eventually)",
  "cameraPassword" : "admin user password",
  "processNotifyEvents" : false,
  "processPostEvents" : true,
  "sendNotifyEventAsEmail" : false,
  "sendPostEventAsEmail" : true,
  "cameraTriggerUrl" : "url of your camera http_trigger.cgi(used for trigger events; like front door opening)",
  "cameraZone" : "useful name for where the camera is located"
}
```


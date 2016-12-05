#Camera Notifier
Purpose of this application is to provider owners of Sercomm cameras(tested with an old Xfinity home camera, iCamera-1000) a way to capture motion events and send email and other information through a more centralized means.  And personally, so I could send my notifications using my gmail account.

## How to use this software
1. You will need administrator access to your camera
2. You need to mark what you want for motion detection
3. You need to go to HTTP page under the events subsection
4. You need to provide the URL and port that this software is running at(e.g. http://a.b.c.d:8080/api/camera/post/<your camera name no spaces>)
5. Eventually, if there is enough support for this software maybe I will centralize where it runs at and require a user name and password
6. Once your camera is setup, you will need TomEE 7.0.x and configure the tomee.xml adding a <Resource> section for your email see:
```
<?xml version="1.0" encoding="UTF-8"?>
<tomee>
  <!-- see http://tomee.apache.org/containers-and-resources.html -->

  <!-- activate next line to be able to deploy applications in apps -->
  <!-- <Deployments dir="apps" /> -->
  	<Resource id="mail" type="javax.mail.Session">
   		mail.smtp.host=your smtp host
   		mail.smtp.starttls.enable=true
   		mail.smtp.port=587
   		mail.transport.protocol=smtp
   		mail.smtp.auth=true
   		mail.smtp.user=your email address
   		mail.smtp.password=your password
   		password=your password again?  not sure this is needed
	</Resource>
</tomee>
```
7. Compile the war file(eventually I will provide a self executable jar, this is still an app in very early development)
8. Deploy the app to TomEE
9. This should be it, at this point your camera should talk to this app and send you emails when it sees any motion

## TODOs
0. Make this a fatjar; easier deployment eventually
1. Make this so it can be a service on the web, that will simply email when a users camera sees motion
2. In order to make 1 work, need to add database entities
3. Also for 1 to work, need a front end and the ability for a user to create an account
4. Further TODOs, depending on interest in this; provide a way to do admin interfaces on cameras, etc.

All of the above depends on level of interest in the application, and how much free time I continue to have and whether ultimately, anybody wishes to donate time, talent, or the root of all evils money to help further this along.

## Contact
I can be contacted via github, and the issues tabs I believe.  Or you can email me, promise it isn't hard to find that email I'm sure.

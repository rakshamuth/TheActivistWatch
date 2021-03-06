<p align="center">
  <h1>ActivistWatch</h1>
</p>

ActivistWatch is an application that connects activists with organizations to help promote activism. ActivistWatch brings together organizers who are hosting events and activists who are looking for causes to be a part of.

--- 
#### Current Version: 1.01

##### New Features
1. Allows "swiping" on all events the dashboard
2. View your organizations and events
3. Load event images on pages and dashboard
4. **Fix** Login and registration errors that resulted in bad calls to the database
5. **Fix** Client-side decoding of getEvents() database response

--- 

## Installation ##

ActivistWatch is coded in Android Java and is only compatible on Android devices. 

#### Requirements (requires one of the following) ####

* An Android Device with Android KitKat (v4.4, API level 19) or sooner.
* OR Android Studio with an Android Emulator (v4.4, API level 19 or sooner) installed.

#### Installation Instructions:

* Ideally, ActivistWatch will be hosted on the Google Play store. To download, simply search "ActivistWatch" and install the official ActivistWatch application. No further setup is required, and no depdendent libraries must be included.

### Until ActivistWatch is on the play store:
* If not installed, please install Android Studio (https://developer.android.com/studio/index.html)
* If you plan on implementing local versions of the application, ensure you have a proper JDK installed (tutorial on link provided above). Android Studio comes with a simplified JDK for rapid development, but some issues may arise down the line if you rely solely on this simplified version.
* Open Android Studio and select "Import Project from Existing Sources"
* Select "Version Control", and type "https://github.com/rakshamuth/TheActivistWatch.git" in the box. Select "Next" until you get to "Finish." This will load the project code. Instantiating the project in this manner will automatically kickoff Gradle for you, downloading all dependencies found in gradle.build without any further work necessary.

* Once completed, hit the "Run" button at the top. If you have an Android Device connected via USB, you will be prompted to run on your device, otherwise, it will ask you to create a Virtual Android Device
   * If you do not have an Android Device, select "Create New Android Device," select "API 21" and "Install"
   * After this has installed, select the Android Device created, and hit "Start" (once the Android Device has loaded, you may need to hit the run button again)
   
Voila! The application will run on the simulator!

## Troubleshooting ##

The most common problems we have run into are:
* Ensure you have internet before calling functions that depend on the Database
* Ensure the server is running! Being unable to access the server does not throw catchable errors for sake of application functionality.
* Make sure you are not referencing JDK 1.8 features, and that your JDK is valid if looking to do extensive changes to the application.
* Make sure calls to the Database are asynchronous. All methods in Database.java use asynchronous calls, so if you use these, you need not worry about the synchroncy of your calls, but if you implement your own calls, synchronous calls will usually fail.

## Use ##

The application will guide you through setting up an account and using the interface. ActivistWatch is modeled after Tinder. Events will show up in your dashboard. "Swiping" yes or no on the events will add them to your events. In the future, we would like to add ways for these Events to push notifications and a matching algorithm for new Events based on previously swiped Events.


## Developers ##

For future developers, here are the specifications that define how the application works:

* src/main/java/com/example/patrickcaruso/activistwatch/ contains the modified src files
* ActivistWatch follows an MVC format
  * Models are located in their respective directories (Event, Organization, User, etc.)
  * Controllers are located in -Activity.java files in the designated directory
  * Views are located in src/main/res/layout/ and contain the screens visible in the application
  
* Database calls are static methods made within the Database/Database.java file
* Database calls supported (refer to javadocs for specifics):
  * **register(...)** - Attempts to register a new user in the database
  * **createEvent(...)** - creates an Event in the database
  * **createOrganization(...)** - creates an Organization in the database
  * **editUser(...)** - Updates Database entry of a User with a new User
  * **editOrganization(...)** - Updates Database entry of an Organization with a new Organization
  * **editEvent(...)** - Updates Database entry of an Event with a new Event
  * **lookupEvent(...)** - Returns database entry for a specific Event
  * **lookupUser(...)** - Returns database entry for a specific User
  * **lookupOrganization(...)** - Returns database entry for a specific Organization

* **IMPORTANT**: Database files are currently hosted on our own personal website for now. To enable this database functionality on your own server, please follow the following steps:
  * Download all files in databaseServer/ folder. This includes PHP files that are used to communicate with your server.
  * Setup an SQL Database by following this guide (use default 'localhost' user & no pass configuration) [https://support.managed.com/kb/a450/how-to-create-a-new-database-or-database-user-in-the-plesk-control-panel.aspx]
  * Upload the files in databaseServer/ to a directory on your website/server (follow this guide to use FTP to do this: https://www.digitalocean.com/community/tutorials/how-to-use-filezilla-to-transfer-and-manage-files-securely-on-your-vps)
  * Change any variable ending in "BASE" in URLConstants.java to point to the correct endpoint on your server. For example, if your server is "http://activistwatch.com", you will want to change 
```public static final String ADD_USER_URL_BASE = "http://patrickcaruso.com/addUser.php";```
  
  to 
  
```public static final String ADD_USER_URL_BASE = "http://activistwatch.com/addUser.php";```
  * Once these are changed, the application will properly be able to communicate with the server!

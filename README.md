Phonegap Netmera.com Plugin
=========================

Phonegap 3.0.0 plugin for Netmera.com push and event services

Using [Netmera.com's](http://www.netmera.com) REST API for push requires the installation id, which isn't available in JS
Registering to push notifications and receiving them requires native code. In applications where Javascript is preferred instead of native Android or iOS, using push notifications is not possible.

This plugin makes it possible to use native Netmera SDK's from Javascript in your phonegap application.

Installation
------------

Pick one of these two commands:

```

phonegap local plugin add https://github.com/Netmera/netmera-phonegap-plugin --variable NM_API_KEY=NETMERA_API_KEY --variable GOOGLE_PROJECT_ID=GOOGLE_PROJECT_ID
cordova plugin add https://github.com/Netmera/netmera-phonegap-plugin --variable NM_API_KEY=NETMERA_API_KEY --variable GOOGLE_PROJECT_ID=GOOGLE_PROJECT_ID

```

Initial Setup
-------------


Usage
-----
```javascript

/**
 * Registering the user to push notifications
 */
netmeraPlugin.register(function() {
	alert("Register successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Getting Device Detail
 */
netmeraPlugin.getDeviceDetail(function (deviceDetail) {
	// Getting location
	console.log(deviceDetail.location.latitude + ", " + deviceDetail.location.longitude);
	
	// Getting tags
	console.log(deviceDetail.tags);
	
	// Getting custom fields
	console.log(deviceDetail.customFields);
}, function(e) {
	// Handle error
	alert(e);
});


/**
 * Add Tags to the push user
 */
netmeraPlugin.addTags(["Sports", "News"], function() {
	alert("Adding tags successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Override existing tags of the push user
 * Example: The user will be registered to only Music tag.
 */
netmeraPlugin.overrideTags(["Music"], function() {
	alert("Overriding tags successfull");
}, function (e) {
	// Handle error
	alert(e);
});



/**
 * Update location of the user.
 */
netmeraPlugin.updateLocation(41.212413, 29.8719310, function() {
	alert("Update user location successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Set custom fields of the user
 */
var customFields = { 'age': 21, 'gender': 'male', 'city': 'Istanbul' };
netmeraPlugin.setCustomFields(customFields, function() {
	alert("Update user's Custom Fields successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Removing specific tags from the user
 */
netmeraPlugin.removeTags(["Music"], function() {
	alert("Remove Tags successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Unregistering the user from push notifications
 * The user will not get push notifications until you register him/her back.
 */
netmeraPlugin.unregister(function() {
	alert("Unregister successfull");
}, function (e) {
	// Handle error
	alert(e);
});


/**
 * Sending Events
 */
netmeraPlugin.sendEvent('AddToCartEvent', function () {
	alert("Event sent successfully");
}, function (e) {
	// Handle error
	alert(e);
});

/**
 * Sending Events with Custom Data
 */
var customData = { 'product': 'Basket Ball', 'color': 'Blue' };
netmeraPlugin.sendEventWithData('AddToCartEvent', customData, function () {
	alert("Event sent successfully");
}, function (e) {
	// Handle error
	alert(e);
});

```

Quirks
------

### Android

Netmera needs to be initialized once in the `onCreate` method of your application class using the `NetmeraPlugin.initNetmera(Application app, Class<? extends Activity> pushActivityClass)` method.

If you don’t have an application class (which is most likely the case for a Cordova app), you can create one using this template:

```java
package my.package.namespace;

import android.app.Application;
import org.apache.cordova.core.NetmeraPlugin;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetmeraPlugin.initNetmera(this, MainActivity.class);
    }
}
```
where MainActivity is the activity that opens when the application is opened. Then, you will need to add your application name to `AndroidManifest.xml`:

```xml
<application android:name="my.package.namespace.App" ... >...</application>
```
------
### iOS

Copy the following files to your project :

```ios

NetmeraPlugin.h
NetmeraPlugin.m

```

Add a reference for this plugin to the plugins section in config.xml :

```ref

 <!--NetmeraPlugin  -->
    <feature name="NetmeraPlugin">
        <param name="ios-package" value="NetmeraPlugin"/>
    </feature>
    
```
Manuel installation for iOS :
	[Netmera API](http://www.netmera.com/docs/#document-1)
    


Compatibility
-------------
Phonegap >= 3.0.0

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
```
<script type="text/javascript">
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
</script>
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


Compatibility
-------------
Phonegap >= 3.0.0

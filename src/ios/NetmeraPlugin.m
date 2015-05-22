//
//


#import <Netmera/Netmera.h>
#import <Netmera/NMEvent.h>
#import <Netmera/NMLocationManager.h>
#import <Netmera/NMGeolocation.h>
#import <Cordova/CDVViewController.h>
#import <Cordova/CDVCommandDelegateImpl.h>
#import <Cordova/CDVCommandQueue.h>
#import <Cordova/CDV.h>
#import <Cordova/CDVPluginResult.h>
#import <objc/runtime.h>

#import "NetmeraPlugin.h"

@implementation NetmeraPlugin

- (void)initialize:(CDVInvokedUrlCommand *)command                    // set Netmera apiKey
{
    NSString *NMapiKey = [command.arguments objectAtIndex:0];
    [Netmera setApiKey: NMapiKey];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK ];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getInstallationId:(CDVInvokedUrlCommand *)command             //returns a strings value
{
    [self.commandDelegate runInBackground:^{
    NSString *NMid = [Netmera getInstallationId];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:NMid];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}];
}

- (void)setTags: (CDVInvokedUrlCommand *)command
{
    NMDeviceDetail *deviceDetail = [[NMDeviceDetail alloc]init];
    NSArray *NMtags = [command.arguments objectAtIndex:0];
    BOOL NMboolControl = nil;
    NMboolControl = [[command.arguments objectAtIndex:1] boolValue];
    
    if (NMboolControl)
    {
        [deviceDetail overrideTagsWithNewTags:NMtags];
    }
    else
    {
        [deviceDetail addTags:NMtags];
    }
    
    [NMPushManager registerWithDeviceDetail:deviceDetail completionHandler:^(BOOL deviceDidRegister, NSError *error){
        
        CDVPluginResult *pluginResult =nil;
        if (!error)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)removeTags: (CDVInvokedUrlCommand *)command
{
    NMDeviceDetail *deviceDetail = [[NMDeviceDetail alloc ]init];
    NSArray *NMtags = [command.arguments objectAtIndex:0];
    [deviceDetail addTags:NMtags];                                        //deviceDetail addTags - must take array value
    
    [NMPushManager unregisterWithDeviceDetail:deviceDetail completionHandler:^(BOOL deviceDidUnregister, NSError *error){
        
        CDVPluginResult *pluginResult =nil;
        if (!error)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    }];
}

- (void)unregister:(CDVInvokedUrlCommand *)command
{
    [NMPushManager unregisterWithCompletionHandler:^(BOOL deviceDidUnregister, NSError *error){
        CDVPluginResult *pluginResult =nil;

        if (!error)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
        else
        {
           pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    }];
}

- (void)getDeviceDetail: (CDVInvokedUrlCommand *)command  //returns dictionary
{
    
    [NMDeviceDetail getDeviceDetailWithCompletionHandler:^(NMDeviceDetail *deviceDetail, NSError *error) {
        
        CDVPluginResult *pluginResult =nil;
        NSDictionary *NMdeviceDetailDic = [self getDeviceDetailDictionary:deviceDetail];
        
        if (!error)
        {
           pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:NMdeviceDetailDic];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    }];
}

-(NSDictionary*)getDeviceDetailDictionary:(NMDeviceDetail *)deviceDetail
{
    NSMutableDictionary *NMdeviceDetailDic = [[NSMutableDictionary alloc]init];
    NMGeolocation *NMgeo = deviceDetail.location;
   
    [NMdeviceDetailDic setValue:[NSString stringWithFormat:@"%f", NMgeo.latitude] forKey:@"Latitude"];
    [NMdeviceDetailDic setValue:[NSString stringWithFormat:@"%f", NMgeo.longitude] forKey:@"Longitude"];
    [NMdeviceDetailDic setValue:[NSString stringWithFormat:@"%f", NMgeo.timezone] forKey:@"Timezone"];
    [NMdeviceDetailDic setValue:deviceDetail.getTags forKey:@"Tags"];
    [NMdeviceDetailDic addEntriesFromDictionary:deviceDetail.customDictionary];
    
    return NMdeviceDetailDic;
}

- (void)getAllAplicationTags:(CDVInvokedUrlCommand *)command  // returns an array
{
    
    [NMPushManager getDeviceTagsWithCompletionHandler:^( NSArray *tags, NSError *error){
       
        CDVPluginResult *pluginResult =nil;
        if (!error)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:tags];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    }];
}

- (void)setCustomFields: (CDVInvokedUrlCommand *)command
{
    __block CDVPluginResult *pluginResult =nil;

    [NMDeviceDetail getDeviceDetailWithCompletionHandler:^(NMDeviceDetail *deviceDetail, NSError *error) {
        if (!error)
        {
            NSDictionary *NMdic = [command.arguments objectAtIndex:0];
            [deviceDetail.customDictionary addEntriesFromDictionary:NMdic];
            
            [NMPushManager registerWithDeviceDetail:deviceDetail completionHandler:^(BOOL deviceDidRegister, NSError *error) {
                if (!error)
                {
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
                }
                else
                {
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
                }
            }];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)overrideCustomFields:(CDVInvokedUrlCommand *)command
{
    NMDeviceDetail *deviceDetail = [[NMDeviceDetail alloc] init];
    deviceDetail.customDictionary = [[NSMutableDictionary alloc] init];

    NSDictionary *NMdic = [command.arguments objectAtIndex:0];
    deviceDetail.customDictionary = [NMdic mutableCopy];
  
    [NMPushManager registerWithDeviceDetail:deviceDetail completionHandler:^(BOOL deviceDidRegister, NSError *error) {
      
        CDVPluginResult *pluginResult =nil;
        if (!error)
        {
             pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.localizedDescription];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

    }];
}

- (void)sendEvent:(CDVInvokedUrlCommand *)command
{
    NSString *NMkey = [command.arguments objectAtIndex:0];
    NSDictionary *NMvalue = nil;
    [NMEvent sendEventWithKey:NMkey value:NMvalue];
    
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK ];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)sendEventWithData: (CDVInvokedUrlCommand *)command                  // "value" must take dictionary value and "key" takes string
{
    NSString *NMkey = [command.arguments objectAtIndex:0];
    NSDictionary *NMvalue = [command.arguments objectAtIndex:1];
    [NMEvent sendEventWithKey:NMkey value:NMvalue];
    
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK ];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)registerPush:(CDVInvokedUrlCommand *)command
{
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK ];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)updateLocation: (CDVInvokedUrlCommand *)command
{
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK ];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end

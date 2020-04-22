
#import <Cordova/CDVPlugin.h>

@interface CDVWebView : CDVPlugin

- (void)show:(CDVInvokedUrlCommand*)command;

- (void)close:(CDVInvokedUrlCommand*)command;
@end



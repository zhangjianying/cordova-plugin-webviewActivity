#import "CDVWebView.h"
#import <Cordova/CDVPlugin.h>
#import "WebViewViewController.h"

@implementation CDVWebView

- (void)show:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* url = [command.arguments objectAtIndex:0];

     WebViewViewController *webViewController = [[WebViewViewController alloc] init];
     webViewController.wwwFolderName = @"";
    webViewController.startPage = url;

    [self.viewController presentViewController:webViewController animated:YES completion:nil];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)close:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    
    UIViewController *controller = self.viewController;
    while(controller.presentingViewController != nil){
        controller = controller.presentingViewController;
    }
    [controller dismissViewControllerAnimated:YES completion:nil];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


@end

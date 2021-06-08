import Flutter
import UIKit

public class SwiftTscPosFlutterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "tsc_pos_flutter", binaryMessenger: registrar.messenger())
    let instance = SwiftTscPosFlutterPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if (call.method == "getPlatformVersion") {
               result("iOS " + UIDevice.current.systemVersion)
           }
           else if (call.method == "showAlertDialog") {
               DispatchQueue.main.async {
                SwiftTscPosFlutterPlugin tsc = new SwiftTscPosFlutterPlugin
                tsc.openport_ble("")
               }
           }
  }
}

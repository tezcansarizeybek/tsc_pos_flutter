package com.bilnex.tsc_pos_flutter;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import com.example.tscdll.TSCActivity;

import androidx.annotation.NonNull;

import java.io.IOException;

public class TscPosFlutterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "tsc_pos_flutter");
    channel.setMethodCallHandler(this);
  }
  TSCActivity tscActivity = new TSCActivity();
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method){
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "startConnection":
        startConnection(call);
        result.success("1");
        break;
      case "stopConnection":
        stopConnection(call);
        result.success("1");
        break;
      case "sendText":
        sendText(call);
        result.success("1");
        break;
      case "sendByteCommand":
        sendByteCommand(call);
        result.success("1");
        break;
      case "sendCommandText":
        sendCommand(call);
        result.success("1");
        break;
      case "sendFile":
        sendFile(call);
        result.success("1");
        break;
      case "downloadFile":
        downloadFile(call);
        result.success("1");
        break;
      case "clearBuffer":
        clearBuffer();
        result.success("1");
        break;
      case "printPdfByPath":
        pdfByPath(call);
        result.success("1");
        break;
      case "printPdfFile":
        printPdfFile(call);
        result.success("1");
        break;
      case "noBackFeed":
        tscActivity.nobackfeed();
        result.success("1");
        break;
      case "deleteFiles":
        deleteFile(call);
        result.success("1");
        break;
      case "restart":
        restart();
        result.success("1");
      default:
        result.notImplemented();
        result.success("0");
        break;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public void startConnection(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String mac = call.argument("mac");
      tscActivity.openport(mac);
    }}).start();
  }

  public void stopConnection(@NonNull final MethodCall call){
    new Thread(new Runnable(){
      public void run(){
      int timeout = (int)call.argument("timeout");
      tscActivity.closeport(timeout);
    }}).start();
  }

  public void sendText(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String msg = call.argument("msg");
      int x = (int)call.argument("x");
      int y = (int)call.argument("y");
      tscActivity.sendcommand("TEXT "+x+","+y+",\"3\",0,1,1,\""+msg+"\"");
    }}).start();
  }

  public void sendByteCommand(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      byte[] byteArray = (byte[])call.argument("bytes");
      tscActivity.sendcommand(byteArray);
    }}).start();
  }

  public void sendCommand(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String cmd = call.argument("cmd");
      tscActivity.sendcommand(cmd);
    }}).start();
  }

  public void sendCommand(final String command){
    new Thread(new Runnable(){ public void run(){
      tscActivity.sendcommand(command);
    }}).start();
  }

  public void sendFile(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String path = call.argument("path");
      String fileName = call.argument("fileName");
      tscActivity.sendfile(path,fileName);
    }}).start();
  }

  public void downloadFile(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String path = call.argument("path");
      String fileName = call.argument("fileName");
      String saveName = call.argument("saveName");
      tscActivity.downloadfile(path,fileName,fileName);
    }}).start();
  }

  public void deleteFile(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String deletedFile = call.argument("deleteFile");
      tscActivity.deleteFile(deletedFile);
    }}).start();
  }

  public void clearBuffer(){
    new Thread(new Runnable(){ public void run(){
      tscActivity.clearbuffer();
    }}).start();
  }

  public void restart(){
    new Thread(new Runnable(){ public void run(){
      tscActivity.restart();
    }}).start();
  }

  public void pdfByPath(@NonNull final MethodCall call){
    new Thread(new Runnable(){ public void run(){
      String fileName = call.argument("fileName");
      int x = (int)call.argument("x");
      int y = (int)call.argument("y");
      int dpi = (int)call.argument("dpi");
      tscActivity.printPDFbyPath(fileName,x,y,dpi);
    }}).start();
  }

  public void printPdfFile(@NonNull MethodCall call) {
    startConnection(call);
    String xsize = call.argument("xsize");
    String ysize = call.argument("ysize");
    sendCommand("SIZE "+xsize+","+ysize+"\r\n");
    sendCommand("GAP 0,0\r\n");
    clearBuffer();
    sendCommand("CLS\r\n");
    downloadFile(call);
    pdfByPath(call);
    stopConnection(call);
  }
}

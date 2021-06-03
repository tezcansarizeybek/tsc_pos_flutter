package com.bilnex.tsc_pos_flutter;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import com.example.tscdll.TSCActivity;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
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
        clearBuffer(call);
        result.success("1");
        break;
      case "printPdfByPath":
        pdfByPath(call);
        result.success("1");
        break;
      case "pdfByFile":
        try {
          pdfByFile(call);
        } catch (IOException e) {
          e.printStackTrace();
        }
        result.success("1");
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

  public void startConnection(@NonNull MethodCall call){
    String mac = call.argument("mac");
    tscActivity.openport(mac);
  }

  public void stopConnection(@NonNull MethodCall call){
    int timeout = (int)call.argument("timeout");
    tscActivity.closeport(timeout);
  }

  public void sendText(@NonNull MethodCall call){
    String msg = call.argument("msg");
    int x = (int)call.argument("x");
    int y = (int)call.argument("y");
    tscActivity.sendcommand("TEXT "+x+","+y+",\"3\",0,1,1,\""+msg+"\"");
  }

  public void sendByteCommand(@NonNull MethodCall call){
    byte[] byteArray = (byte[])call.argument("bytes");
    tscActivity.sendcommand(byteArray);
  }

  public void sendCommand(@NonNull MethodCall call){
    String cmd = call.argument("cmd");
    tscActivity.sendcommand(cmd);
  }

  public void sendCommand(String command){
    tscActivity.sendcommand(command);
  }

  public void sendFile(@NonNull MethodCall call){
    String path = call.argument("path");
    String fileName = call.argument("fileName");
    tscActivity.sendfile(path,fileName);
  }

  public void downloadFile(@NonNull MethodCall call){
    String path = call.argument("path");
    String fileName = call.argument("fileName");
    tscActivity.downloadfile(path,fileName);
  }

  public void deleteFile(@NonNull MethodCall call){
    String deletedFile = call.argument("deleteFile");
    tscActivity.deleteFile(deletedFile);
  }

  public void clearBuffer(@NonNull MethodCall call){
    tscActivity.clearbuffer();
  }

  public void restart(){
    tscActivity.restart();
  }

  public void pdfByPath(@NonNull MethodCall call){
    String fileName = call.argument("fileName");
    int x = (int)call.argument("x");
    int y = (int)call.argument("y");
    int dpi = (int)call.argument("dpi");
    tscActivity.printPDFbyPath(fileName,x,y,dpi);
  }

  public void pdfByFile(@NonNull MethodCall call) throws IOException {
    String data = call.argument("data");
    String fileName = call.argument("fileName");
    int x = (int)call.argument("x");
    int y = (int)call.argument("y");
    int dpi = (int)call.argument("dpi");

    File file = new File(fileName);
    if (file.createNewFile()) {
      System.out.println("File created: " + file.getName());
    } else {
      System.out.println("File already exists.");
    }
    FileWriter myWriter = new FileWriter(fileName);
    myWriter.write(data);
    myWriter.close();
    tscActivity.printPDFbyFile(file,x,y,dpi);
  }

  public void printPdfFile(@NonNull MethodCall call) {
    startConnection(call);
    String xsize = call.argument("xsize");
    String ysize = call.argument("ysize");
    sendCommand("SIZE "+xsize+","+ysize+"\r\n");
    sendCommand("GAP 0,0\r\n");
    clearBuffer(call);
    sendCommand("CLS\r\n");
    downloadFile(call);
    pdfByPath(call);
    stopConnection(call);
  }
}

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class TscPosFlutter {
  TscPosFlutter();
  static const MethodChannel _channel = const MethodChannel('tsc_pos_flutter');
  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static TscPosFlutter _instance = TscPosFlutter();

  static TscPosFlutter get instance => _instance;

  Future startConnection({@required String? mac}) async {
    return _channel.invokeMethod("startConnection", {"mac": mac.toString()});
  }

  Future stopConnection({int timeout = 5000}) async {
    return _channel.invokeMethod("stopConnection", {"timeout": timeout});
  }

  Future sendText({@required String? msg, int x = 0, int? y = 0}) async {
    return _channel.invokeMethod("sendText", {"msg": msg, "x": x, "y": y});
  }

  Future sendByteCommand({@required List<int>? bytes}) async {
    return _channel.invokeMethod("sendByteCommand", {"bytes": bytes});
  }

  Future sendCommand({@required String? cmd}) async {
    return _channel.invokeMethod('sendCommandText', {"cmd": cmd});
  }

  Future sendFile({@required String? path, @required String? fileName}) async {
    return _channel.invokeMethod("sendFile", {"path": path, "fileName": fileName});
  }

  Future downloadFile({@required String? path, @required String? fileName}) async {
    return _channel.invokeMethod("downloadFile", {"path": path, "fileName": fileName});
  }

  Future clearBuffer() async {
    return _channel.invokeMethod("clearBuffer");
  }

  Future pdfByPath({@required String? fileName, @required String? saveName, int x = 0, int y = 0, int dpi = 80}) async {
    return _channel.invokeMethod("printPdfByPath", {"fileName": fileName, "x": x, "y": y, "dpi": dpi, "saveName": saveName});
  }

  Future noBackFeed() async {
    return _channel.invokeMethod("noBackFeed");
  }

  Future deleteFile({@required String? deleteFile}) {
    return _channel.invokeMethod("deleteFiles", {"deleteFile": deleteFile});
  }

  Future restart() {
    return _channel.invokeMethod("restart");
  }

  Future pdfByFile({@required String? data, @required String? fileName, int x = 0, int y = 0, int dpi = 80}) {
    return _channel.invokeMethod("pdfByFile", {"data": data, "fileName": fileName, "x": x, "y": y, "dpi": dpi});
  }

  Future printPdfFile(
      {@required String? fileName,
      @required String? path,
      @required String? mac,
      String xsize = "4",
      String ysize = "3",
      int timeout = 5000,
      int x = 0,
      int y = 0,
      int dpi = 80}) async {
    return _channel
        .invokeMethod("printPdfFile", {"fileName": fileName, "x": x, "y": y, "dpi": dpi, "path": path, "mac": mac, "timeout": timeout, "xsize": xsize, "ysize": ysize});
  }
}

import 'dart:io';

import 'package:external_path/external_path.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:pdf_render_plus/pdf_render_widgets.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:tsc_pos_flutter/tsc_pos_flutter.dart';

void main()
{
  runApp(
      MaterialApp(
        title: 'TSC POS Yazdır',
        home: const MainPage(),
      )
  );
}

class MainPage extends StatefulWidget
{
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage>
{
  TscPosFlutter tscPosFlutter = TscPosFlutter.instance;

  String pdfPath = "lib/assets/sample.pdf";

  bool loading = false;

  void showMessage(String message) => ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(message)),);

  Future<bool> getPermissions() async
  {
    Map<Permission, PermissionStatus> permissions = await
    [
      Permission.bluetooth,
      Permission.bluetoothScan,
      Permission.bluetoothConnect,
      Permission.bluetoothAdvertise,
      Permission.location,
      Permission.storage,
      // Permission.manageExternalStorage,
      Permission.accessMediaLocation,
      Permission.mediaLibrary,
    ].request();
    List<PermissionStatus> statuses = permissions.values.toList();
    permissions.forEach((permission, status)
    {
      print("$permission $status");
    });
    if (statuses.every((status) => status.isGranted))
    {
      return true;
    }
    return false;
  }

  Future<void> printTsc() async
  {
    setState(() => loading = true);
    try
    {
      if(await getPermissions())
      {
        final ByteData bytes = await rootBundle.load(pdfPath);
        final Uint8List byteArray = bytes.buffer.asUint8List();

        String directory = await ExternalPath.getExternalStoragePublicDirectory(ExternalPath.DIRECTORY_DOWNLOAD);
        final tempPath = '$directory/sample.pdf';

        final file = File(tempPath);
        await file.writeAsBytes(byteArray);

        await tscPosFlutter.printPdfFile(
          fileName: pdfPath.split("/").last,
          path: tempPath,
          mac: "00:0C:BF:35:5E:BB",
        );
      }
      else
      {
        showMessage("İzin verilmedi.");
      }
    }
    catch (e,s)
    {
      showMessage("Hata: $e");
      print(e.toString()+s.toString());
    }
    finally
    {
      setState(() => loading = false);
    }
  }

  @override
  Widget build(BuildContext context)
  {
    return Scaffold(
      appBar: AppBar(
          title: const Text('TSC POS Yazdır')
      ),
      body: SingleChildScrollView(
        child: Column(
          children:
          [
            SizedBox(
              width: double.infinity,
              height: MediaQuery.sizeOf(context).height * 0.8,
              child: PdfViewer.openAsset(
                pdfPath,
                onError: (e) {
                  showMessage("Dosya yüklenemedi: $e");
                  print(e);
                },
              ),
            ),
            loading
                ? CircularProgressIndicator()
                : ElevatedButton(
                onPressed: printTsc,
                child: Text("Yazdır")
            )
        ]),
      )
    );
  }
}
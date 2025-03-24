# TscPosFlutter

TscPosFlutter is a package used for communicating with TSC printers in Flutter applications. This package allows sending commands to the printer via Bluetooth or other connection methods.

## Features
- **Connection Management**: Connect to and disconnect from the printer
- **Send Text**: Print text at specified coordinates
- **Send Commands**: Execute TSC printer-specific commands
- **Send and Download Files**: Send files to or download files from the printer
- **Print PDF**: Print PDF files at specified DPI and coordinates
- **Clear Buffer**: Clear the printer's memory
- **Restart Printer**

## Installation
```yaml
dependencies:
  tsc_pos_flutter: ^latest_version
```

## Usage

```dart
import 'package:tsc_pos_flutter/tsc_pos_flutter.dart';

void main() async {
  TscPosFlutter printer = TscPosFlutter.instance;

  await printer.startConnection(mac: "00:11:22:33:44:55");
  await printer.sendText(msg: "Hello, World!", x: 10, y: 10);
  await printer.stopConnection();
}
```

## Methods

### **Connection Operations**
```dart
Future startConnection({@required String? mac});
Future stopConnection({int timeout = 5000});
```

### **Sending Data to Printer**
```dart
Future sendText({@required String? msg, int x = 0, int? y = 0});
Future sendByteCommand({@required List<int>? bytes});
Future sendCommand({@required String? cmd});
```

### **File Operations**
```dart
Future sendFile({@required String? path, @required String? fileName});
Future downloadFile({@required String? path, @required String? fileName, @required String? saveName});
```

### **PDF Printing**
```dart
Future pdfByPath({@required String? fileName, int x = 0, int y = 0, int dpi = 200});
Future pdfByFile({@required String? data, @required String? fileName, int x = 0, int y = 0, int dpi = 200});
Future printPdfFile({@required String? fileName, @required String? path, @required String? mac, String xsize = "4", String ysize = "3", int timeout = 5000, int x = 0, int y = 0, int dpi = 200});
```

### **Printer Management**
```dart
Future clearBuffer();
Future noBackFeed();
Future deleteFile({@required String? deleteFile});
Future restart();
```

## License
This project is licensed under the MIT License.
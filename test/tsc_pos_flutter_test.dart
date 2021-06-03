import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:tsc_pos_flutter/tsc_pos_flutter.dart';

void main() {
  const MethodChannel channel = MethodChannel('tsc_pos_flutter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await TscPosFlutter.platformVersion, '42');
  });
}

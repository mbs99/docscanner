import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';
import 'package:scanner/log.dart';
import 'package:image/image.dart';

class ImageService {
  static const platform = const MethodChannel('scanner.opencv');

  Future<Image> cvtColor(List<int> image) async {
    Image result;
    try {
      Uint8List buffer;
      buffer = await platform.invokeMethod('cvtColor', image);
      result = decodeImage(buffer);
      
    } on PlatformException catch (e) {
      logError("", e.message);
    }
    return result;
  }

  Future<Image> prepareImage(List<int> image) async {
    Image result;
    try {

      List<int> workingCopy = image;

      workingCopy = await platform.invokeMethod('cvtColor', workingCopy);

      workingCopy = await platform.invokeMethod('GaussianBlur', workingCopy);

      workingCopy = await platform.invokeMethod('Canny', workingCopy);

      List<double> flatPoints = await platform.invokeMethod('findContours', workingCopy);

      result = decodeImage(workingCopy);
      
    } on PlatformException catch (e) {
      logError("", e.message);
    }
    return result;
  }
}
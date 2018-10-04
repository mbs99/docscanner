package com.example.scanner;

import android.os.Bundle;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "scanner.opencv";

    public MainActivity() {
        super();
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {

                        if("cvtColor".equals(call.method)) {

                            final Object argument = call.argument("");
                            if(argument instanceof  byte[]) {
                                MatOfByte source = new MatOfByte((byte[])argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.cvtColor(source, outputImg, Imgproc.COLOR_BGR2GRAY);
                                result.success(outputImg.toArray());
                            }
                            else {
                                result.error("","", argument);
                            }

                        }
                        else if("GaussianBlur".equals(call.method)) {
                            final Object argument = call.argument("");
                            if(argument instanceof  byte[]) {
                                MatOfByte source = new MatOfByte((byte[])argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.blur(source, outputImg, new Size(5,5));
                                result.success(outputImg.toArray());
                            }
                            else {
                                result.error("","", argument);
                            }

                        }
                        else if("Canny".equals(call.method)) {
                            final Object argument = call.argument("");
                            if(argument instanceof  byte[]) {
                                MatOfByte source = new MatOfByte((byte[])argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.Canny(source, outputImg, 75, 200);
                                result.success(outputImg.toArray());
                            }
                            else {
                                result.error("","", argument);
                            }
                        }
                        else if("findContours".equals(call.method)) {
                            final Object argument = call.argument("");
                            if(argument instanceof  byte[]) {
                                MatOfByte source = new MatOfByte((byte[])argument);
                                List<MatOfPoint> points = new ArrayList<>();
                                Imgproc.findContours(source, points, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
                                result.success(points);
                            }
                            else {
                                result.error("","", argument);
                            }
                        }
                        else if("threshold_local".equals(call.method)) {
                            final Object argument = call.argument("img");
                            if(argument instanceof  byte[]) {
                                MatOfByte source = new MatOfByte((byte[])argument);
                                MatOfByte dest = new MatOfByte();
                                Imgproc.adaptiveThreshold(source, dest, 11, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 40);
                                result.success(dest.toArray());
                            }
                            else {
                                result.error("","", argument);
                            }
                        }
                        else {
                            result.notImplemented();
                        }
                    }
                });
    }
}

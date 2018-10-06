package de.mbs.scanner;

import android.os.Bundle;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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

                        if ("cvtColor".equals(call.method)) {

                            final byte[] argument = call.arguments();
                            if (argument != null) {
                                Mat source = fromArray(argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.cvtColor(source, outputImg, Imgproc.COLOR_BGR2GRAY);
                                result.success(toArray(outputImg));
                            } else {
                                result.error("", "", argument);
                            }

                        } else if ("cvtColor2".equals(call.method)) {

                            final String argument = (String) call.arguments();
                            if (argument != null) {
                                try {
                                    Mat source = Imgcodecs.imdecode(new MatOfByte(FileUtils.readFileToByteArray(new File(argument))),
                                            Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
                                    MatOfByte outputImg = new MatOfByte();
                                    Imgproc.cvtColor(source, outputImg, Imgproc.COLOR_BGR2GRAY);
                                    result.success("");
                                } catch (java.io.IOException e) {
                                    result.error("", e.getMessage(), e);
                                }

                            } else {
                                result.error("", "", argument);
                            }

                        } else if ("GaussianBlur".equals(call.method)) {
                            final byte[] argument = call.arguments();
                            if (argument != null) {
                                Mat source = fromArray(argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.blur(source, outputImg, new Size(5, 5));
                                result.success(toArray(outputImg));
                            } else {
                                result.error("", "", argument);
                            }

                        } else if ("Canny".equals(call.method)) {
                            final byte[] argument = call.arguments();
                            if (argument != null) {
                                Mat source = fromArray(argument);
                                MatOfByte outputImg = new MatOfByte();
                                Imgproc.Canny(source, outputImg, 75, 200);
                                result.success(toArray(outputImg));
                            } else {
                                result.error("", "", argument);
                            }
                        } else if ("findContours".equals(call.method)) {
                            final byte[] argument = call.arguments();
                            if (argument != null) {
                                Mat source = fromArray(argument);
                                List<MatOfPoint> points = new ArrayList<>();
                                Imgproc.findContours(source, points, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
                                Point[] contours = points.get(1).toArray();
                                double[] flatPoints = new double[contours.length * 2];
                                int i = 0;
                                for(Point p:contours) {
                                    flatPoints[i++] = p.x;
                                    flatPoints[i++] = p.y;
                                }
                                result.success(flatPoints);


                                SortedSet<Map.Entry<Double, Point>> xxx = new TreeSet<>();
                                for(Point p:contours) {
                                    xxx.add(new AbstractMap.SimpleEntry<>(Imgproc.contourArea(new MatOfPoint(p)),p));
                                }


                            } else {
                                result.error("", "", argument);
                            }
                        } else if ("threshold_local".equals(call.method)) {
                            final Object argument = call.argument("img");
                            if (argument instanceof byte[]) {
                                MatOfByte source = new MatOfByte((byte[]) argument);
                                MatOfByte dest = new MatOfByte();
                                Imgproc.adaptiveThreshold(source, dest, 11, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 40);
                                result.success(dest.toArray());
                            } else {
                                result.error("", "", argument);
                            }
                        } else {
                            result.notImplemented();
                        }
                    }
                });
    }

    protected static Mat fromArray(byte[] raw) {
        return Imgcodecs.imdecode(new MatOfByte(raw),
                Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    protected static byte[] toArray(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpeg", mat, buffer);
        return buffer.toArray();
    }
}

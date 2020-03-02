package frc.robot;

import java.util.ArrayList;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class VisionHelper {
  public static final double FOVAngleWidth = Math.toRadians(58.5) / 2; // degrees
  public static final double TcmWidth = 99.695;// width of vision target in cm
  public static final int FOVpixelWidth = Robot.imgWidth;

  public static final double FOVAngleHeight = Math.toRadians(45.6) / 2;
  public static final double TcmHeight = 43.18; // heigh of vision target in cm
  public static final int FOVpixelHeight = Robot.imgHeight;

  public static final double Tratio = 0.475;// TcmHeight/TcmWidth;
  private static TreeMap<Double, Double> distances = new TreeMap<Double, Double>();

  public VisionHelper() {
    initializeDistanceTable();
  }

  public static int[] findCenter(MatOfPoint contour) {
    // [x,y]
    int[] centerCoor = { -1, -1 };
    Rect boundingRect = Imgproc.boundingRect(contour);
    centerCoor[0] = (int) (boundingRect.x + (boundingRect.width / 2.0));
    centerCoor[1] = (int) (boundingRect.y + (boundingRect.height / 2));
    return centerCoor;
  }

  public static MatOfPoint getLargestContour(Mat mat) {
    Robot.pipeline.process(mat);
    ArrayList<MatOfPoint> contours = Robot.pipeline.filterContoursOutput();
    if(contours.size()>0)
      {int index = 0;
      int largestArea = 0;
      for (int i = 0; i < contours.size(); i++) {
        Rect boundingRect = Imgproc.boundingRect(contours.get(i));
        if (boundingRect.width * boundingRect.height > largestArea) {
          index = i;
          largestArea = boundingRect.width * boundingRect.height;
        }
      }
      return contours.get(index);
    }
    return null;
  }

  public static double visionDistanceWidth(MatOfPoint contour) {
    double TPixelsWidth = Imgproc.boundingRect(contour).width;
    double TPixelsHeight = Imgproc.boundingRect(contour).height;
    double TPixelRatio = 1.0 * TPixelsHeight / TPixelsWidth;
    // System.out.println("/n"+TPixelRatio+" "+Tratio);
    double factor = 0.375 * (TPixelRatio / Tratio - 1) + 1;
    TPixelsWidth = TPixelsWidth * factor;
    double distX = (TcmWidth * FOVpixelWidth) / (2 * TPixelsWidth * Math.tan(FOVAngleWidth));
    return (1 * distX) - (distX * - 0.2367765961928018 + 79.99503478331746);
  }

  public static double visionDistanceHeight(MatOfPoint contour) {
    double TPixelsHeight = Imgproc.boundingRect(contour).height;
    double distY = (TcmHeight * FOVpixelHeight) / (2 * TPixelsHeight * Math.tan(FOVAngleHeight));
    return (1 * distY) - (distY * -0.42008428032422024 + 83.9420165461663);
  }

  public static double averageVisionDistance(MatOfPoint contour) {
    double measuredDistance = (visionDistanceHeight(contour) + visionDistanceWidth(contour)) / 2.0;
    return  measuredDistance - (-0.11626697954008355 * measuredDistance + 27.101836829039314);
  }

  public static void initializeDistanceTable() {
    distances.put(0.664, 13.4300048828125);
    distances.put(0.665, 13.310000152587891);
    distances.put(0.666, 13.140000305175782);
    distances.put(0.667, 12.980000305175782);
    distances.put(0.668, 12.820001220703126);
    distances.put(0.669, 12.680000610351563);
    distances.put(0.67, 12.53000244140625);
    distances.put(0.671, 12.400001220703125);
    distances.put(0.672, 12.260004882812499);
    distances.put(0.673, 12.14000244140625);
    distances.put(0.674, 12.010009765625);
    distances.put(0.675, 11.9000048828125);
    distances.put(0.676, 11.780009765625);
    distances.put(0.677, 11.670009765625);
    distances.put(0.678, 11.560019531250001);
    distances.put(0.679, 11.460019531250001);
    distances.put(0.68, 11.36001953125);
    distances.put(0.681, 11.260039062499999);
    distances.put(0.682, 11.170039062499999);
    distances.put(0.683, 11.0800390625);
    distances.put(0.684, 10.990078125);
    distances.put(0.685, 10.910039062500001);
    distances.put(0.686, 10.820078125);
    distances.put(0.687, 10.740078125);
    distances.put(0.688, 10.66015625);
    distances.put(0.689, 10.590078125);
    distances.put(0.69, 10.51015625);
    distances.put(0.691, 10.44015625);
    distances.put(0.692, 10.37015625);
    distances.put(0.693, 10.30015625);
    distances.put(0.694, 10.2303125);
    distances.put(0.695, 10.1703125);
    distances.put(0.696, 10.11015625);
    distances.put(0.697, 10.040312499999999);
    distances.put(0.698, 9.980625);
    distances.put(0.699, 9.9303125);
    distances.put(0.7, 9.8703125);
    distances.put(0.701, 9.810625);
    distances.put(0.702, 9.7603125);
    distances.put(0.703, 9.700625);
    distances.put(0.704, 9.650625);
    distances.put(0.705, 9.600624999999999);
    distances.put(0.706, 9.550625);
    distances.put(0.707, 9.500625);
    distances.put(0.708, 9.45125);
    distances.put(0.709, 9.410625);
    distances.put(0.71, 9.360624999999999);
    distances.put(0.711, 9.311250000000001);
    distances.put(0.712, 9.271249999999998);
    distances.put(0.713, 9.230625);
    distances.put(0.714, 9.181249999999999);
    distances.put(0.715, 9.14125);
    distances.put(0.716, 9.10125);
    distances.put(0.717, 9.061250000000001);
    distances.put(0.718, 9.021249999999998);
    distances.put(0.719, 8.982500000000002);
    distances.put(0.72, 8.95125);
    distances.put(0.721, 8.911249999999999);
    distances.put(0.722, 8.872499999999999);
    distances.put(0.723, 8.841249999999999);
    distances.put(0.724, 8.8025);
    distances.put(0.725, 8.771249999999998);
    distances.put(0.726, 8.732500000000002);
    distances.put(0.727, 8.7025);
    distances.put(0.728, 8.67125);
    distances.put(0.729, 8.6325);
    distances.put(0.73, 8.6025);
    distances.put(0.731, 8.572500000000002);
    distances.put(0.732, 8.5425);
    distances.put(0.733, 8.5125);
    distances.put(0.734, 8.482500000000002);
    distances.put(0.735, 8.4525);
    distances.put(0.736, 8.4225);
    distances.put(0.737, 8.395);
    distances.put(0.738, 8.372499999999999);
    distances.put(0.739, 8.342500000000001);
    distances.put(0.74, 8.315000000000001);
    distances.put(0.741, 8.2925);
    distances.put(0.742, 8.2625);
    distances.put(0.743, 8.235);
    distances.put(0.744, 8.2125);
    distances.put(0.745, 8.184999999999999);
    distances.put(0.746, 8.165);
    distances.put(0.747, 8.142500000000002);
    distances.put(0.748, 8.114999999999998);
    distances.put(0.749, 8.094999999999999);
    distances.put(0.75, 8.072500000000002);
    distances.put(0.751, 8.045);
    distances.put(0.752, 8.024999999999999);
    distances.put(0.753, 8.004999999999999);
    distances.put(0.754, 7.9825);
    distances.put(0.755, 7.955);
    distances.put(0.756, 7.9350000000000005);
    distances.put(0.757, 7.915);
    distances.put(0.758, 7.895);
    distances.put(0.759, 7.875);
    distances.put(0.76, 7.855);
    distances.put(0.761, 7.835);
    distances.put(0.762, 7.8149999999999995);
    distances.put(0.763, 7.795);
    distances.put(0.764, 7.78);
    distances.put(0.765, 7.765);
    distances.put(0.766, 7.745);
    distances.put(0.767, 7.725);
    distances.put(0.768, 7.705);
    distances.put(0.769, 7.69);
    distances.put(0.77, 7.675);
    distances.put(0.771, 7.655);
    distances.put(0.772, 7.635);
    distances.put(0.773, 7.62);
    distances.put(0.774, 7.605);
    distances.put(0.775, 7.585);
    distances.put(0.776, 7.57);
    distances.put(0.777, 7.555);
    distances.put(0.778, 7.54);
    distances.put(0.779, 7.525);
    distances.put(0.78, 7.51);
    distances.put(0.781, 7.495);
    distances.put(0.782, 7.48);
    distances.put(0.783, 7.465);
    distances.put(0.784, 7.45);
    distances.put(0.785, 7.4350000000000005);
    distances.put(0.786, 7.42);
    distances.put(0.787, 7.405);
    distances.put(0.788, 7.39);
    distances.put(0.789, 7.38);
    distances.put(0.79, 7.365);
    distances.put(0.791, 7.35);
    distances.put(0.792, 7.34);
    distances.put(0.793, 7.325);
    distances.put(0.794, 7.31);
    distances.put(0.795, 7.3);
    distances.put(0.796, 7.285);
    distances.put(0.797, 7.27);
    distances.put(0.798, 7.26);
    distances.put(0.799, 7.25);
    distances.put(0.8, 7.235);
    distances.put(0.801, 7.22);
    distances.put(0.802, 7.21);
    distances.put(0.803, 7.2);
    distances.put(0.804, 7.19);
    distances.put(0.805, 7.18);
    distances.put(0.806, 7.165);
    distances.put(0.807, 7.15);
    distances.put(0.808, 7.14);
    distances.put(0.809, 7.13);
    distances.put(0.81, 7.12);
    distances.put(0.811, 7.11);
    distances.put(0.812, 7.1);
    distances.put(0.813, 7.09);
    distances.put(0.814, 7.08);
    distances.put(0.815, 7.07);
    distances.put(0.816, 7.06);
    distances.put(0.817, 7.05);
    distances.put(0.818, 7.04);
    distances.put(0.819, 7.03);
    distances.put(0.82, 7.02);
    distances.put(0.821, 7.01);
    distances.put(0.822, 7.0);
    distances.put(0.823, 6.99);
    distances.put(0.824, 6.98);
    distances.put(0.825, 6.97);
    distances.put(0.826, 6.96);
    distances.put(0.827, 6.95);
    distances.put(0.828, 6.94);
    distances.put(0.829, 6.93);
    distances.put(0.83, 6.92);
    distances.put(0.832, 6.91);
    distances.put(0.833, 6.9);
    distances.put(0.834, 6.89);
    distances.put(0.835, 6.88);
    distances.put(0.836, 6.87);
    distances.put(0.837, 6.86);
    distances.put(0.839, 6.85);
    distances.put(0.84, 6.84);
    distances.put(0.841, 6.83);
    distances.put(0.842, 6.82);
    distances.put(0.844, 6.81);
    distances.put(0.845, 6.8);
    distances.put(0.846, 6.79);
    distances.put(0.848, 6.78);
    distances.put(0.849, 6.77);
    distances.put(0.85, 6.76);
    distances.put(0.852, 6.75);
    distances.put(0.853, 6.74);
    distances.put(0.854, 6.73);
    distances.put(0.856, 6.72);
    distances.put(0.857, 6.71);
    distances.put(0.859, 6.7);
    distances.put(0.86, 6.69);
    distances.put(0.862, 6.68);
    distances.put(0.863, 6.67);
    distances.put(0.865, 6.66);
    distances.put(0.867, 6.65);
    distances.put(0.868, 6.64);
    distances.put(0.87, 6.63);
    distances.put(0.871, 6.62);
    distances.put(0.873, 6.61);
    distances.put(0.875, 6.6);
    distances.put(0.876, 6.59);
    distances.put(0.878, 6.58);
    distances.put(0.88, 6.57);
    distances.put(0.882, 6.56);
    distances.put(0.884, 6.55);
    distances.put(0.885, 6.54);
    distances.put(0.887, 6.53);
    distances.put(0.889, 6.52);
    distances.put(0.891, 6.51);
    distances.put(0.893, 6.5);
    distances.put(0.895, 6.49);
    distances.put(0.897, 6.48);
    distances.put(0.899, 6.47);
    distances.put(0.901, 6.46);
    distances.put(0.904, 6.45);
    distances.put(0.906, 6.44);
    distances.put(0.908, 6.43);
    distances.put(0.91, 6.42);
    distances.put(0.913, 6.41);
    distances.put(0.915, 6.4);
    distances.put(0.918, 6.39);
    distances.put(0.92, 6.38);
    distances.put(0.923, 6.37);
    distances.put(0.925, 6.36);
    distances.put(0.928, 6.35);
    distances.put(0.93, 6.34);
    distances.put(0.933, 6.33);
    distances.put(0.936, 6.32);
    distances.put(0.939, 6.31);
    distances.put(0.942, 6.3);
    distances.put(0.945, 6.29);
    distances.put(0.948, 6.28);
    distances.put(0.951, 6.27);
    distances.put(0.954, 6.26);
    distances.put(0.958, 6.25);
    distances.put(0.961, 6.24);
    distances.put(0.965, 6.23);
    distances.put(0.968, 6.22);
    distances.put(0.972, 6.21);
    distances.put(0.976, 6.2);
    distances.put(0.98, 6.19);
    distances.put(0.984, 6.18);
    distances.put(0.988, 6.17);
    distances.put(0.993, 6.16);
    distances.put(0.997, 6.15);
    distances.put(1.002, 6.14);
    distances.put(1.007, 6.13);
    distances.put(1.012, 6.12);
    distances.put(1.018, 6.11);
    distances.put(1.023, 6.1);
    distances.put(1.029, 6.09);
    distances.put(1.035, 6.08);
    distances.put(1.042, 6.07);
    distances.put(1.049, 6.06);
    distances.put(1.056, 6.05);
    distances.put(1.064, 6.04);
    distances.put(1.073, 6.03);
    distances.put(1.082, 6.02);
    distances.put(1.092, 6.01);
    distances.put(1.103, 6.0);
    distances.put(1.115, 5.99);
    distances.put(1.13, 5.98);
    distances.put(1.147, 5.97);
    distances.put(1.17, 5.96);
    distances.put(1.206, 5.95);
    distances.put(1.241, 5.93);
    distances.put(1.245, 5.94);
    distances.put(1.293, 5.95);
    distances.put(1.337, 5.96);
    distances.put(1.368, 5.97);
    distances.put(1.393, 5.98);
    distances.put(1.416, 5.99);
    distances.put(1.437, 6.0);
    distances.put(1.456, 6.01);
    distances.put(1.475, 6.02);
    distances.put(1.492, 6.03);
    distances.put(1.509, 6.04);
    distances.put(1.525, 6.05);
    distances.put(1.541, 6.06);
    distances.put(1.556, 6.07);
    distances.put(1.571, 6.08);
    distances.put(1.586, 6.09);
    distances.put(1.6, 6.1);
    distances.put(1.614, 6.11);
    distances.put(1.628, 6.12);
    distances.put(1.641, 6.13);
    distances.put(1.655, 6.14);
    distances.put(1.668, 6.15);
    distances.put(1.681, 6.16);
    distances.put(1.694, 6.17);
    distances.put(1.706, 6.18);
    distances.put(1.719, 6.19);
    distances.put(1.731, 6.2);
    distances.put(1.744, 6.21);
    distances.put(1.756, 6.22);
    distances.put(1.768, 6.23);
    distances.put(1.78, 6.24);
    distances.put(1.792, 6.25);
    distances.put(1.804, 6.26);
    distances.put(1.816, 6.27);
    distances.put(1.828, 6.28);
    distances.put(1.84, 6.29);
    distances.put(1.851, 6.3);
    distances.put(1.863, 6.31);
    distances.put(1.875, 6.32);
    distances.put(1.886, 6.33);
    distances.put(1.897, 6.34);
    distances.put(1.909, 6.35);
    distances.put(1.92, 6.36);
    distances.put(1.931, 6.37);
    distances.put(1.943, 6.38);
    distances.put(1.954, 6.39);
    distances.put(1.965, 6.4);
    distances.put(1.976, 6.41);
    distances.put(1.987, 6.42);
    distances.put(1.998, 6.43);
    distances.put(2.01, 6.44);
    distances.put(2.021, 6.45);
    distances.put(2.032, 6.46);
    distances.put(2.043, 6.47);
    distances.put(2.054, 6.48);
    distances.put(2.064, 6.49);
    distances.put(2.075, 6.5);
    distances.put(2.086, 6.51);
    distances.put(2.097, 6.52);
    distances.put(2.108, 6.53);
    distances.put(2.119, 6.54);
    distances.put(2.13, 6.55);
    distances.put(2.14, 6.56);
    distances.put(2.151, 6.57);
    distances.put(2.162, 6.58);
    distances.put(2.173, 6.59);
    distances.put(2.183, 6.6);
    distances.put(2.194, 6.61);
    distances.put(2.205, 6.62);
    distances.put(2.215, 6.63);
    distances.put(2.226, 6.64);
    distances.put(2.237, 6.65);
    distances.put(2.247, 6.66);
    distances.put(2.258, 6.67);
    distances.put(2.269, 6.68);
    distances.put(2.279, 6.69);
    distances.put(2.29, 6.7);
    distances.put(2.301, 6.71);
    distances.put(2.311, 6.72);
    distances.put(2.322, 6.73);
    distances.put(2.332, 6.74);
    distances.put(2.343, 6.75);
    distances.put(2.354, 6.76);
    distances.put(2.364, 6.77);
    distances.put(2.375, 6.78);
    distances.put(2.385, 6.79);
    distances.put(2.396, 6.8);
    distances.put(2.407, 6.81);
    distances.put(2.417, 6.82);
    distances.put(2.428, 6.83);
    distances.put(2.438, 6.84);
    distances.put(2.449, 6.85);
    distances.put(2.459, 6.86);
    distances.put(2.47, 6.87);
    distances.put(2.48, 6.88);
    distances.put(2.491, 6.89);
    distances.put(2.502, 6.9);
    distances.put(2.512, 6.91);
    distances.put(2.523, 6.92);
    distances.put(2.533, 6.93);
    distances.put(2.544, 6.94);
    distances.put(2.554, 6.95);
    distances.put(2.565, 6.96);
    distances.put(2.575, 6.97);
    distances.put(2.586, 6.98);
    distances.put(2.597, 6.99);
    distances.put(2.607, 7.0);
    distances.put(2.618, 7.01);
    distances.put(2.628, 7.02);
    distances.put(2.639, 7.03);
    distances.put(2.649, 7.04);
    distances.put(2.66, 7.05);
    distances.put(2.671, 7.06);
    distances.put(2.681, 7.07);
    distances.put(2.692, 7.08);
    distances.put(2.702, 7.09);
    distances.put(2.713, 7.1);
    distances.put(2.723, 7.11);
    distances.put(2.734, 7.12);
    distances.put(2.745, 7.13);
    distances.put(2.755, 7.14);
    distances.put(2.766, 7.15);
    distances.put(2.776, 7.16);
    distances.put(2.787, 7.17);
    distances.put(2.798, 7.18);
    distances.put(2.808, 7.19);
    distances.put(2.819, 7.2);
    distances.put(2.83, 7.21);
    distances.put(2.84, 7.22);
    distances.put(2.851, 7.23);
    distances.put(2.861, 7.24);
    distances.put(2.872, 7.25);
    distances.put(2.883, 7.26);
    distances.put(2.893, 7.27);
    distances.put(2.904, 7.28);
    distances.put(2.915, 7.29);
    distances.put(2.925, 7.3);
    distances.put(2.936, 7.31);
    distances.put(2.947, 7.32);
    distances.put(2.957, 7.33);
    distances.put(2.968, 7.34);
    distances.put(2.979, 7.35);
    distances.put(2.99, 7.36);
    distances.put(3.0, 7.37);
    distances.put(3.011, 7.38);
    distances.put(3.022, 7.39);
    distances.put(3.032, 7.4);
    distances.put(3.043, 7.41);
    distances.put(3.054, 7.42);
    distances.put(3.065, 7.43);
    distances.put(3.075, 7.44);
    distances.put(3.086, 7.45);
    distances.put(3.097, 7.46);
    distances.put(3.108, 7.47);
    distances.put(3.119, 7.48);
    distances.put(3.129, 7.49);
    distances.put(3.14, 7.5);
    distances.put(3.151, 7.51);
    distances.put(3.162, 7.52);
    distances.put(3.173, 7.53);
    distances.put(3.183, 7.54);
    distances.put(3.194, 7.55);
    distances.put(3.205, 7.56);
    distances.put(3.216, 7.57);
    distances.put(3.227, 7.58);
    distances.put(3.238, 7.59);
    distances.put(3.249, 7.6);
    distances.put(3.26, 7.61);
    distances.put(3.27, 7.62);
    distances.put(3.281, 7.63);
    distances.put(3.292, 7.64);
    distances.put(3.303, 7.65);
    distances.put(3.314, 7.66);
    distances.put(3.325, 7.67);
    distances.put(3.336, 7.68);
    distances.put(3.347, 7.69);
    distances.put(3.358, 7.7);
    distances.put(3.369, 7.71);
    distances.put(3.38, 7.72);
    distances.put(3.391, 7.73);
    distances.put(3.402, 7.74);
    distances.put(3.413, 7.75);
    distances.put(3.424, 7.76);
    distances.put(3.435, 7.77);
    distances.put(3.446, 7.78);
    distances.put(3.457, 7.79);
    distances.put(3.468, 7.8);
    distances.put(3.479, 7.81);
    distances.put(3.49, 7.82);
    distances.put(3.501, 7.83);
    distances.put(3.512, 7.84);
    distances.put(3.523, 7.85);
    distances.put(3.534, 7.86);
    distances.put(3.545, 7.87);
    distances.put(3.557, 7.88);
    distances.put(3.568, 7.89);
    distances.put(3.579, 7.9);
    distances.put(3.59, 7.91);
    distances.put(3.601, 7.92);
    distances.put(3.612, 7.93);
    distances.put(3.623, 7.94);
    distances.put(3.635, 7.95);
    distances.put(3.646, 7.96);
    distances.put(3.657, 7.97);
    distances.put(3.668, 7.98);
    distances.put(3.679, 7.99);
    distances.put(3.691, 8.0);
    distances.put(3.702, 8.01);
    distances.put(3.713, 8.02);
    distances.put(3.724, 8.03);
    distances.put(3.736, 8.04);
    distances.put(3.747, 8.05);
    distances.put(3.758, 8.06);
    distances.put(3.769, 8.07);
    distances.put(3.781, 8.08);
    distances.put(3.792, 8.09);
    distances.put(3.803, 8.1);
    distances.put(3.815, 8.11);
    distances.put(3.826, 8.12);
    distances.put(3.837, 8.13);
    distances.put(3.849, 8.14);
    distances.put(3.86, 8.15);
    distances.put(3.871, 8.16);
    distances.put(3.883, 8.17);
    distances.put(3.894, 8.18);
    distances.put(3.906, 8.19);
    distances.put(3.917, 8.2);
    distances.put(3.928, 8.21);
    distances.put(3.94, 8.22);
    distances.put(3.951, 8.23);
    distances.put(3.963, 8.24);
    distances.put(3.974, 8.25);
    distances.put(3.986, 8.26);
    distances.put(3.997, 8.27);
    distances.put(4.009, 8.28);
    distances.put(4.02, 8.29);
    distances.put(4.032, 8.3);
    distances.put(4.043, 8.31);
    distances.put(4.055, 8.32);
    distances.put(4.066, 8.33);
    distances.put(4.078, 8.34);
    distances.put(4.089, 8.35);
    distances.put(4.101, 8.36);
    distances.put(4.113, 8.37);
    distances.put(4.124, 8.38);
    distances.put(4.136, 8.39);
    distances.put(4.147, 8.4);
    distances.put(4.159, 8.41);
    distances.put(4.171, 8.42);
    distances.put(4.182, 8.43);
    distances.put(4.194, 8.44);
    distances.put(4.206, 8.45);
    distances.put(4.217, 8.46);
    distances.put(4.229, 8.47);
    distances.put(4.241, 8.48);
    distances.put(4.252, 8.49);
    distances.put(4.264, 8.5);
    distances.put(4.276, 8.51);
    distances.put(4.288, 8.52);
    distances.put(4.299, 8.53);
    distances.put(4.311, 8.54);
    distances.put(4.323, 8.55);
    distances.put(4.335, 8.56);
    distances.put(4.346, 8.57);
    distances.put(4.358, 8.58);
    distances.put(4.37, 8.59);
    distances.put(4.382, 8.6);
    distances.put(4.394, 8.61);
    distances.put(4.406, 8.62);
    distances.put(4.417, 8.63);
    distances.put(4.429, 8.64);
    distances.put(4.441, 8.65);
    distances.put(4.453, 8.66);
    distances.put(4.465, 8.67);
    distances.put(4.477, 8.68);
    distances.put(4.489, 8.69);
    distances.put(4.501, 8.7);
    distances.put(4.513, 8.71);
    distances.put(4.524, 8.72);
    distances.put(4.536, 8.73);
    distances.put(4.548, 8.74);
    distances.put(4.56, 8.75);
    distances.put(4.572, 8.76);
    distances.put(4.584, 8.77);
    distances.put(4.596, 8.78);
    distances.put(4.608, 8.79);
    distances.put(4.62, 8.8);
    distances.put(4.632, 8.81);
    distances.put(4.645, 8.82);
    distances.put(4.657, 8.83);
    distances.put(4.669, 8.84);
    distances.put(4.681, 8.85);
    distances.put(4.693, 8.86);
    distances.put(4.705, 8.87);
    distances.put(4.717, 8.88);
    distances.put(4.729, 8.89);
    distances.put(4.741, 8.9);
    distances.put(4.753, 8.91);
    distances.put(4.766, 8.92);
    distances.put(4.778, 8.93);
    distances.put(4.79, 8.94);
    distances.put(4.802, 8.95);
    distances.put(4.814, 8.96);
    distances.put(4.827, 8.97);
    distances.put(4.839, 8.98);
    distances.put(4.851, 8.99);
    distances.put(4.863, 9.0);
    distances.put(4.876, 9.01);
    distances.put(4.888, 9.02);
    distances.put(4.9, 9.03);
    distances.put(4.912, 9.04);
    distances.put(4.925, 9.05);
    distances.put(4.937, 9.06);
    distances.put(4.949, 9.07);
    distances.put(4.962, 9.08);
    distances.put(4.974, 9.09);
    distances.put(4.986, 9.1);
    distances.put(4.999, 9.11);
    distances.put(5.011, 9.12);
    distances.put(5.023, 9.13);
    distances.put(5.036, 9.14);
    distances.put(5.048, 9.15);
    distances.put(5.061, 9.16);
    distances.put(5.073, 9.17);
    distances.put(5.085, 9.18);
    distances.put(5.098, 9.19);
    distances.put(5.11, 9.2);
    distances.put(5.123, 9.21);
    distances.put(5.135, 9.22);
    distances.put(5.148, 9.23);
    distances.put(5.16, 9.24);
    distances.put(5.173, 9.25);
    distances.put(5.185, 9.26);
    distances.put(5.198, 9.27);
    distances.put(5.21, 9.28);
    distances.put(5.223, 9.29);
    distances.put(5.236, 9.3);
    distances.put(5.248, 9.31);
    distances.put(5.261, 9.32);
    distances.put(5.273, 9.33);
    distances.put(5.286, 9.34);
    distances.put(5.299, 9.35);
    distances.put(5.311, 9.36);
    distances.put(5.324, 9.37);
    distances.put(5.337, 9.38);
    distances.put(5.349, 9.39);
    distances.put(5.362, 9.4);
    distances.put(5.375, 9.41);
    distances.put(5.387, 9.42);
    distances.put(5.4, 9.43);
    distances.put(5.413, 9.44);
    distances.put(5.425, 9.45);
    distances.put(5.438, 9.46);
    distances.put(5.451, 9.47);
    distances.put(5.464, 9.48);
    distances.put(5.477, 9.49);
    distances.put(5.489, 9.5);
    distances.put(5.502, 9.51);
    distances.put(5.515, 9.52);
    distances.put(5.528, 9.53);
    distances.put(5.541, 9.54);
    distances.put(5.553, 9.55);
    distances.put(5.566, 9.56);
    distances.put(5.579, 9.57);
    distances.put(5.592, 9.58);
    distances.put(5.605, 9.59);
    distances.put(5.618, 9.6);
    distances.put(5.631, 9.61);
    distances.put(5.644, 9.62);
    distances.put(5.657, 9.63);
    distances.put(5.669, 9.64);
    distances.put(5.682, 9.65);
    distances.put(5.695, 9.66);
    distances.put(5.708, 9.67);
    distances.put(5.721, 9.68);
    distances.put(5.734, 9.69);
    distances.put(5.747, 9.7);
    distances.put(5.76, 9.71);
    distances.put(5.773, 9.72);
    distances.put(5.786, 9.73);
    distances.put(5.8, 9.74);
    distances.put(5.813, 9.75);
    distances.put(5.826, 9.76);
    distances.put(5.839, 9.77);
    distances.put(5.852, 9.78);
    distances.put(5.865, 9.79);
    distances.put(5.878, 9.8);
    distances.put(5.891, 9.81);
    distances.put(5.904, 9.82);
    distances.put(5.918, 9.83);
    distances.put(5.931, 9.84);
    distances.put(5.944, 9.85);
    distances.put(5.957, 9.86);
    distances.put(5.97, 9.87);
    distances.put(5.983, 9.88);
    distances.put(5.997, 9.89);
    distances.put(6.01, 9.9);
    distances.put(6.023, 9.91);
    distances.put(6.036, 9.92);
    distances.put(6.05, 9.93);
    distances.put(6.063, 9.94);
    distances.put(6.076, 9.95);
    distances.put(6.09, 9.96);
    distances.put(6.103, 9.97);
    distances.put(6.116, 9.98);
    distances.put(6.13, 9.99);
    distances.put(6.143, 10.0);
    distances.put(6.156, 10.01);
    distances.put(6.17, 10.02);
    distances.put(6.183, 10.03);
    distances.put(6.196, 10.04);
    distances.put(6.21, 10.05);
    distances.put(6.223, 10.06);
    distances.put(6.237, 10.07);
    distances.put(6.25, 10.08);
    distances.put(6.263, 10.09);
    distances.put(6.277, 10.1);
    distances.put(6.29, 10.11);
    distances.put(6.304, 10.12);
    distances.put(6.317, 10.13);
    distances.put(6.331, 10.14);
    distances.put(6.344, 10.15);
    distances.put(6.358, 10.16);
    distances.put(6.371, 10.17);
    distances.put(6.385, 10.18);
    distances.put(6.399, 10.19);
    distances.put(6.412, 10.2);
    distances.put(6.426, 10.21);
    distances.put(6.439, 10.22);
    distances.put(6.453, 10.23);
    distances.put(6.466, 10.24);
    distances.put(6.48, 10.25);
    distances.put(6.494, 10.26);
    distances.put(6.507, 10.27);
    distances.put(6.521, 10.28);
    distances.put(6.535, 10.29);
    distances.put(6.548, 10.3);
    distances.put(6.562, 10.31);
    distances.put(6.576, 10.32);
    distances.put(6.589, 10.33);
    distances.put(6.603, 10.34);
    distances.put(6.617, 10.35);
    distances.put(6.631, 10.36);
    distances.put(6.644, 10.37);
    distances.put(6.658, 10.38);
    distances.put(6.672, 10.39);
    distances.put(6.686, 10.4);
    distances.put(6.7, 10.41);
    distances.put(6.713, 10.42);
    distances.put(6.727, 10.43);
    distances.put(6.741, 10.44);
    distances.put(6.755, 10.45);
    distances.put(6.769, 10.46);
    distances.put(6.783, 10.47);
    distances.put(6.796, 10.48);
    distances.put(6.81, 10.49);
    distances.put(6.824, 10.5);
    distances.put(6.838, 10.51);
    distances.put(6.852, 10.52);
    distances.put(6.866, 10.53);
    distances.put(6.88, 10.54);
    distances.put(6.894, 10.55);
    distances.put(6.908, 10.56);
    distances.put(6.922, 10.57);
    distances.put(6.936, 10.58);
    distances.put(6.95, 10.59);
    distances.put(6.964, 10.6);
    distances.put(6.978, 10.61);
    distances.put(6.992, 10.62);
    distances.put(7.006, 10.63);
    distances.put(7.02, 10.64);
    distances.put(7.034, 10.65);
    distances.put(7.048, 10.66);
    distances.put(7.062, 10.67);
    distances.put(7.076, 10.68);
    distances.put(7.091, 10.69);
    distances.put(7.105, 10.7);
    distances.put(7.119, 10.71);
    distances.put(7.133, 10.72);
    distances.put(7.147, 10.73);
    distances.put(7.161, 10.74);
    distances.put(7.175, 10.75);
    distances.put(7.19, 10.76);
    distances.put(7.204, 10.77);
    distances.put(7.218, 10.78);
    distances.put(7.232, 10.79);
    distances.put(7.247, 10.8);
    distances.put(7.261, 10.81);
    distances.put(7.275, 10.82);
    distances.put(7.289, 10.83);
    distances.put(7.304, 10.84);
    distances.put(7.318, 10.85);
    distances.put(7.332, 10.86);
    distances.put(7.347, 10.87);
    distances.put(7.361, 10.88);
    distances.put(7.375, 10.89);
    distances.put(7.39, 10.9);
    distances.put(7.404, 10.91);
    distances.put(7.418, 10.92);
    distances.put(7.433, 10.93);
    distances.put(7.447, 10.94);
    distances.put(7.461, 10.95);
    distances.put(7.476, 10.96);
    distances.put(7.49, 10.97);
    distances.put(7.505, 10.98);
    distances.put(7.519, 10.99);
    distances.put(7.534, 11.0);
    distances.put(7.548, 11.01);
    distances.put(7.563, 11.02);
    distances.put(7.577, 11.03);
    distances.put(7.592, 11.04);
    distances.put(7.606, 11.05);
    distances.put(7.621, 11.06);
    distances.put(7.635, 11.07);
    distances.put(7.65, 11.08);
    distances.put(7.664, 11.09);
    distances.put(7.679, 11.1);
    distances.put(7.694, 11.11);
    distances.put(7.708, 11.12);
    distances.put(7.723, 11.13);
    distances.put(7.737, 11.14);
    distances.put(7.752, 11.15);
    distances.put(7.767, 11.16);
    distances.put(7.781, 11.17);
    distances.put(7.796, 11.18);
    distances.put(7.811, 11.19);
    distances.put(7.825, 11.2);
    distances.put(7.84, 11.21);
    distances.put(7.855, 11.22);
    distances.put(7.869, 11.23);
    distances.put(7.884, 11.24);
    distances.put(7.899, 11.25);
    distances.put(7.914, 11.26);
    distances.put(7.928, 11.27);
    distances.put(7.943, 11.28);
    distances.put(7.958, 11.29);
    distances.put(7.973, 11.3);
    distances.put(7.988, 11.31);
    distances.put(8.002, 11.32);
    distances.put(8.017, 11.33);
    distances.put(8.032, 11.34);
    distances.put(8.047, 11.35);
    distances.put(8.062, 11.36);
    distances.put(8.077, 11.37);
    distances.put(8.092, 11.38);
    distances.put(8.106, 11.39);
    distances.put(8.121, 11.4);
    distances.put(8.136, 11.41);
    distances.put(8.151, 11.42);
    distances.put(8.166, 11.43);
    distances.put(8.181, 11.44);
    distances.put(8.196, 11.45);
    distances.put(8.211, 11.46);
    distances.put(8.226, 11.47);
    distances.put(8.241, 11.48);
    distances.put(8.256, 11.49);
    distances.put(8.271, 11.5);
    distances.put(8.286, 11.51);
    distances.put(8.301, 11.52);
    distances.put(8.316, 11.53);
    distances.put(8.331, 11.54);
    distances.put(8.346, 11.55);
    distances.put(8.361, 11.56);
    distances.put(8.376, 11.57);
    distances.put(8.392, 11.58);
    distances.put(8.407, 11.59);
    distances.put(8.422, 11.6);
    distances.put(8.437, 11.61);
    distances.put(8.452, 11.62);
    distances.put(8.467, 11.63);
    distances.put(8.482, 11.64);
    distances.put(8.498, 11.65);
    distances.put(8.513, 11.66);
    distances.put(8.528, 11.67);
    distances.put(8.543, 11.68);
    distances.put(8.558, 11.69);
    distances.put(8.574, 11.7);
    distances.put(8.589, 11.71);
    distances.put(8.604, 11.72);
    distances.put(8.62, 11.73);
    distances.put(8.635, 11.74);
    distances.put(8.65, 11.75);
    distances.put(8.665, 11.76);
    distances.put(8.681, 11.77);
    distances.put(8.696, 11.78);
    distances.put(8.711, 11.79);
    distances.put(8.727, 11.8);
    distances.put(8.742, 11.81);
    distances.put(8.758, 11.82);
    distances.put(8.773, 11.83);
    distances.put(8.788, 11.84);
    distances.put(8.804, 11.85);
    distances.put(8.819, 11.86);
    distances.put(8.835, 11.87);
    distances.put(8.85, 11.88);
    distances.put(8.865, 11.89);
    distances.put(8.881, 11.9);
    distances.put(8.896, 11.91);
    distances.put(8.912, 11.92);
    distances.put(8.927, 11.93);
    distances.put(8.943, 11.94);
    distances.put(8.958, 11.95);
    distances.put(8.974, 11.96);
    distances.put(8.989, 11.97);
    distances.put(9.005, 11.98);
    distances.put(9.021, 11.99);
    distances.put(9.036, 12.0);
    distances.put(9.052, 12.01);
    distances.put(9.067, 12.02);
    distances.put(9.083, 12.03);
    distances.put(9.099, 12.04);
    distances.put(9.114, 12.05);
    distances.put(9.13, 12.06);
    distances.put(9.146, 12.07);
    distances.put(9.161, 12.08);
    distances.put(9.177, 12.09);
    distances.put(9.193, 12.1);
    distances.put(9.208, 12.11);
    distances.put(9.224, 12.12);
    distances.put(9.24, 12.13);
    distances.put(9.255, 12.14);
    distances.put(9.271, 12.15);
    distances.put(9.287, 12.16);
    distances.put(9.303, 12.17);
    distances.put(9.318, 12.18);
    distances.put(9.334, 12.19);
    distances.put(9.35, 12.2);
    distances.put(9.366, 12.21);
    distances.put(9.382, 12.22);
    distances.put(9.398, 12.23);
    distances.put(9.413, 12.24);
    distances.put(9.429, 12.25);
    distances.put(9.445, 12.26);
    distances.put(9.461, 12.27);
    distances.put(9.477, 12.28);
    distances.put(9.493, 12.29);
    distances.put(9.509, 12.3);
    distances.put(9.525, 12.31);
    distances.put(9.541, 12.32);
    distances.put(9.556, 12.33);
    distances.put(9.572, 12.34);
    distances.put(9.588, 12.35);
    distances.put(9.604, 12.36);
    distances.put(9.62, 12.37);
    distances.put(9.636, 12.38);
    distances.put(9.652, 12.39);
    distances.put(9.668, 12.4);
    distances.put(9.684, 12.41);
    distances.put(9.7, 12.42);
    distances.put(9.716, 12.43);
    distances.put(9.733, 12.44);
    distances.put(9.749, 12.45);
    distances.put(9.765, 12.46);
    distances.put(9.781, 12.47);
    distances.put(9.797, 12.48);
    distances.put(9.813, 12.49);
    distances.put(9.829, 12.5);
    distances.put(9.845, 12.51);
    distances.put(9.861, 12.52);
    distances.put(9.878, 12.53);
    distances.put(9.894, 12.54);
    distances.put(9.91, 12.55);
    distances.put(9.926, 12.56);
    distances.put(9.942, 12.57);
    distances.put(9.959, 12.58);
    distances.put(9.975, 12.59);
    distances.put(9.991, 12.6);
    distances.put(10.007, 12.61);
    distances.put(10.024, 12.62);
    distances.put(10.04, 12.63);
    distances.put(10.056, 12.64);
    distances.put(10.072, 12.65);
    distances.put(10.089, 12.66);
    distances.put(10.105, 12.67);
    distances.put(10.121, 12.68);
    distances.put(10.138, 12.69);
    distances.put(10.154, 12.7);
    distances.put(10.17, 12.71);
    distances.put(10.187, 12.72);
    distances.put(10.203, 12.73);
    distances.put(10.22, 12.74);
    distances.put(10.236, 12.75);
    distances.put(10.252, 12.76);
    distances.put(10.269, 12.77);
    distances.put(10.285, 12.78);
    distances.put(10.302, 12.79);
    distances.put(10.318, 12.8);
    distances.put(10.335, 12.81);
    distances.put(10.351, 12.82);
    distances.put(10.368, 12.83);
    distances.put(10.384, 12.84);
    distances.put(10.401, 12.85);
    distances.put(10.417, 12.86);
    distances.put(10.434, 12.87);
    distances.put(10.45, 12.88);
    distances.put(10.467, 12.89);
    distances.put(10.483, 12.9);
    distances.put(10.5, 12.91);
    distances.put(10.517, 12.92);
    distances.put(10.533, 12.93);
    distances.put(10.55, 12.94);
    distances.put(10.566, 12.95);
    distances.put(10.583, 12.96);
    distances.put(10.6, 12.97);
    distances.put(10.616, 12.98);
    distances.put(10.633, 12.99);
    distances.put(10.65, 13.0);
    distances.put(10.666, 13.01);
    distances.put(10.683, 13.02);
    distances.put(10.7, 13.03);
    distances.put(10.716, 13.04);
    distances.put(10.733, 13.05);
    distances.put(10.75, 13.06);
    distances.put(10.767, 13.07);
    distances.put(10.783, 13.08);
    distances.put(10.8, 13.09);
    distances.put(10.817, 13.1);
    distances.put(10.834, 13.11);
    distances.put(10.851, 13.12);
    distances.put(10.867, 13.13);
    distances.put(10.884, 13.14);
    distances.put(10.901, 13.15);
    distances.put(10.918, 13.16);
    distances.put(10.935, 13.17);
    distances.put(10.952, 13.18);
    distances.put(10.969, 13.19);
    distances.put(10.986, 13.2);
    distances.put(11.002, 13.21);
    distances.put(11.019, 13.22);
    distances.put(11.036, 13.23);
    distances.put(11.053, 13.24);
    distances.put(11.07, 13.25);
    distances.put(11.087, 13.26);
    distances.put(11.104, 13.27);
    distances.put(11.121, 13.28);
    distances.put(11.138, 13.29);
    distances.put(11.155, 13.3);
    distances.put(11.172, 13.31);
    distances.put(11.189, 13.32);
    distances.put(11.206, 13.33);
    distances.put(11.223, 13.34);
    distances.put(11.24, 13.35);
    distances.put(11.257, 13.36);
    distances.put(11.274, 13.37);
    distances.put(11.292, 13.38);
    distances.put(11.309, 13.39);
    distances.put(11.326, 13.4);
    distances.put(11.343, 13.41);
    distances.put(11.36, 13.42);
    distances.put(11.377, 13.43);
    distances.put(11.394, 13.44);

  }

  public static double getShooterSpeed(double distance) {
    distance = (int) (distance * 1000) / (1000.0);
    if (distances.containsKey(distance)) {
      return distances.get(distance);
    } else if (distance > 0.6 && distance < 9.2) {
      double lowKey = distances.floorKey(distance), highKey = distances.ceilingKey(distance);
      return (distances.get(lowKey) + distances.get(highKey)) / 2;
    }
    return -1;
  }
}
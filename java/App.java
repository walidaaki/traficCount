
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import org.opencv.imgproc.Imgproc;

import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class App {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static JFrame frame;
	private static JLabel videoLabel;
	private static CascadeClassifier carDetector;
	static int i=0;
	public static void main(String[] args) {
		
		App.initGUI(1);
		carDetector = new CascadeClassifier("resources/cascades/cars.xml");
		App.runMainLoop(args);
		App.initGUI(2);
		System.out.print("le nombre des voitures est : ");
		System.err.print(i);

	}
	private static void initGUI(int choix) {
		
		if(choix==1) {
		frame = new JFrame("Cars detect & count ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		videoLabel = new JLabel();
		frame.add(videoLabel);
		frame.setVisible(true);}
		else
		{
			frame.setVisible(false);
		}
	}
	private static void runMainLoop(String[] args) {
		ImageProcessor imageProcessor = new ImageProcessor();
		Mat videoMatImage = new Mat();
		Image tempImage;
		VideoCapture capture = new VideoCapture("resources/video/video2.mp4");
		capture.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 500);
		capture.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 400);
		capture.set(Videoio.CAP_PROP_FPS, 1000);
		if (capture.isOpened()) {
			while (true) {
				Mat currentFrame = new Mat();
				boolean cap = capture.read(videoMatImage);
				if (!cap) {
					break;
				}
				MatOfRect carDetections = new MatOfRect();
				carDetector.detectMultiScale(videoMatImage, carDetections);
				if (!videoMatImage.empty()) {
					detectAndDrawCar(videoMatImage);
					tempImage = imageProcessor.toBufferedImage(videoMatImage);
					ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
					videoLabel.setIcon(imageIcon);
					frame.pack(); 
					Imgproc.line(currentFrame, new Point(10, 200), new Point(currentFrame.width() - 10, 200),
							new Scalar(0, 255, 0), 3);
				} else {
					System.out.println(" -- Frame not captured -- Break!");
					break;
				}

			}
		} else {
			System.out.println("Couldn't open capture.");
		}
	}

	private static void detectAndDrawCar(Mat image) {
		
		MatOfRect faceDetections = new MatOfRect();
		carDetector.detectMultiScale(image, faceDetections);
		Imgproc.line(image, new Point(20, 170), new Point(260, 170), new Scalar(0, 255, 0), 2);
		for (Rect rect : faceDetections.toArray()) {

			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 0, 255), 2);

			if ((rect.y < 90 && rect.y > 86) ) {
				++i;
				System.out.println("voiture : "+i);
				Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 255, 0), 2);
				
			}
			
		}
		
	}
}

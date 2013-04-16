package boofcv.example;

import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.openkinect.StreamOpenKinectRgbDepth;
import boofcv.openkinect.UtilOpenKinect;
import boofcv.struct.image.ImageUInt16;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;
import com.sun.jna.NativeLibrary;
import org.openkinect.freenect.Context;
import org.openkinect.freenect.Device;
import org.openkinect.freenect.Freenect;
import org.openkinect.freenect.Resolution;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Peter Abeles
 */
public class OverlayRgbDepthStreamsApp implements StreamOpenKinectRgbDepth.Listener {
	{
		// Modify this link to be where you store your shared library
		if( UtilOpenKinect.PATH_TO_SHARED_LIBRARY != null )
			NativeLibrary.addSearchPath("freenect", UtilOpenKinect.PATH_TO_SHARED_LIBRARY);
	}

	Resolution resolution = Resolution.MEDIUM;

	BufferedImage buffRgb;
	BufferedImage buffDepth;

	ImagePanel gui;

	public void process() {

		int w = UtilOpenKinect.getWidth(resolution);
		int h = UtilOpenKinect.getHeight(resolution);

		buffRgb = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		buffDepth = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

		gui = ShowImages.showWindow(buffRgb,"Kinect Overlay");

		StreamOpenKinectRgbDepth stream = new StreamOpenKinectRgbDepth();
		Context kinect = Freenect.createContext();

		if( kinect.numDevices() < 0 )
			throw new RuntimeException("No kinect found!");

		Device device = kinect.openDevice(0);
		stream.start(device,resolution,this);
	}

	@Override
	public void processKinect(MultiSpectral<ImageUInt8> rgb, ImageUInt16 depth, long timeRgb, long timeDepth) {
		VisualizeImageData.disparity(depth, buffDepth, 0, UtilOpenKinect.FREENECT_DEPTH_MM_MAX_VALUE,0);
		ConvertBufferedImage.convertTo_U8(rgb,buffRgb);

		Graphics2D g2 = buffRgb.createGraphics();
		float alpha = 0.5f;
		int type = AlphaComposite.SRC_OVER;
		AlphaComposite composite =
				AlphaComposite.getInstance(type, alpha);
		g2.setComposite(composite);
		g2.drawImage(buffDepth,0,0,null);

		gui.repaint();
	}

	public static void main( String args[] ) {
		OverlayRgbDepthStreamsApp app = new OverlayRgbDepthStreamsApp();
		app.process();
	}
}

package es.uvigo.ei.sing.mla.view.converters;

import java.awt.Color;

public final class ColorUtils {
	private ColorUtils() {}

	// Base #FF6961
	public final static Color[] THIRTY_COLORS = new Color[] {
		// 1
		new Color(1.00f, 0.41f, 0.38f),
		new Color(0.38f, 1.00f, 0.41f),
		new Color(0.41f, 0.38f, 1.00f),
		// 6
		new Color(0.97f, 1.00f, 0.38f),
		new Color(0.38f, 0.97f, 1.00f),
		new Color(1.00f, 0.38f, 0.97f),
		// 2
		new Color(1.00f, 0.53f, 0.38f),
		new Color(0.38f, 1.00f, 0.53f),
		new Color(0.53f, 0.38f, 1.00f),
		// 7
		new Color(0.84f, 1.00f, 0.38f),
		new Color(0.38f, 0.85f, 1.00f),
		new Color(1.00f, 0.38f, 0.85f),
		// 3
		new Color(1.00f, 0.66f, 0.38f),
		new Color(0.38f, 1.00f, 0.66f),
		new Color(0.65f, 0.38f, 1.00f),
		// 8
		new Color(0.72f, 1.00f, 0.38f),
		new Color(0.38f, 0.85f, 1.00f),
		new Color(1.00f, 0.38f, 0.85f),
		// 4
		new Color(1.00f, 0.78f, 0.38f),
		new Color(0.38f, 1.00f, 0.78f),
		new Color(0.78f, 0.38f, 1.00f),
		// 9
		new Color(0.60f, 1.00f, 0.38f),
		new Color(0.38f, 0.60f, 1.00f),
		new Color(1.00f, 0.38f, 0.60f),
		// 5
		new Color(1.00f, 0.91f, 0.38f),
		new Color(0.38f, 1.00f, 0.91f),
		new Color(0.90f, 0.38f, 1.00f),
		// 10
		new Color(0.47f, 1.00f, 0.38f),
		new Color(0.38f, 0.47f, 1.00f),
		new Color(1.00f, 0.38f, 0.47f)
	};
	
	public static Color getThirtyColor(int offset) {
		return ColorUtils.THIRTY_COLORS[offset%ColorUtils.THIRTY_COLORS.length];
	}
	
	public static String getThirtyColorHex(int offset) {
		return ColorUtils.colorToHexString(
			ColorUtils.THIRTY_COLORS[offset%ColorUtils.THIRTY_COLORS.length]
		);
	}

	public static String colorToHexString(Color color) {
	    String hexString = Integer.toHexString(color.getRGB() & 0x00ffffff);
	    
	    while (hexString.length() < 6) {
	    	hexString = "0" + hexString;
	    }
	    
		return "#" + hexString;
	}
	
	public static Color hexStringToColor(String hexString) {
		return Color.decode(hexString);
	}
	
	public static Color invert(Color color) {
		return new Color(
			255 - color.getRed(), 
			255 - color.getGreen(), 
			255 - color.getBlue()
		);
	}
	
	public static String invert(String color) {
		return colorToHexString(invert(hexStringToColor(color)));
	}
	
	public static String getBestContrast(String color) {
		return colorToHexString(getBestContrast(hexStringToColor(color)));
	}
	
	public static Color getBestContrast(Color color) {
		final double r = Math.pow((double) color.getRed()/255d, 2.2d);
		final double g = Math.pow((double) color.getGreen()/255d, 2.2d);
		final double b = Math.pow((double) color.getBlue()/255d, 2.2d);
		
		final double y = 0.2126*r + 0.7151*g + 0.0721*b;
		
		return y < 0.5 ? Color.WHITE : Color.BLACK;
	}
}

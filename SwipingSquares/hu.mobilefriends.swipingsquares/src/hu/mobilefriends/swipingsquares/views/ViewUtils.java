package hu.mobilefriends.swipingsquares.views;

import android.graphics.Paint;

public class ViewUtils {
	
	static class RectPaint extends Paint
	{
		public RectPaint(int rectColor)
		{
			this.setStyle(Style.FILL); 			
			switch (rectColor) {
				// vörös
				case 0: this.setARGB(255, 255, 0, 0);
						break;
				// zöld
				case 1: this.setARGB(255, 0, 255, 0);
						break;
				// kék
				case 2: this.setARGB(255, 0, 0, 255);
						break;
				// pink
				case 3: this.setARGB(255, 255, 0, 255);
						break;
				// türkiz
				case 4: this.setARGB(255, 0, 255, 255);
						break;
			}			
		}
	}

	public static RectPaint[] rectPaints = {
		new RectPaint(0),
		new RectPaint(1),
		new RectPaint(2),
		new RectPaint(3),
		new RectPaint(4)
	};

}

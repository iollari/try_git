package hu.mobilefriends.swipingsquares.models;

import java.io.Serializable;

public class RectModel implements Serializable {

	private static final long serialVersionUID = -144890411976056698L;
	public int rectLeft;
	public int rectRight;
	public int rectTop;
	public int rectBottom;
	public int rectColor;
	
	public RectModel(int x, int y, int size, int color) {
		rectLeft = x;
		rectTop = y;
		rectRight = x+size;
		rectBottom = y+size;
		rectColor = color;
	}
	
	public int rectCenterX() {
		return rectLeft + Math.round((rectRight-rectLeft)/2);
	}
	
	public int rectCenterY() {
		return rectTop + Math.round((rectBottom-rectTop)/2);
	}

}

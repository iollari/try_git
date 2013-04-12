package hu.mobilefriends.swipingsquares.models;

import java.io.Serializable;

public class PositionModel implements Serializable {
	
	private static final long serialVersionUID = 4583752701891754157L;
	public int cellLeft;
	public int cellTop;
	public int cellRight;
	public int cellBottom;
	public int cellCenterX;
	public int cellCenterY;
	public RectModel rect;
	
}

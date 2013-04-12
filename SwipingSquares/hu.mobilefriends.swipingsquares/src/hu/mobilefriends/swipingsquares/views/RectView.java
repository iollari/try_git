package hu.mobilefriends.swipingsquares.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class RectView {
	
	private CubeView _parentCubeView;
	private Bitmap _bitmap;
	private Canvas _bitmapCanvas;	
	private Drawable _picture;
	
	private int _rectSize;
	
	public Rect rect;
	public int rectColor;	
	
	public RectView(CubeView parentCubeView, int left, int top, int rectSize, int rColor) {

		this._parentCubeView = parentCubeView;

		this.rect = new Rect(left, top, left+rectSize, top+rectSize);
		this.rectColor = rColor;
		this._rectSize = rectSize;
		
		this._bitmap = Bitmap.createBitmap(rectSize, rectSize, Bitmap.Config.ARGB_8888);
		this._bitmapCanvas = new Canvas(this._bitmap);
	}
	
	public void draw(Canvas canvas) {
		int shiftLeft = (this._parentCubeView.cubeLeft + this._parentCubeView.cubePadding) - this.rect.left;
		if (shiftLeft > 0)
		{
			this.rect.left = this.rect.left + shiftLeft;
		}
		
		int shiftRight = this.rect.right - (this._parentCubeView.cubeRight - this._parentCubeView.cubePadding);
		if (shiftRight > 0)
		{
			this.rect.right = this.rect.right - shiftRight;
		}
		
		int shiftTop = (this._parentCubeView.cubeTop + this._parentCubeView.cubePadding) - this.rect.top;
		if (shiftTop > 0)
		{
			this.rect.top = this.rect.top + shiftTop;
		}

		int shiftBottom = this.rect.bottom - (this._parentCubeView.cubeBottom - this._parentCubeView.cubePadding);
		if (shiftBottom > 0)
		{
			this.rect.bottom = this.rect.bottom - shiftBottom;
		}
		
		if ((this.rect.left < this.rect.right) && (this.rect.top < this.rect.bottom))
		{					
	        this._picture = this._parentCubeView.rectPictures[this.rectColor];
	        this._picture.setBounds(0, 0, this._rectSize, this._rectSize);        
	        this._picture.draw(this._bitmapCanvas);

	        Rect picturePart = new Rect(Math.max(0, shiftLeft), 
								  Math.max(0, shiftTop), 
								  Math.min(this._rectSize, this._rectSize - shiftRight), 
								  Math.min(this._rectSize, this._rectSize - shiftBottom)); 
			Rect drawingDest = new Rect(this.rect.left, this.rect.top, this.rect.right, this.rect.bottom);
			canvas.drawBitmap(this._bitmap, picturePart, drawingDest, null); 			
		}
	}
}

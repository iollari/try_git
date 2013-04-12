package hu.mobilefriends.swipingsquares.views;

import hu.mobilefriends.swipingsquares.R;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class CubeView extends View {
		
	public int cubeLeft;
	public int cubeTop;
	private int _rectSize;
	private int _cubeSize;
	public int cubeRight;
	public int cubeBottom;
	private int _cubeRectPerRow;
	public int cubePadding;
	private int _cubeMoveRectCount;
	private Resources _res;
	
	private RectView[] _movingRectsRow;
	private RectView[] _movingRectsColumn;

	public boolean rectTouched;	
	public RectView[][] rectangles;
	
	public Drawable[] rectPictures;
	
	public CubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
	}
	
	public void initView(int cubeLeft, int cubeTop, int rectSize, int cubeSize, int cubeRowPerLine, int cubePadding)
	{
		this.cubeLeft = cubeLeft;
		this.cubeTop = cubeTop;
		this._rectSize = rectSize;
		this._cubeSize = cubeSize;
		this._cubeRectPerRow = cubeRowPerLine;
		this.cubePadding = cubePadding;
		
		// számított értékek
		this.cubeRight = this.cubeLeft + this._cubeSize;
		this.cubeBottom = this.cubeTop + this._cubeSize;
		this._cubeMoveRectCount = (2*this._cubeRectPerRow) + 1;		
			
		// erõforrások elérése
		_res = this.getContext().getResources();
				
		// lehetséges négyzet képek		
		this.rectPictures = new Drawable[5];
		this.rectPictures[0] = _res.getDrawable(R.drawable.rectred);
		this.rectPictures[1] = _res.getDrawable(R.drawable.rectgreen);
		this.rectPictures[2] = _res.getDrawable(R.drawable.rectblue);
		this.rectPictures[3] = _res.getDrawable(R.drawable.rectmagenta);
		this.rectPictures[4] = _res.getDrawable(R.drawable.rectyellow);
		
		// négyzetek létrehozása 
		// (itt mindegyik a bal felsõ pozícióba kerül, majd az elsõ OnDraw fogja a helyükre "tenni" õket)
		this.rectangles = new RectView[this._cubeRectPerRow][this._cubeRectPerRow];	
		int i;
		int j;		
		for (i=0;i<this._cubeRectPerRow;i++) {
			for (j=0;j<this._cubeRectPerRow;j++) {
				this.rectangles[i][j] = new RectView(this,this.cubeLeft+this.cubePadding,
						  						 	 this.cubeTop+this.cubePadding,
						  						 	 this._rectSize,
						  						 	 0);
			}
		}
		
		// mozgatandó négyzeteket tároló tömbök inicializálása
		this._movingRectsColumn = new RectView[this._cubeMoveRectCount];
		this._movingRectsRow = new RectView[this._cubeMoveRectCount];		    			
	}
	
	protected void onDraw(Canvas canvas) {
		
		int i;
		int j;
		
//		canvas.drawBitmap(_backgroundBitmap, 0, 0, null);
		
		// kirajzoljuk a négyzeteket a kapott paraméterekben meghatározott pozícióban
		for (i=0;i<this._cubeRectPerRow;i++) {
			for (j=0;j<this._cubeRectPerRow;j++) {
				if ((this.rectangles[i][j].rect.right >= this.cubeLeft) && (this.rectangles[i][j].rect.left <= this.cubeRight) &&
					(this.rectangles[i][j].rect.bottom >= this.cubeTop) && (this.rectangles[i][j].rect.top <= this.cubeBottom)) {
					this.rectangles[i][j].draw(canvas);
				}
			}
		}
		
		if (this.rectTouched) {
			for (i=0;i<this._cubeMoveRectCount;i++) {
				if ((this._movingRectsRow[i] != null) && (this._movingRectsRow[i].rect.right >= this.cubeLeft) && (this._movingRectsRow[i].rect.left <= this.cubeRight)) {
					this._movingRectsRow[i].draw(canvas);
				}
				if ((this._movingRectsColumn[i] != null) && (this._movingRectsColumn[i].rect.bottom >= this.cubeTop) && (this._movingRectsColumn[i].rect.top <= this.cubeBottom)) {
					this._movingRectsColumn[i].draw(canvas);
				}
			}
		}
		
		int h = this.getHeight();
	}
	
	public void refreshMovingRectRow(int ix, int movingRectLeft, int movingRectTop, int movingRectColor) {
		if (this._movingRectsRow[ix] == null)
		{
			this._movingRectsRow[ix] = new RectView(this,movingRectLeft,
											  	    movingRectTop,
											  	    this._rectSize,
											  	    movingRectColor);
		}
		else
		{
			this._movingRectsRow[ix].rect.left = movingRectLeft;
			this._movingRectsRow[ix].rect.top = movingRectTop;
			this._movingRectsRow[ix].rect.right = movingRectLeft + this._rectSize;
			this._movingRectsRow[ix].rect.bottom = movingRectTop + this._rectSize;
			this._movingRectsRow[ix].rectColor = movingRectColor;
		}
	}
	
	public void refreshMovingRectColumn(int ix, int movingRectLeft, int movingRectTop, int movingRectColor) {
		if (this._movingRectsColumn[ix] == null)
		{
			this._movingRectsColumn[ix] = new RectView(this,movingRectLeft,
												  	 movingRectTop,
												  	 this._rectSize,
												  	 movingRectColor);
		}
		else
		{
			this._movingRectsColumn[ix].rect.left = movingRectLeft;
			this._movingRectsColumn[ix].rect.top = movingRectTop;
			this._movingRectsColumn[ix].rect.right = movingRectLeft + this._rectSize;
			this._movingRectsColumn[ix].rect.bottom = movingRectTop + this._rectSize;
			this._movingRectsColumn[ix].rectColor = movingRectColor;
		}
	}
}

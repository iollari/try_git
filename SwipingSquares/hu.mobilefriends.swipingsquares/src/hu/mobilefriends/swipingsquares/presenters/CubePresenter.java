package hu.mobilefriends.swipingsquares.presenters;

import hu.mobilefriends.swipingsquares.interfaces.OnCubeMoveListener;
import hu.mobilefriends.swipingsquares.models.*;
import hu.mobilefriends.swipingsquares.views.CubeView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CubePresenter {
	
	// Az aktuális View, ami kezeli az inputokat és rajzol a felületre 
	private CubeView cubeView;
	// A Bûvös négyzet, amivel játszunk
	private CubeModel cubeModel;
	
	// Mozgatással, érintéssel, elengedéssel kapcsolatos változók
	private byte _moveDirection;
	private int _touchX;
	private int _touchY;
	private int _currentX;
	private int _currentY;
	private int _selectedPosX;
	private int _selectedPosY;
	
	// Lépés eseménykezelõ
	private OnCubeMoveListener _onCubeMoveListener = null;
	
	private String _movingColorOrder;
	
	public CubePresenter(CubeModel cubeModel, CubeView cubeView, boolean mix) {
		super();
		
		this.cubeModel = cubeModel;

		this.cubeView = cubeView;
		this.cubeView.setOnTouchListener( new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				handleTouchEvent(event);
				return true;
			}
			
		});		
						
		if (mix)
		{
			this.cubeModel.randomMix();
		}
		
		refreshCubeView();
	}

	public void SetOnCubeMoveListener(OnCubeMoveListener listener)
	{
		_onCubeMoveListener = listener;
	}
	
	public void refreshCubeView() {
		int i;
		int j;
		for (i=0;i<this.cubeModel.cstRectPerRow;i++) {
			for (j=0;j<this.cubeModel.cstRectPerRow;j++) {
				this.cubeView.rectangles[i][j].rect.left = this.cubeModel.positions[i][j].rect.rectLeft;
				this.cubeView.rectangles[i][j].rect.top = this.cubeModel.positions[i][j].rect.rectTop;
				this.cubeView.rectangles[i][j].rect.right = this.cubeModel.positions[i][j].rect.rectRight;
				this.cubeView.rectangles[i][j].rect.bottom = this.cubeModel.positions[i][j].rect.rectBottom;
				this.cubeView.rectangles[i][j].rectColor = this.cubeModel.positions[i][j].rect.rectColor;						
			}
		}
		
		if (this.cubeView.rectTouched) {
			for (i=0;i<this.cubeModel.cstMovingRectCount;i++) {
				this.cubeView.refreshMovingRectRow(i,this.cubeModel.movingRectsRow[i].rectLeft,
													 this.cubeModel.movingRectsRow[i].rectTop,
													 this.cubeModel.movingRectsRow[i].rectColor);	
				this.cubeView.refreshMovingRectColumn(i,this.cubeModel.movingRectsColumn[i].rectLeft,
						  								this.cubeModel.movingRectsColumn[i].rectTop,
						  								this.cubeModel.movingRectsColumn[i].rectColor);	
			}
		}
		this.cubeView.invalidate();
	}
	
	private void onTouch(int x, int y)
	{
		// Megnézzük, hogy az érintés melyik négyzeten belül történt
		int i;
		int j;		
		for (i=0;i<this.cubeModel.cstRectPerRow;i++) {
			for (j=0;j<this.cubeModel.cstRectPerRow;j++) {
				if ((x >= this.cubeModel.positions[i][j].rect.rectLeft) && 
					(x <= this.cubeModel.positions[i][j].rect.rectRight) &&
					(y >= this.cubeModel.positions[i][j].rect.rectTop) && 
					(y <= this.cubeModel.positions[i][j].rect.rectBottom)) 
				{							
					this.cubeView.rectTouched = true;
					this._moveDirection = 0;
					this._touchX = x;
					this._touchY = y;								
					this._selectedPosX = i;
					this._selectedPosY = j;
					// itt kell létrehozni az adott sor/oszlop mozgatandó négyzet-tömbjeit
					this.cubeModel.createMovingRects(this._selectedPosX,this._selectedPosY);
					break;
				}					
			}
			if (j<this.cubeModel.cstRectPerRow) {
				break;
			}
		}
	}
	
	private void handleTouchEvent(MotionEvent event) {
		
		_currentX = (int)event.getX();
		_currentY = (int)event.getY();				
		// ÉRINTÉS			
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.onTouch((int)event.getX(),(int)event.getY());
		}

		// MOZGATÁS
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (this.cubeView.rectTouched) {
				if (this._moveDirection == 0) {			
					// CSAK IRÁNYMEGHATÁROZÁS!!!
					if ((Math.abs(this._currentX-this._touchX) > 5) || (Math.abs(this._currentY-this._touchY) > 5)) { 
						if (Math.abs(this._currentX-this._touchX) > Math.abs(this._currentY-this._touchY)) {
							this._moveDirection = 1; // vízszintesen
						} else {
							this._moveDirection = 2; // függõlegesen
						}
						// kiinduló színsorrend meghatározása a mozgatott sorban/oszlopban
						setMovingColorOrder();
					}
				} else {
					// NÉGYZETEK MOZGATÁSA
					if ((this._currentX >= this.cubeModel.cstCubeLeft) && (this._currentX <= this.cubeModel.cstCubeRight) &&
						(this._currentY >= this.cubeModel.cstCubeTop) && (this._currentY <= this.cubeModel.cstCubeBottom)) {
						
						this.cubeModel.move(this._moveDirection, this._currentX - this._touchX, this._currentY - this._touchY);
						
						this._touchX = this._currentX;
						this._touchY = this._currentY;
						
						// az aktuális állapotnak megfelelõen frissíti a felületet					
						refreshCubeView();						
					}
				}
			}
		}

		// ELENGEDÉS
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (this.cubeView.rectTouched) {
				this.cubeView.rectTouched = false;
				if (this._moveDirection > 0) {
					
					this.cubeModel.refreshPositions(this._moveDirection, this._selectedPosX, this._selectedPosY);
					
					// az aktuális állapotnak megfelelõen frissíti a felületet					
					refreshCubeView();
					
					if (movingColorOrderChanged())
					{
						_onCubeMoveListener.OnCubeMove();
					}
				}
			}
			this._moveDirection = 0;
		}
	}

	private boolean movingColorOrderChanged() {
		int i;
		String actualMovingColorOrder = "";
		for (i=0;i<this.cubeModel.cstRectPerRow;i++)
		{
			if (this._moveDirection == 1)
			{
				actualMovingColorOrder += String.valueOf(this.cubeModel.positions[i][this._selectedPosY].rect.rectColor);
			}
			else
			{
				actualMovingColorOrder += String.valueOf(this.cubeModel.positions[this._selectedPosX][i].rect.rectColor);				
			}
		}
		return (!(_movingColorOrder.equals(actualMovingColorOrder)));
	}

	private void setMovingColorOrder() {
		int i;
		_movingColorOrder = "";
		for (i=0;i<this.cubeModel.cstRectPerRow;i++)
		{
			if (this._moveDirection == 1)
			{
				_movingColorOrder += String.valueOf(this.cubeModel.positions[i][this._selectedPosY].rect.rectColor);
			}
			else
			{
				_movingColorOrder += String.valueOf(this.cubeModel.positions[this._selectedPosX][i].rect.rectColor);				
			}
		}
	}
}

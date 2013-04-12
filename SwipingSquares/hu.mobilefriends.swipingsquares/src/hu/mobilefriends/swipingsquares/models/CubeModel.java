package hu.mobilefriends.swipingsquares.models;
import java.io.Serializable;
import java.util.Random;

public class CubeModel implements Serializable {

	private static final long serialVersionUID = -5056227740580235601L;
	public int cstRectPerRow;
	public int cstCubeLeft;
	public int cstCubeTop;
	public int cstRectSize;
	public int cstCubeSize;
	public int cstCubeRight;
	public int cstCubeBottom;
	public int cstPadding;
	public int cstMovingRectCount;
	
	public RectModel[] movingRectsRow;
	public RectModel[] movingRectsColumn;
	
	public PositionModel[][] positions;
	
	public CubeModel(int rectPerRow, int cubeLeft, int cubeTop, int rectSize, int padding) {
		
		this.cstRectPerRow = rectPerRow;	
		this.cstCubeLeft = cubeLeft;
		this.cstCubeTop = cubeTop;
		this.cstRectSize = rectSize;
		this.cstPadding = padding;
		
		// számított értékek
		this.cstCubeSize = cstRectPerRow * (cstRectSize + 2*cstPadding);
		this.cstCubeRight = cstCubeLeft + cstCubeSize;
		this.cstCubeBottom = cstCubeTop + cstCubeSize;
		
		this.cstMovingRectCount = (2*this.cstRectPerRow) + 1;
		
		// pozíciók (PositionModel) és négyzetek (RectModel) létrehozása 
		this.positions = new PositionModel[cstRectPerRow][cstRectPerRow];
		int i = 0;
		int j = 0;
		for (i=0;i<cstRectPerRow;i++) {
			for (j=0;j<cstRectPerRow;j++) {
				PositionModel actPosition = new PositionModel();
				actPosition.cellLeft = cstCubeLeft + (i*(cstRectSize+(2*cstPadding)));
				actPosition.cellTop = cstCubeTop + (j*(cstRectSize+(2*cstPadding)));
				actPosition.cellRight = actPosition.cellLeft + cstRectSize + (2*cstPadding);
				actPosition.cellBottom = actPosition.cellTop + cstRectSize + (2*cstPadding);
				
				actPosition.cellCenterX = actPosition.cellLeft + Math.round((actPosition.cellRight-actPosition.cellLeft)/2);
				actPosition.cellCenterY = actPosition.cellTop + Math.round((actPosition.cellBottom-actPosition.cellTop)/2);

				actPosition.rect = new RectModel(actPosition.cellLeft + cstPadding, 
												 actPosition.cellTop + cstPadding, cstRectSize, j);
				this.positions[i][j] = actPosition;
			}
		}
		
		// mozgatandó négyzeteket tároló tömbök inicializálása
		this.movingRectsColumn = new RectModel[this.cstMovingRectCount];
		this.movingRectsRow = new RectModel[this.cstMovingRectCount];
	}
	
	// egy sor vagy oszlop összes négyzetének léptetése egy pozícióval jobbra vagy lefelé (orientation-tõl függ)
	// (valójában csak a négyzetek színét cserélgetjük)
	private void step(int rowColumn, int orientation) {
		int actRectColor;		
		if (orientation == 0) {
			actRectColor = this.positions[rowColumn][this.cstRectPerRow-1].rect.rectColor;
			int j;
			for (j=this.cstRectPerRow-1;j>0;j--) {
				this.positions[rowColumn][j].rect.rectColor = this.positions[rowColumn][j-1].rect.rectColor;
			}
			this.positions[rowColumn][0].rect.rectColor = actRectColor;
		} else if (orientation == 1) {
			actRectColor = this.positions[this.cstRectPerRow-1][rowColumn].rect.rectColor;
			int i;
			for (i=this.cstRectPerRow-1;i>0;i--) {
				this.positions[i][rowColumn].rect.rectColor = this.positions[i-1][rowColumn].rect.rectColor;
			}
			this.positions[0][rowColumn].rect.rectColor = actRectColor;
		}
	}
	
	public void createMovingRects(int column, int row) {
		int movingRowIx;
		int movingColumnIx;
		int i;
		int movingRectLeft;
		int movingRectTop;
		int movingRectColor;
		movingRowIx = 0;
		movingColumnIx = 0;
		for (i=0;i<this.cstMovingRectCount;i++) {
			this.movingRectsRow[i] = null;
			this.movingRectsColumn[i] = null;
		}		
		// cube-on kívül balra
		for (i=column;i<this.cstRectPerRow;i++) {
			movingRectLeft = this.positions[i][row].rect.rectLeft - (this.cstRectPerRow*((2*this.cstPadding)+this.cstRectSize));
			movingRectTop = this.positions[i][row].rect.rectTop;
			movingRectColor = this.positions[i][row].rect.rectColor;
			this.movingRectsRow[movingRowIx] = new RectModel(movingRectLeft, movingRectTop, this.cstRectSize, movingRectColor);
			movingRowIx++;
		}
		// cube-on belül, adott sor négyzetei
		for (i=0;i<this.cstRectPerRow;i++) {
			this.movingRectsRow[movingRowIx] = this.positions[i][row].rect;
			movingRowIx++;
		}
		
		// cube-on kívül jobbra
		for (i=0;i<=column;i++) {
			movingRectLeft = this.positions[i][row].rect.rectLeft + (this.cstRectPerRow*((2*this.cstPadding)+this.cstRectSize));
			movingRectTop = this.positions[i][row].rect.rectTop;
			movingRectColor = this.positions[i][row].rect.rectColor;
			this.movingRectsRow[movingRowIx] = new RectModel(movingRectLeft, movingRectTop, this.cstRectSize, movingRectColor);
			movingRowIx++;
		}
		
		// cube-on kívül fent
		for (i=row;i<this.cstRectPerRow;i++) {
			movingRectLeft = this.positions[column][i].rect.rectLeft;
			movingRectTop = this.positions[column][i].rect.rectTop - (this.cstRectPerRow*((2*this.cstPadding)+this.cstRectSize));
			movingRectColor = this.positions[column][i].rect.rectColor;
			this.movingRectsColumn[movingColumnIx] = new RectModel(movingRectLeft, movingRectTop, this.cstRectSize, movingRectColor);
			movingColumnIx++;
		}
		
		// cube-on belül, adott oszlop négyzetei
		for (i=0;i<this.cstRectPerRow;i++) {
			this.movingRectsColumn[movingColumnIx] = this.positions[column][i].rect;
			movingColumnIx++;
		}
		
		// cube-on kívül lent
		for (i=0;i<=row;i++) {
			movingRectLeft = this.positions[column][i].rect.rectLeft;
			movingRectTop = this.positions[column][i].rect.rectTop + (this.cstRectPerRow*((2*this.cstPadding)+this.cstRectSize));
			movingRectColor = this.positions[column][i].rect.rectColor;
			this.movingRectsColumn[movingColumnIx] = new RectModel(movingRectLeft, movingRectTop, this.cstRectSize, movingRectColor);
			movingColumnIx++;
		}
	}

	public void randomMix() {
		int i;
		int j;
		// Színek összekeverése (valójában a létrehozott pozíciók randomizálása)
		Random myRandom = new Random();
		int randomStep;
		
		// oszlopokon végig, random pozícióval eltolni
		for (i=0;i<this.cstRectPerRow;i++) {
			randomStep = myRandom.nextInt(this.cstRectPerRow);
			for (j=0;j<randomStep;j++) {
				this.step(i,0);
			}
		}
		
		// sorokon végig, random pozícióval eltolni
		for (j=0;j<this.cstRectPerRow;j++) {
			randomStep = myRandom.nextInt(this.cstRectPerRow);
			for (i=0;i<randomStep;i++) {
				this.step(j,1);
			}
		}			
	}

	public void refreshPositions(int moveDirection, int selectedPosX, int selectedPosY) {
		// a mozgatott négyzetek közül balról/fentrõl haladva megkeressük azt, amelyik az elsõ pozícióba esik,
		// helyreigazítjuk, a többit pedig ehhez képest
		int i;
		int j;
		if (moveDirection == 1) { // vízszintes
			for (i=0;i<this.cstMovingRectCount;i++) {
				if ((this.movingRectsRow[i].rectCenterX() >= this.positions[0][selectedPosY].cellLeft) &&
				    (this.movingRectsRow[i].rectCenterX() < this.positions[0][selectedPosY].cellRight)) {
					break;
				}
			}
			for (j=0;j<this.cstRectPerRow;j++) {			
				this.movingRectsRow[i+j].rectLeft = this.positions[j][selectedPosY].cellLeft + this.cstPadding;
				this.positions[j][selectedPosY].rect = this.movingRectsRow[i+j];				
				this.positions[j][selectedPosY].rect.rectRight = this.positions[j][selectedPosY].rect.rectLeft + this.cstRectSize;
			}
		} else if (moveDirection == 2) { // függõleges
			for (i=0;i<this.cstMovingRectCount;i++) {
				if ((this.movingRectsColumn[i].rectCenterY() >= this.positions[selectedPosX][0].cellTop) &&
				    (this.movingRectsColumn[i].rectCenterY() < this.positions[selectedPosX][0].cellBottom)) {
					break;
				}
			}			
			for (j=0;j<this.cstRectPerRow;j++) {			
				this.movingRectsColumn[i+j].rectTop = this.positions[selectedPosX][j].cellTop + this.cstPadding;
				this.positions[selectedPosX][j].rect = this.movingRectsColumn[i+j];				
				this.positions[selectedPosX][j].rect.rectBottom = this.positions[selectedPosX][j].rect.rectTop + this.cstRectSize;
			}
		}	
	}
	
	public void move(int moveDirection, int deltaX, int deltaY)
	{
		int i;
		if (moveDirection == 1) {
			for (i=0;i<this.cstMovingRectCount;i++) {
				this.movingRectsRow[i].rectLeft = this.movingRectsRow[i].rectLeft + deltaX;
				this.movingRectsRow[i].rectRight = this.movingRectsRow[i].rectLeft + this.cstRectSize;
			}							
		} else if (moveDirection == 2) {
			for (i=0;i<this.cstMovingRectCount;i++) {
				this.movingRectsColumn[i].rectTop =	this.movingRectsColumn[i].rectTop + deltaY;
				this.movingRectsColumn[i].rectBottom = this.movingRectsColumn[i].rectTop + this.cstRectSize;
			}							
		}		
	}
}

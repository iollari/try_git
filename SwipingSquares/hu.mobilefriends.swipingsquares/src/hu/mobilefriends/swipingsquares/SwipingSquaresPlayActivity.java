package hu.mobilefriends.swipingsquares;

import hu.mobilefriends.swipingsquares.R;
import hu.mobilefriends.swipingsquares.interfaces.OnCubeMoveListener;
import hu.mobilefriends.swipingsquares.models.CubeModel;
import hu.mobilefriends.swipingsquares.presenters.CubePresenter;
import hu.mobilefriends.swipingsquares.views.CubeView;
import android.app.Activity;
import android.os.SystemClock;
import android.view.Display;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwipingSquaresPlayActivity extends Activity {
    
	// Presenter	
	private CubePresenter _cubePresenter;	
	// View
	private CubeView _cubeView;	
	// Model
	private CubeModel _cubeModel;
	
	// T�rol� a szerializ�ci�hoz (�llapot ment�shez / visszat�lt�shez)
	private byte[] _cubeBytes;
	
	private int _screenWidth;
	
	// stopper
	private Chronometer _stopWatch;
	private long _elapsedTimeBeforePause;
	
	// l�p�sek sz�ma
	private TextView _moveCountText;
	private int _moveCount;
	
    public void onResume() {
    	super.onResume();
   	         	
        setContentView(R.layout.activity_main);
              
		// l�trehozzuk a j�t�kt�bl�t
        createCube();
        
		// feliratkozunk a mozgat�s esem�nyre
		startMoveListener();

        // elind�tjuk az �r�t
        startWatch();
    }
        
	private void startMoveListener() {
		
		_moveCountText = (TextView) findViewById(R.id.moveCount);
		
		_cubePresenter.SetOnCubeMoveListener(new OnCubeMoveListener ()
		{
			public void OnCubeMove()
			{
				_moveCount++;
				_moveCountText.setText(String.valueOf(_moveCount));
			}
		});
	}
	
	private void startWatch() {
        _stopWatch = (Chronometer) findViewById(R.id.chrono);

        // Ha onPause ut�ni visszat�r�s t�rt�nik, akkor a stoppert "onnan folytatjuk, ahol abbahagytuk"
		_stopWatch.setBase(SystemClock.elapsedRealtime() - _elapsedTimeBeforePause);		

        _stopWatch.start();               		
	}

	private void createCube()
	{
        // l�trehozzuk a Modelt
		createCubeModel();

		// inicializ�ljuk a View-t
		_cubeView = (CubeView)findViewById(R.id.cubeview);
		
        _cubeView.initView(_cubeModel.cstCubeLeft, _cubeModel.cstCubeTop, 
				 _cubeModel.cstRectSize, _cubeModel.cstCubeSize, _cubeModel.cstRectPerRow, 
				 _cubeModel.cstPadding);
        
        _cubeView.getLayoutParams().height = _cubeModel.cstCubeSize;
        
        // l�trehozzuk a Presentert
        createCubePresenter();        		
	}
	
    private void createCubeModel()
    {
        if (_cubeBytes == null) 
        {            
        	// ezt egyel�re itt be�getem, k�s�bb a user �ltal v�lasztott �rt�ket kapom majd...
        	int rectPerRow = 4;
        	
        	// M�retek kisz�m�t�sa, �s a t�bla k�z�pre igaz�t�sa a kijelz� felbont�sa alapj�n
        	Display display = getWindowManager().getDefaultDisplay();
       		this._screenWidth = display.getWidth(); 
       		
        	// padding 1%
        	int padding = Math.round(this._screenWidth / 100);
        	// t�bla m�ret a sz�less�g 90%-a
        	int cubeSize = Math.round((this._screenWidth * 90) / 100);
        	// k�z�pre igaz�t�s
        	int cubeLeft = Math.round((this._screenWidth * 5) / 100);
        	int cubeTop = 0;
        	// n�gyzetek m�rete
        	int rectSize = Math.round((cubeSize - ((rectPerRow * 2) * padding)) / rectPerRow);
        	
        	_cubeModel = new CubeModel(rectPerRow, cubeLeft, cubeTop, rectSize, padding);
        } 
        else
        {
        	_cubeModel = (CubeModel) SerializerClass.deserializeObject(_cubeBytes);        	
        }    	
    }

    private void createCubePresenter()
    {
		_cubePresenter = new CubePresenter(_cubeModel, _cubeView, _cubeBytes == null);
    }

    public void onPause() {
    	super.onPause();
		// szerializ�ljuk a model �llapot�t
    	_cubeBytes = SerializerClass.serializeObject(_cubeModel);
		
		// megjegyezz�k, hol �llt a stopper...
		_elapsedTimeBeforePause = SystemClock.elapsedRealtime() - _stopWatch.getBase();
    }
}
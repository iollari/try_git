package hu.mobilefriends.swipingsquares;

import hu.mobilefriends.swipingsquares.R;
import hu.mobilefriends.swipingsquares.interfaces.OnCubeMoveListener;
import hu.mobilefriends.swipingsquares.models.CubeModel;
import hu.mobilefriends.swipingsquares.presenters.CubePresenter;
import hu.mobilefriends.swipingsquares.views.CubeView;
import android.app.Activity;
import android.os.SystemClock;
import android.view.Display;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class SwipingSquaresPlayActivity extends Activity {
    
	// Presenter	
	private CubePresenter _cubePresenter;	
	// View
	private CubeView _cubeView;	
	// Model
	private CubeModel _cubeModel;
	
	// Tároló a szerializációhoz (Állapot mentéshez / visszatöltéshez)
	private byte[] _cubeBytes;
	
	private int _screenWidth;
	
	// stopper
	private Chronometer _stopWatch;
	private long _elapsedTimeBeforePause;
	
	// lépések száma
	private TextView _moveCountText;
	private int _moveCount;
	
    public void onResume() {
    	super.onResume();
   	         	
        setContentView(R.layout.activity_main);
        
        initLayoutReferences();
              
		// létrehozzuk a játéktáblát
        createCube();
        
		// feliratkozunk a mozgatás eseményre
		startMoveListener();

        // elindítjuk az órát
        resetWatch();
    }
    
	private void initLayoutReferences() 
	{
		_moveCountText = (TextView) findViewById(R.id.moveCount);
		_stopWatch = (Chronometer) findViewById(R.id.chrono);
		_cubeView = (CubeView)findViewById(R.id.cubeview);
	}

	private void createCube()
	{
        // létrehozzuk a Modelt
		createCubeModel();

        // inicializáljuk a View-t
		_cubeView.initView(_cubeModel.cstCubeLeft, _cubeModel.cstCubeTop, 
				 _cubeModel.cstRectSize, _cubeModel.cstCubeSize, _cubeModel.cstRectPerRow, 
				 _cubeModel.cstPadding);
        
        // létrehozzuk a Presentert
        createCubePresenter();        		
	}
	
    @SuppressWarnings("deprecation")
	private void createCubeModel()
    {
        if (_cubeBytes == null) 
        {            
        	// ezt egyelõre itt beégetem, késõbb a user által választott értéket kapom majd...
        	int rectPerRow = 4;
        	
        	// Méretek kiszámítása, és a tábla középre igazítása a kijelzõ felbontása alapján
        	Display display = getWindowManager().getDefaultDisplay();
       		this._screenWidth = display.getWidth(); 
       		
        	// padding 1%
        	int padding = Math.round(this._screenWidth / 100);
        	// tábla méret a szélesség 90%-a
        	int cubeSize = Math.round((this._screenWidth * 90) / 100);
        	// középre igazítás
        	int cubeLeft = Math.round((this._screenWidth * 5) / 100);
        	int cubeTop = 0;
        	// négyzetek mérete
        	int rectSize = Math.round((cubeSize - ((rectPerRow * 2) * padding)) / rectPerRow);
        	
        	_cubeModel = new CubeModel();
        	_cubeModel.Init(rectPerRow, cubeLeft, cubeTop, rectSize, padding);
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
        
	private void startMoveListener() 
	{			
		_cubePresenter.SetOnCubeMoveListener(new OnCubeMoveListener ()
		{
			public void OnCubeMove()
			{
				_moveCount++;
				resetMoveCountText();
			}
		});
	}
	
	private void resetMoveCountText() {
		_moveCountText.setText(String.valueOf(_moveCount));		
	}
	
	private void resetWatch() {        
        // Ha onPause utáni visszatérés történik, akkor a stoppert "onnan folytatjuk, ahol abbahagytuk"
		_stopWatch.setBase(SystemClock.elapsedRealtime() - _elapsedTimeBeforePause);		
        _stopWatch.start();               		
	}

    public void onPause() {
    	super.onPause();
		// szerializáljuk a model állapotát
    	_cubeBytes = SerializerClass.serializeObject(_cubeModel);
		
		// megjegyezzük, hol állt a stopper...
		_elapsedTimeBeforePause = SystemClock.elapsedRealtime() - _stopWatch.getBase();
    }
    
    @SuppressWarnings("deprecation")
	public void onBtnShufflePressed(View btnShuffle)
    {
		_moveCount = 0;
		_elapsedTimeBeforePause = 0;
		resetWatch();
		resetMoveCountText();
		
    	// ezt egyelõre itt beégetem, késõbb a user által választott értéket kapom majd...
    	int rectPerRow = 3;
    	
    	// Méretek kiszámítása, és a tábla középre igazítása a kijelzõ felbontása alapján
    	Display display = getWindowManager().getDefaultDisplay();
   		this._screenWidth = display.getWidth(); 
   		
    	// padding 1%
    	int padding = Math.round(this._screenWidth / 100);
    	// tábla méret a szélesség 90%-a
    	int cubeSize = Math.round((this._screenWidth * 90) / 100);
    	// középre igazítás
    	int cubeLeft = Math.round((this._screenWidth * 5) / 100);
    	int cubeTop = 0;
    	// négyzetek mérete
    	int rectSize = Math.round((cubeSize - ((rectPerRow * 2) * padding)) / rectPerRow);
    	
//    	_cubeModel = new CubeModel();
    	_cubeModel.Init(rectPerRow, cubeLeft, cubeTop, rectSize, padding);
    	
		_cubePresenter.Shuffle();
    }
}
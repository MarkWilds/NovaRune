package wildrune.novarune;

import wildrune.ouyaframework.OuyaGameActivity;
import wildrune.ouyaframework.game.screens.ScreenManager;
import wildrune.ouyaframework.graphics.basic.Color;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;

/**
 * The main game 
 * @author Wildune(Mark van der Wal)
 *
 */
public class Game extends OuyaGameActivity
{
	private ScreenManager screenManager;
	private final String gameName = "Nova Rune";
	
	/**
	 * Constructor
	 */
	public Game()
	{
		screenManager = new ScreenManager(this);
	}
	
	@Override
	protected void Create() 
	{
		// set graphics settings
		Graphics.SetClearColor(Color.BLACK);
		
		// add starting screen
		screenManager.Create("Textures/blank.png");
		screenManager.AddScreen(new MainBackgroundScreen(gameName), 0);
		screenManager.AddScreen(new MainMenuScreen(), 0);
	}

	@Override
	protected void Dispose() 
	{
		screenManager.Dispose();
	}

	@Override
	protected void Update(float dt) 
	{
		// update manager
		screenManager.Update(dt);
		
		Gamepad pad = Input.GetGamepad(0);
		
		if(pad.GetButtonDown(GamepadCodes.BUTTON_U))
		{
			// show current screens in the screenmanager
			screenManager.Debug();
		}
	}

	@Override
	protected void Draw() 
	{
		Graphics.Clear();
		
		// draw manager
		float dt = gameTimer.GetElapsedSeconds();
		screenManager.Draw( dt );
	}
}

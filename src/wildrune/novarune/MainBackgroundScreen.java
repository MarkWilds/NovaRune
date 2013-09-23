package wildrune.novarune;

import wildrune.ouyaframework.OuyaGameActivity;
import wildrune.ouyaframework.audio.Music;
import wildrune.ouyaframework.game.screens.GameScreen;
import wildrune.ouyaframework.graphics.basic.Color;
import wildrune.ouyaframework.graphics.basic.SpriteFont;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;
import wildrune.ouyaframework.input.InputSystem;
import wildrune.ouyaframework.math.RuneMath;
import wildrune.ouyaframework.math.Vec2;

public class MainBackgroundScreen extends GameScreen
{
	// resources
	private SpriteFont titleFont;
	
	// mathematical
	private Vec2 titlePosition;
	
	// other
	private final float bobSpeed = 180.0f;
	private float curBobRot = 0.0f;
	private float titleStartYPosition;
	private String gameName;
	
	public MainBackgroundScreen(String gameName)
	{
		this.gameName = gameName;
	}

	/**
	 * Initialize this screen
	 */
	@Override
	public void Create() 
	{
		// local use of game
		OuyaGameActivity game = screenManager.game;
		game.Graphics.SetClearColor(Color.BLACK);
		
		// load resources
		titleFont = game.Resources.LoadFont("Fonts/square.ttf", true, 102, 1, 1, true, 5);
		
		// set title position
		float screenWidthHalf = game.Graphics.viewportSafeArea.getWidth() / 2.0f;
		float titleXPos = screenWidthHalf - titleFont.MeasureText(gameName) / 2.0f;
		titleStartYPosition = game.Graphics.viewportSafeArea.y;
		titlePosition = new Vec2(titleXPos, titleStartYPosition);
	}

	@Override
	public void Dispose() 
	{
		titleFont.Dispose();
	}
	
	/**
	 * Update this screen, tell that no other screens can cover it
	 */
	@Override
	public void Update(float dt, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		super.Update(dt, otherScreenHasFocus, false);
		
		// bob the title text up and down
		BobTitleText(dt);
	}
	
	/**
	 * Bob the title text up and down
	 */
	private void BobTitleText(float dt)
	{
		titlePosition.y = titleStartYPosition + (float)Math.sin( RuneMath.TORAD * curBobRot ) * 10.0f + 10.0f;
		
		// increase bobbing rotation
		curBobRot += bobSpeed * dt;
		
		// clamp the bobbing rotation
		if(curBobRot > 360.0f)
			curBobRot = 360.0f - curBobRot;
	}

	/**
	 * Draw this screen
	 */
	@Override
	public void Draw(float dt) 
	{
		OuyaGameActivity game = screenManager.game;
		
		// draw title text
		screenManager.spriteBatch.Begin();
		screenManager.spriteBatch.DrawText(titleFont, gameName, titlePosition, Color.ORANGE);
		screenManager.spriteBatch.End();
	}

	/**
	 * Handle input for this screen
	 */
	@Override
	public boolean HandleInput(InputSystem input) 
	{
		return false;
	}
}

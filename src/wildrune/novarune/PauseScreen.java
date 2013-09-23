package wildrune.novarune;

import wildrune.ouyaframework.OuyaGameActivity;
import wildrune.ouyaframework.audio.Sound;
import wildrune.ouyaframework.game.menus.ConsoleMenu;
import wildrune.ouyaframework.game.menus.MenuEventHandler;
import wildrune.ouyaframework.game.menus.MenuTextButton;
import wildrune.ouyaframework.game.menus.ScreenElement;
import wildrune.ouyaframework.game.screens.GameScreen;
import wildrune.ouyaframework.graphics.SpriteBatch;
import wildrune.ouyaframework.graphics.SpriteBatch.SpriteEffect;
import wildrune.ouyaframework.graphics.basic.Color;
import wildrune.ouyaframework.graphics.basic.Rectangle;
import wildrune.ouyaframework.graphics.basic.SpriteFont;
import wildrune.ouyaframework.graphics.basic.Texture2D;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;
import wildrune.ouyaframework.input.InputSystem;
import wildrune.ouyaframework.math.Vec2;

public class PauseScreen extends GameScreen implements MenuEventHandler
{
	private ConsoleMenu pauseMenu;
	private SpriteFont pauseTitleFont;
	private SpriteFont menuTextFont;
	private Sound selectSound;
	private Sound undoSound;
	private Texture2D blank;
	
	// mathematical
	private float startYPos;
	private float endYPos;
	private Vec2 titlePosition;
	
	public PauseScreen()
	{
		this.isPopup = true;
		this.transition.onTime = 0.5f;
		this.startYPos = -96;
	}

	/**
	 * Create this screen
	 */
	@Override
	public void Create() 
	{
		OuyaGameActivity game = screenManager.game;
		
		// initialize used resources
		pauseTitleFont = game.Resources.LoadFont("Fonts/square.ttf", true, 96, 1,1, true, 3);
		menuTextFont = game.Resources.LoadFont("Fonts/square.ttf", true, 96, 1,1, true, 3);
		blank = game.Resources.LoadTexture("Textures/blank.png");
		selectSound = game.Audio.LoadSound("Audio/Sound/select.wav");
		undoSound = game.Audio.LoadSound("Audio/Sound/undo.wav");
		
		// set title position
		float screenWidthHalf = game.Graphics.viewportSafeArea.getWidth() / 2.0f;
		float titleXPos = screenWidthHalf - pauseTitleFont.MeasureText("Pause") / 2.0f;
		endYPos = game.Graphics.viewportSafeArea.y;
		titlePosition = new Vec2(titleXPos, startYPos);
		
		CreateMenus();
	}
	
	/**
	 * Initialize menu's for the main menu
	 */
	public void CreateMenus()
	{
		// create menu items
		pauseMenu = new ConsoleMenu("MainMenu", 4, 96);
		MenuTextButton itemOne = new MenuTextButton("resume_game", "Resume", menuTextFont, Color.ORANGE);
		MenuTextButton itemTwo = new MenuTextButton("quit_game", "Quit", menuTextFont, Color.ORANGE);
		
		itemOne.SetEventHandler(this);
		itemTwo.SetEventHandler(this);
		
		pauseMenu.AddItem(itemOne);
		pauseMenu.AddItem(itemTwo);
		
		// set position
		pauseMenu.position.x = 750;
		pauseMenu.position.y = 600;
	}
	
	/**
	 * Update this screen
	 */
	@Override
	public void Update(float dt, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		super.Update(dt, otherScreenHasFocus, coveredByOtherScreen);
		
		if(this.IsTransisioning())
		{
			titlePosition.y = startYPos + transition.GetTransitionAlpha() * (endYPos - startYPos);
		}
		
		pauseMenu.Update(dt);
	}

	/**
	 * Dispose of this screen
	 */
	@Override
	public void Dispose() 
	{
		pauseTitleFont.Dispose();
		menuTextFont.Dispose();
		blank.Dispose();
		selectSound.Release();
	}

	/**
	 * Draw this screen
	 */
	@Override
	public void Draw(float dt) 
	{
		OuyaGameActivity game = screenManager.game;
		Rectangle viewport = game.Graphics.viewportNormal;
		SpriteBatch batch = screenManager.spriteBatch;

		// draw title text
		batch.Begin();
		
		batch.DrawSprite(blank,
				0, 0, viewport.width, viewport.height, 
				0, 0, blank.width, blank.height, 
				0.0f, 0.0f, 0.0f, transition.GetTransitionAlpha() * 0.5f,
				0.0f, 0.0f, 0.0f, 0.0f, SpriteEffect.NONE);
		
		batch.DrawText(pauseTitleFont, "Pause", titlePosition, Color.ORANGE);
		pauseMenu.Draw(batch, false, dt);
		
		batch.End();
	}
	
	/**
	 * Handles this screen input
	 */
	@Override
	public boolean HandleInput(InputSystem input) 
	{		
		Gamepad pad = input.GetGamepad(0);
		if(pad.GetButtonDown(GamepadCodes.BUTTON_DPAD_DOWN))
		{
			pauseMenu.SelectNext();
			screenManager.game.Audio.Play(selectSound, 1.0f);
		}
		else if(pad.GetButtonDown(GamepadCodes.BUTTON_DPAD_UP))
		{
			pauseMenu.SelectPrev();
			screenManager.game.Audio.Play(selectSound, 1.0f);
		}
		
		pauseMenu.HandleInput(input);

		return true;
	}

	/**
	 * Hanle menu events
	 */
	public void HandleEvent(ScreenElement element)
	{
		if(element.identifier == "resume_game")
		{
			this.ExitScreen();
			screenManager.game.Audio.Play(undoSound, 1.0f);
		}
		
		if(element.identifier == "quit_game")
		{
			screenManager.ExitScreens();
			
			screenManager.game.Audio.Play(undoSound, 1.0f);
			screenManager.AddScreen(new MainBackgroundScreen("Nova Rune"),0 );
			screenManager.AddScreen(new MainMenuScreen(),0 );
		}
	}
}

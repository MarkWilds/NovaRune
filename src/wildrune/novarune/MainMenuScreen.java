package wildrune.novarune;

import wildrune.ouyaframework.OuyaGameActivity;
import wildrune.ouyaframework.audio.Sound;
import wildrune.ouyaframework.game.menus.ConsoleMenu;
import wildrune.ouyaframework.game.menus.MenuEventHandler;
import wildrune.ouyaframework.game.menus.MenuTextButton;
import wildrune.ouyaframework.game.menus.ScreenElement;
import wildrune.ouyaframework.game.screens.GameScreen;
import wildrune.ouyaframework.graphics.SpriteBatch;
import wildrune.ouyaframework.graphics.basic.Color;
import wildrune.ouyaframework.graphics.basic.SpriteFont;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;
import wildrune.ouyaframework.input.InputSystem;

public class MainMenuScreen extends GameScreen
{
	private ConsoleMenu mainMenu;
	private SpriteFont menuTextFont;
	private Sound selectSound;
	private Sound actionSound;
	
	public MainMenuScreen()
	{
		transition.onTime = 1.5f;
	}
	
	@Override
	public void Create() 
	{
		OuyaGameActivity game = screenManager.game;
		
		// initialize used resources
		menuTextFont = game.Resources.LoadFont("Fonts/square.ttf", true, 96, 1,1, true, 3);
		selectSound = game.Audio.LoadSound("Audio/Sound/select.wav");
		actionSound = game.Audio.LoadSound("Audio/Sound/action.wav");
		
		// create menu
		CreateMenus();
	}
	
	/**
	 * Initialize menu's for the main menu
	 */
	public void CreateMenus()
	{
		// create menu items
		mainMenu = new ConsoleMenu("MainMenu", 4, 96);
		MenuTextButton itemOne = new MenuTextButton("start_game", "Start", menuTextFont, Color.ORANGE);
		MenuTextButton itemTwo = new MenuTextButton("exit_game", "Exit", menuTextFont, Color.ORANGE);
		
		menuHandler handler = new menuHandler();
		itemOne.SetEventHandler(handler);
		itemTwo.SetEventHandler(handler);
		
		mainMenu.AddItem(itemOne);
		mainMenu.AddItem(itemTwo);
		
		// set position
		mainMenu.position.x = 750;
		mainMenu.position.y = 600;
	}
	
	@Override
	public void Dispose() 
	{
	}
	
	@Override
	public void Update(float dt, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		super.Update(dt, otherScreenHasFocus, coveredByOtherScreen);
		
		// update the menu
		mainMenu.Update(dt);
	}

	@Override
	public void Draw(float dt) 
	{
		SpriteBatch batch = screenManager.spriteBatch;
		
		batch.Begin();
		
		// draw the menu
		mainMenu.Draw(batch, true, dt);
		
		batch.End();
		
		screenManager.FadeBackBufferToBlack( transition.GetTransitionAlpha() );
	}

	@Override
	public boolean HandleInput(InputSystem input) 
	{		
		Gamepad pad = input.GetGamepad(0);
		if(pad.GetButtonDown(GamepadCodes.BUTTON_DPAD_DOWN))
		{
			mainMenu.SelectNext();
			screenManager.game.Audio.Play(selectSound, 1.0f);
		}
		else if(pad.GetButtonDown(GamepadCodes.BUTTON_DPAD_UP))
		{
			mainMenu.SelectPrev();
			screenManager.game.Audio.Play(selectSound, 1.0f);
		}

		return mainMenu.HandleInput(input);
	}

	private class menuHandler implements MenuEventHandler
	{
		public void HandleEvent(ScreenElement element)
		{
			// play action sound
			screenManager.game.Audio.Play(actionSound, 1.0f);
			
			if(element.identifier == "start_game")
			{
				// remove all screens
				screenManager.ExitScreens();
				screenManager.AddScreen( new PlayScreen(), 0);
			}
			
			if(element.identifier == "exit_game")
			{
				screenManager.game.Exit();
			}
		}
	}
}

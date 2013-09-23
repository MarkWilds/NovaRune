package wildrune.novarune;

import wildrune.ouyaframework.OuyaGameActivity;
import wildrune.ouyaframework.audio.Music;
import wildrune.ouyaframework.audio.Sound;
import wildrune.ouyaframework.game.screens.GameScreen;
import wildrune.ouyaframework.graphics.SpriteBatch;
import wildrune.ouyaframework.graphics.basic.Color;
import wildrune.ouyaframework.graphics.basic.SpriteFont;
import wildrune.ouyaframework.graphics.basic.Texture2D;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;
import wildrune.ouyaframework.input.InputSystem;
import wildrune.ouyaframework.math.Vec2;

public class PlayScreen extends GameScreen 
{
	// resources
	private Music bgMusic;
	private Sound doSound;
	
	private Vec2 scorePosition;
	private Vec2 healthPosition;
	private SpriteFont scoreFont;
	
	// game
	PlayerShip player;
	
	/**
	 * Constructor
	 */
	public PlayScreen()
	{
		transition.onTime = 0.5f;
	}

	@Override
	public void Create() 
	{
		// local use of game
		OuyaGameActivity game = screenManager.game;
		
		// load resources
		scoreFont = game.Resources.LoadFont("Fonts/square.ttf", false, 36, 1,1, true, 1);
		Texture2D playerTex = game.Resources.LoadTexture("Textures/sprites.png");
		bgMusic = game.Audio.LoadMusic("Audio/Music/bgm001.ogg");
		doSound = game.Audio.LoadSound("Audio/Sound/do.wav");
		
		// game
		scorePosition = new Vec2(game.Graphics.viewportSafeArea.x + 10,
				game.Graphics.viewportSafeArea.y + 10);
		healthPosition = new Vec2(scorePosition.x,
				scorePosition.y + 42);
		player = new PlayerShip(playerTex);
		
		// play music
		game.Audio.SetLooping(true);
		game.Audio.SetVolume(0.2f);
		game.Audio.Play(bgMusic);
	}

	@Override
	public void Dispose() 
	{
		// stop music
		if(screenManager.game.Audio.IsPlaying())
			screenManager.game.Audio.Stop();
	}
	
	@Override
	public void Update(float dt,  boolean otherFocus, boolean covered)
	{
		super.Update(dt, otherFocus, covered);
		
		if(this.IsActive())
		{
			// handle player
			player.Update(dt, screenManager.game.Input);
		}
	}

	@Override
	public void Draw(float dt) 
	{
		SpriteBatch batch = screenManager.spriteBatch;

		// start drawing game
		batch.Begin();
		
			player.Draw(batch);
			
			// draw HUD
			batch.DrawText(scoreFont, "Score: 0", scorePosition, Color.YELLOW);
			batch.DrawText(scoreFont, "HP: 100000", healthPosition, Color.GREEN);
		
		batch.End();
	}

	@Override
	public boolean HandleInput(InputSystem input) 
	{	
		Gamepad pad = input.GetGamepad(0);
		if(pad.GetButtonDown(GamepadCodes.BUTTON_Y))
		{
			// show pause menu
			screenManager.AddScreen(new PauseScreen(), 0);
			screenManager.game.Audio.Play(doSound, 1.0f);
			return true;
		}
		
		return false;
	}

}

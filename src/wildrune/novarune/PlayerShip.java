package wildrune.novarune;

import wildrune.ouyaframework.graphics.SpriteBatch;
import wildrune.ouyaframework.graphics.SpriteBatch.SpriteEffect;
import wildrune.ouyaframework.graphics.basic.Rectangle;
import wildrune.ouyaframework.graphics.basic.Texture2D;
import wildrune.ouyaframework.input.Gamepad;
import wildrune.ouyaframework.input.GamepadCodes;
import wildrune.ouyaframework.input.InputSystem;
import wildrune.ouyaframework.math.Vec2;

public class PlayerShip 
{
	// rendering
	private Texture2D playerTexture;
	private Rectangle playerTextRegion;
	private Vec2 playerOrigin;
	
	// physics
	private Vec2 position;
	private Vec2 direction;
	private Vec2 velocity;
	private float rotation;
	
	// constants
	private final float engineForce = 1000.0f;
	private final float dragFactor = 0.9f;
	
	// forces
	private Vec2 dragForce;
	private Vec2 accumForce;
	
	private final float maxSpeed = 600;
	private final float minSpeed = 5.0f;
	
	public PlayerShip(Texture2D texture)
	{
		playerTexture = texture;
		playerTextRegion = new Rectangle(0, 0, 64, 64);
		playerOrigin = new Vec2(32,32);
		
		// initialize
		position = new Vec2(512,312);
		direction = new Vec2(0,-1);
		velocity = new Vec2();
		dragForce = new Vec2();
		accumForce = new Vec2();
	}

	public void Update(float dt, InputSystem input)
	{
		Gamepad pad = input.GetFirstConnectedGamepad();
		
		// get move direction from the left stick
		direction.x = pad.GetAxisRaw(GamepadCodes.AXIS_LS_X);
		direction.y = pad.GetAxisRaw(GamepadCodes.AXIS_LS_Y);
		
		// handle deadzone
		if( !HandleDeadzone(direction) )
		{
			// simulate motor force
			direction.Scale(engineForce);
			accumForce.Add(direction);
		}
		
		// create the drag force
		dragForce.x = -velocity.x * dragFactor;
		dragForce.y = -velocity.y * dragFactor;
		
		accumForce.Add(dragForce);
		
		// integrate velocity
		velocity.Add( accumForce.Scale(dt) );
		accumForce.x = 0.0f;
		accumForce.y = 0.0f;
		
		// limit the speed
		float veLength = velocity.Length();
		if(veLength > maxSpeed)
		{
			velocity.Normalize();
			velocity.Scale(maxSpeed);
		}
		else if ( veLength < minSpeed )
		{
			velocity.x = 0.0f;
			velocity.y = 0.0f;
		}

		// integrate position
		position.Add(velocity.x * dt, velocity.y * dt);
		
		// get look direction from the right stick	
		direction.x = pad.GetAxisRaw(GamepadCodes.AXIS_RS_X);
		direction.y = pad.GetAxisRaw(GamepadCodes.AXIS_RS_Y);
		
		// handle deadzone
		if( !HandleDeadzone(direction) )
			direction.Normalize();
	}
	
	public void Draw(SpriteBatch batch)
	{
		// get rotation
		if(direction.x != 0.0f || direction.y != 0.0f)
			rotation = direction.GetAngle() + 90.0f;
		
		// draw the player
		batch.DrawSprite(playerTexture, position.x, position.y, 
				playerTextRegion.width, playerTextRegion.height, 
				playerTextRegion.x, playerTextRegion.y, 
				playerTextRegion.width, playerTextRegion.height, 
				1, 1, 1, 1, 
				playerOrigin.x, playerOrigin.y, 
				1.0f, rotation, 
				SpriteEffect.NONE);
	}
	
	public boolean HandleDeadzone(Vec2 vec)
	{
		float len = vec.Length();
		
		if(len < 0.15f)
		{
			vec.x = 0.0f;
			vec.y = 0.0f;
			return true;
		}
		
		return false;
	}
}

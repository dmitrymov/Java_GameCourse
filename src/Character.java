import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Character {

	protected int x;
	protected int y;
	protected int velX;
	protected int velY;
	protected BufferedImage player;
	protected int currentSprite;
	protected SpriteSheet characterSprite;
	protected int gameWidth;
	protected int gameHeight;
	protected boolean removeAfterDeath;
	protected int deathDelay;
	
	public Character(int x, int y, Game g, boolean isPlayer) {
		this.x = x;
		this.y = y;
		currentSprite = 0;
		if(isPlayer)
			characterSprite = new SpriteSheet(g.getPlayerSprite());
		else
			characterSprite = new SpriteSheet(g.getEnemySprite());
		player = characterSprite.grabPlayerImage(0, 0, characterSprite.getSpriteWidth(), characterSprite.getSpriteHeight());
		gameWidth = g.getWidth();
		gameHeight = g.getHeight();
		velX = 0;
		velY = 0;
		removeAfterDeath = false;
		deathDelay = 0;
	}
	
	public void tick() {
		if(deathDelay > 0) {
			deathDelay++;
			if(deathDelay > 10)
				removeAfterDeath = true;
			return;
		}
		moveXCoordinate();
		moveYCoordinate();
	}
	
	private void moveYCoordinate() {
		int temp = y + velY;
		if(temp < 0 || temp + characterSprite.getSpriteHeight() > Game.HEIGHT)
			temp = y;
		y = temp;
	}
	
	private void moveXCoordinate() {
		x += velX;
		if(velX > 0)
			moveRight();
		if(velX < 0)
			moveLeft();
		if(velX == 0)
			currentSprite = 0;
		player = characterSprite.grabPlayerImage(currentSprite, 0, characterSprite.getSpriteWidth(), characterSprite.getSpriteHeight());
	}
	
	protected void moveLeft() {
		currentSprite--;
		if(currentSprite < 5 || currentSprite > 9)
			currentSprite = 9;		
	}

	protected void moveRight() {
		currentSprite++;
		if(currentSprite > 4)
			currentSprite = 0;
	}

	public void setPlayerImage(BufferedImage p) {
		player = p;
	}
	
	public BufferedImage getPlayerImage() {
		return player;
	}
	
	public void render(Graphics g) {
		g.drawImage(player, x, y, null);
	}
	
	// animates the character death
	public void die() {
		Graphics g = player.getGraphics();
		player = characterSprite.grabPlayerImage(10, 0, characterSprite.getSpriteWidth(), characterSprite.getSpriteHeight());
		g.drawImage(player, x, y, null);
		deathDelay++;
	}

	public int getXVelocity() {
		return velX;
	}
	
	public int getYVelocity() {
		return velY;
	}
	
	public void setXVelocity(int dx) {
		velX = dx;
	}
	
	public void setYVelocity(int dy) {
		velY = dy;
	}
		
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int value) {
		if(value >= 0 && value < gameWidth)
			x = value;
	}
	
	public void setY(int value) {
		if(value >= 0 && value < gameHeight)
			y = value;
	}
	
	public int getWidth() {
		return characterSprite.getSpriteWidth();
	}
	
	public int getHeight() {
		return characterSprite.getSpriteHeight();
	}
	
	public boolean removeAfterDeath() {
		return removeAfterDeath;
	}
}

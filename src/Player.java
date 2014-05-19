import java.awt.Graphics;

public class Player extends Character{

	//private int x;
	//private int y;
	//private int velX;
	//private int velY;
	//private BufferedImage player;
	private Bullets bullets;
	//private int currentSprite;
	//private SpriteSheet playerSprite;
	
	public Player(int x, int y, Game game) {
		super(x, y, game, true);
		//setX(x);
		//setY(y);
		//characterSprite = new SpriteSheet(game.getPlayerSprite());
		//currentSprite = 0;
		//player = characterSprite.grabPlayerImage(0, currentSprite, characterSprite.getSpriteWidth(), characterSprite.getSpriteHeight());
		//velX = 0;
		//velY = 0;
		bullets = new Bullets(game);
	}
	
	public void tick() {
		moveXCoordinate();
		moveYCoordinate();
		bullets.tick();
		bullets.checkDestroy(x);
	}
	
	private void moveYCoordinate() {
		int temp = y + velY;
		if(temp < 0 || temp + characterSprite.getSpriteHeight() > Game.HEIGHT)
			temp = y;
		y = temp;
	}
	
	private void moveXCoordinate() {
		int temp = x + velX;
		if(temp < 0 || temp + characterSprite.getSpriteWidth() > Game.WIDTH)
			temp = x;
		x = temp;
		if(velX > 0)
			moveRight();
		if(velX < 0)
			moveLeft();
		if(velX == 0)
			currentSprite = 0;
		player = characterSprite.grabPlayerImage(currentSprite, 0, characterSprite.getSpriteWidth(), characterSprite.getSpriteHeight());
	}
	
	/*
	private void moveLeft() {
		currentSprite--;
		if(currentSprite < 5 || currentSprite > 9)
			currentSprite = 9;		
	}

	private void moveRight() {
		currentSprite++;
		if(currentSprite > 4)
			currentSprite = 0;
	}
	*/
	
	public void render(Graphics g) {
		g.drawImage(player, x, y, null);
		bullets.render(g);
	}

	/*
	public void setXVelocity(int dx) {
		velX = dx;
	}
	
	public void setYVelocity(int dy) {
		velY = dy;
	}
	*/
	
	// fire bullet from current x,y coordinates
	public void fire() {
		int bulletX = x + player.getWidth();
		int bulletY = y + player.getHeight() / 3;
		Bullet b = new Bullet(bulletX, bulletY, bullets.getGame());
		bullets.addBullet(b);
	}
	
	public Bullets getBullets() {
		return bullets;
	}
	
	/*
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}*/
	
}

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet {

	private int x;
	private int y;
	private final int width = 30;
	private final int height = 13;
	private int speed;
	private BufferedImage image;
	
	public Bullet(int newx, int newy, Game game) {
		x = newx;
		y = newy;
		speed = 10;
		SpriteSheet s = new SpriteSheet(game.getBulletSprite());
		image = s.grabBulletImage(0, 0, width, height);
	}
	
	public void tick() {
		x += speed;
		
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	public int getX() {
		return x;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}

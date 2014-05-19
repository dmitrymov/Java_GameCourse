import java.awt.image.BufferedImage;


public class SpriteSheet {

	private BufferedImage image;
	private int spriteWidth = 80;
	private int spriteHeight = 134;
	
	public SpriteSheet(BufferedImage img) {
		image = img;
	}
	
	public BufferedImage grabPlayerImage(int col, int row, int width, int height) {
		BufferedImage img = image.getSubimage(col * spriteWidth, row * spriteHeight, width, height);
		return img;
	}
	
	/*
	public BufferedImage grabEnemyImage(int col, int row, int width, int height) {
		BufferedImage img = image.getSubimage(col * spriteWidth, row * spriteHeight, width, height);
		return img;
	}*/
	
	public BufferedImage grabBulletImage(int col, int row, int width, int height) {
		BufferedImage img = image.getSubimage(col, row, width, height);
		return img;
	}
	
	public int getSpriteWidth() {
		return spriteWidth;
	}
	
	public int getSpriteHeight() {
		return spriteHeight;
	}
	
	public void setSpriteWidth(int size) {
		if(size > 0 && size < 256)
			spriteWidth = size;
	}
	
	public void setSpriteHeight(int size) {
		if(size > 0 && size < 256)
			spriteHeight = size;
	}
}

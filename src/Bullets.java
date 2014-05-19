import java.awt.Graphics;
import java.util.LinkedList;


public class Bullets {

	private LinkedList<Bullet> bullets = new LinkedList<Bullet>();
	private Game game;
	
	public Bullets(Game g) {
		game = g;
	}
	
	public void tick() {
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
	}
	
	public void checkDestroy(int playerX) {
		for (int i = 0; i < bullets.size(); i++) {
			int distance = Math.abs(bullets.get(i).getX() - playerX);
			if(distance > Game.WIDTH * 2) {
				removeBullet(i);
				i--;
			}
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
	}
	
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
	
	public void removeBullet(Bullet b) {
		bullets.remove(b);
	}
	
	public void removeBullet(int index) {
		bullets.remove(index);
	}

	public Game getGame() {
		return game;
	}
	
	public int size() {
		return bullets.size();
	}
	
	public Bullet get(int i) {
		return bullets.get(i);
	}
	
	public void destroy(int index) {
		if(index < 0 || index > bullets.size())
			return;
		bullets.remove(index);
	}
}


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 1;
	public static final String TITLE = "Game course project";
	
	private boolean running = false;
	private Thread thread;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage playerSprite = null;
	private BufferedImage bulletSprite = null;
	private BufferedImage enemySprite = null;
	private LinkedList<Character> enemys;
	private Player player;
	private final int moveSpeed = 5;
	//private final int enemysNumber = 10;
	private long lastEnemyAddedAt;
	
	public static void main(String[] args) {
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(Game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
	
	public void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			playerSprite = loader.loadImage("player.png");
			bulletSprite = loader.loadImage("bullet.png");
			enemySprite = loader.loadImage("enemy.png");
		} catch (IOException e) {
			System.out.println("Cannot load image");
		}
		this.requestFocus();
		addKeyListener(new KeyInput(this));
		//addKeyListener(new MyKeyListener(this));
		enemys = new LinkedList<Character>();
		Character enemy = new Character(WIDTH, HEIGHT - playerSprite.getHeight(), this, false);
		enemy.setXVelocity(-moveSpeed);
		enemys.add(enemy);
		lastEnemyAddedAt = System.currentTimeMillis();
		player = new Player(100, HEIGHT - 134, this);
	}

	// start game thread
	private synchronized void start() {
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	// stop game thread
	private synchronized void stop() {
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("try/catch");
		}
		System.exit(1);
	}

	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				tick();
				delta--;
				updates++;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "Ticks, Fps " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		player.tick();
		for(int i = 0; i < enemys.size(); i++) {
			enemys.get(i).tick();
		}
		destroyFarEnemys();
		checkColissions();
		addEnemys();
	}
	
	private void addEnemys() {
		long current = System.currentTimeMillis();
		if(current - lastEnemyAddedAt > 5000) {
			Character enemy = new Character(WIDTH, HEIGHT - playerSprite.getHeight(), this, false);
			enemy.setXVelocity(-moveSpeed);
			enemys.add(enemy);
			lastEnemyAddedAt = current;
		}
	}

	private void destroyFarEnemys() {
		for (int i = 0; i < enemys.size(); i++) {
			int distance = Math.abs(player.getX() - enemys.get(i).getX());
			if(distance > WIDTH*2) {
				enemys.remove(i);
				i--;
			}
		}
	}

	private void checkColissions() {
		Bullets bullets = player.getBullets();
		for (int i = 0; i < bullets.size(); i++) {		// bullets loop
			for (int j = 0; j < enemys.size(); j++) {	// enemys loop
				int bulletX = bullets.get(i).getX();
				int characterX = enemys.get(j).getX();
				// if bullet fly -> and enemy goes <-
				if(bulletX + bullets.get(i).getWidth() > characterX && bulletX < characterX + enemys.get(j).getWidth()) {
					bullets.destroy(i);
					enemys.get(j).die();
					enemys.remove(j);
				}
				// check other cases???
			}
		}
	}

	// animate the game
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		/////////////////////////////////////////
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH*2, HEIGHT*2);
		player.render(g);
		for(int i = 0; i < enemys.size(); i++) {
			enemys.get(i).render(g);
		}
		
		/////////////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT:
			//player.move(moveSpeed, 0);
			player.setXVelocity(moveSpeed);
			break;
		case KeyEvent.VK_LEFT:
			//player.move(-moveSpeed, 0);
			player.setXVelocity(-moveSpeed);
			break;
		case KeyEvent.VK_UP:
			//player.move(0, -moveSpeed);
			player.setYVelocity(-moveSpeed);
			break;
		case KeyEvent.VK_DOWN:
			//player.move(0, moveSpeed);
			player.setYVelocity(moveSpeed);
			break;

		default:
			//System.out.println("default switch case");
			break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT:
			player.setXVelocity(0);
			break;
		case KeyEvent.VK_LEFT:
			player.setXVelocity(0);
			break;
		case KeyEvent.VK_UP:
			player.setYVelocity(0);
			break;
		case KeyEvent.VK_DOWN:
			player.setYVelocity(0);
			break;
		case KeyEvent.VK_SPACE:
			player.fire();
			break;
		default:
			//System.out.println("default switch case");
			break;
		}
	}
	
	public BufferedImage getBulletSprite() {
		return bulletSprite;
	}
	
	public BufferedImage getEnemySprite() {
		return enemySprite;
	}
	
	public BufferedImage getPlayerSprite() {
		return playerSprite;
	}

	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
}

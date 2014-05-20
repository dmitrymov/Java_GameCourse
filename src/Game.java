import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JFrame;


public class Game extends Canvas implements Runnable, MouseListener {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	//public static final int SCALE = 1;
	public static final String TITLE = "Game course project";
	
	private boolean running = false;
	private Thread thread;
	//private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage backgroundSprite = null;
	private BufferedImage playerSprite = null;
	private BufferedImage bulletSprite = null;
	private BufferedImage enemySprite = null;
	private LinkedList<Character> enemys;
	private Player player;
	private final int moveSpeed = 5;
	//private final int enemysNumber = 10;
	private long lastEnemyAddedAt;
	private Menu menu;
	private Help help;
	//public static boolean init;
	
	public enum STATE{
		MENU, GAME, Help
	};
	public static STATE State = STATE.MENU;
	
	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		game.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame(Game.TITLE);
		frame.setPreferredSize(new Dimension(WIDTH+6, HEIGHT+28));
		//frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.add(game);
		//frame.setUndecorated(true);		// remove all decorations 
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
			backgroundSprite = loader.loadImage("background.png");
			playerSprite = loader.loadImage("player.png");
			bulletSprite = loader.loadImage("bullet.png");
			enemySprite = loader.loadImage("enemy.png");
		} catch (IOException e) {
			System.out.println("Cannot load image");
		}
		this.requestFocus();
		addKeyListener(new KeyInput(this));
		//addMouseListener(new MouseList());
		addMouseListener(this);
		menu = new Menu();
		help = new Help();
		/*
		enemys = new LinkedList<Character>();
		Character enemy = new Character(WIDTH, HEIGHT - playerSprite.getHeight(), this, false);
		enemy.setXVelocity(-moveSpeed);
		enemys.add(enemy);
		lastEnemyAddedAt = System.currentTimeMillis();
		player = new Player(100, HEIGHT - 134, this);
		*/
	}
	
	private void startNewGame() {
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
		init();
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
		if(State == STATE.MENU) {
			
		}
		else if(State == STATE.GAME) {
			player.tick();
			for(int i = 0; i < enemys.size(); i++) {
				enemys.get(i).tick();
			}
			destroyFarEnemys();
			checkColissions();
			destroyAfterDeath();
			addEnemys();							// adds enemy every 5 seconds
		}
	}
	
	private void destroyAfterDeath() {
		for (int i = 0; i < enemys.size(); i++) {
			if(enemys.get(i).removeAfterDeath()) {
				enemys.remove(i);
				i--;
			}
		}
		if(player.removeAfterDeath()) {
			System.out.println("The game is over!");
			State = STATE.MENU;
		}
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
		// check bullets collision
		Bullets bullets = player.getBullets();
		for (int i = 0; i < bullets.size(); i++) {		// bullets loop
			for (int j = 0; j < enemys.size(); j++) {	// enemys loop
				int bulletX = bullets.get(i).getX();
				int characterX = enemys.get(j).getX();
				// if bullet fly -> and enemy goes <-
				if(bulletX + bullets.get(i).getWidth() > characterX && bulletX < characterX + enemys.get(j).getWidth()) {
					bullets.destroy(i);
					enemys.get(j).die();
					//enemys.remove(j);
				}
				// check other cases???
				if(i >= bullets.size())
					break;
			}
		}
		// check player collision
		for (int i = 0; i < enemys.size(); i++) {
			// if player moves -> and enemy moves <-
			int characterX = enemys.get(i).getX();
			if(player.getX() + player.getWidth() > characterX && player.getX() < characterX) {
				enemys.get(i).die();
				player.die();
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
		
		//g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		//g.setColor(Color.WHITE);
		//g.fillRect(0, 0, WIDTH, HEIGHT);
		if(State == STATE.GAME) {
			g.drawImage(backgroundSprite, 0, 0, getWidth(), getHeight(), null);
			player.render(g);
			for(int i = 0; i < enemys.size(); i++) {
				enemys.get(i).render(g);
			}
		}
		else if (State == STATE.MENU) {
			menu.render(g);
		}
		else if(State == STATE.Help) {
			help.render(g);
		}
		else if (State == STATE.Help) {
			help.render(g);
		}
		
		/////////////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(State == STATE.MENU) {
			
		}
		else if(State == STATE.GAME) {
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
				//player.setYVelocity(-moveSpeed);
				break;
			case KeyEvent.VK_DOWN:
				//player.move(0, moveSpeed);
				//player.setYVelocity(moveSpeed);
				break;
	
			default:
				//System.out.println("default switch case");
				break;
			}
		}
		else if(State == STATE.Help) {
			if(key == KeyEvent.VK_ESCAPE)
				State = STATE.MENU;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if(State == STATE.MENU) {
			
			return;
		}
		else if(State == STATE.GAME) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_RIGHT:
				player.setXVelocity(0);
				break;
			case KeyEvent.VK_LEFT:
				player.setXVelocity(0);
				break;
			case KeyEvent.VK_UP:
				//player.setYVelocity(0);
				break;
			case KeyEvent.VK_DOWN:
				//player.setYVelocity(0);
				break;
			case KeyEvent.VK_SPACE:
				player.fire();
				break;
			default:
				//System.out.println("default switch case");
				break;
			}
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
	
	public static void changeState(String s) {
		if(s.equals("Menu")) {
			State = STATE.MENU;
		}
		if(s.equals("Game")) {
			//init();
			State = STATE.GAME;
		}
		if(s.equals("Help"));
			State = STATE.Help;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(State == State.GAME)
			return;
		if(State == State.Help) {
			if(x >= 350 && x <= 450 && y >= 500 && y <= 550) {
				//Game.init = true;
				State = STATE.MENU;
			}
			return;
		}
		if(x >= 350 && x <= 450 && y >= 200 && y <= 250) {
			startNewGame();
			State = STATE.GAME;
		}
		if(x >= 350 && x <= 450 && y >= 300 && y <= 350) {
			State = STATE.Help;
		}
		if(x >= 350 && x <= 450 && y >= 400 && y <= 450) {
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}

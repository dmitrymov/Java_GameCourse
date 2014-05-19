import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyInput extends KeyAdapter{
	private Game game;
	
	public KeyInput(Game g) {
		game = g;
	}
	
	public void keyPressed(KeyEvent e) {
		game.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		game.keyReleased(e);
	}
	
}

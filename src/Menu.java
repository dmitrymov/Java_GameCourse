import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Menu{

	private Rectangle playButton = new Rectangle(350, 200, 100, 50);
	private Rectangle helpButton = new Rectangle(350, 300, 100, 50);
	private Rectangle quitButton = new Rectangle(350, 400, 100, 50);
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		Font font = new Font("arial", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Amazing shooting game:", 100, 100);
		font = new Font("arial", Font.BOLD, 30);
		g.setFont(font);
		g2.draw(playButton);
		g.drawString("Play", playButton.x + 20, playButton.y + 35);
		g2.draw(helpButton);
		g.drawString("Help", helpButton.x + 20, helpButton.y + 35);
		g2.draw(quitButton);
		g.drawString("Quit", quitButton.x + 20, quitButton.y + 35);
		
	}
}

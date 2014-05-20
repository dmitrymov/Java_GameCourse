import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Help {
	
	private Rectangle backButton = new Rectangle(350, 500, 100, 50);

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		Font font = new Font("arial", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Game help:", 100, 100);
		font = new Font("arial", Font.BOLD, 30);
		g.setFont(font);
		g.drawString("Key arrow left - move left", 100, 300);
		g.drawString("Key arrow right - move right", 100, 350);
		g.drawString("Key space - shoot bullet", 100, 400);
		g2.draw(backButton);
		g.drawString("Back", backButton.x + 20, backButton.y + 35);
	}
}

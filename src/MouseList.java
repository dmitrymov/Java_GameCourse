import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MouseList implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(Game.State == Game.State.GAME)
			return;
		if(Game.State == Game.State.Help) {
			if(x >= 350 && x <= 450 && y >= 500 && y <= 550) {
				//Game.init = true;
				Game.State = Game.STATE.MENU;
			}
			return;
		}
		if(x >= 350 && x <= 450 && y >= 200 && y <= 250) {
			Game.State = Game.STATE.GAME;
		}
		if(x >= 350 && x <= 450 && y >= 300 && y <= 350) {
			Game.State = Game.STATE.Help;
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

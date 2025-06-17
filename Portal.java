import java.awt.Color;
import java.awt.Graphics;


//portal one, entrance portal, is x1 y1 etc.
public class Portal {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int width;
	private int height;
	private boolean horPortal;
	private Color color;
	
	public Portal (int width, int height, int x1, int y1, int x2, int y2, boolean horPortal, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.width = width;
		this.height = height;
		this.horPortal = horPortal;
		this.color = color;
	}
	
	public int getX1 () {
		return x1;
	}
	public int getY1 () {
		return y1;
	}
	public int getX2 () {
		return x2;
	}
	public int getY2 () {
		return y2;
	}
	public int getWidth () {
		return width;
	}
	public int getHeight () {
		return height;
	}
	public boolean getHorizontal () {
		return horPortal;
	}
	
	public void drawPortalSet(Graphics g) {
		//0x245EB5 & 0x245EB5
        g.setColor(color);
        g.fillRect(x1, y1, width, height);
        g.setColor(color);
        g.fillRect(x2, y2, width, height);
    }
}

import java.awt.Color;
import java.awt.Graphics;

public class Moving_Block extends Block {
	
	// starting x, y, ending x, y, width, height,  
	
	private int width;
	private int height;
	private int x1; //starting x & y
	private int y1;
	private int xx = x1; //current x & y
	private int yy = y1;
	private int x2; //ending x & y
	private int y2;
	private double speed;
	private boolean moveHor;
	private Color color = new Color(0x4B4B4B);
	
	public Moving_Block () {
		width = 0;
		height = 0;
		x1 = 0;
		y1 = 0;
		xx = 0;
		yy = 0;
		x2 = 0;
		y2 = 0;
		speed = 0;
	}
	
	public Moving_Block (int width, int height, int x1, int y1, int xx, int yy, int x2, int y2, double speed, boolean moveHor) {
		super(width, height, xx, yy);
		this.width = width;
		this.height = height;
		this.x1 = x1;
		this.y1 = y1;
		this.xx = xx;
		this.yy = yy;
		this.x2 = x2;
		this.y2 = y2;
		this.speed = speed;
		this.moveHor = moveHor;
	}
	
	public void drawBlock(Graphics g) {
        g.setColor(color);
        g.fillRect(xx, yy, width, height);
    }
	
	public int getCurrentX() {
		return xx;
	}
	public int getCurrentY() {
		return yy;
	}
	public int getFinalX() {
		return x2;
	}
	public int getFinalY() {
		return y2;
	}
	public double getCurrentSpeed() {
		return speed;
	}
	
	public boolean isMovingBlock () {
		return true;
	}
	
	public boolean checkHor() {
		return moveHor;
	}
	
	public int moveBlock() {
		if (moveHor) {
			if (xx+speed>x2 || xx+speed<x1) speed = -speed;
			xx+=speed;
			return (int) Math.round(speed);
		} else {
			if (yy+speed>y2 || yy+speed<y1) speed = -speed;
			yy+=speed;
			return (int) Math.round(speed);
		}
	}
	
	public int getMove() {
	    if (moveHor) {
	        if (xx+speed > x2 || xx+speed < x1) {
	            return (int) Math.round(-speed);
	        } else {
	            return (int) Math.round(speed);
	        }
	    } else {
	        if (yy+speed > y2 || yy+speed < y1) {
	            return (int) Math.round(-speed);
	        } else {
	            return (int) Math.round(speed);
	        }
	    }
	}
	
}

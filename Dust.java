
import java.awt.Color;
import java.awt.Graphics;

public class Dust {
    private int width;
    private int height;
    private int x;
    private int y;
    private int alpha;
    private int stage;
    private Color color;
    
    public Dust() {
        width = 3;
        height = 3;
        x = 100;
        y = 100;
        alpha = 255;
        color = new Color(255, 255, 255, alpha);
    }
    
    public Dust(int x, int y) {
        this.width = 3;
        this.height = 3;
        this.x = x;
        this.y = y;
        alpha = 255;
        color = new Color(255, 255, 255, alpha);
    }
    
    public void drawDust(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    // getter methods
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void advanceStage() {
    	if (stage < 60) {
        	stage += 1;
    	}
    }
    
    public int getStage() {
    	return stage;
    }
    
    public int getAlpha() {
    	return alpha;
    }
    
    public void setX(int x) {
    	this.x = x;
    }
    public void setY(int y) {
    	this.y = y;
    }
    
    public void alphaAdjust() {
    	alpha = 255 - (stage * 5);
    	if (alpha < 0) alpha = 0;
    	color = new Color(255, 255, 255, alpha);
    }
}

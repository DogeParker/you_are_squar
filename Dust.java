
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
        width = 3;   // You can adjust the size as needed
        height = 3;
        x = 100;
        y = 100;
        alpha = 255;
        color = new Color(211, 47, 47, alpha); // Default block color
    }
    
    public Dust(int x, int y, Color color) {
        this.width = 3;
        this.height = 3;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public void drawDust(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    // getter methods
    public int getDustX() {
        return x;
    }
    
    public int getDustY() {
        return y;
    }
    
    public void advanceStage() {
    	if (stage < 20) {
        	stage+=1;
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
    	alpha = 255 - stage*13;
    	if (alpha < 0) alpha = 0;
    	color = new Color(211, 47, 47, alpha);
    }
}

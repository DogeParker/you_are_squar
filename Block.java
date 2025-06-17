import java.awt.Color;
import java.awt.Graphics;
 
public class Block {
    private boolean impassable;
    private boolean isIce;
    private boolean isBouncy;
    private boolean isQuicksand;
    private int width;
    private int height;
    private int x;
    private int y;
    private Color color;
 
    public Block() {
        impassable = true;
        width = 50;
        height = 50;
        x = 0;
        y = 0;
        color = new Color(0x4B4B4B);
        isQuicksand = false;
    }
    public Block(int width, int height, int x, int y) {
        this.impassable = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = new Color(0x4B4B4B);
        isQuicksand = false;
    }
    public Block(int width, int height, int x, int y, boolean q) {
        this.impassable = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = new Color(0x61605e);
        isQuicksand = true;
    }
    public Block(int width, int height, int x, int y, Color color) {
        this.impassable = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        isQuicksand = false;
    }
    public Block(int width, int height, int x, int y, boolean isIce, boolean isBouncy) {
        this.impassable = false;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        if (isIce) {
        	this.color = new Color(0x6DBAE3);
        } else if (isBouncy) {
        	this.color = new Color(0x3BF57C);
        }
        this.isBouncy = isBouncy;
        this.isIce = isIce;
        isQuicksand = false;
    }
 
    /* smooth portals would be cooler but imma be unhappy if
     * i am bothered to figure out the number of things i would have to change
     * 
     * dual rendering two seperate rects to represent each half of the player
     * dual rendering two seperate aimballs in case it goes in
     * --sidenote, did not end up rendering the aimball coming out the other side
     * for ease of coding, and also ease of use... it didnt seem player-friendly to have
     * the one thing that the user controls the player with darting around the screen
     * 
     * public Block(int width, int height, int x, int y, boolean isPortal, ) {
        this.impassable = false;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = new Color(0x6DBAE3);
        this.isIce = isIce;
        this.isBouncy = isBouncy;
    } */ 
 
    public void drawBlock(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
 
    // getter methods bub
    public int getBlockWidth() {
        return width;
    }
 
    public int getBlockHeight() {
        return height;
    }
 
    public int getBlockX() {
        return x;
    }
 
    public int getBlockY() {
        return y;
    }
 
    public void setBlockX(int n) {
    	x = n;
    }
 
    public void setBlockY(int n) {
    	y = n;
    }
 
    public boolean isIce() {
    	return isIce;
    }
 
    public boolean isBouncy() {
    	return isBouncy;
    }
 
    public boolean isImpassable() {
        return impassable;
    }
 
    public boolean isMovingBlock() {
    	return false;
    }
    public int moveBlock() {
    	return -100;
    }
    public int getCurrentX() {
    	return -1;
	}
	public int getCurrentY() {
		return -1;
	}
    public double getCurrentSpeed() {
    	return -69.0;
    }
    public int getMove() {
    	return -1;
    }
    public boolean checkHor() {
    	return false;
    }
    public boolean getQ() {
    	return isQuicksand;
    }
}

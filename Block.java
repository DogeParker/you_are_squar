
import java.awt.Color;
import java.awt.Graphics;

public class Block {
    private boolean impassable;
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
    }
    public Block(int width, int height, int x, int y) {
        this.impassable = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = new Color(0x4B4B4B);
    }
    public Block(int width, int height, int x, int y, Color color) {
        this.impassable = true;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public Block(int width, int height, int x, int y, Color color, boolean imp) {
        this.impassable = imp;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public void drawBlock(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    // getter methods
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
    
    public boolean isImpassable() {
        return impassable;
    }
}

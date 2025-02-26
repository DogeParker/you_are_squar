import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level1 {
    private ArrayList<Block> blocks;
    
    public Level1() {
        blocks = new ArrayList<>();
        
        // blocks are initialized (width, height, x_pos, y_pos, color, (can be set to false to remove collision))
        // sidenote for first time java graphics users... x_pos and y_pos are distance from left of window and distance from top of window
        blocks.add(new Block(50, 50, 300, 400, Color.GRAY));
        blocks.add(new Block(100, 200, 150, 500, Color.DARK_GRAY));
        blocks.add(new Block(60, 20, 10, 50, Color.CYAN));
        blocks.add(new Block(120, 20, 300, 175, Color.ORANGE));
        blocks.add(new Block(60, 20, 70, 300, Color.CYAN));
        blocks.add(new Block(20, 400, 750, 175, Color.CYAN));
        blocks.add(new Block(20, 400, 675, 175, Color.CYAN));

    }
    
    // draw the blocks into the level
    public void drawLevel(Graphics g) {
        for (Block b : blocks) {
            b.drawBlock(g);
        }
    }
    
    // get arraylist of block objects (used for collision detection)
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
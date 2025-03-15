

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level1 {
    // A helper inner class to tie a Block to its position
    private ArrayList<Block> blocks;
    
    public Level1() {
        blocks = new ArrayList<>();
        
        // Initialize blocks here. Adjust positions and block sizes/colors as needed.
        blocks.add(new Block(50, 50, 300, 300));
        blocks.add(new Block(200, 200, 150, 500));
        // Add more blocks for your level...
    }
    
    // Draw all blocks in the level
    public void drawLevel(Graphics g) {
        for (Block b : blocks) {
            b.drawBlock(g);
        }
    }
    
    // Provide a getter for blocks if collision detection is needed in Game
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
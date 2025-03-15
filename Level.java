

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level {
    // A helper inner class to tie a Block to its position
    private ArrayList<Dust> dusts;
    private ArrayList<Block> blocks;
    
    public Level() {
        dusts = new ArrayList<>();
        blocks = new ArrayList<>();
    }
    
    // draw everything in the level.. blocks and dust
    public void drawLevel(Graphics g) {
    	for (Block b : blocks) {
            b.drawBlock(g);
        }
        for (Dust b : dusts) {
            b.drawDust(g);
        }
    }
    //getter methods
    public int getListSize() {
    	return dusts.size();
    }
    
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    public ArrayList<Dust> getDusts() {
        return dusts;
    }
    
    public void addDust(Dust d) {
    	dusts.add(d);
    }
    
    public void addBlock(Block b) {
    	blocks.add(b);
    }
}
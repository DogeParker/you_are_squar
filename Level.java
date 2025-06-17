

import java.awt.Graphics;
import java.util.ArrayList;

public class Level {
    // helper inner class to tie block to its position
    private ArrayList<Dust> dusts;
    private ArrayList<Block> blocks;
    private ArrayList<Portal> portals;
    private ArrayList<Moving_Block> mBlocks;
    private double windStrength;
    
    public Level(double wind) {
        dusts = new ArrayList<>();
        blocks = new ArrayList<>();
        portals = new ArrayList<>();
        mBlocks = new ArrayList<>();
        windStrength = wind;
    }
    
    // draw everything in the level.. blocks and dust
    public void drawLevel(Graphics g) {
    	for (Block b : blocks) {
            b.drawBlock(g);
        }
        for (Dust b : dusts) {
            b.drawDust(g);
        }
        for (Portal b : portals) {
        	b.drawPortalSet(g);
        }
        for (Moving_Block b : mBlocks) {
        	b.drawBlock(g);
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
    
    public ArrayList<Portal> getPortals() {
        return portals;
    }
    
    public double getWind() {
    	return windStrength;
    }
    
    public void addDust(Dust d) {
    	dusts.add(d);
    }
    
    public void addBlock(Block b) {
    	blocks.add(b);
    }
    
    public void addMoveBlock(Moving_Block b) {
    	mBlocks.add(b);
    }
    
    public void addPortalSet(Portal p) {
    	portals.add(p);
    }
}



import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level2 {
    // A helper inner class to tie a Block to its position
    private ArrayList<Dust> dusts;
    
    public Level2() {
        dusts = new ArrayList<>();
        
        // Initialize blocks here. Adjust positions and block sizes/colors as needed.
        dusts.add(new Dust());
        dusts.add(new Dust(200, 200, Color.GRAY));
        // Add more blocks for your level...
    }
    
    // Draw all blocks in the level
    public void drawLevel(Graphics g) {
    	for (int i=0; i<dusts.size(); i++) {
    		if (dusts.get(i).getAlpha()==0) {
    			dusts.remove(i);
    			i--;
    		}
    	}
        for (Dust b : dusts) {
            b.drawDust(g);
        }
    }
    public int getListSize() {
    	return dusts.size();
    }
    
    // Provide a getter for blocks if collision detection is needed in Game
    public ArrayList<Dust> getDusts() {
        return dusts;
    }
}
import java.awt.Color;
import java.util.ArrayList;
 
public class LevelCreator {
	private ArrayList<Level> levelArray;
	public LevelCreator () {
		levelArray = new ArrayList<>();
 
		//BLOCKS
		//width, height, x, y
 
		//PORTALS
		//x1, y1, x2, y2, width, height
 
		//MOVING_BLOCKS
		//width, height, startingX, startingY, currentX, currentY, endingX, endingY, moveHorizontally
 
		//MTL mechanicTestLevel
		//Level MTL = new Level(0);
		//MTL.addBlock(new Moving_Block(100, 100, 400, 100, 400, 100, 500, 400, 2.0, false));
		//MTL.addBlock(new Moving_Block(100, 100, 100, 400, 100, 400, 500, 400, 2.0, true));
		//levelArray.add(MTL);
 
		//level #1 creation
		Level level1 = new Level(0);
		level1.addBlock(new Block(500, 500, 400, 300));
		level1.addBlock(new Block(300, 200, 150, 500));
		level1.addBlock(new Block(300, 400, 650, 100));
		levelArray.add(level1);
 
		Level level2 = new Level(0);
		level2.addBlock(new Block(500, 50, 500, 150));
		level2.addBlock(new Block(200, 50, 50, 300));
		level2.addBlock(new Block(200, 50, 300, 500));
		levelArray.add(level2);
 
		Level level2_ = new Level(0);
		level2_.addBlock(new Block(50, 500, 400, 450));
		level2_.addBlock(new Block(50, 100, 400, 200));
		level2_.addBlock(new Block(250, 50, 300, 200));
		level2_.addBlock(new Block(200, 50, 100, 500));
		level2_.addBlock(new Block(50, 50, 750, 150));
		levelArray.add(level2_);
 
		//level #2 creation
		Level level3 = new Level(1.75);
		level3.addBlock(new Block(50, 400, 50, 0));
		level3.addBlock(new Block(50, 200, 750, 0));
		level3.addBlock(new Block(200, 50, 50, 400));
		level3.addBlock(new Block(150, 50, 450, 500));
		level3.addBlock(new Block(200, 50, 600, 150));
		levelArray.add(level3);
 
		//level #3 creation
		Level level4 = new Level(-1.75);
		level4.addBlock(new Block(50, 400, 50, 500));
		level4.addBlock(new Block(100, 50, 50, 500));
		level4.addBlock(new Block(50, 800, 750, -50));
		level4.addBlock(new Block(150, 50, 250, 250));
		level4.addBlock(new Block(50, 50, 500, 250));
		level4.addBlock(new Block(150, 50, 650, 250));
		
		level4.addBlock(new Block(100, 50, 400, 250, true));
		level4.addBlock(new Block(100, 50, 550, 250, true));
 
		level4.addBlock(new Block(200, 50, 450, 500));
		level4.addBlock(new Block(600, 350, 0, -200));
		level4.addBlock(new Block(50, 50, 600, 100));
		levelArray.add(level4);
 
		Level level4_ = new Level(-1.75);
		level4_.addBlock(new Block(500, 50, 50, 250, true));
		level4_.addBlock(new Block(600, 200, 0, 500));
		level4_.addBlock(new Block(150, 50, 500, 250));
		level4_.addBlock(new Block(50, 500, 0, 250));
		level4_.addBlock(new Block(50, 500, 750, 500));
		level4_.addBlock(new Block(700, 50, 100, 100));
		
		level4_.addBlock(new Block(425, 50, 0, -35));
		level4_.addBlock(new Block(425, 50, 550, -35));
		levelArray.add(level4_);
 
		Level level5 = new Level(0);
		level5.addBlock(new Block(400,50,0,550));
		level5.addBlock(new Block(400,50,550,550));
		level5.addBlock(new Block(250,100,0,250));
		level5.addBlock(new Block(450,100,400,250));
		level5.addBlock(new Block(50,600,375,0));
		level5.addBlock(new Block(75,100,0,0));
		level5.addBlock(new Block(75,100,0,300));
		level5.addBlock(new Block(75,100,725,0));
		level5.addBlock(new Block(75,100,725,300));
		level5.addBlock(new Block(400,50,0,0));
		level5.addBlock(new Block(400,50,550,0));
		level5.addPortalSet(new Portal(75,150,725,400,0,400, true, new Color(0x673f8f)));
		level5.addPortalSet(new Portal(75,150,0,100,725,100, true, new Color(0x8a371c)));
		levelArray.add(level5);
 
		Level level6 = new Level(0);
		level6.addBlock(new Block(75,150,725,250));
		level6.addBlock(new Block(700,50,0,0));
		level6.addBlock(new Block(800,50,0,200));
		level6.addBlock(new Block(50,600,375,250));
		level6.addBlock(new Block(400,50,550,550));
		level6.addPortalSet(new Portal(75,150,725,400,0,50, true, new Color(0x673f8f)));
		levelArray.add(level6);
	}
 
	public Level getLevelAt(int index) {
		return levelArray.get(index);
	}
 
	public ArrayList<Level> getLevels() {
		return levelArray;
	}
 
}

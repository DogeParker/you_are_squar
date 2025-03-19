import java.awt.Color;
import java.util.ArrayList;

public class LevelCreator {
	private ArrayList<Level> levelArray;
	public LevelCreator () {
		levelArray = new ArrayList<>();
		
		//width, height, x, y
		
		//level #1 creation
		Level level1 = new Level(0);
		level1.addBlock(new Block(500, 500, 400, 300));
		level1.addBlock(new Block(300, 200, 150, 500));
		level1.addBlock(new Block(300, 400, 650, 100));
		levelArray.add(level1);
		
		Level level2 = new Level(0);
		level2.addBlock(new Block(200, 50, 500, 150));
		level2.addBlock(new Block(200, 50, 50, 300));
		level2.addBlock(new Block(200, 50, 300, 500));
		levelArray.add(level2);
		
		//level #2 creation
		Level level3 = new Level(2);
		level3.addBlock(new Block(50, 50, 350, 250));
		level3.addBlock(new Block(50, 400, 50, 0));
		level3.addBlock(new Block(50, 200, 750, 0));
		level3.addBlock(new Block(200, 50, 50, 400));
		level3.addBlock(new Block(150, 50, 450, 500));
		level3.addBlock(new Block(200, 50, 600, 150));
		levelArray.add(level3);
		
		//level #3 creation
		Level level4 = new Level(-2);
		level4.addBlock(new Block(50, 400, 50, 500));
		level4.addBlock(new Block(100, 50, 50, 500));
		level4.addBlock(new Block(50, 800, 750, -50));
		level4.addBlock(new Block(600, 50, 250, 250));
		level4.addBlock(new Block(200, 50, 450, 500));
		level4.addBlock(new Block(200, 50, 450, 100));
		levelArray.add(level4);
		
	}
	
	public Level getLevelAt(int ind) {
		return levelArray.get(ind);
	}
	
	public ArrayList<Level> getLevels() {
		return levelArray;
	}
	
}

import java.awt.Color;
import java.util.ArrayList;

public class LevelCreator {
	private ArrayList<Level> levelArray;
	public LevelCreator () {
		levelArray = new ArrayList<>();
		
		//level #1 creation
		Level level1 = new Level();
		level1.addBlock(new Block(50, 50, 300, 300));
		level1.addBlock(new Block(200, 200, 150, 500));
		levelArray.add(level1);
		
		//level #2 creation
		Level level2 = new Level();
		level2.addBlock(new Block(50, 50, 300, 300));
		level2.addBlock(new Block(200, 200, 150, 500));
		level2.addDust(new Dust());
		level2.addDust(new Dust(200, 200));
		levelArray.add(level2);
		
	}
	
	public Level getLevelAt(int ind) {
		return levelArray.get(ind);
	}
	
	public ArrayList<Level> getLevels() {
		return levelArray;
	}
	
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 
/*
 * Hi!
 * This is the demo for my game, you are squar.
 * 	This took far too long for what it is, which is just a simple 2d foddian platformer,
 * but I'm happy to have been able to make all of it in plain java, and the end result
 * looks clean enough.
 * 	The class structuring, and the way in which I designed many of the methods (see 
 * "drawPlayerInPortal") may appear unorthodox, and that's because it is. This was
 * not a challenge to myself to make a game with the cleanest code possible, but rather,
 * a game at all.
 * 	Thank you for playing! (and for reading the code)
 * -Kyle
 */



/*
 * Level mechanics
 * 	-NOT DONE! - Moving Blocks, that make you move along with them... make it so blocks move in path of player to possibly prevent player from falling to the beginning of section
 * 		-continuation of Moving_Block, variation that only moves when a player touches it, like in celeste kevin angry blocks
 * 	-DONE - Ice --- reduce friction while on blocks with ice flag (light blue)
 * 	-DONE - Wind --- subtract (or add) velocityX by some value, making it clear to the player which direction the wind is blowing? (gonna be hard to make it clear)
 *	-........ control wind with arrow keys for added controls complexity?
 * 	-DONE - Portals --- SMOOTH_PORTALS!1!1!! make blocks with portal flag teleport the player to another portal, different colors maybe? (blue - orange)
 * 	-DONE - Bouncy --- (lime)
 * 	-Make player invisible (or maybe just harder to see) when passing through certain passable blocks
 *  -Things i dont like:
 *  	make portal collision method to cut down on if (playerx awjdhnakdb) stuff thats redundant
 *  	FIXED - bounce and slime work seperately but not combined, bounce/ice has no hor. friction at all
 *  	was at one point a weird aimball bug where it could glitch into the screen barrier :/
 *  	some areas that are intended to be unskippable... are skippable :/
 *  	i feel like the code is grossly unoptimized, so i should reeealy do a runthrough of every check occuring in main update loop, and see if its really neccesary
 *  	the aimball does not consistently move left without player intervention... thats a bug for sure
 *  	FIXED... unfixed, was not bug, was 100% needed for code to run properly - when on a block, velocityY oscillates between 0 and 0.3 :/
 * 		FIXED - sometimes velocityY and i think velocityX are reset when changing levels huh?
 */
 
public class Game extends JPanel implements KeyListener, Runnable {
	//java throws an error if i dont include this, eclipse is mid for that ong
	private static final long serialVersionUID = 1L;
 
	private boolean showInstructions = true;
	private boolean levelViewerMode = false;
	private LevelCreator levelCreator = new LevelCreator();
	private int currentLevelIndex = 0;
	private Level currentLevel = levelCreator.getLevelAt(currentLevelIndex);
	private boolean changeLevelUp;
	private boolean changeLevelDown;
 
	private final int SCREEN_WIDTH = 800;  // window width
    private final int SCREEN_HEIGHT = 600; // window height
 
    //hard-coded player values
    private double playerX = 100; // player x position ... default 50
    private double playerY = 300; // player y position ... default 300
    private int width = 50; // player width
    private int height = 50; // player height
 
    //player variables for movement
    private boolean onGround = false;
    private boolean onIce = false;
    private boolean holdingRight = false;
 
    //private boolean holdingLeft = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double gravity = 0.3; //defining gravity
 
    //aimBall variables
    private boolean aimMode = false; // true when controlling aimBall angle
    private boolean aimLocked = false; // true when controlling aimBall radius
    private boolean aimBallPosInvalid = false;
    private boolean comingFromRight = true;
    private int aimBallX = (int) Math.round(playerX) + 15;
    private int aimBallY = (int) Math.round(playerY) + (height / 2) - 100;
    private int aimAngle = 90;
    private int aimRadius = 50;
    private double aimSpeed = 1;
    private boolean shoot = false; // true for ~1 frame after pressing space to assign y and x velocities
 
    int upperBound; // upper bound for aim controls
	int lowerBound; // lower bound for aim controls
	int increment; // what is increasing during update (distance from player or radius around player)
	int outerAimBallBounds; // used to get the position at which aimball hits bounds and assign upper/lower bounds to that
 
	//for wind
	double windStrength = currentLevel.getWind();
	boolean playerControlledWind;
	double ROCofWind = 0;
	private int dustSpawnCounter = 0; // counter to control dust spawn rate
 
	//for portals
	private boolean reconstructedRight = false;
	private boolean reconstructedBottom = false;
	private boolean reconstructedLeft = false;
	private boolean reconstructedTop = false;
	private int otherXPortal;
	private int thisXPortal;
	private int otherYPortal;
	private int thisYPortal;
	private int portalHeightDiff = 0;
	private int portalDistanceDiff = 0;
 
	//for moving blocks... which are unused in the demo but ¯\_(ツ)_/¯
	private boolean onMovingBlock = false;
	private boolean movingBlockHor = false;
	private int movingInParallel = 0;
 
	//should return 1 if OOB (out of bounds) from the right, 2 if OOB from the left, and return 0 if aimball doesn't go OOB
    public int checkAimOutOfBounds() {
    	int tempBallX;
    	int aimBallBounds;
    	upperBound = 180;
		lowerBound = 0;
		increment = aimAngle;
 
		aimBallBounds = lowerBound;
		tempBallX = (int) Math.round(playerX) + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
		if (tempBallX > SCREEN_WIDTH - 20) {
			aimBallBounds = upperBound;
			while (aimBallBounds > lowerBound) {
	    		tempBallX = (int) Math.round(playerX) + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
	    		if (tempBallX > SCREEN_WIDTH - 20) {
	    			outerAimBallBounds = aimBallBounds;
	    			return 2;
	    		}
	    		aimBallBounds--; 
	    	}
	    	return 0;
		}
 
		//runs through every possible degree of angle to check if it goes OOB	
    	while (aimBallBounds < upperBound) {
    		tempBallX = (int) Math.round(playerX) + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
    		if (tempBallX < 0) {
    			//System.out.println("Out of bounds detected: " + tempBallX);
    			outerAimBallBounds = aimBallBounds;
    			return 1;
    		} else if (tempBallX > SCREEN_WIDTH - width) {
    			outerAimBallBounds = aimBallBounds;
    			return 2;
    		}
    		aimBallBounds++; 
    	}
    	return 0;
    }
 
    public void angleAndRadiusOscillation () {
    	if (aimLocked) {
    		upperBound = 150;
    		lowerBound = 50;
    		increment = aimRadius;
    	} else {
    		upperBound = 180;
    		lowerBound = 0;
    		increment = aimAngle;
    	}
 
    	//System.out.println(aimAngle);
 
    	if (aimBallPosInvalid && aimMode) {
    		if (comingFromRight) {
    			upperBound = outerAimBallBounds;
    			lowerBound = 0;
    		}
    		else if (comingFromRight == false) {
    			upperBound = 180;
    			lowerBound = outerAimBallBounds;
    		}
    	}
 
    	// recalculate aimBall position based on corrected aimAngle
    	aimBallX = (int) Math.round(playerX) + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
    	aimBallY = (int) Math.round(playerY) + (height / 2) - 10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
 
    	if (aimLocked && aimBallX < 0) {
    		aimBallX = 0;
    	} else if (aimLocked && aimBallX > SCREEN_WIDTH - 20) {
    		aimBallX = SCREEN_WIDTH - 20;
    	}
 
    	// limiting range of aimBall
    	if (increment > upperBound) {
    		aimSpeed = -aimSpeed;
    	} else if (increment < lowerBound) {
    		aimSpeed = -aimSpeed;
    	}
 
    	// incrementing aimBall movement
    	if (aimMode) {
    		aimAngle += aimSpeed;
    	} else {
    		aimRadius += aimSpeed;
    	}
    }
 
    public void spawnDusts () {
    	int rnX;
		int rnY;
	    for (int i = 0; i < currentLevel.getDusts().size(); i++) {
	        if (currentLevel.getDusts().get(i).getAlpha() == 0) {
	            currentLevel.getDusts().remove(i);
	            i--;
	        }
	    }
	    for (Dust i : currentLevel.getDusts()) {
	        i.advanceStage();
	        i.alphaAdjust();
	        //movement for particles, particle physics if you will
	        i.setX((int) Math.round(i.getX() + windStrength * 5));
	        //i.setY((int) Math.round(i.getY() + 1 + i.getStage() * .05));
	        //i.setY((int) Math.round(i.getY()+.5));
	    }
	    if (windStrength < 2 && windStrength > -2) {
	    	windStrength += ROCofWind;
	    }
 
	    // higher == dust particles less frequently
	    dustSpawnCounter++;
	    if (dustSpawnCounter >= 12) {
	        rnX = (int) (Math.random() * (SCREEN_WIDTH - 20));
	        rnY = (int) (Math.random() * (SCREEN_HEIGHT + 1));
	        currentLevel.addDust(new Dust(rnX, rnY));
	        dustSpawnCounter = 0;
	    }
    }
 
    int jg = 0;
 
    public void blockDetection () {
    	for (Block i: currentLevel.getBlocks()) { //parallel
    		if (i.isMovingBlock()) {
                i.moveBlock();
                if (i.checkHor()) {
                	i.setBlockX(i.getCurrentX());
                } else {
                	i.setBlockY(i.getCurrentY());
                }
            }
	    	int bX = i.getBlockX();
	    	int bY = i.getBlockY();
	    	int bWidth = i.getBlockWidth();
	    	int bHeight = i.getBlockHeight();
	    	if (playerX < bX + bWidth && playerX + width > bX && playerY < bY + bHeight && playerY + height > bY) {
	    	    double overlapLeft = (playerX + width) - bX; // difference between far right of player and far left of block
	    	    double overlapRight = (bX + bWidth) - playerX; // difference between far right of block and far left of player
	    	    double overlapTop = (playerY + height) - bY; // difference between yPos of player bottom to top of block
	    	    double overlapBottom = (bY + bHeight) - playerY; // difference between yPos of block bottom to top of player
 
	    	    // find smallest overlap
	    	    double minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));
 
	    	    //player on top of block
	    	    // holup i think that it only runs overlap once b/c it was previously constantly gett... nvm forgot to finish this comment
	    	    movingInParallel = 0;
	    	    if (minOverlap == overlapTop && !(i.getQ())) {
		            playerY = bY - height;
		            onGround = true;
		            if (velocityY > 0) {
		            	//check for bouncy block
		            	if (i.isBouncy()) {
		            		onGround = false;
		            		if (Math.abs(velocityY)<1.5) {
		            			velocityY = 0;
		            			onGround = true;
		            		} else {
		            			velocityY = -velocityY*0.3;
		            		}
		            	} else {
		            		velocityY = 0.3;
		            	}
		            }
		            //check for ice block
		            if (i.isIce()) {
		            	onIce = true;
		            } else {
		            	onIce = false;
		            }
		            if (i.isMovingBlock()) {
		            	onMovingBlock = true;
		            	System.out.println(i.getMove());
		            	movingInParallel = i.getMove();
		            }
	    	    } else {
	    	    	onMovingBlock = false;
	    	    	onGround = false;
	    	    	onIce = false;
	    	    }
 
 
	    	    if (minOverlap == overlapBottom) {
	                if (i.getQ()) {
	                    if (velocityY < 0) {
	                        playerY = bY + bHeight;
	                        velocityY = 0.3;
	                    }
	                } else {
	                    playerY = bY + bHeight;
	                    if (velocityY < 0) {
	                        velocityY = 0.3;
	                    }
	                }
	            }  if (minOverlap == overlapLeft/*&& velocityX < or whatever if sumthing goes wrong */) {
	    	        playerX = bX - width;
	    	        velocityX = -velocityX*0.5;
	    	    } if (minOverlap == overlapRight) {
	    	        playerX = bX + bWidth;
	    	        velocityX = -velocityX*0.5;
	    	    }
	    	}
	    }
    }
 
    public double percentPlayerInPortal() {
    	for (Portal i: currentLevel.getPortals()) {
	    	int bX = i.getX1();
	    	int bY = i.getY1();
	    	int b2X = i.getX2();
	    	int b2Y = i.getY2();
	    	int bWidth = i.getWidth();
	    	int bHeight = i.getHeight();
	    	boolean pHor = i.getHorizontal();
	    	if (playerX < bX + bWidth && playerX + width > bX && playerY < bY + bHeight && playerY + height > bY) {
	    		return overlapForPortals(bX, bY, b2X, b2Y, bHeight, bWidth, pHor);
	    	} else if (playerX < b2X + bWidth && playerX + width > b2X && playerY < b2Y + bHeight && playerY + height > b2Y) {
	    		return overlapForPortals(b2X, b2Y, bX, bY, bHeight, bWidth, pHor);
    		}
    	}
    	return -1.0;
    }
 
    public double overlapForPortals (int x1, int y1, int x2, int y2, int hei, int wid, boolean hor) {
    	double overlapLeft = (playerX + width) - x1;;
    	double overlapRight = (x1 + wid) - playerX;
    	double overlapTop = (playerY + height) - y1;;// = (playerY + height) - bY; difference between yPos of player bottom to top of block
	    double overlapBottom = (y1 + hei) - playerY;// = (bY + bHeight) - playerY; difference between yPos of block bottom to top of player
	    double overlap;
	    if (hor) {
	    	overlap = Math.min(overlapLeft, overlapRight);
	    } else {
	    	overlap = Math.min(overlapTop, overlapBottom);
	    }
		portalHeightDiff = y2 - y1;
		portalDistanceDiff = x2 - x1;
		if (hor) {
			if (overlap == overlapLeft) {
				otherXPortal = x2 + wid;
				reconstructedRight = true;
			} else if (overlap == overlapRight) {
				thisXPortal = x1 + wid;
				otherXPortal = x2;
				reconstructedLeft = true;
			}
		} else { 
			if (overlap == overlapTop) {
				otherYPortal = y2 + hei;
				reconstructedBottom = true;
			} else if (overlap == overlapBottom) {
				thisYPortal = y1 + hei;
				otherYPortal = y2;
				reconstructedTop = true;
			}
		}
		return overlap/width;
    }
 
    public void drawPlayerInPortal (Graphics in) {//just gonna say it this feels like some yandev ahh coding
    	if (percentPlayerInPortal() <= 0.99) {
    		if (reconstructedRight) {
    			in.fillRect((int) Math.round(playerX), (int) Math.round(playerY), (int) Math.round((1-percentPlayerInPortal())*width), height);
    			in.fillRect((int) otherXPortal, (int) Math.round(playerY+portalHeightDiff), (int) Math.round((percentPlayerInPortal())*width), height);
    		} else if (reconstructedLeft) {
    			in.fillRect((int) Math.round(thisXPortal), (int) Math.round(playerY), (int) Math.round((1-percentPlayerInPortal())*width), height);
    			in.fillRect((int) otherXPortal - (int) Math.round((percentPlayerInPortal())*width), (int) Math.round(playerY+portalHeightDiff), (int) Math.round((percentPlayerInPortal())*width), height);
    		}/*TODO theres an issue when testing portal limits, probably occurs here... when drawing the stuff i think because horizontal
    		 is fine and the code for calculating overlaps is literally just vertical mirroring hor. using the same method :/*/ 
    		else if (reconstructedTop) {
    			in.fillRect((int) Math.round(playerX), (int) Math.round(thisYPortal), width, (int) Math.round((1-percentPlayerInPortal())*height));
    			in.fillRect((int) Math.round(playerX+portalDistanceDiff), (int) otherYPortal - (int) Math.round((percentPlayerInPortal())*height), width, (int) Math.round((percentPlayerInPortal())*height));
    		} else if (reconstructedBottom) {
    			in.fillRect((int) Math.round(playerX), (int) Math.round(playerY), width, (int) Math.round((1-percentPlayerInPortal())*height));
    			in.fillRect((int) Math.round(playerX+portalDistanceDiff), (int) otherYPortal, width, (int) Math.round((percentPlayerInPortal())*height));
    		}
    	} else {
    		if (reconstructedLeft) {
    			playerX = otherXPortal - width;
    			playerY += portalHeightDiff;
    			reconstructedLeft = false;
    		} else if (reconstructedRight) {
    			playerX = otherXPortal;
    			playerY += portalHeightDiff;
    			reconstructedRight = false;
    		} else if (reconstructedBottom) {
    			playerX += portalDistanceDiff;
    			playerY = otherYPortal;
    			reconstructedBottom = false;
    		} else if (reconstructedTop) {
    			playerX += portalDistanceDiff;
    			playerY = otherYPortal - height;
    			reconstructedTop = false;
    		}
    		in.fillRect((int) Math.round(playerX), (int) Math.round(playerY), width, height);
    	}
    }
 
    public Game() { // game loop (bruh)
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        new Thread(this).start();
    }
 
    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16);  //default 16 runs at roughly 60fps, 1000ms/60fps = 16ms per frame
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    double temp;
	public void update() {
		if (!(onGround)) {
			onMovingBlock = false;
		}
 
		if (onMovingBlock) {
			if (movingBlockHor) {
				playerX += movingInParallel;
			}
			else {
				playerY += movingInParallel;
			}
	    }
 
	    //dust! :)
		windStrength = currentLevel.getWind();
		if (windStrength != 0) {
			spawnDusts();
		}
 
		//change level check
		if (changeLevelUp) {
			playerY = SCREEN_HEIGHT-height-1;
			changeLevelUp = false;
		} else if (changeLevelDown) {
			playerY = 1;
			changeLevelDown = false;
		}
 
		// prevents unnecessary calculations on velocityX (ie slowing hor. movement down to 0.0000000000000001 and beyond is superfluous)
		if (-0.01<velocityX && velocityX<0.01) {
			velocityX = 0;
		}
 
		// apply friction when in contact with ground
		if (onGround) {
			if (onIce) {
				velocityX *= 0.965;
			} else {
				velocityX *= 0.82;
			}
		} //apply when in air
		else {
			velocityX *= 0.999;
			velocityX += windStrength*.05;
		}
 
	    // move right velocityX pixels (as long as velocityX is not zero)
		if (velocityX != 0&&!(levelViewerMode)) {
			playerX += Math.round(velocityX*10.0)/10.0;
		}
 
		// collision detection in general
	    if (playerX < 0) {
	        playerX = 0;
	        velocityX = 0; // Stop movement at the left wall
	    }
	    if (playerX + width > SCREEN_WIDTH) {
	        playerX = SCREEN_WIDTH - width;
	        velocityX = 0; // Stop movement at the right wall
	    }
 
	    aimBallX = (int) Math.round(playerX) + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
		aimBallY = (int) Math.round(playerY) + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
 
		//if trying to engage aiming while in air, prevent player from doing so
	    if (!(onGround) && aimMode) {
	    	aimMode = false;
	    } //main aiming loop
	    else if (onGround && aimMode || onGround && aimLocked) {
	    	angleAndRadiusOscillation();
	    }
	    //shoot player towards aimball
	    else if (shoot) {
	        	velocityX += Math.round((aimRadius * (Math.cos(Math.toRadians(aimAngle)))*0.1)*10.0)/10.0;
	        	velocityY += Math.round((-aimRadius * (Math.sin(Math.toRadians(aimAngle)))*0.1)*10.0)/10.0;
	        	onGround = false;
	        	shoot = false;
	        	aimAngle = 90;
	        	aimRadius = 50;
	        	if (aimBallPosInvalid && comingFromRight) {
	        		aimSpeed = -1;
	        	} else if (aimBallPosInvalid){
	        		aimSpeed = 1;
	        	}
	            aimBallPosInvalid = false;
	    }
 
	    // applying gravity
	    if (!(levelViewerMode)) playerY += (double) velocityY;
	    // controls slowing down of jump and downwards falling
	    // wait blud was this just happening regardless of onGround flag what the sigma???
	    // wait bub ur stupid this is the only thing keeping player from looney tunes ahh hahahahooeying onto an invisible platform, bc it needs top collision to detect if its still on a block :(
	    if (Math.abs(velocityY) < 30.0) {
	    	velocityY += gravity;
	    }
 
	    // floor detection
	    if (currentLevelIndex == 0 && playerY + height - 3 >= SCREEN_HEIGHT) {
	        playerY = SCREEN_HEIGHT - height + 3;
	        velocityY = 0;
	        onGround = true;
	    }
 
	    //collision detection with blocks &&&&& if on movingblock move player by X
	    blockDetection();
 
	    // as long as not in aimMode or in aimLocked, if the left or right arrow keys are pressed, aimBall starts oscillating in that direction
	    if (!(aimMode) && !(aimLocked) && holdingRight) {
	    	aimSpeed = -1;
	    } else if (!(aimMode) && !(aimLocked)) {
	    	aimSpeed = 1;
	    }
 
	    //check for level change
	    if (playerY<=-height) {
	    	if (currentLevelIndex == levelCreator.getLevels().size()-1) {
	    		System.out.println("you win good job");
				System.exit(0);
	    	}
			currentLevelIndex++;
			currentLevel = levelCreator.getLevelAt(currentLevelIndex);
			changeLevelUp = true;
		} if (playerY>=SCREEN_HEIGHT-height && currentLevelIndex!=0) {
			if (currentLevelIndex == 5 && playerX < 425) {
				currentLevelIndex-=4;
				currentLevel = levelCreator.getLevelAt(currentLevelIndex);
				changeLevelDown = true;
			} else {
				currentLevelIndex--;
				currentLevel = levelCreator.getLevelAt(currentLevelIndex);
				changeLevelDown = true;
			}
		}
	}
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0x2E2E2E));
        levelCreator.getLevelAt(currentLevelIndex).drawLevel(g);
        // draw aimBall when controlling angle or strength
        if (!(levelViewerMode)) {
	        if ((aimMode||aimLocked)&&onGround) {
	        	g.setColor(new Color(0xD6D6D6));
	        	g.fillOval(aimBallX, aimBallY, 20, 20);
	        }
	        // set player color
	        g.setColor(new Color(0xD72638));
	        // if percent is -1, player is not in any portal
	        if (percentPlayerInPortal() == -1) {
	        	g.fillRect((int) Math.round(playerX), (int) Math.round(playerY), width, height);
	        } // drawing player in portal when percent isnt -1
	        else {
	        	drawPlayerInPortal(g);
	        }
	        if (showInstructions) {
	            g.setColor(Color.WHITE);
	            g.setFont(new Font("Arial", Font.BOLD, 16));
	            String line1 = "Press SPACE to aim and shoot!";
	            String line2 = "Get to the top to win!";
	            FontMetrics fm = g.getFontMetrics();
	            int line1Width = fm.stringWidth(line1);
	            int line2Width = fm.stringWidth(line2);
	            int line1X = (int) Math.round(playerX) + (width / 2) - (line1Width / 2);
	            int line2X = (int) Math.round(playerX) + (width / 2) - (line2Width / 2);
	            int textY = (int) Math.round(playerY) - 30;
	            g.drawString(line1, line1X, textY);
	            g.drawString(line2, line2X, textY + 18);
	        }
        }
    }
 
    @Override
    public void keyPressed(KeyEvent e) {
    	// one button is light work no reaction
    	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
    		if (showInstructions) {
                showInstructions = false;
            }
    	    if (aimLocked) {
    	        aimLocked = false;
    	        shoot = true;
    	        aimBallPosInvalid = false;
    	    } else {
    	    	if (checkAimOutOfBounds() == 1) {
    	    		aimBallPosInvalid = true;
    	    		comingFromRight = true;
    	    	} else if (checkAimOutOfBounds() == 2) {
    	    		aimBallPosInvalid = true;
    	    		comingFromRight = false;
    	    	}
    	        aimMode = true;
    	    }
    	    repaint();
    	}
 
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    		holdingRight = true;
    		if (playerControlledWind) {
    			ROCofWind = 0.2;
    			windStrength += ROCofWind;
    		}
    	}
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		//holdingLeft = true;
    		if (playerControlledWind) {
    			ROCofWind = -0.2;
    			windStrength += ROCofWind;
    		}
    	}
    }
 
    @Override
    public void keyReleased(KeyEvent e) {
        if (aimMode && e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// lock position in place, and from here on only adjust power
        	aimLocked = true;
        	aimMode = false;
        }
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) { 
    		holdingRight = false;
    		if (levelViewerMode&&currentLevelIndex<levelCreator.getLevels().size()-1) {
    			currentLevelIndex++;
    			currentLevel = levelCreator.getLevelAt(currentLevelIndex);
    		}
    	}
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		//holdingLeft = false;
    		if (levelViewerMode&&currentLevelIndex>0) {
    			currentLevelIndex--;
    			currentLevel = levelCreator.getLevelAt(currentLevelIndex);
    		}
    	}
    }
 
    @Override
    public void keyTyped(KeyEvent e) {} // no clue why i absolutely need this for the game to run properly bruh
 
    public static void main(String[] args) {
        JFrame frame = new JFrame("you are squar");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

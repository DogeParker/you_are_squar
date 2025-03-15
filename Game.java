import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Level mechanics
 * 	-Ice --- reduce friction while on blocks with ice flag (light blue)
 * 	-Wind --- subtract (or add) velocityX by some value, making it clear to the player which direction the wind is blowing? (gonna be hard to make it clear)
 *	-........ control wind with arrow keys for added controls complexity?
 * 	-Portals --- make blocks with portal flag teleport the player to another portal, different colors maybe? (blue - orange)
 * 	-Bouncy --- (lime)
 * 	-dunno
 */

public class Game extends JPanel implements KeyListener, Runnable {
	private Level1 level1 = new Level1();
	private Level2 level2 = new Level2();
	private final int SCREEN_WIDTH = 800;  // window width
    private final int SCREEN_HEIGHT = 600; // window height
    
    //hardcoded player values
    private int playerX = 50; // player x position
    private int playerY = 300; // player y position
    private int width = 50; // player width
    private int height = 50; // player height
    
    //player variables for movement
    private boolean onGround = false;
    private boolean holdingRight = false;
    private boolean holdingLeft = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double gravity = 0.3; //defining gravity
    /*private double friction = 0.045; // friction in air
    private double gFriction = 0.7; // friction when on ground*/
    
    //aimBall variables
    private boolean aimMode = false; // true when controlling aimBall angle
    private boolean aimLocked = false; // true when controlling aimBall radius
    private boolean aimBallPosInvalid = false;
    private boolean comingFromRight = true;
    private int aimBallX = playerX + 15;
    private int aimBallY = playerY + (height / 2) - 100;
    private int aimAngle = 90;
    private int aimRadius = 50;
    private double aimSpeed = 1;
    private boolean shoot = false; // true for ~1 frame after pressing space to assign y and x velocities

    int upperBound; //upper bound for aim controls
	int lowerBound; //lower bound for aim controls
	int increment; //what is increasing during update (distance from player or radius around player)
	int outerAimBallBounds; // used to get the position at which aimball hits bounds and assign upper/lower bounds to that
    
	// for wind
	double windStrength = 1;
	
	//should return 1 if OOB (out of bounds) from the right, 2 if OOB from the left, and return 0 if aimball doesn't go OOB
    public int checkAimOutOfBounds() {
    	int tempBallX;
    	int aimBallBounds;
    	upperBound = 180;
		lowerBound = 0;
		increment = aimAngle;
		
		aimBallBounds = lowerBound;
		tempBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
		if (tempBallX > SCREEN_WIDTH - 20) {
			aimBallBounds = upperBound;
			while (aimBallBounds > lowerBound) {
	    		tempBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
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
    		tempBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
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
    
    public Game() { // game loop (no clue)
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
                Thread.sleep(16);  // runs at roughly 60fps, 1000ms/60fps = 16ms per frame
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
double temp;
double diff;
double diff2;
    public void update() {
    	// prevents unnecessary calculations on velocityX (ie slowing hor. movement down to 0.0000000000000001 and beyond is superfluous)
    	if (-0.001<velocityX && velocityX<0.001) {
    		velocityX = 0;
    	}
    	
    	// apply friction when in contact with ground or otherwise
    	if (onGround) {
    		velocityX *= 0.85;
    	} else {
    		velocityX *= 0.999;
    		//velocityX -= windStrength;
    	}
    	diff2 = diff - (velocityX-temp);
    	
        // move right velocityX pixels (as long as velocityX is not zero)
    	temp = playerX;
    	if (velocityX != 0) playerX += (int) Math.round(velocityX);
        // collision detection in general
        if (playerX < 0) {
            playerX = 0;
            velocityX = 0; // Stop movement at the left wall
        }
        if (playerX + width > SCREEN_WIDTH) {
            playerX = SCREEN_WIDTH - width;
            velocityX = 0; // Stop movement at the right wall
        }
        
        // floor detection
        if (playerY + height >= SCREEN_HEIGHT) {
            playerY = SCREEN_HEIGHT - height;
            velocityY = 0;
            onGround = true;
        }
        
        aimBallX = playerX + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
    	aimBallY = playerY + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
    	
    	//if trying to engage aiming while in air, prevent player from doing so
        if (!(onGround) && aimMode) {
        	aimMode = false;
        } //main aiming loop
        else if (onGround && aimMode || onGround && aimLocked) {
        	
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
        	aimBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = playerY + (height / 2) - 10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	
        	if (aimLocked && aimBallX < 0) {
        		aimBallX = 0;
        	} else if (aimLocked && aimBallX > SCREEN_WIDTH-20) {
        		aimBallX = SCREEN_WIDTH-20;
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
        } if (shoot) {
        	velocityX += Math.round(aimRadius * (Math.cos(Math.toRadians(aimAngle)))*0.1);
        	velocityY += Math.round(-aimRadius * (Math.sin(Math.toRadians(aimAngle)))*0.1);
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
        playerY += (int) Math.round(velocityY);
        if (playerY == 0) velocityY=0;
        // controls slowing down of jump and downwards falling
        velocityY += gravity;
        
        //collision detection with blocks
        for (Block i: level1.getBlocks()) {
        	int bX = i.getBlockX();
        	int bY = i.getBlockY();
        	int bWidth = i.getBlockWidth();
        	int bHeight = i.getBlockHeight();
        	
        	if (playerX < bX + bWidth && playerX + width > bX && playerY < bY + bHeight && playerY + height > bY) {
        		// difference between far right of player and far left of block
        	    int overlapLeft = (playerX + width) - bX;
        	    // difference between far right of block and far left of player
        	    int overlapRight = (bX + bWidth) - playerX;
        	    // difference between yPos of player bottom to top of block
        	    int overlapTop = (playerY + height) - bY;
        	    // difference between yPos of block bottom to top of player
        	    int overlapBottom = (bY + bHeight) - playerY;
        	    
        	    // find smallest overlap
        	    int minOverlap = Math.min(Math.min(overlapLeft, overlapRight),Math.min(overlapTop, overlapBottom));
        	    
        	    // push player out of block from side with least overlap
        	    if (minOverlap == overlapTop) {
    	            playerY = bY - height;
    	            velocityY = 0;
    	            onGround = true;   
        	    } else if (minOverlap == overlapBottom) {
        	        playerY = bY + bHeight;
        	        velocityY = 0;
        	    } else if (minOverlap == overlapLeft) {
        	        playerX = bX - width;
        	        velocityX = -velocityX*0.5;
        	    } else if (minOverlap == overlapRight) {
        	        playerX = bX + bWidth;
        	        velocityX = -velocityX*0.5;
        	    }
        	}	
        }
        
        //as long as not in aimMode or in aimLocked, if the left or right arrow keys are pressed, aimBall starts oscillating in that direction
        if (!(aimMode) && !(aimLocked) && holdingLeft) {
        	aimSpeed = 1;
        } else if (!(aimMode) && !(aimLocked) && holdingRight) {
        	aimSpeed = -1;
        }
        
        //dust! :)
        for (Dust i: level2.getDusts()) {
        	i.advanceStage();
        	i.alphaAdjust();
        }
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(0x2E2E2E));
        level2.drawLevel(g);
        // draw aimBall when controlling angle or strength
        if (aimMode||aimLocked) {
        	g.setColor(new Color(0xd6d6d6));
        	g.fillOval(aimBallX, aimBallY, 20, 20);
        }
        // draw player
        g.setColor(new Color(0xD72638));
        g.fillRect(playerX, playerY, width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	// one button is light work no reaction
    	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
    	
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		holdingLeft = true;
    	}
    	
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    		holdingRight = true;
    	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (aimMode && e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// lock position in place, and from here on only adjust power
        	aimLocked = true;
        	aimMode = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		holdingLeft = false;
    	}
    	
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    		holdingRight = false;
    	}
    }

    @Override
    public void keyTyped(KeyEvent e) {} // no clue

    public static void main(String[] args) {
        JFrame frame = new JFrame("you are squar");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
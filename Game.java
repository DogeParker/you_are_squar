import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {
    private Level1 level1 = new Level1();
    private final int SCREEN_WIDTH = 800;  // window width
    private final int SCREEN_HEIGHT = 600; // window height
    
    //hardcoded player values
    private int playerX = 50; // player x position
    private int playerY = 300; // player y position
    private int width = 50; // player width
    private int height = 50; // player height
    
    //player variables for movement
    private boolean onGround = false;
    private double velocityX = 0;
    private double velocityY = 0;
    private double gravity = 0.3; //defining gravity
    /*private double friction = 0.045; // friction in air
    private double gFriction = 0.7; // friction when on ground*/
    
    //aimBall variables
    private boolean aimMode = false; // true when controlling aimBall angle
    private boolean aimLocked = false; // true when controlling aimBall radius
    private boolean aimBallPosInvalid = false;
    private boolean comingFromRight;
    private int aimBallX = playerX + 15;
    private int aimBallY = playerY + (height / 2) - 100;
    private int aimAngle = 180;
    private int aimRadius = 50;
    private double aimSpeed = 1;
    private boolean shoot = false; // true for ~1 frame after pressing space to assign y and x velocities

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

    public void update() {
    	// apply friction when in contact with ground or otherwise
    	if (onGround) {
    		velocityX *= 0.9;
    	} else {
    		velocityX *= 0.99;
    	}

        // move right velocityX pixels
        playerX += (int) Math.round(velocityX);

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
        // jump force upward code
    	int upperBound; //upper bound for aim controls
    	int lowerBound; //lower bound for aim controls
    	int increment; //what is increasing during update (distance from player or radius around player)
    	int aimBallBounds;
    	
    	
        if (!(onGround) && aimMode) {
        	aimMode = false;
        } else if (onGround && aimMode || onGround && aimLocked) {
        	//run check to see if aimball pos is ever at -1
        	upperBound = 180;
    		lowerBound = 0;
    		increment = aimAngle;
    		
    		aimBallBounds = lowerBound;
    		
        	while (aimBallBounds <= upperBound) {
        		aimBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimBallBounds)));
        		if (aimBallX < 0) {
        			aimBallPosInvalid = true;
        			comingFromRight = true;
        			break;
        		} else if (aimBallX > SCREEN_WIDTH - width) {
        			aimBallPosInvalid = true;
        			comingFromRight = false;
        			break;
        		}
        		aimBallBounds++;
        	}
        	
        	if (aimLocked) {
        		upperBound = 150;
        		lowerBound = 50;
        		increment = aimRadius;
        	}
        	
        	System.out.println(aimBallPosInvalid);
        	
        	if (aimBallPosInvalid && aimMode) {
        		if (comingFromRight) {
        			lowerBound = 90;
        		}
        		else if (comingFromRight == false) {
        			upperBound = 90;
        		}
        	}
        	
        	/*
        	if (aimBallX < 0) {
        	    aimAngle = (int) Math.toDegrees(Math.acos((double) -playerX / aimRadius));
        	    aimSpeed = -aimSpeed;
        	} else if (aimBallX > SCREEN_WIDTH - width) {
        	    aimAngle = (int) Math.toDegrees(Math.acos((double) (SCREEN_WIDTH - playerX - width) / aimRadius));
        	    aimSpeed = -aimSpeed;
        	}*/
        	
        	// recalculate aimBall position based on corrected aimAngle
        	aimBallX = playerX + (width / 2) - 10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = playerY + (height / 2) - 10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	
        	// limiting range of aimBall
        	if (increment > upperBound) {
        		aimSpeed = -aimSpeed;
        		System.out.println(aimBallX+", "+aimBallY+" angle1...");
        	} else if (increment < lowerBound) {
        		aimSpeed = -aimSpeed;
        		System.out.println(aimBallX+", "+aimBallY+" angle2...");
        	}
        	if (aimMode) {
        		aimAngle += aimSpeed;
        	} else {
        		aimRadius += aimSpeed;
        	}
        	//reset aimball bounds detection
        	aimBallPosInvalid = false;
        	
        } if (shoot) {
        	velocityX = (int) aimRadius * (Math.cos(Math.toRadians(aimAngle)))*0.1;
        	velocityY = (int) -aimRadius * (Math.sin(Math.toRadians(aimAngle)))*0.1;
        	onGround = false;
        	shoot = false;
        	aimAngle = 180;
            aimRadius = 50;
        }
        // applying gravity
        playerY += (int) Math.round(velocityY);
        // controls slowing down of jump and downwards falling
        velocityY += gravity;
        for (Block i: level1.getBlocks()) {
        	if (!(i.isImpassable())) {
        		System.out.println("here!");
        		continue;
        	}
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        level1.drawLevel(g);
        // draw aimBall when controlling angle or strength
        if (aimMode||aimLocked) {
        	g.setColor(Color.MAGENTA);
        	g.fillOval(aimBallX, aimBallY, 20, 20);
        }
        // draw player
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	// one button is light work no reaction
    	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
    	    if (aimLocked) {
    	        aimLocked = false;
    	        shoot = true;
    	    } else {
    	        aimMode = true;
    	    }
    	    repaint();
    	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (aimMode && e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// lock position in place, and from here on only adjust power
        	aimLocked = true;
        	aimMode = false;
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

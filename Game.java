import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {
	private Level1 level1 = new Level1();
	private final int SCREEN_WIDTH = 800;  // window width
    private final int SCREEN_HEIGHT = 600; // window height
    
    private int x = 50;       // X position
    private int y = 300;      // Y position
    private int width = 50;   // player width
    private int height = 50;  // player height

    private double velocityX = 0;
    private double velocityY = 0;
    private double acceleration = 0.5;  // acceleration (was used for old movement controls)
    private double gravity = 0.3; //defining gravity
    private double friction = 0.05;  // friction in air
    private double gFriction = 0.4; // friction when on ground
    
    private boolean onGround = false;
    
    private boolean aimMode = false; // true when controlling aimBall angle
    private int aimBallX = x + 15;
    private int aimBallY = y + (height / 2) - 100;
    private double aimAngle = 180;
    private int aimRadius = 50;
    private double aimSpeed = 1;
    private boolean aimLocked = false; // true when controlling aimBall radius
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
    	// previously had acceleration for movement controls with WASD, not necessary with single button jump-based movement 
    	
    	// apply friction when in contact with ground or otherwise
        if (velocityX > 0) {
        	if (onGround) {
        		velocityX -= gFriction;
        	} else {
        		velocityX -= friction;
        	}
            if (velocityX < 0) velocityX = 0;
        } else if (velocityX < 0) {
        	if (onGround) {
        		velocityX += gFriction;
        	} else {
        		velocityX += friction;
        	}
            if (velocityX > 0) velocityX = 0;
        }

        // move right velocityX pixels
        x += velocityX;

        // collision detection in general
        if (x < 0) {
            x = 0;
            velocityX = 0; // Stop movement at the left wall
        }
        if (x + width > SCREEN_WIDTH) {
            x = SCREEN_WIDTH - width;
            velocityX = 0; // Stop movement at the right wall
        }
        // floor detection
        if (y + height >= SCREEN_HEIGHT) {
            y = SCREEN_HEIGHT - height;
            velocityY = 0;
            onGround = true;
        }
        
        aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
    	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        // jump force upward code
        if (!(onGround) && aimMode) {
        	aimMode = false;
        } else if (onGround && aimMode) {
        	aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	if (aimBallX<0) aimBallX = 0;
        	else if (aimBallX>SCREEN_WIDTH-width) aimBallX = SCREEN_WIDTH-width;
        	
        	if (aimAngle > 180) {
        		aimSpeed = -aimSpeed;
        		System.out.println(aimBallX+", "+aimBallY+" angle1...");
        	} else if (aimAngle < 0) {
        		aimSpeed = -aimSpeed;
        		System.out.println(aimBallX+", "+aimBallY+" angle2...");
        	}
        	aimAngle += aimSpeed;
        } else if (aimLocked) {
        	// perform similar action to aimMode, but radius oscillates rather than angle
        	aimMode = false;
        	aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	if (aimBallX<0) aimBallX = 0;
        	else if (aimBallX>SCREEN_WIDTH-width) aimBallX = SCREEN_WIDTH-width;
        	if (aimRadius > 150) {
        		aimSpeed = -aimSpeed;
        	} else if (aimRadius < 50) {
        		aimSpeed = -aimSpeed;
        	}
        	onGround = false;
        	aimRadius += aimSpeed;

            System.out.println(aimBallX+", "+aimBallY+" radius...");
        } if (shoot) {
        	velocityX = (int) (aimRadius * 0.125) * (Math.cos(Math.toRadians(aimAngle)));
        	velocityY = (int) -(aimRadius * 0.1) * (Math.sin(Math.toRadians(aimAngle)));
        	onGround = false;
        	shoot = false;
        	aimAngle = 180;
            aimRadius = 50;
        }
        // controls slowing down of jump and downwards falling
        velocityY += gravity;
        // applying gravity
        y += velocityY;
        for (Block i: level1.getBlocks()) {
        	if (!(i.isImpassable())) {
        		System.out.println("here!");
        		continue;
        	}
        	int bX = i.getBlockX();
        	int bY = i.getBlockY();
        	int bWidth = i.getBlockWidth();
        	int bHeight = i.getBlockHeight();
        	
        	if (x < bX + bWidth && x + width > bX && y < bY + bHeight && y + height > bY) {
        		// difference between far right of player and far left of block
        	    int overlapLeft = (x + width) - bX;
        	    // difference between far right of block and far left of player
        	    int overlapRight = (bX + bWidth) - x;
        	    // difference between yPos of player bottom to top of block
        	    int overlapTop = (y + height) - bY;
        	    // difference between yPos of block bottom to top of player
        	    int overlapBottom = (bY + bHeight) - y;
        	    
        	    // find smallest overlap
        	    int minOverlap = Math.min(Math.min(overlapLeft, overlapRight),Math.min(overlapTop, overlapBottom));
        	    
        	    // push player out of block from side with least overlap
        	    if (minOverlap == overlapTop) {
    	            y = bY - height;
    	            velocityY = 0;
    	            onGround = true;
        	    } else if (minOverlap == overlapLeft) {
        	        x = bX - width;
        	        velocityX = -velocityX*0.5;
        	    } else if (minOverlap == overlapRight) {
        	        x = bX + bWidth;
        	        velocityX = -velocityX*0.5;
        	    } else if (minOverlap == overlapBottom) {
        	        y = bY + bHeight;
        	        velocityY = 0;
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
        g.fillRect(x, y, width, height);
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
import package t;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {
	private Level1 level1 = new Level1();
	private final int SCREEN_WIDTH = 800;  // Window width
    private final int SCREEN_HEIGHT = 600; // Window height
    
    private int x = 50;       // X position
    private int y = 300;      // Y position
    private int width = 50;   // Player width
    private int height = 50;  // Player height

    private double velocityX = 0;
    private double velocityY = 0;
    private double acceleration = 0.5;  // Acceleration
    private double gravity = 0.3; //defining gravity
    private double maxSpeed = 5;  // Max speed
    private double friction = 0.01;  // Friction
    private double gFriction = 0.1; // friction when on ground

    private boolean movingRight = false;
    private boolean movingLeft = false;
    
    private boolean onGround = false;
    
    private boolean aimMode = false;
    private int aimBallX = x + 15;
    private int aimBallY = y + (height / 2) - 100;
    private double aimAngle = 180;
    private int aimRadius = 50;
    private double aimSpeed = 1;
    private boolean aimLocked = false;
    private boolean shoot = false;

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        new Thread(this).start();  // Start game loop
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16);  // ~60 FPS (1000ms / 60 ≈ 16ms per frame)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Apply acceleration when moving
        if (movingRight) {
            velocityX += acceleration;
        } else if (movingLeft) {
            velocityX -= acceleration;
        } else {
            // Apply friction to slow down when no key is pressed
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
        }

        // Clamp speed to max
        if (velocityX > maxSpeed) {
        	velocityX = maxSpeed;
        }
        if (velocityX < -maxSpeed) {
        	velocityX = -maxSpeed;
        }

        // Update position
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
        else {
        	onGround = false;
        }
        for (Block i: level1.getBlocks()) {
        	int bX = i.getBlockX();
        	int bY = i.getBlockY();
        	int bWidth = i.getBlockWidth();
        	int bHeight = i.getBlockHeight();
        	
        	if ((x + width > bX && x + width < bX + (bWidth/2))&&(y+height>bY&&y<bY+bHeight)) {
        		velocityX = 0;
        		x = i.getBlockX() - width;
        	}
        	else if ((x < bX + bWidth && x > bX + (bWidth/2))&&(y+height>bY&&y<bY+bHeight)) {
        		velocityX = 0;
        		x = i.getBlockX() + bWidth;
        	}
        	else if ((y + height > bY && y + height < bY + (bHeight/2))&&(x+width>bX&&x<bX+bWidth)) {
        		velocityY = 0;
        		y = i.getBlockY() + height;
        	}
        	else if ((y < bY + bHeight && y > bY + (bHeight/2))&&(x+width>bX&&x<bX+bWidth)) {
        		velocityY = 0;
        		y = i.getBlockY() + bHeight;
        	}
        }
        // jump force upward code
        if (!(onGround) && aimMode) {
        	aimMode = false;
        } else if (onGround && aimMode) {
        	aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	if (aimBallX<0) aimBallX = 0;
        	else if (aimBallX>SCREEN_WIDTH-width) aimBallX = SCREEN_WIDTH-width;
        	if (aimAngle >= 180) {
        		aimSpeed = -aimSpeed;
        	} else if (aimAngle <= 0) {
        		aimSpeed = -aimSpeed;
        	}
        	aimAngle += aimSpeed;
        	
        } else if (onGround && aimLocked) {
        	// perform similar action to aimMode, but radius oscillates rather than angle
        	aimMode = false;
        	aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	if (aimBallX<0) aimBallX = 0;
        	else if (aimBallX>SCREEN_WIDTH-width) aimBallX = SCREEN_WIDTH-width;
        	if (aimRadius >= 150) {
        		aimSpeed = -aimSpeed;
        	} else if (aimRadius <= 50) {
        		aimSpeed = -aimSpeed;
        	}
        	onGround = false;
        	aimRadius += aimSpeed;
        } if (onGround && shoot) {
        	velocityX = (int) (aimRadius * 0.2) * (Math.cos(Math.toRadians(aimAngle)));
        	velocityY = (int) -(aimRadius * 0.1) * (Math.sin(Math.toRadians(aimAngle)));
        	System.out.println(velocityY);
        	onGround = false;
        	shoot = false;
        	aimAngle = 180;
            aimRadius = 50;
        }
        // controls slowing down of jump and downwards falling
        velocityY += gravity;
        // applying gravity
        y += velocityY;
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        level1.drawLevel(g);
        if (aimMode||aimLocked) {
        	g.setColor(Color.BLUE);
        	g.fillOval(aimBallX, aimBallY, 20, 20);
        }
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);  // Draw the player
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
        	//lock position in place, and from here on only adjust power
        	aimLocked = true;
        	aimMode = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("you are circl");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

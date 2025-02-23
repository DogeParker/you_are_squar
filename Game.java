package t;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {
	private final int SCREEN_WIDTH = 800;  // Window width
    private final int SCREEN_HEIGHT = 600; // Window height
    
    private int x = 50;       // X position
    private int y = 300;      // Y position
    private int width = 50;   // Player width
    private int height = 50;  // Player height

    private double velocityX = 0;
    private double velocityY = 0;
    private double acceleration = 0.5;  // Acceleration
    private double maxSpeed = 5;  // Max speed
    private double friction = 0.1;  // Friction

    private boolean movingRight = false;
    private boolean movingLeft = false;
    
    private boolean jumping = false;
    private boolean onGround = false;
    
    private boolean aimMode = false;
    private int aimBallX = x+15;
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
            		velocityX -= friction+.05;
            	} else {
            		velocityX -= friction;
            	}
                if (velocityX < 0) velocityX = 0;
            } else if (velocityX < 0) {
            	if (onGround) {
            		velocityX += friction+.05;
            	} else {
            		velocityX += friction;
            	}
                if (velocityX > 0) velocityX = 0;
            }
        }

        // Clamp speed to max
        if (velocityX > maxSpeed) velocityX = maxSpeed;
        if (velocityX < -maxSpeed) velocityX = -maxSpeed;

        // Update position
        x += velocityX;

        // **Collision Detection with Walls**
        if (x < 0) {
            x = 0;
            velocityX = 0; // Stop movement at the left wall
        }
        if (x + width > SCREEN_WIDTH) {
            x = SCREEN_WIDTH - width;
            velocityX = 0; // Stop movement at the right wall
        }
        
        //p1 Collision detection with screen floor
        if (y + height >= SCREEN_HEIGHT) {
            y = SCREEN_HEIGHT - height;
            velocityY = 0;
            onGround = true;
        }
        else {
        	onGround = false;
        }
        // jump force upward code
        if (onGround && jumping) {
        	velocityY = -7;
        	onGround = false;
        }
        // controls slowing down of jump and downwards falling
        velocityY += acceleration;
        // applying gravity
        y += velocityY;
        // setting jump to false, as the force upwards has already been applied
        jumping = false;
        
        if (!(onGround) && aimMode) {
        	aimMode = false;
        } else if (onGround && aimMode) {
        	aimBallX = x + (width / 2)-10 + (int) (aimRadius * Math.cos(Math.toRadians(aimAngle)));
        	aimBallY = y + (height / 2)-10 - (int) (aimRadius * Math.sin(Math.toRadians(aimAngle)));
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
        	if (aimRadius >= 150) {
        		aimSpeed = -aimSpeed;
        	} else if (aimRadius <= 50) {
        		aimSpeed = -aimSpeed;
        	}
        	aimRadius += aimSpeed;
        } if (onGround && shoot) {
        	velocityX = (int) (aimRadius * 0.2) * (Math.cos(Math.toRadians(aimAngle)));
        	velocityY = (int) -(aimRadius * Math.sin(Math.toRadians(aimAngle)));
        	System.out.println(velocityY);
        	onGround = false;
        	shoot = false;
        	jumping = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (aimMode||aimLocked) {
        	g.setColor(Color.BLUE);
        	g.fillOval(aimBallX, aimBallY, 20, 20);
        }
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);  // Draw the player
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_L) { // Press "L" to test
    	    velocityY = -15;  // Instant strong jump
    	    onGround = false;
    	}

        if (e.getKeyCode() == KeyEvent.VK_D) {
            movingRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            movingLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
        	jumping = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        	aimMode = true;
        	repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && aimLocked) {
        	aimLocked = false;
        	shoot = true;
        	repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            movingRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            movingLeft = false;
        }
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

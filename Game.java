package t;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, Runnable {
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

    private final int SCREEN_WIDTH = 800;  // Window width
    private final int SCREEN_HEIGHT = 600; // Window height
    
    private boolean onGround = false;

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
                velocityX -= friction;
                if (velocityX < 0) velocityX = 0;
            } else if (velocityX < 0) {
                velocityX += friction;
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
        
        //Defying Gravity
        if (y + height > SCREEN_HEIGHT) {
            y = SCREEN_HEIGHT - height;
            velocityY = 0;
            onGround = true;
        }
        if (!(onGround)) {
        	velocityY += acceleration;
        	y += velocityY;
        }
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(x, y, width, height);  // Draw the player
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            movingRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            movingLeft = true;
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
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Physics & Collision");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

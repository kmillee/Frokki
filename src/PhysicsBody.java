import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents a physics body that can be used to simulate simple physics
 */
public class PhysicsBody{
    private double x, y; // Top-left position
    private double velX, velY; // velocity
    private double friction = 0.9;
    private double gravity = 9.81 * 500;
    private boolean active = false; // Indicates if a body is moving or not
    private Timer timer;
    private int parentHeight, bodyHeight;

    public final int id;
    private static int idCounter = 0;

    // Change listeners
    private final java.util.List<ChangeListener> listeners = new ArrayList<>();



    public PhysicsBody(){
        this.id = ++idCounter;
    }

    /**
     * Starts the physics body simulation with the given parameters.
     * @param startX The initial x position
     * @param startY The initial y position
     * @param vx The initial x velocity
     * @param vY The initial y velocity
     * @param parentHeight The height of the parent container
     * @param bodyHeight The height of the physics body
     */
    public void start(double startX, double startY, double vx, double vY, int parentHeight, int bodyHeight){
        this.x = startX;
        this.y = startY;
        this.velX = vx;
        this.velY = vY;
        this.active = true;
        this.parentHeight = parentHeight;
        this.bodyHeight = bodyHeight;

        if(timer == null){
            timer = new Timer(16, e -> update(0.016));
            timer.start();
        }
    }

    /**
     * Updates the physics body position.
     * @param dt The time since the last update
     */
    public void update(double dt){
        if(!active) return;

        velY += gravity * dt;
        velX *= friction;

        x += velX * dt;
        y += velY * dt;

        notifyChangeListener();

        // body reached the ground
        if(y + bodyHeight >= parentHeight){
            y = parentHeight - bodyHeight;
            stop();
        }

    }

    public int getX(){
        return (int)x;
    }
    public int getY(){
        return (int)y;
    }

    /**
     * Used to know whether the body is still in movement or not
     * @return true if the body is still moving, false otherwise
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Stops the physics body simulation.
     */
    public void stop(){
        velY = 0;
        velX = 0;
        active = false;
        if(timer != null){
            timer.stop();
            timer = null;
        }
        notifyChangeListener();
    }

    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }

    private void notifyChangeListener(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}

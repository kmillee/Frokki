import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class PhysicsBody{
    private double x, y; // Top-left position
    private double velX, velY; // velocity
    private double friction = 1;
    private double gravity = 9.81 * 200;
    private boolean active = false;
    private Timer timer;
    private int parentHeight, bodyHeight;

    // Change listeners
    private final java.util.List<ChangeListener> listeners = new ArrayList<>();

    public void start(double startX, double startY, double vx, double vY, int parentHeight, int bodyHeight){
        this.x = startX;
        this.y = startY;
        this.velX = vx;
        this.velY = vY;
        this.active = true;
        this.parentHeight = parentHeight;
        this.bodyHeight = bodyHeight;

        if(timer == null){
            System.out.println("timer in");
            timer = new Timer(16, e -> update(0.016));
            timer.start();
        }
    }

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

    public boolean isActive(){
        return active;
    }

    public void stop(){
        System.out.println("PhysicsBody stop");
        velY = 0;
        velX = 0;
        active = false;
        if(timer != null){
            timer.stop();
            timer = null;
        }
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

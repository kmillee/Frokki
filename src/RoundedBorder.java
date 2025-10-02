/**
 * Created: 01/10/2025
 *
 * Adapted from https://forums.oracle.com/ords/apexds/post/jpanel-border-with-rounded-corners-0946
 */

import javax.swing.border.Border;
import java.awt.*;


public class RoundedBorder implements Border {

    private final int radius;
    private final int thickness;
    private final Color color;

    /**
     * Creates a rounded border with specified radius, thickness and color. The border
     * is drawn inside the bounds of the element.
     * @param radius outer radius of the border
     * @param thickness thickness of the border
     * @param color color of the border
     */
    public RoundedBorder(int radius, int thickness, Color color) {
        this.radius = radius;
        this.thickness = thickness;
        this.color = color;
    }

    /**
     *  Creates a rounded border with specified radius. The border
     *  is drawn inside the bounds of the element. The created border
     *  is black and has a thickness of 1px.
     * @param radius outer radius of the border.
     */
    public RoundedBorder(int radius) {
        this(radius, 1,  Color.BLACK);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        // Using rounded corner formula outerRadius - gap = innerRadius
        g2.setStroke(new BasicStroke(thickness));

        // Inner bounds and inner radius
        int innerX = x + thickness / 2;
        int innerY = y + thickness / 2;
        int innerWidth = width - thickness ;
        int innerHeight = height - thickness;
        int innerRadius = Math.max(0, radius - thickness);

        g2.drawRoundRect(innerX, innerY, innerWidth, innerHeight, innerRadius, innerRadius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}

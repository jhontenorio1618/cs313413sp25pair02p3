package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

import java.util.List;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {
    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        if (canvas == null || paint == null) {
            throw new IllegalArgumentException("Canvas and Paint cannot be null");
        }
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int prevColor = paint.getColor();
        Style prevStyle = paint.getStyle();

        paint.setStyle(Style.FILL_AND_STROKE);  // Set style before drawing
        paint.setColor(c.getColor());

        c.getShape().accept(this);

        paint.setColor(prevColor);
        paint.setStyle(prevStyle);

        return null;
    }



    @Override
    public Void onFill(final Fill f) {
        Style prevStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);

        f.getShape().accept(this);

        paint.setStyle(prevStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        if (paint.getStyle() != Style.STROKE) {
            paint.setStyle(Style.STROKE);
        }
        o.getShape().accept(this);
        return null;
    }


    @Override
    public Void onPolygon(final Polygon s) {
        List<Point> points = s.getPoints();
        if (points == null || points.size() < 2) return null;

        int n = points.size();
        float[] pts = new float[n * 4];

        // Draw line segments between consecutive points
        for (int i = 0; i < n - 1; i++) {
            pts[i * 4] = points.get(i).getX();
            pts[i * 4 + 1] = points.get(i).getY();
            pts[i * 4 + 2] = points.get(i + 1).getX();
            pts[i * 4 + 3] = points.get(i + 1).getY();
        }

        // Close the polygon by connecting the last point to the first
        pts[(n - 1) * 4] = points.get(n - 1).getX();
        pts[(n - 1) * 4 + 1] = points.get(n - 1).getY();
        pts[(n - 1) * 4 + 2] = points.get(0).getX();
        pts[(n - 1) * 4 + 3] = points.get(0).getY();

        canvas.drawLines(pts, paint);
        return null;
    }

}

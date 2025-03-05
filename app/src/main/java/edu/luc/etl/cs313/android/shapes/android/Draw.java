package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

import java.util.List;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

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
        paint.setStyle(Style.STROKE);
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
        List<Point> points = (List<Point>) s.getPoints();
        if (points == null || points.size() < 2) return null;

        float[] pts = new float[(points.size() - 1) * 4];
        for (int i = 0; i < points.size() - 1; i++) {
            pts[i * 4] = points.get(i).getX();
            pts[i * 4 + 1] = points.get(i).getY();
            pts[i * 4 + 2] = points.get(i + 1).getX();
            pts[i * 4 + 3] = points.get(i + 1).getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }

}

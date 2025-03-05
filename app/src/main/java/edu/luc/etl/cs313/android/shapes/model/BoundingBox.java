package edu.luc.etl.cs313.android.shapes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onLocation(final Location l) {
        Location shapeBounds = l.getShape().accept(this);
        return new Location(l.getX() + shapeBounds.getX(), l.getY() + shapeBounds.getY(), shapeBounds.getShape());
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {

        List<Shape> shapes = new ArrayList<>(g.getShapes());

        if (shapes.isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        Location firstBox = shapes.get(0).accept(this);
        int minX = firstBox.getX();
        int minY = firstBox.getY();
        int maxX = minX + ((Rectangle) firstBox.getShape()).getWidth();
        int maxY = minY + ((Rectangle) firstBox.getShape()).getHeight();


        for (Shape shape : shapes) {
            Location box = shape.accept(this);
            minX = Math.min(minX, box.getX());
            minY = Math.min(minY, box.getY());
            maxX = Math.max(maxX, box.getX() + ((Rectangle) box.getShape()).getWidth());
            maxY = Math.max(maxY, box.getY() + ((Rectangle) box.getShape()).getHeight());
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }


    @Override
    public Location onPolygon(final Polygon s) {
        List<Point> points = s.getPoints();

        if (points.isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        int minX = points.get(0).getX();
        int minY = points.get(0).getY();
        int maxX = points.get(0).getX();
        int maxY = points.get(0).getY();

        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

}

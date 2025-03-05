package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

    @Override
    public Integer onCircle(final Circle c) {
        return 1;
    }

    @Override
    public Integer onRectangle(final Rectangle q) {
        return 1;
    }

    @Override
    public Integer onPolygon(final Polygon p) {
        System.out.println("Visiting Polygon: " + p);
        return 1;
    }

    @Override
    public Integer onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Integer onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Integer onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Integer onLocation(final Location l) {
        return l.getShape().accept(this);
    }

    @Override
    public Integer onGroup(final Group g) {
        int count = 0;
        for (Shape shape : g.getShapes()) {
            int shapeCount = shape.accept(this);
            System.out.println("Shape count: " + shapeCount);
            count += shapeCount;
        }
        return count;
    }

}

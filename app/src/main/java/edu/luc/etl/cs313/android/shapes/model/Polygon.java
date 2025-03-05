package edu.luc.etl.cs313.android.shapes.model;
import java.util.ArrayList;
import java.util.List;

/**
 * A special case of a group consisting only of Points.
 */
public class Polygon extends Group {

    public Polygon(final Point... points) {
        super(points);
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        for (Shape shape : getShapes()) {
            if (shape instanceof Point) {
                points.add((Point) shape);
            } else {
                throw new IllegalStateException("Polygon contains non-Point shapes");
            }
        }
        return points;
    }

    @Override
    public <Result> Result accept(final Visitor<Result> v) {
        return v.onPolygon(this);
    }
}

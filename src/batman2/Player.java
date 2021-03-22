package batman2;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static final int ACCURACY = 10;

    static class Game {

        Polygon building;
        Point batmanPosition;
        Point prevPosition = new Point();

        List<Point> triedPoints = new ArrayList<>();


        public Game(final int W, final int H, final int batmanX, final int batmanY) {
            building = new Polygon();
            building.addPoint(0, 0);
            building.addPoint(0, H * ACCURACY);
            building.addPoint(W * ACCURACY, H * ACCURACY);
            building.addPoint(W * ACCURACY, 0);

            batmanPosition = new Point(batmanX * ACCURACY, batmanY * ACCURACY);
        }

        private void warmer() {
            cutBuilding(prevPosition, batmanPosition, true);
        }

        private void colder() {
            cutBuilding(batmanPosition, prevPosition, true);
        }

        private void same() {
            cutBuilding(prevPosition, batmanPosition, false);
            cutBuilding(batmanPosition, prevPosition, false);

        }


        private String getNextMove() {
            //            int x = (int) building.getBounds().getCenterX();
            //            int y = (int) building.getBounds().getCenterY();
            int x;
            int y;
            triedPoints.add(new Point(batmanPosition.x / ACCURACY, batmanPosition.y / ACCURACY));
            if (building.contains(batmanPosition.x, batmanPosition.y)) {
                Rectangle bounds = building.getBounds();

                if (batmanPosition.x / ACCURACY == bounds.getHeight() / 2 / ACCURACY) {
                    x = batmanPosition.x + 1 * ACCURACY;
                } else if (batmanPosition.x > bounds.getHeight() / 2) {
                    x = (int) (bounds.getMaxX() - batmanPosition.x);
                } else {
                    x = (int) (bounds.getMinX() + batmanPosition.x);
                }

                if (batmanPosition.y / ACCURACY == bounds.getWidth() / 2 / ACCURACY) {
                    y = batmanPosition.y + 1 * ACCURACY;
                } else if (batmanPosition.y > bounds.getWidth() / 2) {
                    y = (int) (bounds.getMaxY() - batmanPosition.y);
                } else {
                    y = (int) (bounds.getMinY() + batmanPosition.y);
                }
            } else {
                int sumX = 0;
                for (final int xpoint : building.xpoints) {
                    sumX = sumX + xpoint;
                }
                x = sumX / building.xpoints.length;

                int sumY = 0;
                for (final int ypoint : building.ypoints) {
                    sumY = sumY + ypoint;
                }
                y = sumY / building.ypoints.length;
            }


            int roundedX = x / ACCURACY;
            int roundedY = y / ACCURACY;

            int originalX = roundedX;
            int originalY = roundedY;

            int tryIndex = 0;
            while (isTriedPoint(roundedX, roundedY) || !building.contains(roundedX * ACCURACY, roundedY * ACCURACY)) {
                int wholeIndex = tryIndex / 8 + 1;
                switch (tryIndex % 8) {
                    case 0:
                        roundedX = originalX;
                        roundedY = originalY + wholeIndex;
                        break;
                    case 1:
                        roundedX = originalX - wholeIndex;
                        roundedY = originalY + wholeIndex;
                        break;
                    case 2:
                        roundedX = originalX;
                        roundedY = originalY - wholeIndex;
                        break;
                    case 3:
                        roundedX = originalX + wholeIndex;
                        roundedY = originalY + wholeIndex;
                        break;
                    case 4:
                        roundedX = originalX + wholeIndex;
                        roundedY = originalY - wholeIndex;
                        break;
                    case 5:
                        roundedX = originalX - wholeIndex;
                        roundedY = originalY;
                        break;
                    case 6:
                        roundedX = originalX + wholeIndex;
                        roundedY = originalY;
                        break;
                    case 7:
                        roundedX = originalX - wholeIndex;
                        roundedY = originalY - wholeIndex;
                        break;
                }
                tryIndex++;
            }

//            if (x / ACCURACY == batmanPosition.x / ACCURACY && y / ACCURACY == batmanPosition.y / ACCURACY) {
//                //                    Point randomPoint;
//                debug("SAME POINT");
//                //                 do {
//                //                    randomPoint = generatePoint(building);
//                //                } while (randomPoint.x / ACCURACY == x / ACCURACY && randomPoint.y / ACCURACY == y / ACCURACY);
//                Point newPoint = getPointWhenInTheMiddle();
//                x = newPoint.x;
//                y = newPoint.y;
//                //x = randomPoint.x;
//                //y = randomPoint.y;
//            }

            prevPosition.setLocation(batmanPosition);
            batmanPosition.setLocation(roundedX * ACCURACY, roundedY * ACCURACY);
            debug("Jump pos: " + x + ", " + y);
            debug("Jump rounded pos: " + roundedX + ", " + roundedY);
            return roundedX + " " + roundedY;
        }


        private Point generatePoint(Polygon polygon) {
            Rectangle r = polygon.getBounds();
            double x, y;
            do {
                x = r.getX() + r.getWidth() * Math.random();
                y = r.getY() + r.getHeight() * Math.random();
            } while (!polygon.contains(x, y));

            return new Point((int) x, (int) y);
        }

        private Point getPointWhenInTheMiddle() {
            Rectangle rectangle = building.getBounds();
            Rectangle newRectangle = new Rectangle();
            newRectangle.setBounds(
                    (int) rectangle.getMinX(),
                    (int) rectangle.getMinY(),
                    (int) (batmanPosition.x - rectangle.getMinX()),
                    (int) (batmanPosition.y - rectangle.getMinY()));
            return new Point((int) newRectangle.getCenterX(), (int) newRectangle.getCenterY());
        }

        private boolean isTriedPoint(int x, int y) {
            for (Point triedPoint : triedPoints) {
                if (x == triedPoint.x && y == triedPoint.y) {
                    debug("Tried");
                    return true;
                }
            }
            return false;
        }

        private boolean isContains(){
            //TODO
            return false;
        }


        //Cut, rotate polgions

        private void cutBuilding(Point from, Point to, boolean middleRotate) {
            Point[] points;

            if (middleRotate) {
                points = rotateLineInTheMiddle(from, to);
            } else {
                points = rotateLineInTheEnd(from, to);
            }

            Point C = points[0];
            Point D = points[1];

            ArrayList<Point> intersections = getIntersections(building, C, D);
            if (intersections.isEmpty()) {
                return;
            }
            Polygon cutPoly = cutPolygon(building, intersections.get(0), intersections.get(1), from, to);
            building = cutPoly;
        }


        private Point[] rotateLineInTheMiddle(Point first, Point second) {
            Point OM = new Point((first.x + second.x) / 2, (first.y + second.y) / 2);
            Point MB = new Point(second.x - OM.x, second.y - OM.y);

            Point MC = new Point(-MB.y, +MB.x);
            Point MA = new Point(+MB.y, -MB.x);

            Point OC = new Point(OM.x + MC.x, OM.y + MC.y);
            Point OA = new Point(OM.x + MA.x, OM.y + MA.y);
            return new Point[]{OC, OA};
        }

        private Point[] rotateLineInTheEnd(Point first, Point second) {
            Point OM = first;
            Point MB = new Point(second.x - OM.x, second.y - OM.y);

            Point MC = new Point(-MB.y, +MB.x);
            Point MA = new Point(+MB.y, -MB.x);

            Point OC = new Point(OM.x + MC.x, OM.y + MC.y);
            Point OA = new Point(OM.x + MA.x, OM.y + MA.y);
            return new Point[]{OC, OA};
        }


        private ArrayList<Point> getIntersections(Polygon polygon, Point A, Point B) {
            ArrayList<Point> intersections = new ArrayList<>();
            for (int i = 0; i != polygon.npoints; i++) {
                Point polyA = new Point(polygon.xpoints[i], polygon.ypoints[i]);
                Point polyB;
                if (i == polygon.npoints - 1) {
                    polyB = new Point(polygon.xpoints[0], polygon.ypoints[0]);
                } else {
                    polyB = new Point(polygon.xpoints[i + 1], polygon.ypoints[i + 1]);
                }

                Point intersection = lineLineIntersection(A, B, polyA, polyB);
                Rectangle rectangle = polygon.getBounds();
                if (intersection != null
                        && intersection.x <= polygon.getBounds().getMaxX()
                        && intersection.y <= polygon.getBounds().getMaxY()
                        && intersection.x >= polygon.getBounds().getMinX()
                        && intersection.y >= polygon.getBounds().getMinY()) {
                    intersections.add(intersection);
                }
            }

            return intersections;
        }


        private Point lineLineIntersection(Point A, Point B, Point C, Point D) {
            // Line AB represented as a1x + b1y = c1
            double a1 = B.getY() - A.getY();
            double b1 = A.getX() - B.getX();
            double c1 = a1 * (A.getX()) + b1 * (A.getY());

            // Line CD represented as a2x + b2y = c2
            double a2 = D.getY() - C.getY();
            double b2 = C.getX() - D.getX();
            double c2 = a2 * (C.getX()) + b2 * (C.getY());

            double determinant = a1 * b2 - a2 * b1;

            if (determinant == 0) {
                // The lines are parallel. This is simplified
                // by returning a pair of FLT_MAX
                return null;
            } else {
                double x = (b2 * c1 - b1 * c2) / determinant;
                double y = (a1 * c2 - a2 * c1) / determinant;
                Point point = new Point();
                point.setLocation(x, y);
                return point;
            }
        }

        private Polygon cutPolygon(Polygon polygon, Point lineA, Point lineB, Point from, Point to) {
            ArrayList<Point> left = new ArrayList<>();
            left.add(lineA);
            left.add(lineB);
            ArrayList<Point> right = new ArrayList<>();
            right.add(lineA);
            right.add(lineB);
            for (int i = 0; i != polygon.npoints; i++) {
                Point polyPoint = new Point(polygon.xpoints[i], polygon.ypoints[i]);
                if (isLeftSide(lineA, lineB, polyPoint)) {
                    left.add(polyPoint);
                } else {
                    right.add(polyPoint);
                }
            }

            right = sortPoly(right);
            Polygon rightPoly = new Polygon();
            for (Point point : right) {
                rightPoly.addPoint(point.x, point.y);
            }

            left = sortPoly(left);
            Polygon leftPoly = new Polygon();
            for (Point point : left) {
                leftPoly.addPoint(point.x, point.y);
            }

            return leftPoly.contains(to) ? leftPoly : rightPoly;
        }


        private ArrayList<Point> sortPoly(ArrayList<Point> originalPoints) {
            ArrayList<Point> sortedList = new ArrayList<>();

            sortedList.add(originalPoints.remove(0));
            while (originalPoints.size() > 0) {
                Point nearest = originalPoints.get(
                        getNearestIndex(originalPoints, sortedList.get(sortedList.size() - 1)));
                sortedList.add(nearest);
                originalPoints.remove(nearest);
            }

            return sortedList;
        }

        private int getNearestIndex(List<Point> points, Point from) {
            int nearestIndex = 0;
            double distance = Double.MAX_VALUE;
            for (int i = 0; i != points.size(); i++) {
                Point polyPoint = points.get(i);
                double distanceTemp = distance(polyPoint, from);
                if (distanceTemp < distance) {
                    distance = distanceTemp;
                    nearestIndex = i;
                }
            }
            return nearestIndex;
        }

        private boolean isLeftSide(Point linePointA, Point linePointB, Point point) {
            int x0 = linePointA.x;
            int y0 = linePointA.y;
            int x1 = linePointB.x;
            int y1 = linePointB.y;
            int x2 = point.x;
            int y2 = point.y;

            return (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0) > 0;

        }


        private double distance(Point A, Point B) {
            return Math.pow(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2), 0.5);
        }

        private double distance(int x1, int y1, int x2, int y2) {
            return Math.pow(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2), 0.5);
        }

        public String playRound(final String bombDir) {
//            printBuilding();
            switch (bombDir) {
                case "UNKNOWN":
                    return getNextMove();

                case "SAME":
                    same();
                    return getNextMove();
                case "WARMER":
                    warmer();
                    return getNextMove();

                case "COLDER":
                    colder();
                    return getNextMove();
            }
            return "";
        }

        public void printBuilding() {
            StringBuffer sb = new StringBuffer();
//            sb.append(WIDHT * ACCURACY + ";" + HEIGHT * ACCURACY + ";");
//            sb.append(batmanPosition.x + "," + batmanPosition.y + ";");
            for (int i = 0; i != building.npoints; i++) {
                sb.append(building.xpoints[i] + "," + building.ypoints[i] + "; ");
            }
            debug(sb.toString());
        }

    }

    static int WIDHT;
    static int HEIGHT;

//        public static void main(String args[]) {
//        Scanner in = new Scanner(System.in);
//        int W = in.nextInt(); // width of the building.
//        int H = in.nextInt(); // height of the building.
//        int N = in.nextInt(); // maximum number of turns before game over.
//        int X0 = in.nextInt();
//        int Y0 = in.nextInt();
//
//        WIDHT = W;
//        HEIGHT = H;
//        debug("W: " + String.valueOf(W));
//        debug("H: " + String.valueOf(H));
//
//        Game game = new Game(W, H, X0, Y0);
//        // game loop
//        while (true) {
//            String bombDir = in.next(); // Current distance to the bomb compared to previous distance (COLDER, WARMER, SAME or UNKNOWN)
//            String result = game.playRound(bombDir);
//
//            System.out.println(result);
//        }
//
//    }


    static Integer rounds = 80;

    public static void main(String args[]) {
        int W = 1;
        int H = 100;
        int batmanStartX = 0;
        int batmanStartY = 98;

        Point bombPos = new Point(0, 92);


        Simulator simulator = new Simulator(bombPos, new Point(batmanStartX, batmanStartY));
        Game game = new Game(W, H, batmanStartX, batmanStartY);
        Point batmanJump = null;
        while (true) {
            debug("Round: " + rounds);
            String bombdir = simulator.getNextRound(batmanJump);
            debug(bombdir);
            game.printBuilding();
            String batmanResult = game.playRound(bombdir);
            String[] strings = batmanResult.split(" ");
            batmanJump = new Point(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new JRisk(H, W, bombPos, game.prevPosition, game.building, rounds);
                }
            });


            Scanner scanner = new Scanner(System.in);
            String line = null;
            while (line == null) {
                line = scanner.nextLine();
            }
            rounds--;
        }
    }


    public static void debug(String string) {
        System.err.println(string);
    }

    public static void debug(int integer) {
        System.err.println(integer);
    }

}
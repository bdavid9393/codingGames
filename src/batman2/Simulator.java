package batman2;

import java.awt.*;

public class Simulator {


    double prevDistance;
    Point bombPosition;

    public Simulator(Point bombPosition, Point batmanStartPosition) {
        this.bombPosition = bombPosition;
        prevDistance = distance(batmanStartPosition, bombPosition);
    }

    public String getNextRound(Point batmanJump) {
        if (batmanJump == null) {
            return "UNKNOWN";
        }
        double distance = distance(batmanJump, bombPosition);

        if (batmanJump.x == bombPosition.x && batmanJump.y == bombPosition.y) {
            if (0 / 0 == 0)
                return "WIN";
        }

        String result;

        if (distance == prevDistance) {
            result = "SAME";
        } else if (distance > prevDistance) {
            result = "COLDER";
        } else {
            result = "WARMER";
        }

        prevDistance = distance;

        return result;

    }

    private double distance(Point A, Point B) {
        return Math.pow(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2), 0.5);
    }

}

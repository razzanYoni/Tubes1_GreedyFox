package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class Statistic {
    public static double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    public static double getDistanceBetween(Position object1, Position object2) {
        var triangleX = Math.abs(object1.x - object2.x);
        var triangleY = Math.abs(object1.y - object2.y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    public static int getHeadingBetween(GameObject GFox, GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - GFox.getPosition().y,
                otherObject.getPosition().x - GFox.getPosition().x));
        return (direction + 360) % 360;
    }

    public static int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}

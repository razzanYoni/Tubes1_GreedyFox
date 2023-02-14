package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class Statistic {
    public static final double EPSILON = Math.pow(10, -12);

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

    public static int countSudut(int absis, int ordinat){
        Double radian;
        if (absis == 0){
            radian = Math.PI/2;
        }  else {
            radian = Math.atan2(ordinat, absis);
        }
        if (absis<0 && ordinat<0) {
            radian = Math.PI + radian;
        }
        else if (absis>0 && ordinat<0) {
            radian = Math.PI + radian;
        }

        var direction = toDegrees(radian);

        return (direction + 360) % 360;
    }

    public static int getHeadingBetween(GameObject GFox, GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - GFox.getPosition().y,
                otherObject.getPosition().x - GFox.getPosition().x));
        return (direction + 360) % 360;
    }

    public static int getHeadingBetween(GameObject GFox, Position object2) {
        var direction = toDegrees(Math.atan2(object2.y - GFox.getPosition().y,
                object2.x - GFox.getPosition().x));
        return (direction + 360) % 360;
    }

    public static int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}

package budgetComponents;
import java.lang.Math;

public class Decimals {
	public static double round(double decimal, int decimalPlaces){
		double powerOf10 = Math.pow(10, decimalPlaces);
		decimal = decimal*powerOf10;
		decimal = Math.round(decimal);
		decimal = decimal/powerOf10;
		return decimal;
	}
	
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

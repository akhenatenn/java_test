

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AMIN {

	char[] source, target;

	public static void main(String[] args) {
		/* apply on clean and upper case string */
		double b = new AMIN().AMIN_percentage("DNYANDEV", "GYANDEV");
		System.out.println("Cost " + b);
	}

	public double AMIN_percentage(String strsource, String strtarget) {

		int len1 = strsource.length();
		int len2 = strtarget.length();
		// System.out.println("length" + len1);
		// System.out.println("length" + len2);

		int max = Math.max(len1, len2);

		double levensteinPen = Double
				.valueOf(AMIN_English(strsource, strtarget));
		double penalty = ((levensteinPen) / max);
		double decPercentage = Math.round((1.0 - penalty) * 100);
		return decPercentage;
	}

	/* Considering than Name String are clean and in Upper case */
	public double AMIN_English(String str1, String str2) {
		double[][] d = new double[str1.length() + 1][str2.length() + 1];
		int i = 0;
		int j = 0;
		double value1 = 0.0;
		double value2 = 0.0;
		double value3 = 0.0;
		double minValue = 0.0;

		// Building srs arr
		str1 = "#" + str1 + "!";
		source = str1.toCharArray();
		str2 = "#" + str2 + "!";
		target = str2.toCharArray();
		// **************

		int len1 = str1.length() - 2;
		int len2 = str2.length() - 2;

		for (i = 0; i <= len1; i++) {
			d[i][0] = i;
		}
		for (j = 0; j <= len2; j++) {
			d[0][j] = j;
		}

		for (i = 1; i <= len1; i++) {
			for (j = 1; j <= len2; j++) {
				double[] values = new double[3];

				/* cost of deletion */
				value1 = d[i - 1][j]
						+ costInsertDelete_EN(source[i], source[i - 1],
								source[i + 1]);

				/* cost insertion */
				value2 = d[i][j - 1]
						+ costInsertDelete_EN(target[j], target[j - 1],
								target[j + 1]);
				
				/* cost substitution */
				value3 = d[i - 1][j - 1]
						+ costsubstitution_EN(source[i], target[j]);

				minValue = Math.min(value1, value2);
				minValue = Math.min(value3, minValue);
				d[i][j] = minValue;
				// System.out.println("min bval" + minValue);
				
				values = resizeArray(values, 2);
				/* here we are checking cost of swapping
				 * considering some time swapping of character by mistake
				 * for eg. 'Dev'-'Dve'*/
				if ((i > 2 && j > 2)) {
					if (source[i] == target[j - 1]
							&& source[i - 1] == target[j]) {
						values[0] = d[i][j];
						values[1] = d[i - 2][j - 2]
								+ costswapping_EN(source[i], target[j]);
						d[i][j] = Min(values);
					}
				}

				/* */
				values = resizeArray(values, 2);

				/* considering this as by default simplification cost*/
				double Simplificationcost = 0.95;

				/*
				 * "KS" is treated almost equal to "X" value of matrix differs
				 * where KS is matched with X and it is different when X is
				 * matched with KS as length row and columns differs in both
				 * cases that is why two cases are handled differently For eg.
				 * two possibilities 1) source - LAXMI and target - LAKSMI Or 2)
				 * source - LAKSMI and target - LAXMI
				 */
				if (source[i] == 'S' && source[i - 1] == 'K') {
					if (target[j] == 'X') {

						values[0] = d[i][j];
						if (d[i - 1][j - 1] > 0.95) {
							values[1] = d[i - 1][j - 1] - Simplificationcost;
						} else {
							values[1] = d[i][j - 1] - Simplificationcost;
						}
						d[i][j] = Min(values);
					}
				} else if (target[j] == 'S') {
					if (target[j - 1] == 'K' && source[i] == 'X') {
						values[0] = d[i][j];
						values[1] = d[i][j - 1] - Simplificationcost;
						d[i][j] = Min(values);
					}
				} else
				/*
				 * EE treated almost equal to "I" for eg. RAJEEV or RAJIV
				 */
				if (source[i] == 'E' && source[i - 1] == 'E') {
					if (j == i - 1) {
						if (target[j] == 'I') {
							values[0] = d[i][j];
							if (j > 1) {
								/*cost is less if occur in between of word*/
								Simplificationcost = 0.2;
							} else {
								/*cost is more if occur at start of word*/
								Simplificationcost = 0.95;
							}
							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);
						}
					}
				} else if (target[j] == 'E' && target[j - 1] == 'E') {
					if (i == j - 1) {
						if (source[i] == 'I') {
							values[0] = d[i][j];
							if (i > 1) {
								Simplificationcost = 0.2;
							} else {
								Simplificationcost = 0.95;
							}

							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);

						}
					}
				}
				/* For eg. Rasool or Rasul */
				else if (source[i] == 'O' && source[i - 1] == 'O') {
					if (j == i - 1) {
						if (target[j] == 'U') {
							values[0] = d[i][j];
							if (j > 1) {
								Simplificationcost = 0.2;
							} else {
								Simplificationcost = 0.95;
							}
							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);
						}
					}

				} else if (target[j] == 'O' && target[j - 1] == 'O') {
					if (i == j - 1) {
						if (source[i] == 'U') {
							values[0] = d[i][j];
							if (i > 1) {
								Simplificationcost = 0.2;
							} else {
								Simplificationcost = 0.95;
							}
							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);
						}
					}
				}
				/*
				 * Name usually written in marathi 'Gyanesh' as 'Dnyanesh' so at
				 * start 'DN' can be treated as 'G'
				 */
				else if (source[i] == 'N' && source[i - 1] == 'D') {
					if (j == i - 1) {
						if (target[j] == 'G') {
							values[0] = d[i][j];
							if (j > 1) {
								Simplificationcost = 0.2;
							} else {
								Simplificationcost = 0.95;
							}
							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);
						}
					}

				} else if (target[j] == 'N' && target[j - 1] == 'D') {
					if (i == j - 1) {
						if (source[i] == 'G') {
							values[0] = d[i][j];
							if (i > 1) {
								Simplificationcost = 0.2;
							} else {
								Simplificationcost = 0.95;
							}
							values[1] = d[i - 1][j - 1] - Simplificationcost;
							d[i][j] = Min(values);
						}
					}
				}
			}
		}

		/* now last value in matrix will be cost or distance in two string */
		double dist = d[len1][len2];

		/* reseting cost arrays */
		for (int n = 0; n < source.length; n++) {
			source[n] = 0;
		}
		for (int n = 0; n < target.length; n++) {
			target[n] = 0;
		}
		return dist;
	}

	/*
	 * This function can be used in substitution of two characters based on the
	 * requirement for example: GY-JN for hindi
	 */
	public double twoCharacterSubstitution(String srcValue, String targetValue) {
		return 0.0;
	}

	public boolean isNearbyFirstChar_EN(char char1, char char1Next, char char2,
			char char2Next) {

		if (char1 == char2) {
			return true;
		}
		if (IsVowel(char1) && IsVowel(char2)
				&& IsValidVowelCombination(char1, char2)) {
			return true;
		}
		String strToCmp = String.format("%c%c%c", char1, char1Next, char2);
		if (strToCmp.equals("ESS") || strToCmp.equals("YOU")
				|| strToCmp.equals("YUU") || strToCmp.equals("GNN")
				|| strToCmp.equals("PSS")) {
			return true;
		}
		strToCmp = String.format("%c%c%c", char1, char2, char2Next);
		if (strToCmp.equals("SES") || strToCmp.equals("UYO")
				|| strToCmp.equals("UYU") || strToCmp.equals("NGN")
				|| strToCmp.equals("SPS")) {
			return true;
		}
		strToCmp = String.format("%c%c", char1, char2);
		if (strToCmp.equals("CK") || strToCmp.equals("KC")
				|| strToCmp.equals("KQ") || strToCmp.equals("QK")
				|| strToCmp.equals("PF") || strToCmp.equals("FP")
				|| strToCmp.equals("GJ") || strToCmp.equals("JG")
				|| strToCmp.equals("BV") || strToCmp.equals("VB")
				|| strToCmp.equals("VW") || strToCmp.equals("WV")
				|| strToCmp.equals("BW") || strToCmp.equals("WB")
				|| strToCmp.equals("JZ") || strToCmp.equals("ZJ")
				|| strToCmp.equals("XZ") || strToCmp.equals("ZX")
				|| strToCmp.equals("XS") || strToCmp.equals("SX")
				|| strToCmp.equals("ZS") || strToCmp.equals("SZ")
				|| strToCmp.equals("SC") || strToCmp.equals("CS")
				|| strToCmp.equals("YU") || strToCmp.equals("UY")) {
			return true;
		} else {
			return false;
		}

	}

	/* following are not the nearest combinations of vowels */
	public boolean IsValidVowelCombination(char char1, char char2) {

		String strToCmp = String.format("%c%c", char1, char2);
		if (strToCmp.equals("AU") || strToCmp.equals("UA")
				|| strToCmp.equals("EU") || strToCmp.equals("UE")
				|| strToCmp.equals("IU") || strToCmp.equals("UI")
				|| strToCmp.equals("IO") || strToCmp.equals("OI")) {

			return false;
		} else {
			return true;
		}

	}

	public int asciiOf(char ch) {
		String strCh = String.format("%c", ch);
		int asciiChr1 = (int) strCh.charAt(0);
		return asciiChr1;
	}

	/*
	 * Cost Insertion for the Target Character and cost deletion for the source
	 * character
	 */
	private double costInsertDelete_EN(char thischar, char prevchar,
			char nextchar) {

		if (asciiOf(thischar) < 65 || asciiOf(thischar) > 90) {
			return 0.05;
		} else if ((thischar == prevchar) && thischar != 'E' && thischar != 'O') {
			// System.out.println("true");
			return 0.15;
		}
		if (IsVowel(thischar)) {
			// System.out.println("true");
			return 0.25;
		} else if (IsChkWY(thischar) == true) {
			// System.out.println("true 2");
			return 0.5;
		} else if (thischar == 'H') {
			// System.out.println("true 3");
			if (IsChkBC(thischar) == true) {
				// System.out.println("true 4");
				return 0.15;
			} else {
				return 0.25;
			}
		} else if (thischar == 'C' && IsChkSX(prevchar) == true) {
			// System.out.println("true 4");
			return 0.25;
		} else if (thischar == 'N' && IsConsonant(nextchar) == true) {
			// System.out.println("true 5");
			return 0.35;
		} else {
			return 1.0;
		}
	}

	/*
	 * cost of substituting source character with target character, only if the
	 * substitution is valid
	 */
	private double costsubstitution_EN(char chr1, char chr2) {
		int asciiChr1 = asciiOf(chr1);
		int asciiChr2 = asciiOf(chr2);

		if (chr1 == chr2) {
			return 0.0;
		} else if (IsVowel(chr1) && IsVowel(chr2)
				& IsValidVowelCombination(chr1, chr2)) {
			return 0.25;
		} else if (asciiChr1 < 65 || asciiChr1 > 90 || asciiChr2 < 65
				|| asciiChr2 > 90) {
			return 0.05;
		}

		String strToCmp = String.format("%c%c", chr1, chr2);
		if (strToCmp.equals("YI") || strToCmp.equals("IY")
				|| strToCmp.equals("RD") || strToCmp.equals("DR")
				|| strToCmp.equals("CK") || strToCmp.equals("KC")
				|| strToCmp.equals("CS") || strToCmp.equals("SC")
				|| strToCmp.equals("GJ") || strToCmp.equals("JG")
				|| strToCmp.equals("ZJ") || strToCmp.equals("JZ")
				|| strToCmp.equals("XZ") || strToCmp.equals("ZX")
				|| strToCmp.equals("XS") || strToCmp.equals("SX")
				|| strToCmp.equals("XJ") || strToCmp.equals("JS")
				|| strToCmp.equals("SZ") || strToCmp.equals("ZS")) {
			return 0.25;
		} else if (strToCmp.equals("KQ") || strToCmp.equals("QK")
				|| strToCmp.equals("WV") || strToCmp.equals("VW")
				|| strToCmp.equals("BV") || strToCmp.equals("VB")
				|| strToCmp.equals("PF") || strToCmp.equals("FP")) {
			return 0.15;
		} else {
			return 1.0;
		}
	}

	/* cost of swapping two characters */
	private double costswapping_EN(char chr1, char chr2) {
		// System.out.println("IN costswapping_EN");

		if (IsVowel(chr1) && IsVowel(chr2)
				&& IsValidVowelCombination(chr1, chr2)) {
			return 0.15;
		} else if ((IsVowel(chr1) && IsLetter(chr2) == true)
				|| (IsVowel(chr2) && ChkValue(chr1) == true)) {
			return 0.25;
		} else if ((IsVowel(chr1) && chr2 == 'R')
				|| (IsVowel(chr2) && chr2 == 'R')) {
			return 0.15;
		}

		else {
			return 0.35;
		}
	}

	private boolean ChkValue(char ch1) {

		boolean chkflg = false;
		String pattern = "^[BCDFGHJKLMNPQSTVWXYZ]$";
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(ch1);
		Matcher m = replace.matcher(tmp);
		chkflg = m.matches();
		return chkflg;
	}

	private boolean IsVowel(char chr) {

		boolean functionreturnvalue = false;
		functionreturnvalue = false;
		String pattern = "^[AEIOU]$";
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(chr);
		Matcher m = replace.matcher(tmp);
		functionreturnvalue = m.matches();
		if (functionreturnvalue == true) {
			return true;
		}
		return functionreturnvalue;
	}

	private boolean IsLetter(char char2) {

		boolean chkflag = false;

		String pattern = "^[BCDFGHJKLMNPQSTVWXYZ]$";
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(char2);
		Matcher m = replace.matcher(tmp);
		chkflag = m.matches();
		// System.out.println("chkflag" + chkflag);
		return chkflag;
	}

	private boolean IsConsonant(char chr) {

		boolean functionreturnvalue = false;
		functionreturnvalue = false;
		String pattern = "^[AEIOU]$";
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(chr);
		Matcher m = replace.matcher(tmp);
		functionreturnvalue = m.matches();
		if (functionreturnvalue == true)
			return true;
		return functionreturnvalue;
	}

	private boolean IsChkWY(char chr) {

		boolean chkflag = false;
		String pattern = "^[WY]$";
		Pattern replace = Pattern.compile(pattern);
		String trmp = Character.toString(chr);
		Matcher m = replace.matcher(trmp);
		chkflag = m.matches();

		return chkflag;
	}

	private boolean IsChkBC(char chr) {

		boolean chkflg = false;

		String pattern = "^[BCDGKPJS]$";
		// System.out.println("In IsChkBC");
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(chr);
		Matcher m = replace.matcher(tmp);
		chkflg = m.matches();

		return chkflg;
	}

	private boolean IsChkSX(char prevchr) {

		boolean chkflg = false;

		String pattern = "^[SX]$";
		// System.out.println("In IsChkSX");
		Pattern replace = Pattern.compile(pattern);
		String tmp = Character.toString(prevchr);
		// System.out.println("value of ISchk" + tmp);
		Matcher m = replace.matcher(tmp);
		chkflg = m.matches();

		return chkflg;
	}

	private double Min(double[] values) {
		if (values.length > 2)
			System.out.println("length to find min ->" + values.length);
		double min_value = values[0];
		for (int i = 1; i < values.length; i++) {
			if (min_value > values[i]) {
				min_value = values[i];
			}
		}
		return min_value;
	}

	public double[] resizeArray(double[] arry, int count) {
		double[] d = new double[count];
		if (count > arry.length) {
			count = arry.length;
		}
		for (int k = 0; k < count; k++) {
			d[k] = arry[k];
		}
		return d;
	}

}

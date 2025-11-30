/**
 * Introduction to Computer Science 2026, Ariel University,
 * Ex1: arrays, static functions and JUnit
 * https://docs.google.com/document/d/1GcNQht9rsVVSt153Y8pFPqXJVju56CY4/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 * <p>
 * This class represents a set of static methods on a polynomial functions - represented as an array of doubles.
 * The array {0.1, 0, -3, 0.2} represents the following polynomial function: 0.2x^3-3x^2+0.1
 * This is the main Class you should implement (see "add your code below")
 *
 * @author boaz.benmoshe
 */
public class Ex1 {
    /**
     * Epsilon value for numerical computation, it serves as a "close enough" threshold.
     */
    public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
    /**
     * The zero polynomial function is represented as an array with a single (0) entry.
     */
    public static final double[] ZERO = {0};

    /**
     * Computes the f(x) value of the polynomial function at x.
     *
     * @param poly - polynomial function
     * @param x
     * @return f(x) - the polynomial function value at x.
     */
    public static double f(double[] poly, double x) {
        double ans = 0;
        int len = poly.length;
        double pow = 1;
        for (int i = 0; i < len; i++) {
            ans += poly[i] * pow;
            pow *= x;
        }
        return ans;
    }

    /**
     * Given a polynomial function (p), a range [x1,x2] and an epsilon eps.
     * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps,
     * assuming p(x1)*p(x2) <= 0.
     * This function should be implemented recursively.
     *
     * @param p   - the polynomial function
     * @param x1  - minimal value of the range
     * @param x2  - maximal value of the range
     * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
     * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
     */
    public static double root_rec(double[] p, double x1, double x2, double eps) {
        double f1 = f(p, x1);
        double x12 = (x1 + x2) / 2;
        double f12 = f(p, x12);
        if (Math.abs(f12) < eps) {
            return x12;
        }
        if (f12 * f1 <= 0) {
            return root_rec(p, x1, x12, eps);
        } else {
            return root_rec(p, x12, x2, eps);
        }
    }

    /**
     * This function computes a polynomial representation from a set of 2D points on the polynom.
     * The solution is based on: //	http://stackoverflow.com/questions/717762/how-to-calculate-the-vertex-of-a-parabola-given-three-points
     * Note: this function only works for a set of points containing up to 3 points, else returns null.
     *
     * @param xx
     * @param yy
     * @return an array of doubles representing the coefficients of the polynom.
     */
    public static double[] PolynomFromPoints(double[] xx, double[] yy) {
        double[] ans = null;
        int lx = xx.length;
        int ly = yy.length;
        if (xx != null && yy != null && lx == ly && lx > 1 && lx < 4) {
            if (lx == 2) return polynom2Points(xx, yy);
            if (lx == 3) return polynom3Points(xx, yy);
        }
        return ans;
    }

    public static double[] polynom2Points(double[] xx, double[] yy) {
        double a = (yy[1] - yy[0]) / (xx[1] - xx[0]);
        double b = yy[0] - a * xx[0];
        double[] Polynom = new double[]{b, a};
        return Polynom;
    }

    public static double[] polynom3Points(double[] xx, double[] yy) {
        double x1 = xx[0], x2 = xx[1], x3 = xx[2], y1 = yy[0], y2 = yy[1], y3 = yy[2];
        double denom = (x1 - x2) * (x1 - x3) * (x2 - x3);
        double A = (x3 * (y2 - y1) + x2 * (y1 - y3) + x1 * (y3 - y2)) / denom;
        double B = (x3 * x3 * (y1 - y2) + x2 * x2 * (y3 - y1) + x1 * x1 * (y2 - y3)) / denom;
        double C = (x2 * x3 * (x2 - x3) * y1 + x3 * x1 * (x3 - x1) * y2 + x1 * x2 * (x1 - x2) * y3) / denom;
        double[] Polynom = new double[]{C, B, A};
        return Polynom;
    }

    /**
     * Two polynomials functions are equal if and only if they have the same values f(x) for n+1 values of x,
     * where n is the max degree (over p1, p2) - up to an epsilon (aka EPS) value.
     *
     * @param p1 first polynomial function
     * @param p2 second polynomial function
     * @return true iff p1 represents the same polynomial function as p2.
     */
    public static boolean equals(double[] p1, double[] p2) {
        boolean ans = true;

        if (p1.length > p2.length) {
            p2 = fixArr(p1, p2);
        }
        if (p1.length < p2.length) {
            p1 = fixArr(p2, p1);
        }
        int len = p1.length;
        for (int i = 0; i < len; i++) {
            if (Math.abs(p1[i] - p2[i]) <= EPS) {
            } else {
                return false;
            }
        }
        return ans;
    }

    public static double[] fixArr(double[] bigger, double[] smaller) {
        int lenB = bigger.length;
        int lenS = smaller.length;
        double[] temp = new double[lenB];
        for (int i = 0; i < lenB; i++) {
            if (i < lenS) {
                temp[i] = smaller[i];
            } else temp[i] = 0;
        }
        return temp;
    }

    /**
     * Computes a String representing the polynomial function.
     * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
     *
     * @param poly the polynomial function represented as an array of doubles
     * @return String representing the polynomial function:
     */
    public static String poly(double[] poly) {
        String ans = "";
        String[] fixed = new String[poly.length];
        fixed = fixedPoly(poly);
        int len = poly.length;
        if (poly.length == 0) {
            ans = "0";
        } else {
            for (int i = len - 1; i >= 0; i--) {
                if (poly[i] == 0) {
                } else if (i == 0) {
                    ans += fixed[i];
                } else if (i == 1) {
                    ans += fixed[i] + "x";
                } else {
                    ans += fixed[i] + "x^" + i;
                }
            }
        }
        return ans;
    }

    public static String[] fixedPoly(double[] poly) {
        String[] ans = new String[poly.length];
        for (int i = 0; i < ans.length; i++) {
            if (i == ans.length - 1) {
                ans[i] = "" + poly[i];
                break;
            }
            if (poly[i] >= 0) {
                ans[i] = " +" + poly[i];
            } else {
                ans[i] = " " + poly[i];
            }
        }
        return ans;
    }

    /**
     * Given two polynomial functions (p1,p2), a range [x1,x2] and an epsilon eps. This function computes an x value (x1<=x<=x2)
     * for which |p1(x) -p2(x)| < eps, assuming (p1(x1)-p2(x1)) * (p1(x2)-p2(x2)) <= 0.
     *
     * @param p1  - first polynomial function
     * @param p2  - second polynomial function
     * @param x1  - minimal value of the range
     * @param x2  - maximal value of the range
     * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
     * @return an x value (x1<=x<=x2) for which |p1(x) - p2(x)| < eps.
     */
    public static double sameValue(double[] p1, double[] p2, double x1, double x2, double eps) {
        double ans = x1;
        double f1 = f(p1, x1) - f(p2, x1);
        double mid = (x1 + x2) / 2;
        double fmid = f(p1, mid) - f(p2, mid);
        if (Math.abs(fmid) < eps) {
            return mid;
        }
        if (f1 * fmid <= 0) {
            return sameValue(p1, p2, x1, mid, eps);
        } else return sameValue(p1, p2, mid, x2, eps);
    }

    /**
     * Given a polynomial function (p), a range [x1,x2] and an integer with the number (n) of sample points.
     * This function computes an approximation of the length of the function between f(x1) and f(x2)
     * using n inner sample points and computing the segment-path between them.
     * assuming x1 < x2.
     * This function should be implemented iteratively (none recursive).
     *
     * @param p                - the polynomial function
     * @param x1               - minimal value of the range
     * @param x2               - maximal value of the range
     * @param numberOfSegments - (A positive integer value (1,2,...).
     * @return the length approximation of the function between f(x1) and f(x2).
     */
    public static double length(double[] p, double x1, double x2, int numberOfSegments) {
        double ans = 0;
        double dis = (x2 - x1) / numberOfSegments;
        double lastX = x1;
        double lastY = f(p, lastX);
        double currentX, currentY , segments;
        for (int i = 0; i < numberOfSegments; i++) {
            currentX = lastX+dis;
            currentY = f(p, currentX);
            segments = Math.sqrt(Math.pow(currentX-lastX, 2) + Math.pow(currentY-lastY, 2));
            ans += segments;
            lastX = currentX;
            lastY = currentY;
        }
        return ans;
    }

    /**
     * Given two polynomial functions (p1,p2), a range [x1,x2] and an integer representing the number of Trapezoids between the functions (number of samples in on each polynom).
     * This function computes an approximation of the area between the polynomial functions within the x-range.
     * The area is computed using Riemann's like integral (https://en.wikipedia.org/wiki/Riemann_integral)
     *
     * @param p1                - first polynomial function
     * @param p2                - second polynomial function
     * @param x1                - minimal value of the range
     * @param x2                - maximal value of the range
     * @param numberOfTrapezoid - a natural number representing the number of Trapezoids between x1 and x2.
     * @return the approximated area between the two polynomial functions within the [x1,x2] range.
     *
     */
    public static double area(double[] p1, double[] p2, double x1, double x2, int numberOfTrapezoid) {
        double ans = 0;
        double dis = (x2 - x1) / numberOfTrapezoid;
        double lastX = x1;
        double lastY = Math.abs(f(p1, lastX) - f(p2, lastX));
        double currentX, currentY , area;
        for (int i = 0; i < numberOfTrapezoid; i++) {
            currentX = lastX+dis;
            currentY = Math.abs(f(p1, currentX) - f(p2, currentX));
            area = (currentY+lastY)/2 * dis;
            ans += area;
            lastX = currentX;
            lastY = currentY;
        }
        return ans;
    }

    /**
     * This function computes the array representation of a polynomial function from a String
     * representation. Note:given a polynomial function represented as a double array,
     * getPolynomFromString(poly(p)) should return an array equals to p.
     *
     * @param p - a String representing polynomial function.
     * @return
     */
    public static double[] getPolynomFromString(String p) {
        p = p.replace(" ", "");//  -1.0x^2+3.0x+2.0
        p = p.replace("-", "+-");
        if (p.charAt(0) == '+') {
            p = p.substring(1);
        }
        String[] splitted = p.split("\\+");
        double[] polynom = makeArr(splitted);
        double coefficient=0;
        int pow = 0 , indexX,indexPow;
        String split;
        for (int i = 0; i < splitted.length; i++) {
            split = splitted[i];
            if(!split.equals("")){
                if (split.contains("x^")) {
                    indexX = split.indexOf("x");
                    indexPow = split.indexOf("^");
                    String start = split.substring(0, indexX);
                    if (start.equals("") || start.equals("+")) coefficient = 1;
                    else if (start.equals("-")) coefficient = -1;
                    else coefficient = Double.parseDouble(start);
                    pow = Integer.parseInt(split.substring(indexPow + 1));
                }
                else if (split.contains("x")) {
                    indexX = split.indexOf("x");
                    String c = split.substring(0, indexX);
                    if (c.equals("") || c.equals("+")) coefficient = 1;
                    else if (c.equals("-")) coefficient = -1;
                    else coefficient = Double.parseDouble(c);
                    pow = 1;
                }
                else {
                    coefficient = Double.parseDouble(split);
                    pow = 0;
                }
                polynom[pow] += coefficient;
            }
        }
        return polynom;
    }
    public static double[] makeArr(String[] splitted) {
            int maxPow = 0;
            for (int i = 0; i < splitted.length; i++) {
                String temp = splitted[i];

                if (temp.contains("^")) {
                    int pow = Integer.parseInt (temp.substring(temp.indexOf("^") + 1));
                    if (pow > maxPow) maxPow = pow;
                }
                else if (temp.contains("x")) {
                    if (maxPow < 1) maxPow = 1;
                }
            }
            double[] ans = new double[maxPow + 1];
            return ans;
    }

    /**
     * This function computes the polynomial function which is the sum of two polynomial functions (p1,p2)
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] add(double[] p1, double[] p2) {
        double[] ans = ZERO;//
        /** add you code below

         /////////////////// */
        return ans;
    }

    /**
     * This function computes the polynomial function which is the multiplication of two polynoms (p1,p2)
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] mul(double[] p1, double[] p2) {
        double[] ans = ZERO;//
        /** add you code below

         /////////////////// */
        return ans;
    }

    /**
     * This function computes the derivative of the p0 polynomial function.
     *
     * @param po
     * @return
     */
    public static double[] derivative(double[] po) {
        double[] ans = ZERO;//
        /** add you code below

         /////////////////// */
        return ans;
    }
}

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
     * <p>
     * In this function we calculate the value of the polynomial at a given x.
     * - how dose it work?
     *   1. each index in the array represents the coefficient of x^i.
     *      So we go through the array from i = 0 and up.
     *   2. we keep a variable called "pow" that always holds the current x^i.
     *      (It starts as 1 because x^0 = 1), and in every loop we multiply it by x
     *      so next time it becomes x^1, x^2, x^3...
     *   3. Inside the loop we just take the coefficient poly[i] and multiply it
     *      by the current power and add everything to "ans" :)
     *   4. and lastly we return "ans" which is the polynomial function's value at x.
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
     * The idea is based on the fact that if p(x1) * p(x2) <= 0,
     * then there is a root somewhere between x1 and x2.
     *
     * - How does it work?
     *   1. First, we calculate f(x1) and also the midpoint x12 = (x1 + x2) / 2.
     *   2. we compute f(x12) because this is the point we are checking right now.
     *   3. If |f(x12)| < eps, then we basically found our root so we return x12.
     *   4. Otherwise we check where the sign changes:
     *      - If f(x12) * f(x1) <= 0, the root is in [x1, x12] so we recursively search that half.
     *      - else the root must be in [x12, x2]  so we recursively search that half instead.
     *   5. The range gets smaller and smaller until we hit a point where f(x) is close enough to zero.
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
     * This function is supposed to take a few points on the XY-plane
     * and return the polynomial that passes through them.
     *
     * - How does it work?
     *   1. we check that both arrays (xx and yy) exist and have the same size.
     *   2. Then we check how many points we got.
     *   3. If it's exactly 2 points then we call "polynom2Points".
     *   4. If it's exactly 3 points then we call "polynom3Points".
     *   5. Otherwise the input is not supported so the function returns null.
     *
     * - How does "polynom2Points" work?
     *   1. we compute the slope "a" using the classic formula a = (y2 - y1) / (x2 - x1)
     *   2. Then I find "b" by plugging one of the points: b = y1 - a * x1
     *   3. I return the polynomial as {b, a} (which means b is the constant term, a is the x^1 coefficient).
     *
     * - How does "polynom3Points" work?
     *   Here we get 3 points which defines a unique parabola of the form y = A*x^2 + B*x + C
     *   So our goal is basically to find the values of A, B, and C that make the parabola pass through all 3 points.
     *   1. I rename the variables (x1,x2,x3,y1,y2,y3) just to make my life easier.
     *   2. we compute a denominator that appears in all the formulas.
     *      (It’s part of the math that solves a system of 3 equations for A, B, C).
     *   3. Then we calculate A, B, and C using the closed-form equations.
     *      It's a bit of algebra nothing special just long formulas :)
     *      (and tbh I took them from the link that you guys added tehehe :) ).
     *   4. Finally we return the polynomial as {C, B, A} (C is for x^0, B for x^1, A for x^2).
     *   And now congrats we have the parabola.
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
        double denominator = (x1 - x2) * (x1 - x3) * (x2 - x3);
        double A = (x3 * (y2 - y1) + x2 * (y1 - y3) + x1 * (y3 - y2)) / denominator;
        double B = (x3 * x3 * (y1 - y2) + x2 * x2 * (y3 - y1) + x1 * x1 * (y2 - y3)) / denominator;
        double C = (x2 * x3 * (x2 - x3) * y1 + x3 * x1 * (x3 - x1) * y2 + x1 * x2 * (x1 - x2) * y3) / denominator;
        double[] Polynom = new double[]{C, B, A};
        return Polynom;
    }

    /**
     * Two polynomials functions are equal if and only if they have the same values f(x) for n+1 values of x,
     * where n is the max degree (over p1, p2) - up to an epsilon (aka EPS) value.
     *
     * This function checks if two polynomial functions are actually the same.
     * But here’s the thing sometimes the polynomial array has extra zeros at the end
     * (you know… like {2, 3, 0, 0, 0} which is basically just 2 + 3x).
     *
     * So before I even compare anything, I clean both polynomials.(I learned this the hard way lol ).
     * - How does it work?
     *   1. First we call "trimZeros" for both p1 and p2.
     *      What trimZeros does is simply remove the useless zeros at the end of the array,
     *      so we only keep the real coefficients that matter.
     *   2. After trimming, if the two arrays don’t have the same length,
     *      that means the polynomials can't be equal so obv we return false.
     *   3. If they do have the same length, we start comparing coefficient by coefficient.
     *      In every index i, we check the difference |a[i] - b[i]| > EPS
     *      If yes they are not equal (like not even close) sooo we return false.
     *   4. If the function finishes the whole loop and nothing failed,
     *      that means the polynomials are the same (up to epsilon),
     *      so we return true :).
     *      And that's literally it.
     *
     * trimZeros :
     * This helper function removes unnecessary zeros at the end of the polynomial.
     * - How does it work?
     *   1. we start from the last index and move backwards as long as the coefficient is zero.
     *      Basically we are asking: “is the end of this array just useless zeros?”
     *   2. When we reach the last non zero coefficient we stop.
     *   3. Then we create a new array exactly from index 0 to that last non zero index.
     *   4. we copy the valid coefficients into it.
     *   5. Finally we return the cleaned array.
     *
     * @param p1 first polynomial function
     * @param p2 second polynomial function
     * @return true iff p1 represents the same polynomial function as p2.
     */
    public static boolean equals(double[] p1, double[] p2) {
        double[] a = trimZeros(p1);
        double[] b = trimZeros(p2);
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > EPS) {
                return false;
            }
        }
        return true;
    }

    public static double[] trimZeros(double[] p) {
        int last = p.length - 1;
        while (last > 0 && p[last] == 0) {
            last--;
        }
        double[] ans = new double[last + 1];
        for (int i = 0; i <= last; i++) {
            ans[i] = p[i];
        }
        return ans;
    }

    /**
     * Computes a String representing the polynomial function.
     * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
     *
     * This function builds a String that represents the polynomial nicely.
     * Like if I have the array {2, 0, 3.1, -1.2} the result will look like:  "-1.2x^3 +3.1x^2 +2.0".
     * Basically were taking the coefficients and turning them into something readable.
     *
     * - How does it work?
     *   1. First we call "fixedPoly" which is a helper function that fixes the signs
     *      so the output looks clean (adds a space, "+" or keeps the "-").
     *      we store the result in a String array called "fixed".
     *   2. If the polynomial is empty (length 0) we just return "0".
     *      (Because there's nothing to print lol).
     *   3. Otherwise, we loop from the highest power to the lowest.
     *      For each index i:
     *      - if poly[i] == 0 → I skip it because zero terms don’t show up in the final string.
     *      - if i == 0  this is the constant term, so we add fixed[i] only.
     *      - if i == 1  this is the x term, so we add fixed[i] + "x".
     *      - if i >= 2  this is x^i, so we add fixed[i] + "x^" + i.
     *   4. At the end we return the whole String which represents the polynomial.
     *   So yeah its basically formatting and making the polynomial look nice :)
     *
     * fixedPoly :
     * This helper function fixes the signs for each coefficient before printing.
     * It makes sure the string looks like: " +3.1", " -2.0", etc.
     * - How does it work?
     *   1. we create a String array with the same size as the polynomial.
     *   2. we loop over all coefficients.
     *   3. For the last coefficient (highest degree), I don’t add "+" or extra spaces,
     *      because I want the polynomial to start clean.
     *   4. For the rest:
     *      - If the number is positive we add " +" in front.
     *      - If the number is negative we add a space then the number (the minus is already there).
     *   5. Finally, we return the array with all the formatted coefficients.
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
     * Given two polynomial functions (p1,p2), a range [x1,x2] and an epsilon eps.
     * This function computes an x value (x1<=x<=x2)
     * for which |p1(x) -p2(x)| < eps, assuming (p1(x1)-p2(x1)) * (p1(x2)-p2(x2)) <= 0.
     *
     * This function finds a point x inside the range [x1, x2] where the two
     * polynomials p1 and p2 basically have the same value meaning the
     * difference |p1(x) - p2(x)| becomes smaller than eps.
     *
     * - How does it work?
     *   1. First we calculate the difference at the start of the interval: f1 = f(p1, x1) - f(p2, x1)
     *   2. Then we find the midpoint (mid = (x1 + x2) / 2)
     *   3. we calculate the difference ( fmid = f(p1, mid) - f(p2, mid) ).
     *   4. If |fmid| < eps that means the values of p1 and p2 are basically the same at this x
     *      (within the allowed error) so we just return mid.
     *      (Mission accomplisheddddddddddd)
     *   5. If not we check where the sign changes:
     *      if (f1 * fmid <= 0) This means the solution is somewhere between x1 and mid,
     *      so I call the function again on the left half.
     *      Otherwise, the solution must be in the right half.
     *   6. And because we are doing this recursively, the interval keeps shrinking
     *      until we hit a point where the difference is small enough.
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
     * This function computes an approximation of the length of the function between f(x1) and f(x2)
     * by splitting the range into small straight-line segments and adding their lengths up.
     * (Basically we approximate the curve using tiny pieces.)
     *
     * - How does it work?
     *   1. First we calculate the distance between sample points:
     *      dis = (x2 - x1) / numberOfSegments
     *      This tells us how far apart each segment is on the x-axis.
     *   2. we start from x1 and compute f(x1). I call them lastX and lastY
     *      because in every loop we will move forward and compare with the “previous” point.
     *   3. Then we loop numberOfSegments times.
     *      In each round:
     *      - we move to the next x value: currentX = lastX + dis
     *      - we compute the function's value there.
     *   4. Now we have two points:
     *      (lastX, lastY)  and  (currentX, currentY)
     *      we treat the curve between them as a straight line,
     *      so we compute the distance using the classic formula: segment = sqrt( (delta x)^2 + (delta y)^2 ).
     *   5. we add this segment length to the total "ans".
     *   6. Finally we update lastX and lastY to move forward and repeat.
     *   7. After the loop ends "ans" should contains the full approximation of the curve length between x1 and x2.
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
        double currentX, currentY, segments;
        for (int i = 0; i < numberOfSegments; i++) {
            currentX = lastX + dis;
            currentY = f(p, currentX);
            segments = Math.sqrt(Math.pow(currentX - lastX, 2) + Math.pow(currentY - lastY, 2));
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
     * This function computes an approximation of the length of the function between f(x1) and f(x2)
     * by splitting the range into small straight line segments and adding their lengths up.
     * (Basically we approximate the curve using tiny pieces.)
     *
     * - How does it work?
     *   1. First we calculate the distance between sample points:
     *      dis = (x2 - x1) / numberOfSegments
     *      This tells us how far apart each segment is on the x axis.
     *   2. we start from x1 and compute f(x1). we call them lastX and lastY
     *      because in every loop we will move forward and compare with the “previous” point.
     *   3. Then we loop numberOfSegments times.
     *      In each round:
     *      - we move to the next x value (currentX = lastX + dis)
     *      - and we compute the function value there (currentY = f(p, currentX))
     *   4. Now we have two points:
     *     (lastX, lastY)  and  (currentX, currentY)
     *     we treat the curve between them as a straight line,
     *     so we compute the distance using the formula.
     *   5. we add this segment length to the total "ans".
     *   6. Finally we update lastX and lastY to move forward and repeat.
     *   7. After the loop ends, "ans" contains the full approximation of the curve length between x1 and x2.
     *
     * @param p1                - first polynomial function
     * @param p2                - second polynomial function
     * @param x1                - minimal value of the range
     * @param x2                - maximal value of the range
     * @param numberOfTrapezoid - a natural number representing the number of Trapezoids between x1 and x2.
     * @return the approximated area between the two polynomial functions within the [x1,x2] range.
     */
    public static double area(double[] p1, double[] p2, double x1, double x2, int numberOfTrapezoid) {
        double ans = 0;
        double dis = (x2 - x1) / numberOfTrapezoid;
        double lastX = x1;
        double lastY = Math.abs(f(p1, lastX) - f(p2, lastX));
        double currentX, currentY, area;
        for (int i = 0; i < numberOfTrapezoid; i++) {
            currentX = lastX + dis;
            currentY = Math.abs(f(p1, currentX) - f(p2, currentX));
            area = (currentY + lastY) / 2 * dis;
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
     * (For reference: I used this website to check some of the String methods)
     * https://www.geeksforgeeks.org/java/java-string-methods/
     *
     * This function takes a String that represents a polynomial and turns it back
     * into the double[] array form.
     * Basically, if we have a String like "-1.0x^2 + 3.0x + 2.0",
     * we wanna convert it back to something like: {2.0, 3.0, -1.0}
     *
     * - How does it work?
     *   1. we remove all the spaces (p = p.replace(" ", "")) so everything becomes clean, like:  -1.0x^2+3.0x+2.0
     *   2. Then we replace every "-" with "+-"  (p = p.replace("-", "+-"))
     *      This lets us split the polynomial easily later.
     *   3. Sometimes we get a string that starts with a "+" (because of the replace) so if that's the case, we remove it.
     *   4. we split the whole string at the "+" signs so each piece in the array is one term like:
     *      "-1.0x^2", "3.0x", "2.0".
     *   5. Before we start filling the polynomial array, we call makeArr.
     *      This function checks what the highest power is (like x^2, x^5…)
     *      and then creates a double[] with the correct size.
     *   6. Now the fun part we loop over all the splitted terms and figure out what's the coefficient? and what's the power?
     *      we check three cases:
     *      term contains "x^" this means something like 4x^3 .
     *      term contains "x"  this means something like 5x (power is 1).
     *      else it's just a number (constant term, power 0).
     *
     *   7. we extract the coefficient:
     *      - if the part before x is "", "+" it means 1 .
     *      - if it's "-" it means -1 .
     *      - else we just parse the number :) .
     *
     *   8. we extract the power:
     *      - if there is "^" we read the number after it
     *      - if only "x" then the power is 1.
     *      - if no x at all then power is 0.
     *   9. Finally, we add the coefficient to the right spot polynom[pow] += coefficient
     *   10. When we finish the loop, the polynom array is ready and I return it.
     *
     * makeArr :
     * This helper function creates the array that will hold the coefficients.
     * Its size depends on the highest power found in the splitted terms.
     * - How does it work?
     *   1. we start by assuming the highest power is 0.
     *   2. we loop over all terms:
     *      - If we see something like x^5 then the power is 5 so we update maxPow.
     *      - If we see something like x then at least the power is 1.
     *   3. At the end, we create an array of size maxPow + 1
     *      (because if the highest power is 3, I need spots for: x^0, x^1, x^2, x^3)
     *      And then we return it.
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
        double coefficient = 0;
        int pow = 0, indexX, indexPow;
        String split;
        for (int i = 0; i < splitted.length; i++) {
            split = splitted[i];
            if (!split.equals("")) {
                if (split.contains("x^")) {
                    indexX = split.indexOf("x");
                    indexPow = split.indexOf("^");
                    String start = split.substring(0, indexX);
                    if (start.equals("") || start.equals("+")) coefficient = 1;
                    else if (start.equals("-")) coefficient = -1;
                    else coefficient = Double.parseDouble(start);
                    pow = Integer.parseInt(split.substring(indexPow + 1));
                } else if (split.contains("x")) {
                    indexX = split.indexOf("x");
                    String c = split.substring(0, indexX);
                    if (c.equals("") || c.equals("+")) coefficient = 1;
                    else if (c.equals("-")) coefficient = -1;
                    else coefficient = Double.parseDouble(c);
                    pow = 1;
                } else {
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
                int pow = Integer.parseInt(temp.substring(temp.indexOf("^") + 1));
                if (pow > maxPow) maxPow = pow;
            } else if (temp.contains("x")) {
                if (maxPow < 1) maxPow = 1;
            }
        }
        double[] ans = new double[maxPow + 1];
        return ans;
    }

    /**
     * This function computes the polynomial function which is the sum of two polynomial functions (p1,p2)
     *
     * This function adds two polynomial functions together.
     * Basically, we take p1 and p2 and return a new polynomial where each
     * coefficient is the sum of the matching coefficients in both arrays.
     * - How does it work?
     *   1. First, we check if the two arrays have the same length.
     *      If not, we fix the smaller one using "fixArr".
     *      forexample:
     *      p1 = {2, 3, 1}
     *      p2 = {5, 4}
     *      After fixing p2 it becomes {5, 4, 0}
     *   2. Then we create a new array "ans" with the same length as the (now) equal arrays.
     *   3. we loop over all indexes:
     *      ans[i] = p1[i] + p2[i] (which is basically how polynomial addition works: we add same power terms.)
     *   4. At the end, we return "ans" which is the polynomial representing p1(x) + p2(x).
     *      And that's it super simple :).
     *
     * fixArr :
     * This helper function makes the smaller polynomial match the size of the bigger one.
     * - How does "fixArr" work?
     *   1. we create a temporary array with the same size as the bigger array.
     *   2. we copy all coefficients from the smaller array into it.
     *   3. For the extra spots we just fill them with zeros.
     *      and we return the array.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] add(double[] p1, double[] p2) {
        if (p1.length > p2.length) {
            p2 = fixArr(p1, p2);
        }
        if (p1.length < p2.length) {
            p1 = fixArr(p2, p1);
        }
        double[] ans = new double[p1.length];//

        for (int i = 0; i < p1.length; i++) {
            ans[i] = p1[i] + p2[i];
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
     * This function computes the polynomial function which is the multiplication of two polynoms (p1,p2)
     *
     * This function computes the multiplication of two polynomials (p1 and p2).
     * Basically we take every term from p1 and multiply it with every term from p2,
     * then we put the result in the correct power spot.
     * - How does it work?
     *   1. First we create the answer array
     *   2 . Then we use two nested loops:
     *       Outer loop goes over every coefficient in p1.
     *       Inner loop goes over every coefficient in p2.
     *   3. For each pair (i, j), we multiply the terms p1[i] * p2[j]
     *   4. The result belongs in ans[i + j] because when you multiply x^i * x^j you get x^(i + j).
     *   5. we keep adding to ans[i + j] because different pairs (i,j) might have the same resulting power.
     *   6. At the end, we return the ans array.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double[] mul(double[] p1, double[] p2) {
        double[] ans = new double[p1.length + p2.length - 1];
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p2.length; j++) {
                ans[i + j] += p1[i] * p2[j];
            }
        }
        return ans;
    }

    /**
     * This function computes the derivative of the p0 polynomial function.
     *
     * - How does it work?
     *   1. First we check if the polynomial length is <= 1.
     *      If so, that means the polynomial is constant (or empty),
     *      and the derivative is just ZERO (because the slope of a constant is always 0).
     *      So we return ZERO right away.
     *   2. Otherwise we create an array "ans" that is one cell smaller than the original.
     *      Why? Because the derivative of the highest term x^n
     *      becomes something with power n-1, so the array shrinks by one.
     *   3. we loop from i = 1 to the end of the polynomial.
     *      we skip i = 0 because the derivative of a constant term is 0.
     *   4. Inside the loop ans[i - 1] = i * po[i] his matches the formula for derivatives.
     *   5. After finishing the loop, "ans" now represents the full derivative so we return it.
     *
     * @param po
     * @return
     */
    public static double[] derivative(double[] po) {
        double[] ans = new double[po.length - 1];
        if (po.length <= 1) {
            return ZERO;
        }
        for (int i = 1; i < po.length; i++) {
            ans[i - 1] = i * po[i];
        }
        return ans;
    }
}

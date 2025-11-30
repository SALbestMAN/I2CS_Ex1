import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  * Introduction to Computer Science 2026, Ariel University,
 *  * Ex1: arrays, static functions and JUnit
 *
 * This JUnit class represents a JUnit (unit testing) for Ex1-
 * It contains few testing functions for the polynomial functions as define in Ex1.
 * Note: you should add additional JUnit testing functions to this class.
 *
 * @author boaz.ben-moshe
 */

class Ex1Test {
	static final double[] P1 ={2,0,3, -1,0}, P2 = {0.1,0,1, 0.1,3};
	static double[] po1 = {2,2}, po2 = {-3, 0.61, 0.2};;
	static double[] po3 = {2,1,-0.7, -0.02,0.02};
	static double[] po4 = {-3, 0.61, 0.2};
	
 	@Test
	/**
	 * Tests that f(x) == poly(x).
	 */
	void testF() {
		double fx0 = Ex1.f(po1, 0);
		double fx1 = Ex1.f(po1, 1);
		double fx2 = Ex1.f(po1, 2);
		assertEquals(fx0, 2, Ex1.EPS);
		assertEquals(fx1, 4, Ex1.EPS);
		assertEquals(fx2, 6, Ex1.EPS);
	}

	@Test
	/**
	 * Tests that p1(x) + p2(x) == (p1+p2)(x)
	 */
	void testF2() {
		double x = Math.PI;
		double[] po12 = Ex1.add(po1, po2);
		double f1x = Ex1.f(po1, x);
		double f2x = Ex1.f(po2, x);
		double f12x = Ex1.f(po12, x);
		assertEquals(f1x + f2x, f12x, Ex1.EPS);
	}
	@Test
	/**
	 * Tests that p1+p2+ (-1*p2) == p1
	 */
	void testAdd() {
		double[] p12 = Ex1.add(po1, po2);
		double[] minus1 = {-1};
		double[] pp2 = Ex1.mul(po2, minus1);
		double[] p1 = Ex1.add(p12, pp2);
		assertTrue(Ex1.equals(p1, po1));
	}
	@Test
	/**
	 * Tests that p1+p2 == p2+p1
	 */
	void testAdd2() {
		double[] p12 = Ex1.add(po1, po2);
		double[] p21 = Ex1.add(po2, po1);
		assertTrue(Ex1.equals(p12, p21));
	}
	@Test
	/**
	 * Tests that p1+0 == p1
	 */
	void testAdd3() {
		double[] p1 = Ex1.add(po1, Ex1.ZERO);
		assertTrue(Ex1.equals(p1, po1));
	}
	@Test
	/**
	 * Tests that p1*0 == 0
	 */
	void testMul1() {
		double[] p1 = Ex1.mul(po1, Ex1.ZERO);
		assertTrue(Ex1.equals(p1, Ex1.ZERO));
	}
	@Test
	/**
	 * Tests that p1*p2 == p2*p1
	 */
	void testMul2() {
		double[] p12 = Ex1.mul(po1, po2);
		double[] p21 = Ex1.mul(po2, po1);
		assertTrue(Ex1.equals(p12, p21));
	}
	@Test
	/**
	 * Tests that p1(x) * p2(x) = (p1*p2)(x),
	 */
	void testMulDoubleArrayDoubleArray() {
		double[] xx = {0,1,2,3,4.1,-15.2222};
		double[] p12 = Ex1.mul(po1, po2);
		for(int i = 0;i<xx.length;i=i+1) {
			double x = xx[i];
			double f1x = Ex1.f(po1, x);
			double f2x = Ex1.f(po2, x);
			double f12x = Ex1.f(p12, x);
			assertEquals(f12x, f1x*f2x, Ex1.EPS);
		}
	}
	@Test
	/**
	 * Tests a simple derivative examples - till ZERO.
	 */
	void testDerivativeArrayDoubleArray() {
		double[] p = {1,2,3}; // 3X^2+2x+1
		double[] pt = {2,6}; // 6x+2
		double[] dp1 = Ex1.derivative(p); // 2x + 6
		double[] dp2 = Ex1.derivative(dp1); // 2
		double[] dp3 = Ex1.derivative(dp2); // 0
		double[] dp4 = Ex1.derivative(dp3); // 0
		assertTrue(Ex1.equals(dp1, pt));
		assertTrue(Ex1.equals(Ex1.ZERO, dp3));
		assertTrue(Ex1.equals(dp4, dp3));
	}
	@Test
	/** 
	 * Tests the parsing of a polynom in a String like form.
	 */
	public void testFromString() {
		double[] p = {-1.1,2.3,3.1}; // 3.1X^2+ 2.3x -1.1
		String sp2 = "3.1x^2 +2.3x -1.1";
		String sp = Ex1.poly(p);
		double[] p1 = Ex1.getPolynomFromString(sp);
		double[] p2 = Ex1.getPolynomFromString(sp2);
		boolean isSame1 = Ex1.equals(p1, p);
		boolean isSame2 = Ex1.equals(p2, p);
		if(!isSame1) {fail();}
		if(!isSame2) {fail();}
		assertEquals(sp, Ex1.poly(p1));
	}
	@Test
	/**
	 * Tests the equality of pairs of arrays.
	 */
	public void testEquals() {
		double[][] d1 = {{0}, {1}, {1,2,0,0}};
		double[][] d2 = {Ex1.ZERO, {1+ Ex1.EPS/2}, {1,2}};
		double[][] xx = {{-2* Ex1.EPS}, {1+ Ex1.EPS*1.2}, {1,2, Ex1.EPS/2}};
		for(int i=0;i<d1.length;i=i+1) {
			assertTrue(Ex1.equals(d1[i], d2[i]));
		}
		for(int i=0;i<d1.length;i=i+1) {
			assertFalse(Ex1.equals(d1[i], xx[i]));
		}
	}

	@Test
	/**
	 * Tests is the sameValue function is symmetric.
	 */
	public void testSameValue2() {
		double x1=-4, x2=0;
		double rs1 = Ex1.sameValue(po1,po2, x1, x2, Ex1.EPS);
		double rs2 = Ex1.sameValue(po2,po1, x1, x2, Ex1.EPS);
		assertEquals(rs1,rs2, Ex1.EPS);
	}
	@Test
	/**
	 * Test the area function - it should be symmetric.
	 */
	public void testArea() {
		double x1=-4, x2=0;
		double a1 = Ex1.area(po1, po2, x1, x2, 100);
		double a2 = Ex1.area(po2, po1, x1, x2, 100);
		assertEquals(a1,a2, Ex1.EPS);
}
	@Test
	/**
	 * Test the area f1(x)=0, f2(x)=x;
	 */
	public void testArea2() {
		double[] po_a = Ex1.ZERO;
		double[] po_b = {0,1};
		double x1 = -1;
		double x2 = 2;
		double a1 = Ex1.area(po_a,po_b, x1, x2, 1);
		double a2 = Ex1.area(po_a,po_b, x1, x2, 2);
		double a3 = Ex1.area(po_a,po_b, x1, x2, 3);
		double a100 = Ex1.area(po_a,po_b, x1, x2, 100);
		double area =2.5;
		assertEquals(a1,area, Ex1.EPS);
		assertEquals(a2,area, Ex1.EPS);
		assertEquals(a3,area, Ex1.EPS);
		assertEquals(a100,area, Ex1.EPS);
	}
	@Test
	/**
	 * Test the area function.
	 */
	public void testArea3() {
		double[] po_a = {2,1,-0.7, -0.02,0.02};
		double[] po_b = {6, 0.1, -0.2};
		double x1 = Ex1.sameValue(po_a,po_b, -10,-5, Ex1.EPS);
		double a1 = Ex1.area(po_a,po_b, x1, 6, 8);
		double area = 58.5658;
		assertEquals(a1,area, Ex1.EPS);
	}





	@Test
	/**
	 * Tests that f(x) returns 0 for the zero polynomial.
	 */
	void testF_zeroPoly() {
		double[] p = {0,0,0,0};
		assertEquals(0, Ex1.f(p, 0), Ex1.EPS);
		assertEquals(0, Ex1.f(p, 10), Ex1.EPS);
		assertEquals(0, Ex1.f(p, -5), Ex1.EPS);
	}
	@Test
	/**
	 * Tests f(x) with negative x values.
	 */
	void testF_negativeX() {
		double[] p = {3, -2, 1};
		assertEquals(6, Ex1.f(p, -1), Ex1.EPS);
		assertEquals(11, Ex1.f(p, -2), Ex1.EPS);
	}

	@Test
	/**
	 * Tests that f(x) returns 0 for the zero polynomial.
	 */
	void testF_zeroPolynomial() {
		double[] p = {0,0,0,0};
		assertEquals(0, Ex1.f(p, 0), Ex1.EPS);
		assertEquals(0, Ex1.f(p, 10), Ex1.EPS);
		assertEquals(0, Ex1.f(p, -5), Ex1.EPS);
	}





	@Test
	/**
	 * Tests root_rec on a simple linear polynomial p(x)=x-3.
	 */
	void testRootRec_1() {
		double[] p = {-3, 1};
		double root = Ex1.root_rec(p, 0, 5, Ex1.EPS);
		assertEquals(3, root, Ex1.EPS);
	}
	@Test
	/**
	 * Tests root_rec on a quadratic polynomial p(x)=x^2-4.
	 */
	void testRootRec_2() {
		double[] p = {-4, 0, 1}; // x^2 - 4
		double root = Ex1.root_rec(p, 1, 3, Ex1.EPS);
		assertEquals(2, root, Ex1.EPS);
	}





	@Test
	/**
	 * Tests PolynomFromPoints with exactly 2 points.
	 */
	void testPolynomFromPoints_twoPoints() {
		double[] xx = {0, 1};
		double[] yy = {2, 4};
		double[] expected = {2, 2};
		double[] poly = Ex1.PolynomFromPoints(xx, yy);
		assertTrue(Ex1.equals(expected, poly));
	}
	@Test
	/**
	 * Tests PolynomFromPoints with 3 points (should call polynom3Points).
	 */
	void testPolynomFromPoints_threePoints() {
		double[] xx = {0, 1, 2};
		double[] yy = {1, 3, 7}; // from y = x^2 + x + 1
		double[] expected = {1, 1, 1}; // C=1, B=1, A=1
		double[] poly = Ex1.PolynomFromPoints(xx, yy);
		assertTrue(Ex1.equals(expected, poly));
	}





	@Test
	/**
	 * Tests equals for polynomials that become identical after trimming zeros.
	 */
	void testEquals_1() {
		double[] p1 = {2, 3, 0, 0};
		double[] p2 = {2, 3};// should be true (obv lol)
		assertTrue(Ex1.equals(p1, p2));
	}

	@Test
	/**
	 * Tests equals when coefficients differ more than EPS.
	 */
	void testEquals_2() {
		double[] p1 = {1, 2, 3};
		double[] p2 = {1, 2 + Ex1.EPS*2, 3}; // should be false
		assertFalse(Ex1.equals(p1, p2));
	}





	@Test
	/**
	 * Tests poly() with a polynomial that has positive and negative coefficients.
	 */
	void testPoly_mixedSigns() {
		double[] p = {2, -3, 1};
		           // 1x^2 -3x +2
		String expected = "1.0x^2 -3.0x +2.0";
		assertEquals(expected, Ex1.poly(p));
	}

	@Test
	/**
	 * Tests poly() skipping zero coefficients in the middle.
	 */
	void testPoly_withZeros() {
		double[] p = {5, 0, -2, 0, 1};
		           // 1x^4 -2x^2 +5
		String expected = "1.0x^4 -2.0x^2 +5.0";
		assertEquals(expected, Ex1.poly(p));
	}





	@Test
/**
 * Tests sameValue on two simple linear polynomials that intersect at x=3.
 */
	void testSameValue_simple() {
		double[] p1 = {0, 1};
		              // x
		double[] p2 = {-3, 2};
		            // 2x - 3
		double sameValPoint = Ex1.sameValue(p1, p2, 0, 5, Ex1.EPS);
		assertEquals(3, sameValPoint, Ex1.EPS);
	}
	@Test
	/**
	 * Tests that sameValue returns the same point when swapping p1 and p2.
	 */
	void testSameValue_samesame() {
		double[] p1 = {1, -1};
	      	     // = -x + 1
		double[] p2 = {-1, 1};
		          // = x - 1
		// both functions intersect exactly at x = 1
		double x1 = Ex1.sameValue(p1, p2, -5, 5, Ex1.EPS);
		double x2 = Ex1.sameValue(p2, p1, -5, 5, Ex1.EPS);

		assertEquals(1, x1, Ex1.EPS);
		assertEquals(1, x2, Ex1.EPS);
		assertEquals(x1, x2, Ex1.EPS);
	}





	@Test
	/**
	 * Tests length() on f(x)=x .
	 */
	void testLength_1() {
		double[] p = {0,1};
		             // x
		// distance from (0,0) to (3,3)
		double expected = Math.sqrt(18);
		double len = Ex1.length(p, 0, 3, 10);
		assertEquals(expected, len, Ex1.EPS);
	}

	@Test
	/**
	 * Tests length() on f(x)=x+2.
	 * The exact distance from (0,2) to (4,6) is sqrt(32).
	 */
	void testLength_2() {
		double[] p = {2, 1};
		          // =1*x + 2
		// distance from (0,2) to (4,6)
		double expected = Math.sqrt(32);
		double len = Ex1.length(p, 0, 4, 20);
		assertEquals(expected, len, Ex1.EPS);
	}





	@Test
	/**
	 * Tests area() when the difference between p1 and p2 is constant.
	 */
	void testArea_simple() {
		double[] p1 = {5};
		            // 5
		double[] p2 = {2};
		            // 2
		double expected = 12;  // (5-2)*4
		double a = Ex1.area(p1, p2, 0, 4, 10);
		assertEquals(expected, a, Ex1.EPS);
	}

	@Test
	/**
	 * Tests area() between f(x)=x+3 and g(x)=x^2-2 on the interval [0,3].
	 * The exact area is 10.5.
	 */
	void testArea_simple2() {
		double[] p1 = {3, 1};
		            // x+3
		double[] p2 = {-2, 0, 1};
		            // x^2-2
		double expected = 10.5;
		double a = Ex1.area(p1, p2, 0, 3, 200);

		assertEquals(expected, a, Ex1.EPS);
	}





	@Test
	/**
	 * Tests getPolynomFromString on 3.0x^2 +2.0x -1.0
	 */
	void testGetPolynomFromString_1() {
		String s = "3.0x^2 +2.0x -1.0";
		double[] expected = {-1.0, 2.0, 3.0};
		                 // {  C ,  B ,  A }
		double[] result = Ex1.getPolynomFromString(s);
		assertTrue(Ex1.equals(expected, result));
	}

	@Test
	/**
	 * Tests getPolynomFromString on "   4x^3   -   2x   +   1  ".
	 */
	void testGetPolynomFromString_2() {
		String s = "   4x^3   -   2x   +   1  ";
		double[] expected = {1, -2, 0, 4};   // 4x^3 -2x +1
		double[] result = Ex1.getPolynomFromString(s);
		assertTrue(Ex1.equals(expected, result));
	}





	@Test
	/**
	 * Tests add() with polynomials 3x^2 +2x +1 and 5x +4  .
	 */
	void testAdd_1() {
		double[] p1 = {1, 2, 3};
		            // 3x^2 +2x +1
		double[] p2 = {4, 5};
		            // 5x +4   → should become {4,5,0}
		double[] expected = {5, 7, 3}; // (1+4), (2+5), (3+0)
		double[] result = Ex1.add(p1, p2);
		assertTrue(Ex1.equals(expected, result));
	}

	@Test
/**
 * Tests add() with polynomials x^2 -2x +3 and -2x^2 +4x -1
 */
	void testAdd_2() {
		double[] p1 = {3, -2, 1};
		            // x^2 -2x +3
		double[] p2 = {-1, 4, -2};
		           // -2x^2 +4x -1
		double[] expected = {2, 2, -1}; // (3-2), (-2+4), (1-2)
		double[] result = Ex1.add(p1, p2);
		assertTrue(Ex1.equals(expected, result));
	}





	@Test
	/**
	 * Tests mul() on 3x + 2 and x + 1.
	 */
	void testMul_1() {
		double[] p1 = {2, 3};
		            // 3x + 2
		double[] p2 = {1, 1};
		             // x + 1
		double[] expected = {2, 5, 3}; // 2 +5x +3x^2
		double[] result = Ex1.mul(p1, p2);
		assertTrue(Ex1.equals(expected, result));
	}

	@Test
	/**
	 * Tests mul() on x and x^2 + 1.
	 */
	void testMul_2() {
		double[] p1 = {0, 1};
		            // x
		double[] p2 = {1, 0, 1};
		            // x^2 + 1
		double[] expected = {0, 1, 0, 1}; // x + x^3
		double[] result = Ex1.mul(p1, p2);
		assertTrue(Ex1.equals(expected, result));
	}





	@Test
	/**
	 * Tests derivative() on 2x^2 -3x +5
	 */
	void testDerivative_1() {
		double[] p = {5, -3, 2};
		           // 2x^2 -3x +5
		double[] expected = {-3, 4}; // -3 +4x
		double[] result = Ex1.derivative(p);
		assertTrue(Ex1.equals(expected, result));
	}

	@Test
	/**
	 * Tests derivative() on 2x^2 -3x +5
	 */
	void testDerivative_2() {
		double[] p = {-4, 0, 7, -1};
		          // -x^3 +7x^2 +0x -4
		double[] expected = {0, 14, -3}; // 14x-3x^2
		double[] result = Ex1.derivative(p);
		assertTrue(Ex1.equals(expected, result));
	}
}

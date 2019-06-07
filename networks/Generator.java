package networks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;


public class Generator {
	protected double[] g0ps;
	protected double[] cumul;
	protected closedGen lambda;
	
	public interface closedGen{
		double p_k(double k);
	}
	public double operate(double k, closedGen cgen) {
		return cgen.p_k(k);
	}
	
	Generator() {
		g0ps = new double[] {1};
		cumul = cumul();
	}
	Generator(double[] g0ps) {
		this.g0ps = g0ps;
		cumul = cumul();
	}
	
	Generator(closedGen lambda, double[] kVals) {
		this.lambda = lambda;
		
		
		g0ps = new double[kVals.length];
		for (int i = 0; i < kVals.length; i++) {
			g0ps[i] = operate(kVals[i], lambda);
		}
	}
	public double closedEvaluate(double k) {
		return operate(k, lambda);
	}
	
	Generator(closedGen lambda, int num) {
		this.lambda = lambda;
		
		double sum = 0;
		g0ps = new double[num];
		for (int k = 0; k < num; k++) {
			g0ps[k] = operate(k, lambda);
			sum += g0ps[k];
		}
		if (sum > 1) {
			System.out.println("Warning: non-normalized generating function");
		}
		cumul = cumul();
	}
	
	public double[] cumul() {
		double[] cdf = new double[g0ps.length];
		double sum = 0;
		for (int k = 0; k < g0ps.length; k++) {
			sum += g0ps[k];
			cdf[k] = sum;
		}
		
		return cdf;
	}
	
	public int randomWeighted() {
		double random = Math.random();
		for (int i = 0; i < cumul.length; i++) {
			if (random < cumul[i]) {
				return i;
			}
		}
		return -1;
	}
	
	
	public double[] getg0ps() {
		return g0ps;
	}
	public double[] normalize() {
		if (g0ps.length == 0) {
			return g0ps;
		}
		double sum = Arrays.stream(g0ps).sum();
		g0ps = Arrays.stream(g0ps).map(i -> (double) i / sum).toArray();
		
		return g0ps;
	}
	public void setg0ps(double[] g0ps) {
		this.g0ps = g0ps;
		cumul = cumul();
	}
	
}

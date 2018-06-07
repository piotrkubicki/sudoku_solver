package sudoku_solver;

import java.util.Random;

public class Parameters {

	public static final int BOARD_SIZE = 9;
	public static final long RANDOM_SEED = System.currentTimeMillis();
	public static Random random = new Random(RANDOM_SEED);
	
	public static int populationSize = 200;
	public static double mutationRate = 0.1;
	public static int maxIterations = 10000000;

	public static Class optimizer = Backtracking.class;

}

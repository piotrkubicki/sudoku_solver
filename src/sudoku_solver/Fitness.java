package sudoku_solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Fitness {

	public static double calculateFitness(Sudoku sudoku) {
		double totalError = 0;
		
		// horizontal line
		for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
			List<Integer> line = new ArrayList<>();
			
			for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
				line.add(sudoku.gameboard[i][j].value);
			}
	
			Set<Integer> values = new HashSet<>(line);
//			System.out.println("ERROR H LINE " + i + ": " + (Parameters.BOARD_SIZE - values.size()));
			
			totalError += Parameters.BOARD_SIZE - values.size();
		}
		
		// vertical line
		for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
			List<Integer> line = new ArrayList<>();
			
			for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
				line.add(sudoku.gameboard[j][i].value);
			}
	
			Set<Integer> values = new HashSet<>(line);
//			System.out.println("ERROR V LINE " + i + ": " + (Parameters.BOARD_SIZE - values.size()));
			
			totalError += Parameters.BOARD_SIZE - values.size();
		}
		
		// inner squares
//		int xPos = 0;
//		int yPos = 0;
//		int xLimit = 3;
//		int yLimit = 3;
////		int section = 1;
//		
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				List<Integer> line = new ArrayList<>();
//
//				for (int k = xPos; k < xLimit; k++) {
//					for (int l = yPos; l < yLimit; l++) {
////						System.out.println("X: " + k + " Y: " + l);
//						line.add(sudoku.gameboard[k][l].value);
//					}
//				}
//				
//				yPos += 3;
//				yLimit += 3;
//				
//				Set<Integer> values = new HashSet<>(line);
////				System.out.println("Sector " + section + ": " + (Parameters.BOARD_SIZE - values.size()));
////				section++;
//				totalError += Parameters.BOARD_SIZE - values.size();
//			}
//			
//			xPos += 3;
//			xLimit += 3;
//			
//			yPos = 0;
//			yLimit = 3;
//		}
		
		return 1 - (totalError / 100);
	}
}

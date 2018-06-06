package sudoku_solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Backtracking extends Optimizer {

	public Sudoku sudoku;
	
	@Override
	public void execute() {
		sudoku = Parameters.problemInstance.clone();
		
		backtrack();
		
		sudoku.print();
	}
	
	private boolean backtrack() {
		int col = 0;
		int row = 0;
		
		boolean found = false;
		
		for (row = 0; row < Parameters.BOARD_SIZE; row++) {
			for (col = 0; col < Parameters.BOARD_SIZE; col++) {
				int val = sudoku.gameboard[row][col].value;
				
				if (val == 0) {
					found = true;
					break;
				}
			}
			
			if (found)
				break;
		}
		
		if (found == false)
			return true;
		
		for (int i = 1; i <= Parameters.BOARD_SIZE; i++) {
			
			if (checkValid(i, row, col)) {
				sudoku.gameboard[row][col].value = i;
					
				if (backtrack()) {
					return true;
				}

				sudoku.gameboard[row][col].value = 0;
			}
		}
		
		return false;
	}

	private boolean checkValid(int insertedVal, int row, int col) {
		// horizontal line
		List<Integer> line = new ArrayList<>();
		
		for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
			line.add(sudoku.gameboard[row][j].value);
			
		}

		if (line.contains(insertedVal)) {
			return false;
		}
		
		// vertical line
		line = new ArrayList<>();
		
		for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
			line.add(sudoku.gameboard[j][col].value);
		}

		if (line.contains(insertedVal)) {
			return false;
		}
		
		// inner squares
		int xPos = 0;
		int yPos = 0;
		
		if (row < 3) {
			xPos = 0;
		} else if (row >= 3 && row < 6) {
			xPos = 3;
		} else {
			xPos = 6;
		}
		
		if (col < 3) {
			yPos = 0;
		} else if (col >= 3 && col< 6) {
			yPos = 3;
		} else {
			yPos = 6;
		}
		
		line = new ArrayList<>();
		
		int xLimit = xPos + 3;
		int yLimit = yPos + 3;
		
		for (int i = xPos; i < xLimit; i++) {
			for (int j = yPos; j < yLimit; j++) {
				line.add(sudoku.gameboard[i][j].value);
			}
		}
		
		if (line.contains(insertedVal)) {
			return false;
		}
		
		return true;
	}
}

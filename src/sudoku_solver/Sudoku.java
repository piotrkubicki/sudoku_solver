package sudoku_solver;

public class Sudoku {
	
	public Field[][] gameboard;
	public double fitness;
	
	public Sudoku() {
		gameboard = new Field[Parameters.BOARD_SIZE][Parameters.BOARD_SIZE];
		fitness = 0;
	}
	
	public Sudoku(Field[][] gameboard) {
		this.gameboard = gameboard;
		fitness = 0;
	}
	
	public static Sudoku generate(Sudoku sudoku) {
		Field[][] gameboard = new Field[Parameters.BOARD_SIZE][Parameters.BOARD_SIZE];
		
		for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
			for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
				gameboard[i][j] = new Field();
				
				if (sudoku.gameboard[i][j].changeable) {
					gameboard[i][j].value = Parameters.random.nextInt(Parameters.BOARD_SIZE) + 1;
					gameboard[i][j].changeable = true;
				} else {
					gameboard[i][j].value = sudoku.gameboard[i][j].value;
					gameboard[i][j].changeable = false;
				}
			}
		}
		
		return new Sudoku(gameboard);
	}
	
	public Sudoku clone() {
		Sudoku clone = new Sudoku();
		
		for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
			for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
				clone.gameboard[i][j] = new Field();
				clone.gameboard[i][j].value = this.gameboard[i][j].value;
				clone.gameboard[i][j].changeable = this.gameboard[i][j].changeable;
			}
		}
		
		clone.fitness = this.fitness;
		
		return clone;
	}
	
	public void print() {
		String output = "";
		
		for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
			for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
				output += gameboard[i][j].value + "|";
			}
			
			output += "\n";
		}
		
		System.out.println(output);
	}
}

class Field {
	public int value;
	public boolean changeable;
}

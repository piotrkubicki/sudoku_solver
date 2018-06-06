package sudoku_solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;

public class App extends Observable implements Runnable {

	private String problemInstanceFilename = "/home/pz/Projects/sudoku_solver/sudoku_hard.sdk";
	
	@Override
	public void run() {
		prepareData();
		
		Optimizer optimizer = null;
		
		try {
			optimizer = (Optimizer) Parameters.optimizer.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		optimizer.execute();
	}

	private void prepareData() {
		Sudoku sudoku = new Sudoku();
		
		try {
			FileReader fr = new FileReader(problemInstanceFilename);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int lineCounter = 0;
			
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				
				for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
					int val = Integer.parseInt(values[i]);
					sudoku.gameboard[lineCounter][i] = new Field();
							
					if (val != 0) {
						sudoku.gameboard[lineCounter][i].value = val;
						sudoku.gameboard[lineCounter][i].changeable = false;
					} else {
						sudoku.gameboard[lineCounter][i].value = val;
						sudoku.gameboard[lineCounter][i].changeable = true;
					}
				}
				
				lineCounter++;
			}
			
			Parameters.problemInstance = sudoku.clone();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

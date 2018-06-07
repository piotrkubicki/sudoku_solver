package sudoku_solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Solver {

	private String problemInstanceFilename = "/home/pz/Projects/sudoku_solver/sudoku_hard.sdk";
	private final List<SudokuSolverListener> observers = new ArrayList<>();
	Sudoku sudoku;
	ExecutorService service;
	private boolean finished;
	
	public Solver() {
		service = Executors.newCachedThreadPool();
		sudoku = new Sudoku();
	}
	
	public boolean solve() {
		service.submit(new Runnable() {

			@Override
			public void run() {
//				prepareData();
				
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
				
				while (!finished) {
					finished = optimizer.execute(sudoku);
					
					fireEvent();
				}
			}
			
		});
		System.out.println("DONE");
		return finished;
	}
	
	private void fireEvent() {
		for (SudokuSolverListener listener : observers) {
			listener.onGameboardChange();
		}
	}
	
	public void addListener(SudokuSolverListener listener) {
		observers.add(listener);
	}
	
	public void shutdown() {
		service.shutdown();
	}

	public void prepareData() {
		
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
			
			fireEvent();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

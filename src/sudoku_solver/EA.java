package sudoku_solver;

import java.util.ArrayList;
import java.util.List;

public class EA extends Optimizer {

	public Sudoku sudoku;
	public List<Sudoku> population;
	public Sudoku best;
	
	private Operator selector;
	private Operator recombinator;
	private Operator mutator;
	private Operator inserter;
	
	@Override
	public boolean execute(Sudoku sudoku) {
		setOperators();
		this.sudoku = sudoku;
		
		generatePopulation();
		best = findBest();
		int iter = 0;
		
		while (best.fitness < 1.0 && iter < Parameters.maxIterations) {
			List<Sudoku> parents = selector.execute(population);
			List<Sudoku> childs = recombinator.execute(parents);
			List<Sudoku> mutatedChilds = mutator.execute(childs);
			population.addAll(mutatedChilds);
			population = inserter.execute(population);
			
			best = findBest();
			
			System.out.println("Fitness: " + best.fitness);
			
			iter++;
//			System.out.println("POPULATION");
//			for (Sudoku s : population) {
//				System.out.println(s.fitness);
//				s.print();
//				
//			}
//			System.out.println("END\n\n");
		}
		
		System.out.println("BEST Fitness: " + best.fitness);
		best.print();
		
		return true;
	}
	
	private void generatePopulation() {
		population = new ArrayList<>();
		
		for (int i = 0; i < Parameters.populationSize; i++) {
			population.add(Sudoku.generate(sudoku));
		}
	}
	
	private Sudoku findBest() {
		Sudoku currentBest = best;
		
		for (Sudoku sudoku : population) {
			if (currentBest == null) {
				currentBest = sudoku;
			} 
			else if (currentBest.fitness < sudoku.fitness) {
				currentBest = sudoku.clone();
			}
		}
		
		return currentBest;
	}
	
	private void setOperators() {
		selector = new TournamentSelection();
		recombinator = new UniformCrossover();
		mutator = new RandomValueMutation();
		inserter = new RemoveWorseInsertion();
	}

}

abstract class Operator {
	public abstract List<Sudoku> execute(List<Sudoku> population);
}

class TournamentSelection extends Operator {
	
	private int tournamentSize = 2;
	
	public List<Sudoku> execute(List<Sudoku> population) {
		List<Sudoku> selected = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			List<Sudoku> candidates = new ArrayList<>();
			
			for (int j = 0; j < tournamentSize; j++) {
				candidates.add(population.get(Parameters.random.nextInt(population.size())).clone());
			}
			
			selected.add(candidates.get(Parameters.random.nextInt(candidates.size())).clone());
		}
		
		return selected;
	}
}

class UniformCrossover extends Operator {
	
	public List<Sudoku> execute(List<Sudoku> population) {
		List<Sudoku> childs = new ArrayList<>();
		
		if (population.size() == 2) {
			Sudoku firstParent = population.get(0);
			Sudoku secondParent = population.get(1);
			
			for (int k = 0; k < 2; k++) {
				Sudoku child = new Sudoku();
				
				for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
					for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
						child.gameboard[i][j] = new Field();
						
						if (Parameters.random.nextDouble() > 0.5) {
							child.gameboard[i][j].value = secondParent.gameboard[i][j].value;
							child.gameboard[i][j].changeable = secondParent.gameboard[i][j].changeable;
						} else {
							child.gameboard[i][j].value = firstParent.gameboard[i][j].value;
							child.gameboard[i][j].changeable = firstParent.gameboard[i][j].changeable;
						}
					}
				}
				
				child.fitness = Fitness.calculateFitness(child);
				childs.add(child.clone());
			}
		} else {
			System.out.println("Recombination: incorrect population size");
		}
		
		return childs;
	}
}

class RandomValueMutation extends Operator {
	
	public List<Sudoku> execute(List<Sudoku> population) {
		List<Sudoku> childs = new ArrayList<>();
		
		for (Sudoku child : population) {
			Sudoku clone = child.clone();
			
			for (int i = 0; i < Parameters.BOARD_SIZE; i++) {
				for (int j = 0; j < Parameters.BOARD_SIZE; j++) {
					if (clone.gameboard[i][j].changeable && Parameters.random.nextDouble() < Parameters.mutationRate) {
						clone.gameboard[i][j].value = Parameters.random.nextInt(Parameters.BOARD_SIZE) + 1;
					}
				}
			}
			
			clone.fitness = Fitness.calculateFitness(clone);
			childs.add(clone.clone());
		}
		
		return childs;
	}
}

class RemoveWorseInsertion extends Operator {
	
	public List<Sudoku> execute(List<Sudoku> population) {
		Sudoku worse;
		
		while (population.size() > Parameters.populationSize) {
			List<Sudoku> candidates = new ArrayList<>();
			
			for (int i = 0; i < 5; i++) {
				candidates.add(population.get(Parameters.random.nextInt(population.size())));
			}
			
			worse = null;
			
			for (Sudoku member : candidates) {
				
				if (worse == null) {
					worse = member;
				} else {
					if (worse.fitness > member.fitness) {
						worse = member;
					}
				}
			}
			
			population.remove(worse);
		}
		
		return population;
	}
}

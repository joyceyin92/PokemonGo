package pokemon;

import java.util.*;

public class Solution {
	
	
	static class Pokemon {

	    final double attack;
	    final double defense;
	    final double stamina;
	    double level;

	    Pokemon(double attack, double defense, double stamina, double level) {
	        this.attack = attack;
	        this.defense = defense;
	        this.stamina = stamina;
	        this.level = level;
	    }
	}

	static class CPMultiplier {

	    private final double[] multipliers;

	    CPMultiplier(double[] multipliers) {
	        this.multipliers = multipliers;
	    }


	    double getMultiplierForLevel(double level) {
	        if (level > 40.0d) {
	            return multipliers[multipliers.length - 1];
	        }
	        return multipliers[(int) (level * 2 - 2)];
	    }
	}

	private static double sqrt(double a) {
	    return Math.sqrt(a);
	}

	private static double roundToPlaces(double d, int numDecimalPlaces) {
	    int scaleFactor = (int) Math.pow(10, numDecimalPlaces);
	    return ((double) Math.floor(d * scaleFactor)) / scaleFactor;
	}



	static double findMaximumCPGained(long numPowerUps, Pokemon[] pokemonArray, CPMultiplier cpMultiplierMapping) {
	    PriorityQueue<Pokemon> queue = new PriorityQueue<Pokemon>(new Comparator<Pokemon>(){
	        public int compare(Pokemon a, Pokemon b){
	            if(getCPGain(a,cpMultiplierMapping) > getCPGain(b,cpMultiplierMapping)) return -1;
				else if(getCPGain(a,cpMultiplierMapping) < getCPGain(b,cpMultiplierMapping)) return 1;
				else return 0;
	        }
	    });
	    for(int i = 0; i < pokemonArray.length; i++){
			queue.offer(pokemonArray[i]);
		}
		double maxGain = 0;
		while(numPowerUps > 0 && !queue.isEmpty()){
			Pokemon cur = queue.poll();
			double gain = getCPGain(cur,cpMultiplierMapping);
			maxGain += gain;
			numPowerUps--;
			if(cur.level < 40){
				Pokemon levelup = new Pokemon(cur.attack, cur.defense, cur.stamina, cur.level+0.5);
				queue.offer(levelup);
			}
		}
		return roundToPlaces(maxGain,8);
	}

	public static double getCPGain(Pokemon p,CPMultiplier cpMultiplierMapping){
		double before = p.attack * sqrt(p.defense * p.stamina) * Math.pow(cpMultiplierMapping.getMultiplierForLevel(p.level), 2) / 10;
	    double after = p.attack * sqrt(p.defense * p.stamina) * Math.pow(cpMultiplierMapping.getMultiplierForLevel(p.level+0.5), 2) / 10;
		return after - before;
	}



	static class PokeMap {

	    private final long[][] grid;

	    PokeMap(long[][] grid) {
	        this.grid = grid;
	    }
	    
	    public long getSizeX() {
	        return grid[0].length;
	    }
	    
	    public long getSizeY() {
	        return grid.length;
	    }

	    public long getValue(Location loc) {
	        return getValue(loc.getX(), loc.getY());
	    }

	    long getValue(int x, int y) {
	        if (y >= grid.length || x >= grid[y].length) {
	            throw new IllegalArgumentException("Requested coordinates outside of map");
	        }
	        return grid[y][x];
	    }


	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        String lineSeparator = System.getProperty("line.separator");
	        for (long[] row : grid) {
	            sb.append(Arrays.toString(row)).append(lineSeparator);
	        }
	        return sb.toString();
	    }
	}

	static class Location {

	    private final int x;
	    private final int y;

	    Location(int x, int y) {
	     this.x = x;
	     this.y = y;
	    }

	    int getY() {
	        return y;
	    }

	    int getX() {
	       return x;
	    }

	    @Override
	    public String toString() {
	        return "Location{" +
	              "x=" + x +
	             ", y=" + y +
	              '}';
	    }
	}

	
	private static double log10(double a) {
	    return Math.log10(a);
	}

	
	static double findMaximumXP(Location playerPosition, long numPokeBalls, PokeMap pokeMap) {
	    PriorityQueue<Double> queue = new PriorityQueue<Double>(new Comparator<Double>(){
	        public int compare(Double a, Double b){
	            if(a > b) return -1;
	            else if(a < b) return 1;
	            else return 0;
	        }
	    });
	    for(int i = 0; i < pokeMap.getSizeY(); i++){
	        for(int j = 0; j < pokeMap.getSizeX(); j++){
	            if(pokeMap.getValue(j,i) > 0){
	                double dist =sqrt(Math.pow((i-playerPosition.getY()),2)+Math.pow((j-playerPosition.getX()),2));
	                double cost = dist + log10(pokeMap.getValue(j,i));
	                double curGain = pokeMap.getValue(j,i) / cost;
	                queue.offer(curGain);
	            }
	        }
	    }
	    double gain = 0;
	    while(numPokeBalls > 0 && !queue.isEmpty()){
	        double tmp = queue.poll();
	        gain += tmp;
	        numPokeBalls--;
	    }
	    return gain == 0 ? 0.000 : roundToPlaces(gain,3);
	}




	/*public static void dfs(int PowerUp, int[][] pokeMon, double[] CPmultiplier, double gain, double maxGain){
		if(PowerUp == 0){
			if(gain > maxGain) maxGain = gain;
			return;
		}
		for(int i = 0; i<pokeMon.length; i++){
			int[][] pokeMonUpdate = new int[pokeMon.length][pokeMon[0].length];
			for(int k = 0; k < pokeMon.length; k++){
				for(int j = 0; j < pokeMon[0].length; j++){
					pokeMonUpdate[k][j] = pokeMon[k][j];
				}
			}
			pokeMonUpdate[i][3] ++; 
			if(pokeMonUpdate[i][3] <= CPmultiplier.length){
				double curGain = getCP(pokeMonUpdate[i],CPmultiplier) - getCP(pokeMon[i],CPmultiplier);
				dfs(PowerUp-1, pokeMonUpdate, CPmultiplier, gain+curGain, maxGain);
			}
		}
	}
	
	public static double getCP(int[] pokeMon, double[] CPmultiplier){
		return (double) pokeMon[0] * Math.sqrt(pokeMon[1]) * Math.sqrt(pokeMon[2]) * Math.pow(CPmultiplier[pokeMon[3]-1], 2) / 10;
	}*/
	
}





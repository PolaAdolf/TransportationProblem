package TransportationProblem;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class TransportationProblem {
	
	
    double []required;
    double []stock;
    double [][]cost;
    LinkedList<Variable> feasible = new LinkedList<Variable>();
    int stockSize;    
    int requiredSize;    
    
    public TransportationProblem(int stockSize, int requiredSize ){
        this.stockSize = stockSize;
        this.requiredSize = requiredSize;
        stock = new double[stockSize];
        required = new double[requiredSize];
        cost = new double[stockSize][requiredSize];
        for(int i=0; i < (requiredSize + stockSize -1); i++)
            feasible.add(new Variable());
    }
    
    public void setStock(double value, int index){
        stock[index] = value;        
    }
    
    public void setRequired(double value, int index){
        required[index] = value;        
    }
    
    
    public void setCost(double value, int stock, int required){
        cost[stock][required] = value;
    }
    
    /**
     * initializes the feasible solution list using the North-West Corner
     * @return time elapsed
     */
    
    public double northWestCorner() {
        long start = System.nanoTime();
        double min;        
        int k = 0; //feasible solutions counter
        
        //isSet is responsible for annotating cells that have been allocated
        boolean [][]isSet = new boolean[stockSize][requiredSize];        
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                    isSet[i][j] = false;
        
        //the for loop is responsible for iterating in the 'north-west' manner
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                if(!isSet[i][j]){
                    
                    //allocating stock in the proper manner
                    min = Math.min(required[j], stock[i]);
                    
                    feasible.get(k).setRequired(j);
                    feasible.get(k).setStock(i);
                    feasible.get(k).setValue(min);
                    k++;

                    required[j] -= min;
                    stock[i] -= min;
                    
                    //allocating null values in the removed row/column
                    if(stock[i] == 0)
                        for(int l = 0; l < requiredSize; l++)
                            isSet[i][l] = true;                    
                    else
                        for(int l = 0; l < stockSize; l++)
                            isSet[l][j] = true;
                }
        return (System.nanoTime() - start) * 1.0e-9;
    }
    
    
    public double getSolution(){
        double result = 0;
        for(Variable x: feasible){
            result += x.getValue() * cost[x.getStock()][x.getRequired()];
        }
        
        return result;
    
    }
    

    /**
     * @param args the command line arguments
     */
	public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please enter the problem size:");
        int s = scanner.nextInt();
        int r = scanner.nextInt();
        double x;
        TransportationProblem test = new TransportationProblem(s, r);
        
        System.out.println("Please enter the stocks capacity:");
        for (int i = 0; i < test.stockSize; i++){
            x = scanner.nextDouble();
            test.setStock(x, i);
        }
        
        System.out.println("Please enter the requirements:");
        for (int i = 0; i < test.requiredSize; i++){
            x = scanner.nextDouble();
            test.setRequired(x, i);
        }
        
        System.out.println("Please enter the transportation costs:");
        for (int i = 0; i < test.stockSize; i++)
            for (int j = 0; j < test.requiredSize; j++) {
                x = scanner.nextDouble();
                test.setCost(x, i, j);
            }
        
        test.northWestCorner();
        
        for(Variable t: test.feasible){
            System.out.println(t);        
        }
        
        System.out.println("Target function: " + test.getSolution());
        
    }

}

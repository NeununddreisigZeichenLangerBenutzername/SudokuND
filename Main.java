public class Main{
    public static void main(String[] args){
        SudokuND sudoku=new SudokuND(2,2);
        for(int a=0;a<Math.pow(sudoku.volume,sudoku.dimension);a++){
            String line="";
            for(int b=0;b<Math.pow(sudoku.volume,sudoku.dimension);b++){
                if(sudoku.puzzle.containsKey(a*(int)Math.pow(sudoku.volume,sudoku.dimension)+b)){
                    line=line+"["+sudoku.puzzle.get(a*(int)Math.pow(sudoku.volume,sudoku.dimension)+b)+"]";
                }else{
                    line=line+"[ ]";
                }
            }
            System.out.println(line);
        }
    }
}

package sample;

public class Life {
    static final boolean debug = false;
    private int h;
    private int w;
    private int[][] count;
    private boolean[][] cell;
    private boolean[][] cellNext;
    private boolean[][] condition = new boolean[2][8];;
    private int[] survive = {2,3};
    private int[] repro = {3};
    private int[][] initCondition = {survive,repro};
    int getH(){return h;}
    int getW(){return w;}
    Life(int h, int w){this.h=h;this.w=w;init();}
    boolean[][] getCell(){return cell;}
    void init(){
        initCount();
        initCell();
        initCellNext();
        initCondition();
        setCondition(initCondition);
    }
    void initCount(){
        count = new int[h][w];
        for (int i = 0; i < count.length; i++){
            for(int j = 0; j < count[i].length; j++){
                count[i][j] = 0;
            }
        }
    }
    void initCell(){
        cell = new boolean[h][w];
        for (int i = 0; i < cell.length; i++){
            for(int j = 0; j < cell[i].length; j++){
                cell[i][j] = false;
            }
        }
    }
    void initCellNext(){
        cellNext = new boolean[h][w];
        for (int i = 0; i < cellNext.length; i++){
            for(int j = 0; j < cellNext[i].length; j++){
                cellNext[i][j] = false;
            }
        }
    }
    void initCondition(){
        for (int i = 0; i < condition.length; i++){
            for(int j = 0; j < condition[i].length; j++){
                condition[i][j] = false;
            }
        }
    }
    void setAliveCell(int[]... in){
        for (int i = 0; i < in.length; i++){
            cell[in[i][0]][in[i][1]]=true;
        }
    }
    void revertCell(int[]... in){
        for (int i = 0; i < in.length; i++){
            cell[in[i][0]][in[i][1]]=!cell[in[i][0]][in[i][1]];
        }
    }
    void setCondition(int[][] in){
        initCondition();
        setSurvive(in[0]);
        setRepro(in[1]);
    }
    void setSurvive(int[] in){
        for (int i = 0; i < condition[0].length; i++){
            condition[0][i]=false;
        }
        for (int i = 0; i < in.length; i++){
            condition[0][in[i]-1]=true;
        }
    }
    void setRepro(int[] in){
        for (int i = 0; i < condition[1].length; i++){
            condition[1][i]=false;
        }
        for (int i = 0; i < in.length; i++){
            condition[1][in[i]-1]=true;
        }
    }
    String getConditionToText(){
        String out = "";
        out += "Survive: { ";
        for (int i = 0; i < condition[0].length; i++){
            if(condition[0][i])out+=(i+1) + " ";
        }
        //
        out += "}, Repro: { ";
        for (int i = 0; i < condition[1].length; i++){
            if(condition[1][i])out+=(i+1) + " ";
        }
        out += "}";
        return out;
    }
    void display(){
        for (int i = 0; i < cell.length; i++){
            for(int j = 0; j < cell[i].length; j++){
                if (debug)System.out.print(count[i][j]);
                if (cell[i][j])System.out.print("O ");
                else System.out.print("X ");
            }
            System.out.println();
        }
        System.out.println();
    }
    void refresh(){
        count();
        display();
        step();
    }
    void count(){
        for (int i = 0; i < count.length; i++){
            for(int j = 0; j < count[i].length; j++){
                int c = 0;
                int top = i-1;
                int bot = i+1;
                int left = j-1;
                int right = j+1;
                //
                if (i==0){top=count.length-1;}
                if (i==count.length-1){bot=0;}
                if (j==0){left=count[i].length-1;}
                if (j==count[i].length-1){right=0;}
                //handling axis
                if (cell[top][j])c++;
                if (cell[bot][j])c++;
                if (cell[i][left])c++;
                if (cell[i][right])c++;
                //handling diagonal
                if (cell[top][left])c++;
                if (cell[top][right])c++;
                if (cell[bot][left])c++;
                if (cell[bot][right])c++;
                //
                count[i][j]=c;
            }
        }
    }
    void step(){
        for (int i = 0; i < count.length; i++){
            for(int j = 0; j < count[i].length; j++){
                int c = count[i][j];
                //survive
                if (cell[i][j]){
                    if (c==0||!condition[0][c-1])cellNext[i][j]=false;
                    else cellNext[i][j]=true;
                }
                //repro
                else {
                    if (c==0||!condition[1][c-1])cellNext[i][j]=false;
                    else cellNext[i][j]=true;
                }
            }
        }
        for (int i = 0; i < count.length; i++){
            for(int j = 0; j < count[i].length; j++){
                cell[i][j]=cellNext[i][j];
            }
        }
    }
}

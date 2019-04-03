package filecontributesupport;

/*
@author MirageLyu
*
 */

public class LOC {
    private int addition;
    private int deletion;

    LOC(){
        addition = 0;
        deletion = 0;
    }

    LOC(int addition, int deletion){
        this.addition = addition;
        this.deletion = deletion;
    }

    public void addAddition(int diff){
        this.addition += diff;
    }
    public void addDeletion(int diff){
        this.deletion += diff;
    }


    public void setAddition(int addition) {
        this.addition = addition;
    }
    public void setDeletion(int deletion) {
        this.deletion = deletion;
    }
    public int getAddition() {
        return addition;
    }
    public int getDeletion() {
        return deletion;
    }
}

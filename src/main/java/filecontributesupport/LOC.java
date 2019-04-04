package filecontributesupport;

/*
@author MirageLyu
*
 */

public class LOC {
    private int addition;
    private int deletion;

    public LOC(){
        addition = 0;
        deletion = 0;
    }

    public LOC(int addition, int deletion){
        this.addition = addition;
        this.deletion = deletion;
    }

    public LOC addAddition(int diff){
        this.addition += diff;
        return this;
    }
    public LOC addDeletion(int diff){
        this.deletion += diff;
        return this;
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

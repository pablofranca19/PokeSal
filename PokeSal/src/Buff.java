public class Buff {
    private int atk;
    private int def;
    private int spd;

    public Buff(int atk, int def, int spd){
        this.atk = atk;
        this.def = def;
        this.spd = spd;
    }

    public int getAtk(){
        return atk;
    }
    public int getDef(){
        return def;
    }
    public int getSpd(){
        return spd;
    }
}

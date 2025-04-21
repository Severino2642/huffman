package mg.algorithme;

import java.util.ArrayList;
import java.util.List;

public class Source {
    String s;
    double probabiliter;
    boolean isFusionner;
    Source parent;
    List<Arete> aretes = new ArrayList<>();

    public Source() {
    }

    public Source(String s, double probabiliter) {
        this.s = s;
        this.probabiliter = probabiliter;
        this.initialize();
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public double getProbabiliter() {
        return probabiliter;
    }

    public void setProbabiliter(double probabiliter) {
        this.probabiliter = probabiliter;
    }

    public boolean isFusionner() {
        return isFusionner;
    }

    public void setFusionner(boolean fusionner) {
        isFusionner = fusionner;
    }

    public Source getParent() {
        return parent;
    }

    public void setParent(Source parent) {
        this.parent = parent;
    }

    public void initialize(){
        this.isFusionner = false;
        this.parent = null;
    }

    public List<Arete> getAretes() {
        return aretes;
    }

    public void setAretes(List<Arete> aretes) {
        this.aretes = aretes;
    }

    public Arete findArete(Source source) {
        for (Arete a : aretes) {
            if (a.getSources().contains(source) && a.getSources().contains(this)) {
                return a;
            }
        }

        return null;
    }
}

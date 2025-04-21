package mg.algorithme;

import java.util.ArrayList;
import java.util.List;

public class Arete {
    List<Source> sources = new ArrayList<Source>();
    int etiquettes;

    public Arete(List<Source> sources, int etiquettes) {
        this.sources = sources;
        this.etiquettes = etiquettes;
    }

    public Arete() {
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public int getEtiquettes() {
        return etiquettes;
    }

    public void setEtiquettes(int etiquettes) {
        this.etiquettes = etiquettes;
    }
}

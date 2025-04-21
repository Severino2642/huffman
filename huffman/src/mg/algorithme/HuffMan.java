package mg.algorithme;

import mg.util.StringAnalyser;

import java.util.ArrayList;
import java.util.List;

public class HuffMan {
    List<Source> noeuds = new ArrayList<>();
    List<Code> codes = new ArrayList<>();

    public List<Source> getNoeuds() {
        return noeuds;
    }

    public void setNoeuds(List<Source> noeuds) {
        this.noeuds = noeuds;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public Source fusionner (Source s1, Source s2) {
        // Fusion
        s1.setFusionner(true);
        s2.setFusionner(true);
        Source result = new Source(""+s1.getS()+s2.getS(),s1.getProbabiliter()+s2.getProbabiliter());
        result.setFusionner(false);
        s1.setParent(result);
        s2.setParent(result);

        // Creation des arestes
        Arete arete_gauche = new Arete();
        arete_gauche.setSources(List.of(s1,result));
        arete_gauche.setEtiquettes(0);
        s1.getAretes().add(arete_gauche);

        Arete arete_droite = new Arete();
        arete_droite.setSources(List.of(s2,result));
        arete_droite.setEtiquettes(1);
        s2.getAretes().add(arete_droite);

        result.getAretes().add(arete_gauche);
        result.getAretes().add(arete_droite);


        return result;
    }

    public boolean isRoot(List<Source> sources) {
        for (Source s : sources) {
            if (!s.isFusionner()) {
                return false;
            }
        }
        return true;
    }

    public Source [] getNotFusionner(List<Source> sources) {
        List<Source> result = new ArrayList<>();
        for (Source s : sources) {
            if (!s.isFusionner()) {
                result.add(s);
            }
        }
        return result.toArray(new Source[]{});
    }

    public Code [] algoHuffman (List<Source> sources){
        List<Code> codes = new ArrayList<>();
        Source [] init_sources = sources.toArray(new Source[]{});
        Source [] new_sources = this.getNotFusionner(sources);
        boolean isRoot = false;
        while (!isRoot) {
            if (new_sources.length > 1){
                this.triage(new_sources);
//                System.out.println(new_sources[0].getS() +" + "+new_sources[1].getS());
                sources.add(this.fusionner(new_sources[0],new_sources[1]));
                new_sources = this.getNotFusionner(sources);
            }
            if (new_sources.length == 1) {
                isRoot = true;
            }
        }

        int sizeInBit = 0;
        for (Source s : init_sources) {
            Code code = new Code();
            code.setSource(s);
            code.setCodeString(this.getCode(s));
            code.setCode(StringAnalyser.convertToByte(code.getCodeString()));
            code.setSize(code.getCodeString().length());
            if (sizeInBit < code.getSize()) {
                sizeInBit = code.getSize();
            }
            codes.add(code);
        }
        for (Code code : codes) {
            code.setSize(sizeInBit);
        }
        return codes.toArray(new Code[]{});
    }

    public String getCode (Source source){
        StringBuilder code = new StringBuilder();
        while (source.getParent() != null) {
            Arete arete = source.findArete(source.getParent());
            code.append(arete.getEtiquettes());
            source = source.getParent();
        }
        return code.reverse().toString();
    }

    public void triage(Source[] array) {
        if (array.length < 2) {
            return;
        }

        int mid = array.length / 2;
        Source[] left = new Source[mid];
        Source[] right = new Source[array.length - mid];

        // Diviser le tableau en deux moitiés
        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        // Tri récursif des deux moitiés
        triage(left);
        triage(right);
        // Fusion des deux moitiés triées
        merge(array, left, right);
    }

    // Fonction de fusion des deux sous-tableaux triés
    private void merge(Source [] array, Source[] left, Source[] right) {
        int i = 0, j = 0, k = 0;

        // Fusionner les deux sous-tableaux
        while (i < left.length && j < right.length) {
            if (left[i].getProbabiliter()<=right[j].getProbabiliter()) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Copier les éléments restants du tableau gauche (le cas échéant)
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Copier les éléments restants du tableau droit (le cas échéant)
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }
}

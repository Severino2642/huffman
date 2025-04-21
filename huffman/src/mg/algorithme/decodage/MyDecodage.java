package mg.algorithme.decodage;

import mg.algorithme.Code;

import java.util.ArrayList;
import java.util.List;

public class MyDecodage {

    public List<String> convetToLangages (Code [] codes){
        List<String> langages = new ArrayList<>();
        for (Code code : codes){
            langages.add(code.getCodeString());
        }
        return langages;
    }

    public List<String> formatToListString (char [] caratere){
        List<String> result = new ArrayList<>();
        for (char ch : caratere) {
            result.add(String.valueOf(ch));
        }
        return result;
    }

    public String formatToString (List<String> list){
        String result = "";
        for (String c : list) {
            result+=c;
        }
        return result;
    }

    public String checkInLangage (List<String> langages,String u){
        String result = null;
        for (String c : langages) {
            if (c.equals(u)) {
                result = c;
            }
        }
        return result;
    }

    public List<String> renouvellement (List<String> caratere,boolean isGauche){
        List<String> result = new ArrayList<>();
        if (!isGauche) {
            for (int i = caratere.size()-1; i >= 0 ; i--) {
                result.add(caratere.get(i));
            }
        }
        else {
            result.addAll(caratere);
        }
        return result;
    }

    public String formatToResult (List<String> list,boolean isGauche){
        String result = "";
        if (!isGauche) {
            for (int i = list.size()-1; i >= 0 ; i--) {
                if (!result.isEmpty()){
                    result+=" . ";
                }
                result += list.get(i);
            }
        }
        else {
            for (int i = 0; i < list.size() ; i++) {
                if (!result.isEmpty()){
                    result+=" . ";
                }
                result += list.get(i);
            }
        }
        return result;
    }

    public List<String> factorisation (List<String> langages,String mot) {
        List<String> result = new ArrayList<>();

        char [] caracteres = mot.toCharArray();
        List<String> words = this.formatToListString(caracteres);
        List<String> corbeille = new ArrayList<>();

        List<String> factorisation = new ArrayList<>();
        while (words.size() > 0) {
            String u = this.formatToString(words);
            String checkIn = this.checkInLangage(langages,u);
            if (checkIn!=null) {
                factorisation.add(checkIn);
                words = this.renouvellement(corbeille,false);
                corbeille.clear();
            }
            else {
                corbeille.add(words.get(words.size()-1));
                words.remove(words.size()-1);
            }
        }
        if (mot.equals(this.formatToString(factorisation))){
            result.add(this.formatToResult(factorisation,true));
        }

        words = this.formatToListString(caracteres);
        factorisation.clear();
        while (words.size() > 0) {
            String u = this.formatToString(words);
            String checkIn = this.checkInLangage(langages,u);
            if (checkIn!=null) {
                factorisation.add(checkIn);
                words = this.renouvellement(corbeille,true);
                corbeille.clear();
            }
            else {
                corbeille.add(words.get(0));
                words.remove(0);
            }
        }

        if (mot.equals(this.formatToString(factorisation))){
            String y = this.formatToResult(factorisation,false);
            if (!result.get(0).equals(y)){
                result.add(y);
            }
        }


        return result;
    }
}

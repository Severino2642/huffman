package mg.util;

import mg.algorithme.Code;
import mg.algorithme.HuffMan;
import mg.algorithme.Source;
import mg.algorithme.decodage.MyDecodage;

import java.util.ArrayList;
import java.util.List;

public class StringCodage {
    public String codage (String str,Code [] codes){
        char [] chars = str.toCharArray();
        List<String> list = new ArrayList<>();

        for (char ch : chars){
            Code code = this.findCode(codes,String.valueOf(ch));
            list.add(code.getCodeString());
        }

        return assemblage(list);
    }

    public String assemblage (List<String> strings){
        StringBuilder result = new StringBuilder();
        for (String string : strings){
            result.append(string);
        }
        return result.toString();
    }

    public String decode(String str,Code [] codes){
        String result = null;
        MyDecodage myDecodage = new MyDecodage();
        List<String> factorisations = myDecodage.factorisation(myDecodage.convetToLangages(codes),str);
        for (String f: factorisations){
            String [] chars = f.split(" . ");
            List<String> list = new ArrayList<>();

            for (String ch : chars){
                Code code = this.findCodeInverse(codes,ch);
                list.add(code.getSource().getS());
            }
            result = assemblage(list);
        }
        return result;
    }

    public Code findCode(Code [] codes,String source_name){
        for (Code code : codes) {
            if (code.getSource().getS().equals(source_name)) {
                return code;
            }
        }
        return null;
    }

    public Code findCodeInverse(Code [] codes,String c){
        for (Code code : codes) {
            if (code.getCodeString().equals(c)) {
                return code;
            }
        }
        return null;
    }
}

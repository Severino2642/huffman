package mg.util;

import mg.algorithme.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringAnalyser {

    public Map<Character,Integer> getCharFrequency(String text){
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    public List<Source> getSources(String str) {
        String txt = new String(str);
        List<Source> sources = new ArrayList<Source>();
        int nbCaractere = txt.toCharArray().length;
        Map<Character, Integer> freqMap = getCharFrequency(txt);
        for (Map.Entry<Character,Integer> data : freqMap.entrySet()){
            sources.add(new Source(String.valueOf(data.getKey()), (double) data.getValue() /nbCaractere));
        }
        return sources;
    }

    public static byte convertToByte(String stringBinary){
        byte code = 0;
        char [] chars = stringBinary.toCharArray();
        for (int i = chars.length-1; i >= 0; i--){
            code = (byte) ((code<<1) | (chars[i]));
        }
        return code;
    }

    public static String formatToString(byte code,int size){
        return String.format("%"+size+"s",Integer.toBinaryString(code&0xFF)).replace(' ', '0');
    }

}

package com.project.util;

import java.util.Locale;

public class StringSimilarity {
    public static boolean isMatch(String s1, String s2){
        String longer = s1;
        String shorter = s2;
        if(s1.length() < s2.length()){
            longer = s2;
            shorter = s1;
        }

        if(s1.equals(s2)){
            return true;
        }

        int i = 0;
        int maxLen = longer.length();
        while (i <maxLen-1){
            if(s1.charAt(i) == s2.charAt(0)){
                int minLen =  s2.length();
                int count = 0;
                for(int j=0; j<shorter.length(); j++){
                    if(s1.charAt(j+i) == s2.charAt(j)){
                        count+=1;
                    }
                }

                if(count == 0){
                    return false;
                }else{
                    double rate = count / (double)minLen;
                    if(rate >= 0.5){
                        return true;
                    }
                }
            }else{
                i++;
            }
        }
        return false;
    }

}

package utils;

public class FarsiConvertor {
    final  String regExlt = "&lt";
    final  String regExgt = "&gt";
    final  char SPACE_CHAR = 0x0020;


    public  String getUTFFromCMFarsi(String cmFarsiContent) {

        cmFarsiContent = cmFarsiContent.replaceAll(regExlt, "<");
        cmFarsiContent = cmFarsiContent.replaceAll(regExgt, ">");
        cmFarsiContent = correctDoubleR(cmFarsiContent);
        StringBuffer sb = new StringBuffer();

        for (int pointer = 0; pointer < cmFarsiContent.length(); pointer++) {
            if (cmFarsiContent.charAt(pointer) != '-') {
                if (cmFarsiContent.charAt(pointer) == 'Q') {
                    sb.append((char) 0x0644);
                    sb.append((char) 0x0627);
                } else sb.append(getUnicodeString(cmFarsiContent.charAt(pointer)));

            }
        }
        return  correctYa(sb.toString());
    }

    private String getUnicodeString(char cmChar) {
        switch (cmChar) {

            // Punctuation Marks
            case '&':
                return String.valueOf('*');
            case '/':
                return String.valueOf(SPACE_CHAR);
            case '{':
                return String.valueOf((char) 0x0698);
            case '[':
                return String.valueOf((char) 0x0698);
            case '!':
                return String.valueOf((char) 0x063a);
            case ':':
                return String.valueOf(SPACE_CHAR);
            case '.':
                return String.valueOf((char) 0x0627);
            case '$':
                return String.valueOf((char) 0x062B);
            case ',':
                return String.valueOf((char) 0x062D) + String.valueOf(SPACE_CHAR);
            case '#':
                return String.valueOf((char) 0x062F);
            case '<':
                return String.valueOf((char) 0x0627);
            case '*':
                return String.valueOf((char) 0x062c) + String.valueOf(SPACE_CHAR);
            case '%':
                return String.valueOf((char) 0x062d);
            case '@':
                return String.valueOf((char) 0x0630);
            case '(':
                return String.valueOf((char) 0x0628);
            case ')':
                return String.valueOf((char) 0x062c);
            case '_':
                return String.valueOf((char) 0x0645) + String.valueOf(SPACE_CHAR);
            case '\'':
                return String.valueOf((char) 0x0631);
            case '+':
                return String.valueOf((char) 0x067e);
            case ';':
                return String.valueOf((char) 0x0686) + String.valueOf(SPACE_CHAR);
            case '>':
                return String.valueOf((char) 0x062e) + String.valueOf(SPACE_CHAR);
            case '=':
                return String.valueOf((char) 0x0632);
            case '|':
                return String.valueOf((char) 0x062a);
            case '~':
                return String.valueOf((char) 0x0686);
            case '?':
                return String.valueOf((char) 0x062e);
            case '"':
                return String.valueOf((char) 0x0626);

                // Numbers
            case '0':
                return String.valueOf((char) 0x0660);
            case '1':
                return String.valueOf((char) 0x0661);
            case '2':
                return String.valueOf((char) 0x0662);
            case '3':
                return String.valueOf((char) 0x0663);
            case '4':
                return String.valueOf((char) 0x0664);
            case '5':
                return String.valueOf((char) 0x0665);
            case '6':
                return String.valueOf((char) 0x0666);
            case '7':
                return String.valueOf((char) 0x0667);
            case '8':
                return String.valueOf((char) 0x0668);
            case '9':
                return String.valueOf((char) 0x0669);

                // Alphabets
            case 'A':
                return String.valueOf((char) 0x0633);
            case 'J':
                return String.valueOf((char) 0x0641);
            case 'B':
                return String.valueOf((char) 0x0634);
            case 'K':
                return String.valueOf((char) 0x0642);
            case 'S':
                return String.valueOf((char) 0x0646)+ String.valueOf(SPACE_CHAR);
            case 'C':
                return String.valueOf((char) 0x0635);
            case 'L':
                return String.valueOf((char) 0x0642);
            case 'T':
                return String.valueOf((char) 0x0646);
            case 'D':
                return String.valueOf((char) 0x0636);
            case 'M':
                return String.valueOf((char) 0x0643);
            case 'U':
                return String.valueOf((char) 0x0648);
            case 'E':
                return String.valueOf((char) 0x0637);
            case 'N':
                return String.valueOf((char) 0x06af);
            case 'V':
                return String.valueOf((char) 0x0647)+ String.valueOf(SPACE_CHAR);
            case 'F':
                return String.valueOf((char) 0x0638);
            case 'O':
                return String.valueOf((char) 0x0644)+ String.valueOf(SPACE_CHAR);
            case 'W':
                return String.valueOf((char) 0x0647);
            case 'G':
                return String.valueOf((char) 0x0639)+ String.valueOf(SPACE_CHAR);
            case 'P':
                return String.valueOf((char) 0x0644);
            case 'X':
                return String.valueOf((char) 0x06CC);
            case 'H':
                return String.valueOf((char) 0x0639);
            case 'Y':
                return String.valueOf((char) 0x06CC)+ String.valueOf(SPACE_CHAR);
            case 'I':
                return String.valueOf((char) 0x063A)+ String.valueOf(SPACE_CHAR);
            case 'R':
                return String.valueOf((char) 0x0645);
            case 'Z':
                return String.valueOf('/');

            default:
                return String.valueOf(SPACE_CHAR);
        }

    }

    private String correctDoubleR(String s){
        int loopCount=s.length();
        int index=0;
        String temp="";
        while (index<loopCount){
            temp=String.valueOf(s.charAt(index));
            try{
             if (String.valueOf(s.charAt(index)).equals("'") &&(String.valueOf(s.charAt(index+1)).equals("'"))){
                 s=s.substring(0,index)+s.substring(index+1);
                 loopCount=s.length();
                 index=0;
             }else index++;
            }catch (Exception e){
              break;
            }

        }
        return s;
    }

    private String correctYa(String s){
        int loopCount=s.length();
        int index=0;
        String temp="";
        while (index<loopCount){
            temp=String.valueOf(s.charAt(index));
            try{
                if (String.valueOf(s.charAt(index)).equals("?")){
                    s=s.substring(0,index)+"?"+s.substring(index+1);
                    loopCount=s.length();
                    index=0;
                }else index++;
            }catch (Exception e){
                break;
            }

        }
        return s;
    }
}

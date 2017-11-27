package cn.deepmax.util;


public class StringUtils {


    public static boolean isEmpty(String s){
        return (s==null||s.length()==0);
    }
    public static boolean isNotEmpty(String s){
        return (s!=null && s.length()!=0);
    }


    /**
     *  my_name_space  ->  myNameSpace
     *  it_is_a_note -> itIsANote
     * @param source
     * @return
     */
    public static String lowerCaseUnderlineToCamelCase(String source){
        if(isEmpty(source)){
            return source;
        }
        String[] strings = source.split("_");
        return toCamelCase(strings);
    }

    private static String toCamelCase(String[] source){
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(String it:source){
            if(isEmpty(it)) continue;
            if(index==0){
                sb.append(it);
            }else{
                if(it.length()==1){
                    sb.append(it.toUpperCase());
                }else{
                    sb.append(it.substring(0,1).toUpperCase()).append(it.substring(1));
                }
            }
            index++;
        }
        return sb.toString();
    }

    /**
     *  getUserName -> get_user_name
     *  getANoteFromWeb -> get_a_note_from_web
     * @param source
     * @return
     */
    public static String camelCaseToLowerCaseUnderLine(String source){
        if(isEmpty(source)){
            return source;
        }
        final char UNDERLINE = '_';
        char[] chars = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char it:chars){
            if(isUpperCase(it)){
                sb.append(UNDERLINE).append(String.valueOf(it).toLowerCase());
            }else{
                sb.append(it);
            }
        }
        return sb.toString();
    }

    private static boolean isUpperCase(char c){

        return Character.isUpperCase(c);

    }





}

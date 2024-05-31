public class asd {
    public static void main(String[] args) {
        String text = "1 d d d d e e 2 d d d d e e e e";
        char [] letters = text.toCharArray();
        String f = "";
        for (char x : letters){
            if (x!=' '){
                f+=x;
            }
        }
        System.out.println(f);
    }
}

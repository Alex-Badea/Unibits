import text.Text;

/**
 * Created by balex on 09.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        Text text = new Text("in.txt");

        String[] longestWordChain = text.getLongestWordChain();

        for(String s : longestWordChain)
            System.out.println(s);
    }
}

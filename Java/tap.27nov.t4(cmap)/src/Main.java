import map.Map;

/**
 * Created by balex on 22.11.2016.
 */
public class Main {
    public static void main(String[] args){
        Map map = new Map("in.txt");
        System.out.print(map.cmap());
    }
}

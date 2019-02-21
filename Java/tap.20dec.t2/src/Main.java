import table.Coordinate;
import table.Table;

/**
 * Created by balex on 14.12.2016.
 */
public class Main {
    public static void main(String[] args){
        Table table = new Table("in.txt");

        table.getOptimalRoute();
    }
}

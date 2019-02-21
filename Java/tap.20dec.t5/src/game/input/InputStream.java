package game.input;

import game.Selection;

import java.util.Scanner;

/**
 * Created by balex on 18.12.2016.
 */
public enum InputStream implements Streamable<Selection> {
    KEYBOARD, RANDOM;

    ////
    @Override
    public Selection getNext() {
        if (this == KEYBOARD) {
            Scanner in = new Scanner(System.in);
            String nextSelection;

            do {
                System.out.print("Selecția dvs.(S/D): ");
                nextSelection = in.next();
                if (nextSelection.equals("S") || nextSelection.equals("s"))
                    return Selection.LEFT;
                else if (nextSelection.equals("D") || nextSelection.equals("d"))
                    return Selection.RIGHT;
                else System.out.println("Selecție invalidă.");
            } while (!nextSelection.equals("S") && !nextSelection.equals("s") && !nextSelection.equals("D") && !nextSelection.equals("d"));

            in.close();

        } else if (this == RANDOM) {
            System.out.print("Selecție aleatoare: ");
            if (Math.random() < 0.5) {
                System.out.println("S");
                return Selection.LEFT;
            }
            else{
                System.out.println("D");
                return Selection.RIGHT;
            }
        }
        return null;
    }
}

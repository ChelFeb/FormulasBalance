import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class CheckFormula {

    private final static String open = "([<{";
    private final static String close = ")]>}";

    static boolean isOpen(char c) {
        return open.indexOf(c) != -1;
    }

    static boolean isClose(char c) {
        return close.indexOf(c) != -1;
    }

    public static List<Integer> isBalanced(String input) {
        Stack<Character> brackets = new Stack<Character>();
        Stack<Integer> openBracketPosition = new Stack<Integer>();
        List<Integer> position = new ArrayList<Integer>();

        for (int index = 0; index < input.length(); ++index) {
            if (isOpen(input.charAt(index))) {
                brackets.push(input.charAt(index));
                openBracketPosition.push(index);
            } else if (isClose(input.charAt(index))) {
                if (!brackets.isEmpty()) {
                    Character openingBracket = open.charAt(close.indexOf(input.charAt(index)));
                    if (openingBracket.equals(brackets.peek())) {
                        brackets.pop();
                        openBracketPosition.pop();
                    } else {
                        position.add(index);
                    }
                } else {
                    position.add(index);
                }
            }

            if (index == input.length() - 1) {
                if (!openBracketPosition.isEmpty()) {
                    for (Integer pos : openBracketPosition)
                        position.add(pos);
                }
            }
        }

        return position;
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Please type path to file: ");
        String path = in.next();
        try {
            // read file with formulas
            BufferedReader br = new BufferedReader(new FileReader(path));
            ArrayList<String> formulas = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                formulas.add(line);
            }
            br.close();

            // Select the path to file
            path = path.substring(0, path.lastIndexOf("/"));

            File incorrectFormulas = new File(path + "/incorrectFormulas.txt");

            if (!incorrectFormulas.exists()) {
                incorrectFormulas.createNewFile();
            }

            FileWriter fw = new FileWriter(incorrectFormulas.getAbsoluteFile());
            BufferedWriter wb = new BufferedWriter(fw);

            // Select the incorrect formulas and write them to file
            List<Integer> list;
            for (String formula : formulas) {
                list = isBalanced(formula);
                System.out.println(list);
                if (!list.isEmpty()) {
                    wb.write(formula);
                    wb.newLine();
                    Collections.sort(list);
                    for (int k = 1; k < list.size(); ++k) {
                        list.set(k, list.get(k) - list.get(k - 1) - 1);
                    }
                    for (Integer i : list) {
                        wb.write((String.format("%1$" + (i == 0 ? 1 : i) + "s", " ") + "^"));
                    }
                    wb.newLine();
                }
            }
            wb.close();
            fw.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Can not read/write file: " + e);
        }

    }
}

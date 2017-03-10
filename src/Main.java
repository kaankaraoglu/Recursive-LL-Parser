import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RecursiveLLParser parser = new RecursiveLLParser();
        Scanner scan = new Scanner(System.in);
        parser.getEBNFStatement();
        parser.getChar();
        parser.grammar();
        scan.close();
    }
}

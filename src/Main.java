public class Main {
    public static void main(String[] args) {
        Converter converter = new Converter("Hello World!");
        converter.convert();

        System.out.println("Text has been converted to an image!");

        Unconverter unconverter = new Unconverter("output.png");
        System.out.println(unconverter.unconvert());
    }
}
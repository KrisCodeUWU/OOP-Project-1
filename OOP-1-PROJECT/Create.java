import java.io.*;
import java.nio.file.*;

public class Create {
    private String filePath;

    public Create(String filePath) {
        this.filePath = filePath;
    }

    public void addShape(Figure figure) {
        File file = new File(filePath);
        String svgTemplate = "<svg width=\"500\" height=\"500\" xmlns=\"http://www.w3.org/2000/svg\">\n</svg>";

        try {
            if (!file.exists()) {
                Files.write(Paths.get(filePath), svgTemplate.getBytes());
            }

            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Insert new shape before </svg> tag
            int insertPosition = content.lastIndexOf("</svg>");
            if (insertPosition != -1) {
                content = content.substring(0, insertPosition) + figure.drawFigure() + "\n" + content.substring(insertPosition);
            }

            Files.write(Paths.get(filePath), content.getBytes());

            System.out.println("Shape added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

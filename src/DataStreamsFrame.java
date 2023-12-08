import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


public class DataStreamsFrame extends JFrame
{
    JPanel panelMain, panelNorth, panelEast, panelWest;

    JTextArea loadedFileTA, searchFIleTA;

    JScrollPane loadedFilePane, searchFilePane;

    JButton selectFileBut, searchFileBut, quitBut, eraseBut;

    JTextField textField;
    JLabel jLabel;
    StringBuffer stringBuffer;
    List<String> list = new LinkedList<>(Arrays.asList());
    Stream<String> stream;

    public DataStreamsFrame()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension sizeScreen = kit.getScreenSize();
        int heightScreen = sizeScreen.height;
        int widthScreen = sizeScreen.width;

        setSize(1230, 615);
        setLocation(widthScreen / 12, (heightScreen - 615) / 2);

        setTitle("The Process of Data Streaming");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createGUI();
        setVisible(true);

    }

    private void createGUI()
    {
        panelMain = new JPanel();
        panelWest = new JPanel();
        panelEast = new JPanel();
        panelNorth = new JPanel();

        panelMain.setLayout(new BorderLayout());
        panelMain.add(panelWest, BorderLayout.WEST);
        panelMain.add(panelEast, BorderLayout.EAST);
        panelMain.add(panelNorth, BorderLayout.NORTH);

        add(panelMain);

        createPanelNorth();
        createPanelEast();
        createPanelWest();
    }

    private void createPanelWest()
    {
        panelWest.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 1), "The Original File"));

        loadedFileTA = new JTextArea(30, 45);
        loadedFilePane = new JScrollPane(loadedFileTA);
        loadedFileTA.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        panelWest.add(loadedFilePane);
        loadedFileTA.setEditable(false);

    }

    private void createPanelEast()
    {
        panelEast.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 1), "File that is Filtered"));

        searchFIleTA = new JTextArea(30,45);
        searchFIleTA.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        searchFilePane = new JScrollPane(searchFIleTA);

        panelEast.add(searchFilePane); searchFIleTA.setEditable(false);
    }

    private void createPanelNorth()
    {
        quitBut = new JButton("Quit");
        quitBut.addActionListener((ActionEvent ae) -> System.exit(0));

        selectFileBut = new JButton("Select File");
        selectFileBut.addActionListener((ActionEvent ae) -> selectFile());

        jLabel = new JLabel("Search File: ", JLabel.RIGHT);
        textField = new JTextField(8);
        searchFileBut = new JButton("Search");
        searchFileBut.addActionListener((ActionEvent ae) -> searchSelectedFile());

        eraseBut = new JButton("Erase");
        eraseBut.addActionListener((ActionEvent ae) -> clearFilter());

        panelNorth.add(selectFileBut);
        panelNorth.add(jLabel);
        panelNorth.add(textField); textField.setEnabled(false);
        panelNorth.add(searchFileBut); searchFileBut.setEnabled(false);
        panelNorth.add(eraseBut);
        panelNorth.add(quitBut);
    }

    private void clearFilter() {
        loadedFileTA.setText("");
        searchFIleTA.setText("");
        textField.setText("");

        selectFileBut.setEnabled(true);
        searchFileBut.setEnabled(false);
    }

    private void selectFile()
    {
        JFileChooser chooser = new JFileChooser();
        File fileSelected;
        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);


            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                fileSelected = chooser.getSelectedFile();
                loadedFileTA.setText(fileSelected.getName() + "\n\n");

                Stream lines = Files.lines(Paths.get(fileSelected.toURI()));
                list = lines.toList();
                list.forEach(word -> loadedFileTA.append(word.concat("\n")));
            }
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File not found");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        selectFileBut.setEnabled(false);
        searchFileBut.setEnabled(true);
        textField.setEnabled(true);


    }

    private void searchSelectedFile() {
        if (loadedFileTA.getText().isBlank())
        {
            JOptionPane.showMessageDialog(null, "You failed to select a file", "Error Message", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            String res = textField.getText().toLowerCase();

            if (res.isBlank())
            {
                JOptionPane.showMessageDialog(null, "Text Field is empty", "Error Message", JOptionPane.ERROR_MESSAGE);
                searchFileBut.setEnabled(true);
            }
            else
            {
                stream = list.stream().filter(word -> word.toLowerCase().contains(res));
                stream.forEach(word -> searchFIleTA.append(word.concat("\n")));

                searchFileBut.setEnabled(false);
                textField.setEnabled(false);
            }
        }
    }
}
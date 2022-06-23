import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class BackendAndUI extends JFrame {

    public Set<String> box2=new HashSet<>();
    public static boolean check = false;
    String s1;

    String s2;

    public static int numKeys;

    public static int beforeFlag = 0;

    FileWriter output;

    //Name of the class being tested
    public static String nameOfClassBeingTested;

    //Name of current function being parsed. Helps to associate the lines being read in the function body with the function
    public static String currFunction;

    //The absolute filepath taken as input from the user via the Java Swing GUI
    public static String inputFilePath;

    //Stores the external objects being referenced in a class which would be mocked
    public static Vector<String> externalObjectList = new Vector<>();

    //Stores the functions to be used in @Test
    public static Vector<String> functionsToBeTested = new Vector<>();

    public static Vector<String> autowiredObjectList = new Vector<>();

    //Hashmap storing information about the public functions being declared in a class, what external objects these functions use and what functions fo these external objects call
    public static HashMap<String, HashMap<String, List<String>>> functionData = new LinkedHashMap();

    //Hashmap to store what dummy value will be returned for the mocked functions

    public static HashMap<String, Vector<String>> whenReturnThisFunctions = new LinkedHashMap();

    public static Vector<String> beforeData = new Vector<>();

    public static String t1;

    public static String t2;

    public static String t3;

    public static String t4;

    //Flag for registering that previous line had an @Autowired object
    public static int autowiredFlag = 0;
    private JPanel panel1;
    private JTextField textField1;
    private JLabel l1;
    private JTable table1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton chooseFilePathButton;
    private JLabel Before;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton generateBeforeButton;
    private JButton generateTestButton;

    private JLabel tableTitle;

    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton setupCompleteButton;
    private JLabel testHead;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JComboBox comboBox6;
    private JButton testCompleteButton;
    private JButton insertAssertsButton;
    private JButton createUnitTestButton;

    public int ActionFlag=0;

    public int ActionFlag2=0;

    public int ActionFlag3=0;

    public int testedBefore=0;

    DefaultTableModel model;
    BackendAndUI() throws IOException {

        add(panel1);
        comboBox1.setEditable(true);
        Color color=new Color(70,73,76);
        comboBox1.getEditor().getEditorComponent().setBackground(color);
        comboBox2.setEditable(true);
        Color color2=new Color(251,251,251);
        comboBox2.getEditor().getEditorComponent().setBackground(color);
        comboBox1.getEditor().getEditorComponent().setForeground(color2);
        comboBox2.getEditor().getEditorComponent().setForeground(color2);
        comboBox3.setEditable(true);
        comboBox3.getEditor().getEditorComponent().setBackground(color);
        comboBox3.getEditor().getEditorComponent().setForeground(color2);
        comboBox4.setEditable(true);
        comboBox4.getEditor().getEditorComponent().setBackground(color);
        comboBox4.getEditor().getEditorComponent().setForeground(color2);
        comboBox5.setEditable(true);
        comboBox5.getEditor().getEditorComponent().setBackground(color);
        comboBox5.getEditor().getEditorComponent().setForeground(color2);
        comboBox6.setEditable(true);
        comboBox6.getEditor().getEditorComponent().setBackground(color);
        comboBox6.getEditor().getEditorComponent().setForeground(color2);

        table1.setBackground(color);
        table1.setForeground(Color.WHITE);
        setSize(1400,1200);
        model=new DefaultTableModel();
        Object[] column={"Public functions being called","External objects being referenced","Functions being called by external objects"};
        model.setColumnIdentifiers(column);
        table1.setModel(model);

        chooseFilePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputFilePath=textField1.getText();
                try {
                    readUsingFileReader(inputFilePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        generateBeforeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generateBefore();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        generateTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generateTest();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                comboBox2.removeAllItems();
                if(ActionFlag==0){
                    ActionFlag=1;
                }
                else{
                    String compare= (String) comboBox1.getSelectedItem();
                    for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
                        for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()){
                            if(entry2.getKey()==compare){
                                for(int i=0;i<entry2.getValue().size();i++){
                                    box2.add(entry2.getValue().get(i));
                                }
                            }
                        }
                    }
                    for(String temp:box2){
                        comboBox2.addItem(temp);
                    }
                }
            }
        });
        setupCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
                    s1=entry.getKey();
                    for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()) {
                        s2=entry2.getKey();
                        for(int i=0;i<functionData.get(s1).get(s2).size();i++){
                            if(beforeData.contains(functionData.get(s1).get(s2).get(i))){
                                functionData.get(s1).get(s2).set(i,"");
                            }
                        }
                    }
                }

                for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
                    s1=entry.getKey();
                    for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()){
                        s2=entry2.getKey();
                        testHead.setText(s1.substring(0,s1.indexOf("("))+"():"+functionData.get(s1).get(s2).get(0));
                        break;
                    }
                    break;
                }
            }
        });
        comboBox3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                comboBox4.removeAllItems();
                if(ActionFlag2==0){
                    ActionFlag2=1;
                }
                else{
                    String compare=(String)comboBox3.getSelectedItem();
                    for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
                        if(entry.getKey()==compare){
                            for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()){
                                comboBox4.addItem(entry2.getKey());
                            }
                            break;
                        }
                    }
                }
            }
        });
        comboBox4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                comboBox5.removeAllItems();
                if(ActionFlag3==0){
                    ActionFlag3=1;
                }
                else{
                    String compare1=(String)comboBox3.getSelectedItem();
                    String compare2=(String)comboBox4.getSelectedItem();
                    for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
                        if(entry.getKey()==compare1){
                            for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()){
                                if(entry2.getKey()==compare2){
                                    for(int i=0;i<entry2.getValue().size();i++){
                                        comboBox5.addItem(entry2.getValue().get(i));
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });
        insertAssertsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    output.write((String)comboBox6.getSelectedItem()+";\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        testCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    output.write("\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        createUnitTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    output.write("}\n");
                    output.write("}\n");
                    output.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    //Function that parses the code to be tested
    public void readUsingFileReader(String filePath) throws IOException {

        File file = new File(filePath);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Reads the file line by line
        BufferedReader br = new BufferedReader(fr);
        //Stores the current line of code that was read
        String line;

        //Creates file that will store the unit test code corresponding to the code to be tested
        File file1 = new File("TestCodeTester.java");

        //To write into the unit test code file
        //FileWriter output = new FileWriter("TestCodeTester.java");

        try {
            // create a new file with name specified
            // by the file object
            boolean value = file1.createNewFile();

            if (value) {
                System.out.println("New Java File is created.");
                output = new FileWriter("TestCodeTester.java", true);
            } else {
                System.out.println("The file already exists.");
                output = new FileWriter("TestCodeTester.java", true);
                testedBefore = 1;
                JOptionPane.showMessageDialog(panel1,"A unit test for this code already exists. Proceed with Test generation.");
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //This is where the actual parsing happens
            //Every line of code will be parsed to identify the tokens present in it

            //Line read was an import statement
            //Copy the import to the unit test file

            if (testedBefore == 0 && line.contains("import")) {

                output.write(line);
                output.write("\n");
            }

            //Detects the class which is being tested
            else if (testedBefore == 0 && line.contains("public class")) {
                //System.out.println("Hi in pc");
                //Importing the additional dependencies here (as to be imported only once)
                output.write("import static org.junit.Assert.assertEquals;\n");
                output.write("import static org.junit.Assert.assertFalse;\n");
                output.write("import static org.junit.Assert.assertNotNull;\n");
                output.write("import static org.junit.Assert.assertTrue;\n");
                output.write("import org.junit.Before;\n");
                output.write("import org.junit.Rule;\n");
                output.write("import org.junit.Test;\n");
                output.write("import org.junit.mockito2.*;\n");
                output.write("import static org.mockito2.Mockito.mock;\n");
                output.write("import static org.mockito2.Mockito.spy;\n");
                output.write("import static org.mockito2.Mockito.when;\n");
                output.write("import static org.mockito2.Mockito.doNothing;\n");
                output.write("\n");

                //Declares the public class which will hold the mocks and tests in the unit test code file
                output.write("public class TestCodeTester{\n\n");

                //@InjectMocks creates class instances which need to be tested in the test class
                output.write("@InjectMocks\n");

                //Logic to extract class name (Assumes starts with public class)
                int i = line.indexOf("public class") + 13;
                String temp = "";
                while (i < line.length()) {
                    if (line.charAt(i) == '{' || line.charAt(i) == ' ') {
                        break;
                    }
                    temp += line.charAt(i);
                    i++;
                }
                nameOfClassBeingTested = temp;
                output.write(temp + " " + temp.toLowerCase() + ";\n\n");
            }
            //Public function has been detected in the line
            else if (line.contains("throws") && line.contains("public")) {

                //System.out.println("Hi in throw");
                //Logic to extract function name
                int index = line.indexOf("public") + 7;
                int flag = 0;

                while (flag != 1) {
                    if (line.charAt(index) == ' ') {
                        flag = 1;
                    }
                    index++;
                }

                String functionName = line.substring(index, line.indexOf(')') + 1);

                /*
                    Check if testedBefore==0 && the functionName is present in TestCodeTester.java . If not present then add in functionData
                    List<String> lines= Files.lines(Paths.get(inputFilePath)).findFirst("test"+functionName);
                */

                if (testedBefore == 0 || Files.lines(Paths.get("TestCodeTester.java")).filter(line1 -> line1.contains("test" + functionName)).count() == 0) {
                    HashMap<String, List<String>> temp = new LinkedHashMap<>();

                    functionData.put(functionName, temp);
                    functionsToBeTested.add(functionName);
                }
                currFunction = functionName;
            } else if (line.contains("@Autowired")) {
                //System.out.println("In autowire");
                autowiredFlag = 1;
            } else if (autowiredFlag == 1) {
                //System.out.println("In autowire body");
                String temp = line.substring(line.indexOf("private") + 8, line.indexOf(";")); //Abc abc
                String objName = temp.substring(temp.indexOf(" ") + 1);
                autowiredObjectList.add(objName);
                autowiredFlag = 0;
            } else {
                if (testedBefore == 0 || (currFunction != null && Files.lines(Paths.get("TestCodeTester.java")).filter(line1 -> line1.contains("test" + currFunction)).count() == 0)) {
                    for (int i = 0; i < autowiredObjectList.size(); i++) {
                        if (line.contains(autowiredObjectList.get(i) + ".")) {
                            int startIndex = line.indexOf(autowiredObjectList.get(i));
                            int endIndex = startIndex + 1;
                            while (endIndex < line.length()) {
                                if (line.charAt(endIndex) == '(') {
                                    endIndex++;
                                    break;
                                }
                                endIndex++;
                            }

                            if (!functionData.get(currFunction).containsKey(autowiredObjectList.get(i))) {
                                List<String> temp = new ArrayList<>();
                                temp.add(line.substring(startIndex, endIndex) + ")");
                                functionData.get(currFunction).put(autowiredObjectList.get(i), temp);
                            } else {
                                functionData.get(currFunction).get(autowiredObjectList.get(i)).add(line.substring(startIndex, endIndex) + ")");
                            }
                            line = line.trim();
                            List<String> temp1 = Arrays.asList(line.split(" "));
                            String startWord = temp1.get(0);
                            //Not returning a value
                            if (startWord.contains(".")) {
                                continue;
                            }
                            break;
                        }
                    }
                }
            }
            numKeys = functionData.size();
        }

        if (testedBefore == 0) {
            //Generating @Mocks in the file
            for (String s : externalObjectList) {
                output.write("@Mock\n");
                output.write(s + " mock" + s + ";\n\n");
            }

            for (String s : autowiredObjectList) {
                output.write("@Mock\n");
                output.write(s + " mock" + s + ";\n\n");
                comboBox1.addItem(s);
            }
        }
        final Object[] row = new Object[4];

        for (Map.Entry<String, HashMap<String, List<String>>> entry : functionData.entrySet()) {
            row[0] = entry.getKey();
            comboBox3.addItem(entry.getKey());
            int flag = 0;
            for (Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {

                for (int i = 0; i < entry2.getValue().size(); i++) {
                    if (flag == 0) {
                        flag = 1;
                    } else {
                        row[0] = "";
                    }
                    row[1] = entry2.getKey();
                    row[2] = entry2.getValue().get(i);
                    model.addRow(row);
                }
            }
        }

        if(testedBefore==0){
            output.write("@Before\n");
            output.write("public void beforeTest(){\n\n");
            output.write("MockitoAnnotations.initMocks(this)\n");
            br.close();
            fr.close();
        }
    }

    public void generateBefore() throws IOException {

        t1=(String) comboBox2.getSelectedItem();
        beforeData.add(t1);
        t1=t1.substring(0,t1.indexOf("("));
        //t2=textField9.getText();
        t3=textArea1.getText();
        t4=textArea2.getText();

        output.write("when("+t1+"("+t3+").thenReturn("+t4+");\n");
    }

    public void generateTest() throws IOException {

        String funcName= (String) comboBox3.getSelectedItem();
        String objName= (String) comboBox4.getSelectedItem();
        String objFuncName= (String) comboBox5.getSelectedItem();
        t3=textArea3.getText();
        t4=textArea4.getText();

        String finalFuncName = funcName;
        if(Files.lines(Paths.get("TestCodeTester.java")).filter(line1 -> line1.contains("test" + finalFuncName)).count() == 0){
            output.write("}\n");
            output.write("@Test\n");
            output.write("public void test"+funcName+"{\n");
            output.close();
            output=new FileWriter("TestCodeTester.java",true);
        }

        if(t4=="None"){
            output.write("doNothing().when("+objName+")."+objFuncName.substring(objFuncName.indexOf(".")+1,objFuncName.indexOf(")"))+t3+");\n");
        }
        else{
            output.write("when("+objFuncName.substring(0,objFuncName.indexOf("("))+"("+t3+").thenReturn("+t4+");\n");
        }

    }
}

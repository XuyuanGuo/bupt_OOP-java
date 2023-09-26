import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Date;

public class Starter extends JFrame implements ActionListener {
    //计算器显示区域
    private JTextField displayField;
    //用于获取当前系统时间
    Date date;

    public Starter() {
        // 设置窗口标题
        setTitle("Calculator");

        // 创建面板和布局
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 创建文本框并设置属性
        displayField = new JTextField();
        displayField.setFont(new Font("",Font.BOLD,20));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        panel.add(displayField, BorderLayout.NORTH);

        // 创建按钮面板和布局
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 5, 5, 5));

        // 创建按钮并添加到按钮面板
        String[] buttonLabels = {
                "7", "8", "9", "DEL", "AC",
                "4", "5", "6", "*", "/",
                "1", "2", "3", "+", "-",
                "0", ".", "(", ")", "="
        };
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            if(Character.isDigit(label.charAt(0))||label.equals(".")){
                button.setBackground(new Color(-8929660));
            } else if(label.equals("DEL")||label.equals("AC")) {
                button.setBackground(new Color(-29670));
            }
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        panel.add(buttonPanel, BorderLayout.CENTER);

        //创建“显示结果”按钮
        JButton displayInternalFrame=new JButton("RESULTS");
        displayInternalFrame.addActionListener(this);
        panel.add(displayInternalFrame, BorderLayout.SOUTH);

        //设置窗口内容面板和关闭操作
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // 居中显示
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if("AC".equals(command)){
            displayField.setText("");
        }else if("DEL".equals(command)){
            backSpace();
        }else if("=".equals(command)) {
            evaluateExpression();
        }else if("RESULTS".equals(command)){
            initSubWindow();
        }else{
            updateDisplay(command);
        }
    }

    /**
     * 退格，删除显示区域的最后一个字符
     */
    private void backSpace(){
        String temp=displayField.getText();
        if(temp.length()>0) {
            displayField.setText(temp.substring(0, temp.length() - 1));
        }
    }

    /**
     *计算显示区域中的表达式，并更新显示区域
     */
    private void evaluateExpression() {
        //获取显示区域表达式
        String expression = displayField.getText();
        //检验表达式是否合法
        if(Validator.isValidExpression(expression)){
            //表达式合法，计算表达式结果
            try{
                double result = Calculator.evaluateExpression(expression);
                displayField.setText(String.format("%.6f",result));
                addToLog(expression+"="+result);
            }
            //计算过程中出现错误（如除数为0），输出错误信息
            catch(Exception e){
                System.out.println(e);
                displayField.setText("算数错误");
            }
        }else{
            //表达式不合法，输出错误信息
            displayField.setText("语法错误");
        }
    }

    /**
     * 更新结果显示区域，在原显示内容后衔接一个字符串
     *
     * @param text 原显示内容后衔接的字符串
     */
    private void updateDisplay(String text) {
        displayField.setText(displayField.getText() + text);
    }

    /**
     * 将指定字符串写入文档中
     *
     * @param str 写入文档中的字符
     * @throws IOException 写入出错
     */
    public void addToLog(String str) throws IOException{
        //创建一个输出流
        String fileName = "./log.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
        //获取系统时间
        date=new Date();
        //将时间与内容写入文件
        writer.write(String.format("%tc",date));
        writer.write(" 执行计算： ");
        writer.write(str);
        writer.newLine();
        //关闭输出流
        writer.close();
    }

    /**
     * 创建一个子窗口，显示使用计算器时的所有正确计算
     */
    public void initSubWindow(){
        // 创建子窗口
        JDialog dialog = new JDialog(this, "Results", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());
        // 创建显示区域
        JTextArea displayArea=new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        dialog.add(scrollPane,BorderLayout.CENTER);
        // 读入显示内容
        try{
            String fileName = "./log.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line=reader.readLine();
            while(line!=null){
                displayArea.append(line+"\n");
                line=reader.readLine();
            }
        }catch(Exception e){
            System.out.println("读文件出错");
        }
        // 创建关闭按钮
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 关闭子窗口
                dialog.dispose();
            }
        });
        // 添加关闭按钮到子窗口
        dialog.add(closeButton,BorderLayout.SOUTH);
        // 设置子窗口位置相对于父窗口
        dialog.setLocationRelativeTo(this);
        // 显示子窗口
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        //选择Nimbus风格的LookAndFeel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //Nimbus风格的LookAndFeel不存在，使用默认的LookAndFeel
        }
        SwingUtilities.invokeLater(() -> {
            Starter calculator = new Starter();
            calculator.setSize(300,330);
            calculator.setVisible(true);
        });
    }
}

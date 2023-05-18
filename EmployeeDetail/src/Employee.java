import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField NameTxt;
    private JTextField Salarytxt;
    private JTextField Mobiletxt;
    private JButton Save;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField Searchdetails;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public void Connection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/crud" , "root" , "");
            System.out.println("Success!");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();

        }
        catch (SQLException e){
            e.printStackTrace();

        }

    }

    public void table_load(){
        try {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException exception){
            exception.printStackTrace();

        }
    }


    public Employee() {
        Connection();
        table_load();
        Save.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String empname , salary , mobile;

            empname = NameTxt.getText();
            salary = Salarytxt.getText();
            mobile = Mobiletxt.getText();

            try{
                pst = con.prepareStatement("insert into employee(empname,salary,mobile)values (?,?,?)");
                pst.setString(1,empname);
                pst.setString(2,salary);
                pst.setString(3,mobile);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"Record Add!");

                NameTxt.setText("");
                Salarytxt.setText("");
                Mobiletxt.setText("");
                NameTxt.requestFocus();
            }
            catch (SQLException exception){
                exception.printStackTrace();
            }

        }
    });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String id = Searchdetails.getText();
                     pst = con.prepareStatement("select empname , salary , mobile from employee where id = ?");
                     pst.setString(1,id);
                     ResultSet rs = pst.executeQuery();

                     if(rs.next()==true){

                         String empname = rs.getString(1);
                         String salary = rs.getString(2);
                         String mobile = rs.getString(3);

                         NameTxt.setText(empname);
                         Salarytxt.setText(salary);
                         Mobiletxt.setText(mobile);
                     }
                     else {
                         NameTxt.setText("");
                         Salarytxt.setText("");
                         Mobiletxt.setText("");

                         JOptionPane.showMessageDialog(null,"Invalid Id");
                     }

                }
                catch (SQLException exception){
                    exception.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname , salary , mobile , id;

                empname = NameTxt.getText();
                salary = Salarytxt.getText();
                mobile = Mobiletxt.getText();
                id = Searchdetails.getText();

                try {
                    pst = con.prepareStatement("update employee set empname=? , salary=? , mobile=? where id = ?");
                    pst.setString(1,empname);
                    pst.setString(2,salary);
                    pst.setString(3,mobile);
                    pst.setString(4,id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Updated!");
                    table_load();

                    NameTxt.setText("");
                    Salarytxt.setText("");
                    Mobiletxt.setText("");
                    NameTxt.requestFocus();

                    Searchdetails.setText("");
                }
                catch (SQLException exception){
                    exception.printStackTrace();
                }

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id;

                id = Searchdetails.getText();

                try {
                    pst = con.prepareStatement("delete from employee where id=?");
                    pst.setString(1,id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Recorded are Deleted");
                    table_load();
                    NameTxt.setText("");
                    Salarytxt.setText("");
                    Mobiletxt.setText("");
                    NameTxt.requestFocus();;
                    Searchdetails.setText("");

                }catch (SQLException exception){
                    exception.printStackTrace();
                }




            }
        });
    }



}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitness_tracker;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fawaz
 */
public class recommendations extends javax.swing.JFrame {

    /**
     * Creates new form recommendations
     */
    private static int userid;
    public recommendations(int userID) {
        this.userid = userID;
        initComponents();
    }
    
    public static int getuserID(){
        return userid;
    }
    
    public void close(){
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        follow = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jToggleButton6 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setForeground(new java.awt.Color(153, 153, 153));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(700, 400));

        jLabel1.setText("Recommendations");

        jLabel2.setText("Daily Calories:");

        int userID = getuserID();
        float weight = 0.0f;
        float ft = 0.0f;
        float in = 0.0f;
        int age = 0;
        double calories = 0.0;
        float trgtWeight = 0.0f;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit","root","");
            String sql = "Select weight, age, height_ft, height_inches from users where user_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
            {
                weight = rs.getFloat("weight");
                ft = rs.getFloat("height_ft");
                in = rs.getFloat("height_inches");
                age = rs.getInt("age");

                double height = 2.54*((ft*12)+in);

                double BMR = (10*weight)+(6.25*height)-(5*age)+5;
                double TDEE = (BMR*1.1)*1.55;
                sql = "Select target_weight from weightlossgoals where user_id = ?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, userID);
                rs = pst.executeQuery();
                if(rs.next()){
                    trgtWeight = rs.getFloat("target_weight");
                }
                if (trgtWeight<weight){
                    calories = TDEE - 500;
                }
                else if(trgtWeight>weight){
                    calories = TDEE +500;
                }
                else{
                    calories = TDEE;
                }
            }
            else{
                weight = 3.3f;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel3.setText(String.valueOf(calories));

        jLabel4.setText("Exercises:");

        StringBuilder exercisesText = new StringBuilder();
        float criteria = 6.0f;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit","root","");
            if(trgtWeight>weight){
                String sql = "Select * from exerciseinfo where met < ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setFloat(1, criteria);
                ResultSet rs = pst.executeQuery();

                String insertUserQuery = "INSERT INTO workoutplan (user_id, exercise_name, intensity_level, duration_minutes, met, time_of_day) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertUserStatement = con.prepareStatement(insertUserQuery);

                while(rs.next())
                {
                    exercisesText.append(rs.getString("exercise_name")).append("\n");

                    String ename = rs.getString("exercise_name");
                    String ilvl = rs.getString("intensity_level");
                    int time = rs.getInt("corresponding_duration_minutes");
                    float met = rs.getFloat("met");

                    insertUserStatement.setInt(1, userID);
                    insertUserStatement.setString(2, ename);
                    insertUserStatement.setString(3, ilvl);
                    insertUserStatement.setInt(4, time); // Assuming email is not provided and setting a default value
                    insertUserStatement.setFloat(5, met);
                    insertUserStatement.setString(6, "Evening");

                    int rowsAffected = insertUserStatement.executeUpdate();
                }

            }else{
                String sql = "Select * from exerciseinfo where met >= ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setFloat(1, criteria);
                ResultSet rs = pst.executeQuery();

                String insertUserQuery = "INSERT INTO workoutplan (user_id, exercise_name, intensity_level, duration_minutes, met, time_of_day) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertUserStatement = con.prepareStatement(insertUserQuery);

                while(rs.next())
                {
                    exercisesText.append(rs.getString("exercise_name")).append("\n");

                    String ename = rs.getString("exercise_name");
                    String ilvl = rs.getString("intensity_level");
                    int time = rs.getInt("corresponding_duration_minutes");
                    float met = rs.getFloat("met");

                    insertUserStatement.setInt(1, userID);
                    insertUserStatement.setString(2, ename);
                    insertUserStatement.setString(3, ilvl);
                    insertUserStatement.setInt(4, time); // Assuming email is not provided and setting a default value
                    insertUserStatement.setFloat(5, met);
                    insertUserStatement.setString(6, "Morning");

                    int rowsAffected = insertUserStatement.executeUpdate();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText(exercisesText.toString());
        jScrollPane1.setViewportView(jTextArea1);

        jLabel5.setText("Diet:");

        StringBuilder dietText = new StringBuilder();
        int cal = 100;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fit","root","");
            if(trgtWeight>weight){
                String sql = "Select * from foodinfo where calories_per_quantity > ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, cal);
                ResultSet rs = pst.executeQuery();

                String insertUserQuery = "INSERT INTO dietplan (user_id, food_name, quantity_grams, time_of_day, expected_target_calories, flag) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertUserStatement = con.prepareStatement(insertUserQuery);

                while(rs.next())
                {
                    dietText.append(rs.getString("food_name")).append("\n");

                    String fname = rs.getString("food_name");
                    int quantity = 5*(rs.getInt("quantity"));
                    float exp = 5*(rs.getFloat("calories_per_quantity"));

                    insertUserStatement.setInt(1, userID);
                    insertUserStatement.setString(2, fname);
                    insertUserStatement.setInt(3, quantity);
                    insertUserStatement.setString(4, "Dinner"); // Assuming email is not provided and setting a default value
                    insertUserStatement.setFloat(5, exp);
                    insertUserStatement.setInt(6, 0);

                    int rowsAffected = insertUserStatement.executeUpdate();
                }

            }else{
                String sql = "Select * from foodinfo where calories_per_quantity < ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, cal);
                ResultSet rs = pst.executeQuery();

                String insertUserQuery = "INSERT INTO dietplan (user_id, food_name, quantity_grams, time_of_day, expected_target_calories, flag) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertUserStatement = con.prepareStatement(insertUserQuery);

                while(rs.next())
                {
                    dietText.append(rs.getString("food_name")).append("\n");

                    String fname = rs.getString("food_name");
                    int quantity = 5*(rs.getInt("quantity"));
                    float exp = 5*(rs.getFloat("calories_per_quantity"));

                    insertUserStatement.setInt(1, userID);
                    insertUserStatement.setString(2, fname);
                    insertUserStatement.setInt(3, quantity);
                    insertUserStatement.setString(4, "Lunch"); // Assuming email is not provided and setting a default value
                    insertUserStatement.setFloat(5, exp);
                    insertUserStatement.setInt(6, 0);

                    int rowsAffected = insertUserStatement.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText(dietText.toString());
        jScrollPane2.setViewportView(jTextArea2);

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Custom Diet & Workout Plan");

        follow.setBackground(new java.awt.Color(51, 153, 255));
        follow.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        follow.setText("Done");
        follow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                followActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 255, 204));

        jToggleButton1.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setText("Home");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToggleButton3.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton3.setText("Recommendations");
        jToggleButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToggleButton5.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton5.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton5.setText("Goals");
        jToggleButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jToggleButton4.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton4.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton4.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton4.setText("Log out");
        jToggleButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jComboBox1.setBackground(new java.awt.Color(51, 153, 255));
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Calorie intake", "Calorie expenditure"}));
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setSelectedItem(0);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jToggleButton6.setBackground(new java.awt.Color(51, 153, 255));
        jToggleButton6.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggleButton6.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButton6.setText("Account");
        jToggleButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGap(37, 37, 37)
                .addComponent(jToggleButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton3)
                    .addComponent(jToggleButton5)
                    .addComponent(jToggleButton4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton6))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(295, 295, 295)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(74, 74, 74)
                        .addComponent(jLabel6)))
                .addContainerGap(220, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(274, 274, 274)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(follow)
                        .addGap(361, 361, 361))))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(120, 120, 120))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(follow)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void followActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_followActionPerformed

        close();
        recPlan iFrame = new recPlan(getuserID());
        iFrame.setVisible(true);
        iFrame.pack();
        iFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_followActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        close();
        homepage homeFrame = new homepage(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed

        close();
        recPlan homeFrame = new recPlan(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed

        close();
        Goals homeFrame = new Goals(getuserID());
        homeFrame.setVisible(true);
        homeFrame.pack();
        homeFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        close();
        login loginFrame = new login();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null); //center
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(recommendations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(recommendations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(recommendations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(recommendations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new recommendations(getuserID()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton follow;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    // End of variables declaration//GEN-END:variables
}
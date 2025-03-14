import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TTT2 extends JFrame implements ItemListener, ActionListener {
    int i, j, ii, jj, x, y, yesnull;
    int a[][] = {
        
        {10, 1, 2, 3, 4, 5, 11}, {10, 6, 7, 8, 9, 10, 11}, {10, 11, 12, 13, 14, 15, 11},
        {10, 16, 17, 18, 19, 20, 11}, {10, 21, 22, 23, 24, 25, 11},
        
        {10, 1, 6, 11, 16, 21, 11}, {10, 2, 7, 12, 17, 22, 11}, {10, 3, 8, 13, 18, 23, 11},
        {10, 4, 9, 14, 19, 24, 11}, {10, 5, 10, 15, 20, 25, 11},
        
        {10, 1, 7, 13, 19, 25, 11}, {10, 5, 9, 13, 17, 21, 11}
    };
    int a1[][] = {
        
        {10, 1, 2, 3, 4, 5, 11}, {10, 6, 7, 8, 9, 10, 11}, {10, 11, 12, 13, 14, 15, 11},
        {10, 16, 17, 18, 19, 20, 11}, {10, 21, 22, 23, 24, 25, 11},
        
        {10, 1, 6, 11, 16, 21, 11}, {10, 2, 7, 12, 17, 22, 11}, {10, 3, 8, 13, 18, 23, 11},
        {10, 4, 9, 14, 19, 24, 11}, {10, 5, 10, 15, 20, 25, 11},
        
        {10, 1, 7, 13, 19, 25, 11}, {10, 5, 9, 13, 17, 21, 11}
    }; 

    boolean state, type, set;
    Icon ic1, ic2, icon, ic11, ic22;
    Checkbox c1, c2;
    JLabel l1, l2;
    JButton b[] = new JButton[25]; 
    JButton reset;

    public void showButton() {
        x = 10; y = 10; j = 0;
        for (i = 0; i < 25; i++, x += 80, j++) {
            b[i] = new JButton();
            if (j == 5) { j = 0; y += 80; x = 10; }
            b[i].setBounds(x, y, 80, 80);
            add(b[i]);
            b[i].addActionListener(this);
        }
        reset = new JButton("RESET");
        reset.setBounds(120, 450, 100, 50);
        add(reset);
        reset.addActionListener(this);
    }

    public void check(int num1) {
        for (ii = 0; ii < a.length; ii++) {
            for (jj = 1; jj <= 5; jj++) {
                if (a[ii][jj] == num1) { a[ii][6] = 11; }
            }
        }
    }

    public void complogic(int num) {
        for (i = 0; i < a.length; i++) {
            for (j = 1; j <= 5; j++) {
                if (a[i][j] == num) { a[i][0] = 11; a[i][6] = 10; }
            }
        }
        for (i = 0; i < a.length; i++) {
            set = true;
            if (a[i][6] == 10) {
                int count = 0;
                for (j = 1; j <= 5; j++) {
                    if (b[(a[i][j] - 1)].getIcon() != null) {
                        count++;
                    } else {
                        yesnull = a[i][j];
                    }
                }
                if (count == 4) {
                    b[yesnull - 1].setIcon(ic2);
                    this.check(yesnull);
                    set = false;
                    break;
                }
            } else if (a[i][0] == 10) {
                for (j = 1; j <= 5; j++) {
                    if (b[(a[i][j] - 1)].getIcon() == null) {
                        b[(a[i][j] - 1)].setIcon(ic2);
                        this.check(a[i][j]);
                        set = false;
                        break;
                    }
                }
                if (!set) break;
            }
            if (!set) break;
        }
    }

    TTT2() {
        super("Tic Tac Toe 5x5");
        CheckboxGroup cbg = new CheckboxGroup();
        c1 = new Checkbox("vs computer", cbg, false);
        c2 = new Checkbox("vs friend", cbg, false);
        c1.setBounds(120, 80, 100, 40);
        c2.setBounds(120, 150, 100, 40);
        add(c1);
        add(c2);
        c1.addItemListener(this);
        c2.addItemListener(this);

        state = true;
        type = true;
        set = true;
        ic1 = new ImageIcon("ic1.jpg");
        ic2 = new ImageIcon("ic2.jpg");
        ic11 = new ImageIcon("ic11.jpg");
        ic22 = new ImageIcon("ic22.jpg");

        setLayout(null);
        setSize(450, 550);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void itemStateChanged(ItemEvent e) {
        if (c1.getState()) { type = false; }
        else if (c2.getState()) { type = true; }
        remove(c1);
        remove(c2);
        repaint(0, 0, 450, 550);
        showButton();
    }

    public void actionPerformed(ActionEvent e) {
        if (type==true) {
            if (e.getSource() == reset) {
                for (i = 0; i < 25; i++) { b[i].setIcon(null); }
            } else {
                for (i = 0; i < 25; i++) {
                    if (e.getSource() == b[i] && b[i].getIcon() == null) {
                        icon = state ? ic2 : ic1;
                        state = !state;
                        b[i].setIcon(icon);
                    }
                }
            }
        } else {
            if (e.getSource() == reset) {
                for (i = 0; i < 25; i++) { b[i].setIcon(null); }
                for (i = 0; i < a.length; i++) for (j = 0; j < 6; j++) a[i][j] = a1[i][j];
            } else {
                for (i = 0; i < 25; i++) {
                    if (e.getSource() == b[i] && b[i].getIcon() == null) {
                        b[i].setIcon(ic1);
                        if (b[12].getIcon() == null) {
                            b[12].setIcon(ic2);
                            this.check(13);
                        } else {
                            this.complogic(i);
                        }
                    }
                }
            }
        }

        for (i = 0; i < a.length; i++) {
            Icon icon1 = b[(a[i][1] - 1)].getIcon();
            Icon icon2 = b[(a[i][2] - 1)].getIcon();
            Icon icon3 = b[(a[i][3] - 1)].getIcon();
            Icon icon4 = b[(a[i][4] - 1)].getIcon();
            Icon icon5 = b[(a[i][5] - 1)].getIcon();
            if (icon1 == icon2 && icon2 == icon3 && icon3 == icon4 && icon4 == icon5 && icon1 != null) {
                if (icon1 == ic1) {
                    JOptionPane.showMessageDialog(this, "You won! Click reset.");
                } else if (icon1 == ic2) {
                    JOptionPane.showMessageDialog(this, "Computer won! Click reset.");
                }
                break;
            }
        }
    }

    public static void main(String[] args) {
        new TTT2();
    }
}

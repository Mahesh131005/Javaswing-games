import java.awt.*;
import java.awt.event.*;

public class MyCalculator extends Frame {

    public boolean setClear = true;
    double number, memValue;
    char op;

    String digitButtonText[] = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+/-", "."};
    String operatorButtonText[] = {"/", "sqrt", "*", "%", "-", "1/X", "+", "="};
    String memoryButtonText[] = {"MC", "MR", "MS", "M+"};
    String specialButtonText[] = {"Backspc", "C", "CE"};

    MyDigitButton digitButton[] = new MyDigitButton[digitButtonText.length];
    MyOperatorButton operatorButton[] = new MyOperatorButton[operatorButtonText.length];
    MyMemoryButton memoryButton[] = new MyMemoryButton[memoryButtonText.length];
    MySpecialButton specialButton[] = new MySpecialButton[specialButtonText.length];

    Label displayLabel = new Label("0", Label.RIGHT);
    Label memLabel = new Label(" ", Label.RIGHT);

    final int FRAME_WIDTH = 325, FRAME_HEIGHT = 350;
    final int HEIGHT = 30, WIDTH = 30, H_SPACE = 10, V_SPACE = 10;
    final int TOPX = 30, TOPY = 50;

    Panel specialButtonPanel = new Panel(); // New panel for special buttons

    MyCalculator(String frameText) {
        super(frameText);

        setLayout(null);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);

        // Display label setup
        displayLabel.setBounds(TOPX, TOPY, 240, HEIGHT);
        displayLabel.setBackground(Color.BLUE);
        displayLabel.setForeground(Color.WHITE);
        add(displayLabel);

        // Memory label setup
        memLabel.setBounds(TOPX, TOPY + HEIGHT + V_SPACE, WIDTH, HEIGHT);
        add(memLabel);

        // Special Button Panel setup
        specialButtonPanel.setLayout(null);
        specialButtonPanel.setBounds(TOPX, TOPY + HEIGHT + 2 * V_SPACE, FRAME_WIDTH - 60, HEIGHT);
        specialButtonPanel.setBackground(Color.LIGHT_GRAY);
        add(specialButtonPanel);

        int tempX = 10;
        for (int i = 0; i < specialButton.length; i++) {
            specialButton[i] = new MySpecialButton(tempX, 0, WIDTH * 2, HEIGHT, specialButtonText[i], this);
            specialButton[i].setForeground(Color.RED);
            specialButtonPanel.add(specialButton[i]);
            tempX += WIDTH * 2 + H_SPACE;
        }

        // Setup coordinates for Memory Buttons
        tempX = TOPX;
        int y = TOPY + HEIGHT + 3 * V_SPACE + HEIGHT;
        for (int i = 0; i < memoryButton.length; i++) {
            memoryButton[i] = new MyMemoryButton(tempX, y, WIDTH, HEIGHT, memoryButtonText[i], this);
            memoryButton[i].setForeground(Color.RED);
            y += HEIGHT + V_SPACE;
        }

        // Digit Button Panel setup
        int digitX = TOPX + WIDTH + H_SPACE;
        int digitY = y;
        tempX = digitX;
        y = digitY;
        for (int i = 0; i < digitButton.length; i++) {
            digitButton[i] = new MyDigitButton(tempX, y, WIDTH, HEIGHT, digitButtonText[i], this);
            digitButton[i].setForeground(Color.BLUE);
            tempX += WIDTH + H_SPACE;
            if ((i + 1) % 3 == 0) {
                tempX = digitX;
                y += HEIGHT + V_SPACE;
            }
        }

        // Operator Button Panel setup
        int opsX = digitX + 2 * (WIDTH + H_SPACE) + H_SPACE;
        int opsY = digitY;
        tempX = opsX;
        y = opsY;
        for (int i = 0; i < operatorButton.length; i++) {
            tempX += WIDTH + H_SPACE;
            operatorButton[i] = new MyOperatorButton(tempX, y, WIDTH, HEIGHT, operatorButtonText[i], this);
            operatorButton[i].setForeground(Color.RED);
            if ((i + 1) % 2 == 0) {
                tempX = opsX;
                y += HEIGHT + V_SPACE;
            }
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
    }

    static String getFormattedText(double temp) {
        String resText = "" + temp;
        if (resText.lastIndexOf(".0") > 0)
            resText = resText.substring(0, resText.length() - 2);
        return resText;
    }

    public static void main(String[] args) {
        new MyCalculator("Calculator - JavaTpoint");
    }
}

/*******************************************/

class MyDigitButton extends Button implements ActionListener {
    MyCalculator cl;

    MyDigitButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
    }

    static boolean isInString(String s, char ch) {
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == ch) return true;
        return false;
    }

    public void actionPerformed(ActionEvent ev) {
        String tempText = ((MyDigitButton) ev.getSource()).getLabel();
        if (tempText.equals(".")) {
            if (cl.setClear) {
                cl.displayLabel.setText("0.");
                cl.setClear = false;
            } else if (!isInString(cl.displayLabel.getText(), '.'))
                cl.displayLabel.setText(cl.displayLabel.getText() + ".");
            return;
        }

        int index = 0;
        try {
            index = Integer.parseInt(tempText);
        } catch (NumberFormatException e) {
            return;
        }

        if (index == 0 && cl.displayLabel.getText().equals("0")) return;
        if (cl.setClear) {
            cl.displayLabel.setText("" + index);
            cl.setClear = false;
        } else
            cl.displayLabel.setText(cl.displayLabel.getText() + index);
    }
}

/********************************************/

class MyOperatorButton extends Button implements ActionListener {
    MyCalculator cl;

    MyOperatorButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev) {
        String opText = ((MyOperatorButton) ev.getSource()).getLabel();
        cl.setClear = true;
        double temp = Double.parseDouble(cl.displayLabel.getText());
        if (opText.equals("1/x")) {
            try {
                double tempd = 1 / temp;
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));
            } catch (ArithmeticException excp) {
                cl.displayLabel.setText("Divide by 0.");
            }
            return;
        }
        if (opText.equals("sqrt")) {
            try {
                double tempd = Math.sqrt(temp);
                cl.displayLabel.setText(MyCalculator.getFormattedText(tempd));
            } catch (ArithmeticException excp) {
                cl.displayLabel.setText("Divide by 0.");
            }
            return;
        }
        if (!opText.equals("=")) {
            cl.number = temp;
            cl.op = opText.charAt(0);
            return;
        }
        switch (cl.op) {
            case '+' -> temp += cl.number;
            case '-' -> temp = cl.number - temp;
            case '*' -> temp *= cl.number;
            case '%' -> {
                try {
                    temp = cl.number % temp;
                } catch (ArithmeticException excp) {
                    cl.displayLabel.setText("Divide by 0.");
                    return;
                }
            }
            case '/' -> {
                try {
                    temp = cl.number / temp;
                } catch (ArithmeticException excp) {
                    cl.displayLabel.setText("Divide by 0.");
                    return;
                }
            }
        }
        cl.displayLabel.setText(MyCalculator.getFormattedText(temp));
    }
}

/****************************************/

class MyMemoryButton extends Button implements ActionListener {
    MyCalculator cl;

    MyMemoryButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev) {
        char memop = ((MyMemoryButton) ev.getSource()).getLabel().charAt(1);
        cl.setClear = true;
        double temp = Double.parseDouble(cl.displayLabel.getText());
        switch (memop) {
            case 'C' -> {
                cl.memLabel.setText(" ");
                cl.memValue = 0.0;
            }
            case 'R' -> cl.displayLabel.setText(MyCalculator.getFormattedText(cl.memValue));
            case 'S' -> cl.memValue = 0.0;
            case '+' -> {
                cl.memValue += temp;
                if (!cl.displayLabel.getText().equals("0") && !cl.displayLabel.getText().equals("0.0"))
                    cl.memLabel.setText("M");
                else
                    cl.memLabel.setText(" ");
            }
        }
    }
}

/*****************************************/

class MySpecialButton extends Button implements ActionListener {
    MyCalculator cl;

    MySpecialButton(int x, int y, int width, int height, String cap, MyCalculator clc) {
        super(cap);
        setBounds(x, y, width, height);
        this.cl = clc;
        this.cl.add(this);
        addActionListener(this);
    }

    static String backSpace(String s) {
        return s.length() > 1 ? s.substring(0, s.length() - 1) : "0";
    }

    public void actionPerformed(ActionEvent ev) {
        String opText = ((MySpecialButton) ev.getSource()).getLabel();
        if (opText.equals("Backspc")) {
            String tempText = backSpace(cl.displayLabel.getText());
            cl.displayLabel.setText(tempText);
            return;
        }
        if (opText.equals("C")) {
            cl.number = 0.0;
            cl.op = ' ';
            cl.memValue = 0.0;
            cl.memLabel.setText(" ");
        }
        cl.displayLabel.setText("0");
        cl.setClear = true;
    }
}

/*********************************************
Features not implemented and a few notes:
i) No coding done for "+/-" button.
ii) Menubar is not included.
iii) Not for Scientific calculation.
iv) Some computations may lead to unexpected results due to floating-point approximation.
***********************************************/

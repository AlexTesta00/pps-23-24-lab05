package polyglot.a01b;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import polyglot.CellInGame;
import polyglot.Pair;

public class GUI extends JFrame {

    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton,Pair<Integer,Integer>> buttons = new HashMap<>();
    private final Logics logics;

    public GUI(int size, int mines) {
        this.logics = new LogicsImpl(size,mines);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);

        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(BorderLayout.CENTER,panel);

        ActionListener al = (e)->{
            final JButton bt = (JButton)e.getSource();
            final Pair<Integer,Integer> p = buttons.get(bt);
            this.logics.hit(p.getX(), p.getY());
            if(this.logics.lost()){
                this.finishGame();
                JOptionPane.showMessageDialog(this, "You have lost");
            }else if(this.logics.won()){
                this.finishGame();
                JOptionPane.showMessageDialog(this, "You have won");
                System.exit(0);
            }else{
                updateGUI();
            }
        };

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final JButton bt = (JButton) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON3){
                    logics.flagCell(buttons.get(bt).getX(), buttons.get(bt).getY());
                }
                updateGUI();
            }
        });

        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                jb.addActionListener(al);
                this.buttons.put(jb,new Pair<>(j,i));
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }

    private void updateGUI(){
        for (var entry : this.buttons.entrySet()) {
            final CellInGame cellStatus = logics.getCell(entry.getValue().getX(), entry.getValue().getY());

            if (cellStatus.isReveald()) {
                if (cellStatus.isMine()) {
                    entry.getKey().setText("*");
                } else {
                    entry.getKey().setText(String.valueOf(cellStatus.minesNear()));
                }
                entry.getKey().setEnabled(false);
            } else if (cellStatus.isFlagged()) {
                entry.getKey().setText("F");
            } else {
                entry.getKey().setText(" ");
            }
        }
    }

    private void finishGame(){
        this.updateGUI();
        for (var entry : this.buttons.entrySet()) {
            final CellInGame cellStatus = logics.getCell(entry.getValue().getX(), entry.getValue().getY());
            if (cellStatus.isMine()) {
                entry.getKey().setText("*");
            }
            entry.getKey().setEnabled(false);
        }
    }


}


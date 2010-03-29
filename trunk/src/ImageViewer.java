import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ImageViewer extends JFrame implements ActionListener,
		MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel[][] lb = new JLabel[16][16];
	JPanel p = new JPanel(), pb = new JPanel();
	JTextField tf = new JTextField();
	JButton show = new JButton("show"), save = new JButton("export"),
			clear = new JButton("clear");
	boolean flag = false;

	ImageViewer() {
		this.setVisible(true);
		// this.setPreferredSize(new Dimension(160,160));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		p.setLayout(new GridLayout(16, 16));
		this.add(p, BorderLayout.NORTH);
		this.add(tf, BorderLayout.CENTER);
		pb.setLayout(new GridLayout(1, 3));
		pb.add(show);
		pb.add(save);
		pb.add(clear);
		this.add(pb, BorderLayout.SOUTH);
		clear.addActionListener(this);
		save.addActionListener(this);
		show.addActionListener(this);
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 16; j++) {
				lb[i][j] = new JLabel();
				lb[i][j].addMouseListener(this);
				lb[i][j].setOpaque(true);
				lb[i][j].setPreferredSize(new Dimension(10, 10));
				lb[i][j].setBackground(Color.white);
				p.add(lb[i][j]);
			}
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 16; j++) {
				lb[i][j].setBackground(Color.white);
			}
		this.pack();
	}

	Color gray(double r) {
		float t = (float) (1 - r);
		return new Color(t, t, t);
	}

	public static void main(String[] args) {
		new ImageViewer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == show) {
			String s = tf.getText();
			String[] ds = s.split(",");
			for (int i = 0; i < 16; i++)
				for (int j = 0; j < 16; j++)
					lb[i][j]
							.setBackground(ds[16 * i + j].equals("0") ? Color.white
									: Color.black);
		} else if (e.getSource() == save) {
			String s = "";
			for (int i = 0; i < 16; i++)
				for (int j = 0; j < 16; j++)
					if (lb[i][j].getBackground() == Color.white)
						s += "0,";
					else
						s += "1,";
			tf.setText(s);
		} else if (e.getSource() == clear) {

			for (int i = 0; i < 16; i++)
				for (int j = 0; j < 16; j++)
					lb[i][j].setBackground(Color.white);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		flag = !flag;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (flag)
			((JLabel) e.getSource()).setBackground(Color.black);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

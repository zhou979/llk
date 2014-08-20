/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newpackage1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Steven
 */

public class NewClass extends JFrame {
	JButton[][] Buttons;
	JButton StartButton, ChangeButton;
	Panel GamePanel;
	Panel MassagePanel;
	Label Score;
	JButton FirstButton = null, LastButton = null;
	int[][] Names;
	int NowScore = -1;
	int InitNum = 0;
	int start = 0;

	public void init() {
		{
			InitNum += 1;
			System.err.println("initNum:" + InitNum);
		}

		MassagePanel = new Panel();
		Score = new Label("NowScore：" + NowScore);

		this.setLayout(new BorderLayout());
		this.add(MassagePanel, BorderLayout.NORTH);

		Names = new int[6][8];
		Buttons = new JButton[6][8];
		StartButton = new JButton("重新开始");
		ChangeButton = new JButton("改变位置");
		StartButton.addMouseListener(new RetartListener(this));
		ChangeButton.addMouseListener(new RetartListener(this));

		MassagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		MassagePanel.add(StartButton);
		MassagePanel.add(Score);
		MassagePanel.add(ChangeButton);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(200, 100, 640, 660); // 500 450
		this.setTitle("连连看游戏");

		this.restart();
		this.setFocusable(true);
		this.setVisible(true);

	}

	void restart() {
		{
			start += 1;
			System.out.println("start" + start);
		}
		GamePanel = new Panel(new GridLayout(6, 8));
		this.add(BorderLayout.CENTER, GamePanel);

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				Names[i][j] = j + 1;
			}
		}
		// 通过300次随机交换打乱数组
		for (int num = 0; num <= 300; num++) {
			int i = (int) (Math.random() * (6));
			int j = (int) (Math.random() * (8));
			int m = (int) (Math.random() * (6));
			int n = (int) (Math.random() * (8));

			int tmp = Names[i][j];
			Names[i][j] = Names[m][n];
			Names[m][n] = tmp;

		}
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				Buttons[i][j] = new JButton(String.valueOf(Names[i][j]));
				Buttons[i][j].addMouseListener(new GameButtonListener());
				Buttons[i][j].setName("btn_" + i + "_" + j);
				GamePanel.add(Buttons[i][j]);
			}
		}

		NowScore = 0;
		Score.setText("NowScore：" + NowScore);
		this.validate();
	}

	// void change(){}

	// 游戏按键的监听器
	class GameButtonListener implements MouseListener {

		int FX = -1, FY = -1, LX = -1, LY = -1;
		int MX = -1, MY = -1, NX = -1, NY = -1;

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (FirstButton == null) {
				FirstButton = (JButton) e.getSource();
				FirstButton.setBackground(Color.red);
				System.err.println("FirstButton" + FirstButton.getText());
			} else {

				LastButton = (JButton) e.getSource();
				System.err.println("LastButton" + LastButton.getText());
				if ((LastButton.getText().equals(FirstButton.getText())) && isLinkable()) {
					remove();
				} else {
					FirstButton.setBackground(LastButton.getBackground());
					FirstButton = LastButton;
					FirstButton.setBackground(Color.red);
				}

			}
			// System.out.print(((JButton)e.getSource()).getName());

		}

		boolean isLinkable() {
			if (FirstButton == LastButton) {
				return false;
			}
			String[] XY = FirstButton.getName().split("_");
			FX = Integer.parseInt((XY)[1]);
			FY = Integer.parseInt((XY)[2]);
			XY = LastButton.getName().split("_");
			LX = Integer.parseInt((XY)[1]);
			LY = Integer.parseInt((XY)[2]);

			if (isDirectLinked(FX, FY, LX, LY)) {
				return true;
			} else {
				if (isOneTurnigLinked(FX, FY, LX, LY))
					return true;
				else {
					// f点从Y轴正方向接近l点
					for (int i = FY + 1; i < LY; i++)
						if (!(Buttons[FX][i].isVisible())) {
							if ((isOneTurnigLinked(FX, i, LX, LY)))
								return true;
						} else {
							break;
						}
					// f点从Y轴负方向接近l点
					for (int i = FY - 1; i > LY; i--)
						if (!(Buttons[FX][i].isVisible())) {
							if ((isOneTurnigLinked(FX, i, LX, LY)))
								return true;
						} else {
							break;
						}

					// F点从X轴正方向接近L点
					for (int i = FX + 1; i < LX; i++)
						if (!(Buttons[i][FY].isVisible())) {
							if ((isOneTurnigLinked(i, FY, LX, LY)))
								return true;
						} else {
							break;
						}

					// F点从X轴负方向接近L点
					for (int i = FX - 1; i > LX; i++)
						if (!(Buttons[i][FY].isVisible())) {
							if ((isOneTurnigLinked(i, FX, LX, LY)))
								return true;
						} else {
							break;
						}

				}
			}
			return false;
		}

		boolean isOneTurnigLinked(int FX, int FY, int LX, int LY) {
			return ((!Buttons[FX][LY].isVisible()) && (isDirectLinked(FX, FY, FX, LY) && isDirectLinked(FX, LY, LX, LY)))
			        || ((!Buttons[LX][FY].isVisible()) && (isDirectLinked(FX, FY, LX, FY) && isDirectLinked(LX, FY, LX, LY)));
		}

		boolean isDirectLinked(int FX, int FY, int LX, int LY) {
			if (!(FX == LX || LY == FY)) {
				return false;
			} else {
				if (FX == LX) {
					for (int i = Math.min(FY, LY) + 1; i < Math.max(FY, LY); i++) {
						if (true == Buttons[FX][i].isVisible())
							return false;
					}
				} else {
					for (int i = Math.min(FX, LX) + 1; i < Math.max(FX, LX); i++) {
						if (true == Buttons[i][FY].isVisible())
							return false;
					}

				}
			}
			return true;
		}

		public void remove() {
			FirstButton.setVisible(false);
			FirstButton.setText("0");
			FirstButton = null;
			LastButton.setVisible(false);
			LastButton.setText("0");
			LastButton = null;
			Names[FX][FY] = 0;
			Names[LX][LY] = 0;
			NowScore += 100;
			Score.setText("NowScore：" + NowScore);

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

	class RetartListener implements MouseListener {
		NewClass game;

		public RetartListener(NewClass game) {
			this.game = game;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (((JButton) e.getSource()).getText() == game.StartButton.getText()) {

				game.remove(GamePanel);
				game.restart();

				// game.restart();
			}
			if (((JButton) (e.getSource())).getText() == game.ChangeButton.getText()) {
				game.remove(GamePanel);
				GamePanel = new Panel(new GridLayout(6, 8));
				for (int num = 0; num <= 100; num++) {
					int i = (int) (Math.random() * (6));
					int j = (int) (Math.random() * (8));
					int m = (int) (Math.random() * (6));
					int n = (int) (Math.random() * (8));

					int tmp = Names[i][j];
					Names[i][j] = Names[m][n];
					Names[m][n] = tmp;

				}
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 8; j++) {

						Buttons[i][j] = new JButton(String.valueOf(Names[i][j]));
						Buttons[i][j].addMouseListener(new GameButtonListener());
						Buttons[i][j].setName("btn_" + i + "_" + j);
						if (Buttons[i][j].getText().equals("0")) {
							Buttons[i][j].setVisible(false);
						}
						GamePanel.add(Buttons[i][j]);
					}
				}
				game.add(GamePanel);
				game.validate();
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// throw new UnsupportedOperationException("Not supported yet.");
			// //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// throw new UnsupportedOperationException("Not supported yet.");
			// //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// throw new UnsupportedOperationException("Not supported yet.");
			// //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// throw new UnsupportedOperationException("Not supported yet.");
			// //To change body of generated methods, choose Tools | Templates.
		}

	}

	public static void main(String[] args) {
		NewClass newclass = new NewClass();
		newclass.init();

	}

}

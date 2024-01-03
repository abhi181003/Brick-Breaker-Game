package brickBreaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;


public class GamePlay extends JPanel implements KeyListener, ActionListener{

	private boolean play = false;
	
	private int score = 0;
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int player1 = (int) (Math.random() * 400) + 150;
	
	private int ballPositionX = (int) (Math.random() * 400) + 150;
	private int ballPositionY = (int) (Math.random() * 200) + 300;
	private double ballDirectionX = -1.5;
	private double ballDirectionY = -2.5;
	
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1,1,692,592);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0,0,3,592);
		g.fillRect(0,0,692,3);
		g.fillRect(688,0,3,570);
		
		//paddle
		g.setColor(Color.green);;
		g.fillRect(player1, 550, 100, 8);
		
		//ball
		g.setColor(Color.yellow);;
		g.fillOval(ballPositionX, ballPositionY, 20, 20);
		
		if(ballPositionY > 570 || totalBricks <= 0) {
			play = false;
			ballDirectionX = 0;
			ballDirectionY = 0;
			g.setColor(Color.orange);
			g.setFont(new Font("serif", Font.BOLD, 30));
			if(totalBricks <= 0) {
				g.drawString("You Won, Score: "+score, 210, 300);
			}
			else {
				g.drawString("Game Over, Score: "+score, 210, 300);
			}
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 235, 345);
		}
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score,650, 30);
		
		//draw map
		map.draw((Graphics2D)g);
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.restart();
		if(play) {
			ballPositionX = ballPositionX + (int)ballDirectionX;
			ballPositionY = ballPositionY + (int)ballDirectionY;
			
			if(ballPositionX < 0) {
				ballDirectionX = -ballDirectionX;
			}
			
			if(ballPositionY < 0) {
				ballDirectionY = -ballDirectionY;
			}
			
			if(ballPositionX > 670) {
				ballDirectionX = -ballDirectionX;
			}
			
			if(new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(player1, 550, 100, 8))) {
				ballDirectionY = -ballDirectionY;
			}
			
			A: for(int i=0; i<map.map.length; i++) {
				for(int j=0; j<map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
						Rectangle brickRect= rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score = score + 5;
							ballDirectionX = ballDirectionX + 0.25;
							ballDirectionY = ballDirectionY + 0.25;
							
							if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width) {
								ballDirectionX = -ballDirectionX;
							}
							else {
								ballDirectionY = - ballDirectionY;
							}
							
							break A;
						}
					}
				}
			}
		}
		repaint();
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(player1 >= 600) {
				player1 = 600;
			}
			else {
				moveRight();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(player1 < 10) {
				player1 = 10;
			}
			else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballPositionX = 120;
				ballPositionY = 350;
				ballDirectionX = -1.5;
				ballDirectionY = -2.5;
				player1 = 310;
				score = 0;
				totalBricks = 21;
				map = new MapGenerator( 3, 7);
				repaint();
			}
		}
	}
	
	public void moveRight() {
		play = true;
		player1 = player1 + 20;
	}
	
	public void moveLeft() {
		play = true;
		player1 = player1 - 20;
	}
}

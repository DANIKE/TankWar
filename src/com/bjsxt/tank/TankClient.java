package com.bjsxt.tank;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;
import java.util.*;

/**
 * 主窗口
 * @author DANIKE
 *
 */
public class TankClient extends Frame {
	Tank myTank = new Tank(50,50, true, Direction.STOP, this);
	//Tank enemyTank = new Tank(100,100, false, this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	//Explode e = new Explode (70, 70, this);
	
	//窗口的宽度和高度
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	//墙
	Wall w1 = new Wall(300, 200, 20, 150, this); 
	Wall w2 = new Wall(400, 100, 300, 20, this);
	
	Image OffscreenImage = null;
	
	Blood b = new Blood();
	
	public void paint(Graphics g) {
		
		g.drawString("missiles count:" + missiles.size(), 50, 50);
		g.drawString("explodes count:" + explodes.size(), 50, 60);
		g.drawString("tanks    count:" + tanks.size(), 50, 70);
		g.drawString("tanks    life:" + myTank.getLife(), 50, 80);
		
		if(tanks.size() <= 0){
			for(int i=0; i<5; i++){
				tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
			}
		}
		
		for(int i=0; i<missiles.size(); i++){
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			//if(!m.isLive() ) missiles.remove(m);
			//else m.draw(g);
			m.draw(g);
		}
		
		for(int i=0; i<explodes.size(); i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//e.draw(g);
		
		for(int i=0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		myTank.draw(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
		//enemyTank.draw(g);
		//if(m != null) m.draw(g);
	}
	
	public void update(Graphics g) {
		if(OffscreenImage == null){
			this.OffscreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = OffscreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(OffscreenImage, 0, 0, null);
		
	}

	public void launchFrame() {
		
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int initTankCount = Integer.parseInt(props.getProperty("initTankCount"));
		for(int i=0; i<initTankCount; i++){
			tanks.add(new Tank(50 + 40*(i+1), 50, false, Direction.D, this));
			
		}
		
		//setLocation(300,400);
		setSize(GAME_WIDTH,GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} 
			
		});
		this.setResizable(false);
		this.setTitle("TankWar");
		this.setBackground(Color.GREEN);
		new Thread(new PaintThread()).start();
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
	}
	
	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
			
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
	
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public static void main(String[] args) {
		TankClient tk = new TankClient();
		tk.launchFrame();

	}

}

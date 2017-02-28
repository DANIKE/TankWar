package com.bjsxt.tank;
import java.awt.*;

public class Blood {
	int x, y, w, h;
	TankClient tc;
	
	int step = 0;
	
	private int[][] pos = {
			{350,300},{360,300},{374,274},{40,200},{360,270},{365,290},{340,280}
	};
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x,y,w,h);
		g.setColor(c);
		
		move();
	}
	private void move() {
		step++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
		w = h = 10;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
}

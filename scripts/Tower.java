import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Tower extends Rectangle {
	int type, range, damage, freq, speed;
	
	public Tower(int gridX, int gridY, int type) {
		super(gridX*GameFrame.blockSize+GameFrame.leftMargin,gridY*GameFrame.blockSize+GameFrame.topMargin,GameFrame.blockSize,GameFrame.blockSize);
        this.type = type;
	
	if (type == 1) {
		this.range = 5;
        this.damage = 4;
        this.freq = 8;
        this.speed = 3;
	}
	else if (type == 2){
		this.range = 6;
        this.damage = 5;
        this.freq = 2;
        this.speed = 4;
	}
	else if (type == 3) {
		this.range = 7;
        this.damage = 6;
        this.freq = 1;
        this.speed = 5;
	}
	else if (type == 4) {
		this.range = 8;
        this.damage = 5;
        this.freq = 10;
        this.speed = 6;
	}
	else if (type == 5) {
		this.range = 8;
        this.damage = 15;
        this.freq = 4;
        this.speed = 8;
	}
	else if (type == 6) {
		this.range = 10;
        this.damage = 30;
        this.freq = 1;
        this.speed = 10;
	}
	
	}
}

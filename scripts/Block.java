import java.awt.Rectangle;

public class Block extends Rectangle{
	int cost;
	public Block(int gridX, int gridY, int cost) {
		super(gridX*GameFrame.blockSize+GameFrame.leftMargin,gridY*GameFrame.blockSize+GameFrame.topMargin,GameFrame.blockSize,GameFrame.blockSize);
        this.cost = cost;
	}
}

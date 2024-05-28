import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Tower {

}

/**
 * This is for the block object, independent from towers
 * But towers will follow the same format, inheritting grid x and y instead of xy.
 */
class Block extends Rectangle{
    int cost;
    Block(int gridX, int gridY,int cost){
        super(gridX*GameFrame.blockSize+GameFrame.leftMargin,gridY*GameFrame.blockSize+GameFrame.topMargin,GameFrame.blockSize,GameFrame.blockSize);
        this.cost = cost;
    }
}
package TwoDimTexturing;

import static TwoDimTexturing.World.*;

/**
 * @author Joshua Chen
 * Date Created: Mar 05, 2019
 */
public class BlockGrid {
    private Block[][] blocks = new Block[BLOCKS_WIDTH][BLOCKS_HEIGHT];

    public BlockGrid() {
        for (int x = 0; x < BLOCKS_WIDTH - 1; x++) {
            for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) {
                blocks[x][y] = new Block(BlockType.DIRT, x * BLOCK_SIZE, y * BLOCK_SIZE);
            }
        }
    }

    public void setAt(int x, int y, BlockType b) {
        blocks[x][y] = new Block(b, x * BLOCK_SIZE, y * BLOCK_SIZE);
    }

    public Block getAt(int x, int y) {
        return blocks[x][y];
    }

    public void draw() {
        for (int x = 0; x < BLOCKS_WIDTH - 1; x++) {
            for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) {
                blocks[x][y].draw();
            }
        }
    }

    public void clear() {
        for (int x = 0; x < BLOCKS_WIDTH - 1; x++) {
            for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) {
                blocks[x][y] = new Block(BlockType.DIRT, x * BLOCK_SIZE, y * BLOCK_SIZE);
            }
        }
    }
}

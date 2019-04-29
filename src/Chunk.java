import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import Models.CubeType;

/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */
public class Chunk {

    private final int SIZE = 30;
    private final double PERSISTENCE = .25;
    private final float CUBE_SIZE = .1f;

    private Texture terrainTexture;

    //private CubeModel blocks[][][];
    private Block blocks[][][];
    private SimplexNoise noise;

    private double[][] heightMap;

    public Chunk() {
        long seed = System.currentTimeMillis();
        noise = new SimplexNoise(32, PERSISTENCE, (int) seed);
        try {
            terrainTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/simpleterrain.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cubeInit();

    }

    // method: cubeInit
    // purpose:  initialize cube
    private void cubeInit() {
        //blocks = new CubeModel[SIZE][SIZE][SIZE];
        blocks = new Block[SIZE][SIZE][SIZE];
        heightMap = new double[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                double height = noise.getNoise(x, z) + 1;
                heightMap[x][z] = 0 - height;
                setupColumn(x, z, height);
            }
        }
    }

    // method: getType
    // purpose:  get type of cube
    private CubeType getType(int height) {
        //Random rand = new Random();
        //int texture;
        if (height <= 2) {
            return CubeType.Bedrock;
        } else if (height <= 4 && height > 2) {
            return CubeType.Stone;
        } else if (height <= 6 && height > 4) {
            return CubeType.Dirt;
        } else if (height == 8) {
            return CubeType.Water;
        } else if (height == 10) {
            return CubeType.Sand;
        } else {
            return CubeType.Grass;
        }
    }

    // method: setupColumn
    // purpose:  setup Column of cubes
    private void setupColumn(int n, int m, double height) {
        float x = CUBE_SIZE * n;
        float z = CUBE_SIZE * m;
        if (height >= SIZE) {
            height = SIZE - 1;
        }

        int jMax = (int) (height / CUBE_SIZE);

        for (int j = 0; j < jMax; j++) {
            float y = CUBE_SIZE * j;
            CubeType cubeType;
            if (j == jMax) {
                cubeType = CubeType.Grass;
            } else {
                cubeType = getType(j);
            }

            //Random rand = new Random();
            //cubeType = getType(rand.nextInt(6));
            //blocks[n][j][m] = new CubeModel(new Grid3D(x, y, z), CUBE_SIZE, cubeType);
            blocks[n][j][m] = new Block(cubeType, x, y, z);
            blocks[n][j][m].setTexture(terrainTexture, 64);

        }
    }

    // method: draw
    // purpose:  draw cubes
    public void draw() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (blocks[x][y][z] != null) {
                        blocks[x][y][z].draw();
                    }
                }
            }
        }
    }

    public double[][] getHeightMap() {
        return heightMap;
    }

}
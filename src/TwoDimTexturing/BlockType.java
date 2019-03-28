package TwoDimTexturing;

/**
 * @author Joshua Chen
 * Date Created: Mar 05, 2019
 */
public enum BlockType {
    STONE("2DimRes/stone.jpeg"),
    DIAMOND("2DimRes/diamond.jpeg"),
    GRASS("2DimRes/grass.jpeg"),
    DIRT("2DimRes/dirt.jpeg");

    public final String location;

    BlockType(String location) {
        this.location = location;
    }
}

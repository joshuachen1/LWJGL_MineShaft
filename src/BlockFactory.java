/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 11, 2019
 */

import Models.CubeType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockFactory {

    private static Map<CubeType, Flyweight> map = new ConcurrentHashMap<>();

    public static Flyweight getBlock(CubeType cubeType) {
        if (map.containsKey(cubeType)) {
            return map.get(cubeType);
        } else {
            Flyweight block = new Block(cubeType);
            map.put(cubeType, block);
            return block;
        }
    }

}
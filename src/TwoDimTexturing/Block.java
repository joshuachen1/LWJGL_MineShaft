package TwoDimTexturing;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static TwoDimTexturing.World.BLOCK_SIZE;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author Joshua Chen
 * Date Created: Mar 05, 2019
 */
public class Block {
    private BlockType type = BlockType.DIRT;
    private Texture texture;
    private float x, y;

    public Block(BlockType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        try {
            this.texture = TextureLoader.getTexture("JPEG", new FileInputStream(new File(type.location)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        texture.bind();
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void draw() {
        texture.bind();
        glLoadIdentity();
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);

            glTexCoord2f(1, 0);
            glVertex2f(BLOCK_SIZE, 0);

            glTexCoord2f(1, 1);
            glVertex2f(BLOCK_SIZE, BLOCK_SIZE);

            glTexCoord2f(0, 1);
            glVertex2f(0, BLOCK_SIZE);
        glEnd();
        glLoadIdentity();
    }
}

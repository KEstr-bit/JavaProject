package DOM;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;

import java.util.ArrayList;
import java.util.List;


class TexturePack {
    private List<Texture> textures;
    public static final int SIZE_WALL_TEXT = 128;
    public static final int SIZE_ENTITY_TEXT = 128;
    public static final int SIZE_FLOOR_TEXT = 64;
    public static final int FRAMES_COUNT = 8;

    // Constructor with no parameters
    public TexturePack() throws Exception {

        Image img = new Image();
        int countText = TextureType.values().length;
        for(int i = 0; i < countText; i++) {
            try {
                assert false;
                textures.set(i, new Texture());
                img.loadFromFile(java.nio.file.Paths.get("image" + i + ".png").toAbsolutePath());
                textures.get(i).loadFromImage(img);
            }
            catch (Exception _) {
                throw new Exception("Не удалось загрузить image" + i + ".png");
            }
        }
    }

    // Constructor with an integer parameter
    public TexturePack(int unusedParameter) {
        textures = new ArrayList<>();
        Texture texture = new Texture();
        for (int i = 0; i < TextureType.values().length; i++) {
            textures.add(texture);
        }
    }

    // Get a texture based on the type
    public Texture getTexture(TextureType type) {
        return textures.get(type.ordinal());
    }
}

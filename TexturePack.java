package DOM;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;

public class TexturePack {
    private final Texture[] textures = new Texture[TextureType.values().length];

    public TexturePack() throws Exception {

        Image img = new Image();
        int countText = TextureType.values().length;
        for(int i = 0; i < countText; i++) {
            try {
                textures[i] = new Texture();
                img.loadFromFile(java.nio.file.Paths.get("image" + i + ".png").toAbsolutePath());
                textures[i].loadFromImage(img);
            }
            catch (Exception _) {
                throw new Exception("Не удалось загрузить image" + i + ".png");
            }
        }
    }

    public TexturePack(int a)
    {
        int countText = TextureType.values().length;
        for(int i = 0; i < countText; i++)
                textures[i] = new Texture();
    }

    public Texture getTexture(TextureType type)
    {
        return textures[type.ordinal()];
    }




}

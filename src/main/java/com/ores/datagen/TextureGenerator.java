package com.ores.datagen;

import com.ores.core.Materials;
import com.ores.core.Variants;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Generates textures dynamically for ores, blocks, and items
 * based on the Materials and Variants enums.
 */
public class TextureGenerator {

    // --- Paths ---
    private static String RAW_PALETTE_PATH;
    private static String BASE_PALETTE_PATH;
    private static String ORE_OVERLAY_VARIANT_PATH;
    private static String ORE_MINECRAFT_VARIANT_PATH;
    private static String STORAGE_BLOCK_VARIANT_PATH;
    private static String ITEM_VARIANT_PATH;
    private static String OUTPUT_BLOCK_PATH;
    private static String OUTPUT_ITEM_PATH;

    // --- Pre-loaded Palettes ---
    private static Color[] rawGrayscalePalette;
    private static Color[] baseGrayscalePalette;

    public static void generateAllTextures() {
        try {
            System.out.println("Initializing texture generator...");
            setupPaths();
            loadGrayscalePalettes();

            generateAll();

            System.out.println("\n--- All texture generation completed successfully! ---");
        } catch (IOException e) {
            System.err.println("CRITICAL ERROR during texture generator initialization. Process stopped.");
            e.printStackTrace();
        }
    }

    private static void generateAll() {
        System.out.println("\n--- Starting Texture Generation ---");
        for (Materials material : Materials.values()) {
            List<String> exclusions = material.getVanillaExclusions() != null ?
                    material.getVanillaExclusions().excludedVariantIds() : Collections.emptyList();

            for (Variants variant : material.getVariants()) {
                String id = variant.getFormattedId(material.getId());
                // Skip generation if this variant is in the exclusion list for the material
                if (exclusions != null && exclusions.contains(id)) {
                    continue;
                }

                switch (variant.getCategory()) {
                    case ITEM -> generateSingleItemTexture(material, variant);
                    case BLOCK, FALLING_BLOCK -> generateSingleBlockTexture(material, variant);
                    case ORE, FALLING_ORE -> generateSingleOreTexture(material, variant);
                }
            }
        }
    }

    // --- INDIVIDUAL GENERATION LOGIC ---

    private static void generateSingleOreTexture(Materials material, Variants variant) {
        String materialName = material.getId();
        String outputFileName = variant.getFormattedId(materialName) + ".png";
        Variants.OreProps oreProps = Objects.requireNonNull(variant.getOreProps());

        try {
            String baseStoneName = oreProps.idStone().replace("minecraft:", "") + ".png";
            File baseStoneFile = Paths.get(ORE_MINECRAFT_VARIANT_PATH, baseStoneName).toFile();
            if (!baseStoneFile.exists()) return;
            BufferedImage baseImage = ImageIO.read(baseStoneFile);

            Materials.OreOverlayTexture overlayTexture = material.getOreProps().overlay();
            if (overlayTexture == null) return;
            String overlayName = overlayTexture.name().toLowerCase() + ".png";
            File overlayFile = new File(ORE_OVERLAY_VARIANT_PATH, overlayName);
            if (!overlayFile.exists()) {
                System.err.printf("  -> WARNING: Overlay '%s' not found. Skipping '%s'.%n", overlayName, outputFileName);
                return;
            }
            BufferedImage oreOverlay = ImageIO.read(overlayFile);

            Map<Integer, Color> colorMap = getColorMap(materialName, oreProps.colorType());
            if (colorMap == null) return;

            BufferedImage finalImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = finalImage.createGraphics();
            g.drawImage(baseImage, 0, 0, null);
            g.drawImage(colorizeImage(oreOverlay, colorMap), 0, 0, null);
            g.dispose();

            saveImage(finalImage, outputFileName, OUTPUT_BLOCK_PATH);
        } catch (IOException e) {
            handleException(e, outputFileName);
        }
    }

    private static void generateSingleBlockTexture(Materials material, Variants variant) {
        String materialName = material.getId();
        String outputFileName = variant.getFormattedId(materialName) + ".png";
        Variants.BlockProps blockProps = Objects.requireNonNull(variant.getBlockProps());

        Object textureEnum = getTextureEnum(material, variant);
        if (textureEnum == null) return;

        try {
            String baseTextureName = ((Enum<?>) textureEnum).name().toLowerCase() + ".png";
            String subfolder = variant.getFormattedId("material");
            File baseTextureFile = Paths.get(STORAGE_BLOCK_VARIANT_PATH, subfolder, baseTextureName).toFile();

            if (!baseTextureFile.exists()) {
                System.err.printf("  -> WARNING: Base texture '%s' not found in subfolder '%s'. Skipping '%s'.%n", baseTextureName, subfolder, outputFileName);
                return;
            }

            Map<Integer, Color> colorMap = getColorMap(materialName, blockProps.colorType());
            if (colorMap == null) return;
            BufferedImage baseImage = ImageIO.read(baseTextureFile);

            BufferedImage finalImage = colorizeImage(baseImage, colorMap);
            saveImage(finalImage, outputFileName, OUTPUT_BLOCK_PATH);
        } catch (IOException e) {
            handleException(e, outputFileName);
        }
    }

    private static void generateSingleItemTexture(Materials material, Variants variant) {
        String materialName = material.getId();
        String outputFileName = variant.getFormattedId(materialName) + ".png";
        Variants.ItemProps itemProps = Objects.requireNonNull(variant.getItemProps());

        Object textureEnum = getTextureEnum(material, variant);
        if (textureEnum == null) return;

        try {
            String baseTextureName = ((Enum<?>) textureEnum).name().toLowerCase() + ".png";
            String subfolder = variant.getFormattedId("material");
            File baseTextureFile = Paths.get(ITEM_VARIANT_PATH, subfolder, baseTextureName).toFile();

            if (!baseTextureFile.exists()) {
                System.err.printf("  -> WARNING: Base texture '%s' not found in subfolder '%s'. Skipping '%s'.%n", baseTextureName, subfolder, outputFileName);
                return;
            }

            Map<Integer, Color> itemColorMap = getColorMap(materialName, itemProps.colorType());
            if (itemColorMap == null) return;
            BufferedImage baseImage = ImageIO.read(baseTextureFile);

            BufferedImage finalImage = colorizeImage(baseImage, itemColorMap);
            saveImage(finalImage, outputFileName, OUTPUT_ITEM_PATH);
        } catch (IOException e) {
            handleException(e, outputFileName);
        }
    }

    // --- UTILITY METHODS ---

    private static Object getTextureEnum(Materials material, Variants variant) {
        return switch (variant) {
            case SELF -> material.getItemProps().selfTexture();
            case INGOT -> material.getItemProps().ingotTexture();
            case RAW -> material.getItemProps().rawTexture();
            case NUGGET -> material.getItemProps().nuggetTexture();
            case BLOCK -> material.getBlockProps().blockTexture();
            case RAW_BLOCK -> material.getBlockProps().rawBlockTexture();
            default -> null;
        };
    }

    private static Map<Integer, Color> getColorMap(String materialName, Variants.ColorType type) throws IOException {
        String palettePath = (type == Variants.ColorType.RAW) ? RAW_PALETTE_PATH : BASE_PALETTE_PATH;
        Color[] grayscalePalette = (type == Variants.ColorType.RAW) ? rawGrayscalePalette : baseGrayscalePalette;

        File materialPaletteFile = new File(palettePath, materialName + ".png");
        if (!materialPaletteFile.exists()) {
            System.err.printf("  -> WARNING: Color palette for material '%s' not found. Skipping texture generation for this material.%n", materialName);
            return null;
        }

        BufferedImage materialPaletteImg = ImageIO.read(materialPaletteFile);
        return createColorMap(grayscalePalette, createPaletteArray(materialPaletteImg));
    }

    private static BufferedImage colorizeImage(BufferedImage sourceImage, Map<Integer, Color> colorMap) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        BufferedImage coloredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = sourceImage.getRGB(x, y);
                Color mappedColor = colorMap.get(pixel);
                if (mappedColor != null) {
                    int alpha = (pixel >> 24) & 0xff;
                    if (alpha > 0) {
                        Color newColor = new Color(mappedColor.getRed(), mappedColor.getGreen(), mappedColor.getBlue(), alpha);
                        coloredImage.setRGB(x, y, newColor.getRGB());
                    }
                } else {
                    coloredImage.setRGB(x, y, pixel);
                }
            }
        }
        return coloredImage;
    }

    private static void saveImage(BufferedImage image, String fileName, String outputPath) throws IOException {
        File outputFile = new File(outputPath, fileName);
        outputFile.getParentFile().mkdirs();
        ImageIO.write(image, "png", outputFile);
        System.out.println("  -> Texture created: " + outputFile.getName());
    }

    private static void setupPaths() {
        String projectRoot = new File("").getAbsolutePath();
        if (projectRoot.endsWith(File.separator + "run")) {
            projectRoot = projectRoot.substring(0, projectRoot.length() - (File.separator.length() + 3));
        }
        System.out.println("Project root detected: " + projectRoot);

        String resourcesPath = Paths.get(projectRoot, "src", "main", "resources", "assets", "ores", "textures").toString();

        String MATERIAL_PALETTE_BASE_PATH = Paths.get(resourcesPath, "material", "palette").toString();
        RAW_PALETTE_PATH = Paths.get(MATERIAL_PALETTE_BASE_PATH, "raw").toString();
        BASE_PALETTE_PATH = Paths.get(MATERIAL_PALETTE_BASE_PATH, "base").toString();
        ORE_OVERLAY_VARIANT_PATH = Paths.get(resourcesPath, "variant", "ore", "overlay").toString();
        String ORE_VARIANT_PATH = Paths.get(resourcesPath, "variant", "ore").toString();
        ORE_MINECRAFT_VARIANT_PATH = Paths.get(ORE_VARIANT_PATH, "minecraft").toString();
        STORAGE_BLOCK_VARIANT_PATH = Paths.get(resourcesPath, "variant", "storage_block").toString();
        ITEM_VARIANT_PATH = Paths.get(resourcesPath, "variant", "item").toString();
        OUTPUT_BLOCK_PATH = Paths.get(resourcesPath, "block").toString();
        OUTPUT_ITEM_PATH = Paths.get(resourcesPath, "item").toString();
    }

    private static void loadGrayscalePalettes() throws IOException {
        rawGrayscalePalette = createPaletteArray(ImageIO.read(new File(RAW_PALETTE_PATH, "BASE.png")));
        baseGrayscalePalette = createPaletteArray(ImageIO.read(new File(BASE_PALETTE_PATH, "BASE.png")));
    }

    private static Color[] createPaletteArray(BufferedImage paletteImage) {
        Color[] palette = new Color[paletteImage.getWidth()];
        for (int i = 0; i < paletteImage.getWidth(); i++) {
            palette[i] = new Color(paletteImage.getRGB(i, 0));
        }
        return palette;
    }

    private static Map<Integer, Color> createColorMap(Color[] grayscalePalette, Color[] materialPalette) {
        Map<Integer, Color> map = new HashMap<>();
        int paletteSize = Math.min(grayscalePalette.length, materialPalette.length);
        for (int i = 0; i < paletteSize; i++) {
            map.put(grayscalePalette[i].getRGB(), materialPalette[i]);
        }
        return map;
    }

    private static void handleException(IOException e, String fileName) {
        System.err.println("Error generating texture '" + fileName + "'");
        e.printStackTrace();
    }
}

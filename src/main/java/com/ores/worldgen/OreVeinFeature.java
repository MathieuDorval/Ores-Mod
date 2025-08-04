package com.ores.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class OreVeinFeature extends Feature<OreVeinConfiguration> {

    public OreVeinFeature(Codec<OreVeinConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreVeinConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        OreVeinConfiguration config = context.config();

        int startX = origin.getX() + random.nextInt(16);
        int startY = random.nextInt(level.getMinY(), level.getMaxY());
        int startZ = origin.getZ() + random.nextInt(16);

        int length = (int) (config.veinSize() * 200);
        float radius = config.veinSize();

        float yaw = random.nextFloat() * (float) Math.PI * 2.0F;
        float pitch = (random.nextFloat() - 0.5F) * 2.0F / 8.0F;
        float pitchHorz = Mth.cos(pitch);
        float pitchVert = Mth.sin(pitch);

        double currentX = startX;
        double currentY = startY;
        double currentZ = startZ;

        for (int i = 0; i < length; ++i) {
            if (random.nextInt(6) == 0) {
                yaw += random.nextFloat() * 0.5F - 0.25F;
            }
            if (random.nextInt(6) == 0) {
                pitch += random.nextFloat() * 0.5F - 0.25F;
            }

            currentX += Mth.cos(yaw) * pitchHorz;
            currentY += pitchVert;
            currentZ += Mth.sin(yaw) * pitchHorz;

            pitch *= 0.92F;
            pitch += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 0.1F;
            yaw += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 0.1F;
            pitchHorz = Mth.cos(pitch);
            pitchVert = Mth.sin(pitch);

            double sphereRadius = radius + Mth.sin((float) i * (float) Math.PI / length) * 2.5F;
            carveSphere(level, config, currentX, currentY, currentZ, sphereRadius, random);
        }

        return true;
    }

    private void carveSphere(WorldGenLevel level, OreVeinConfiguration config, double x, double y, double z, double radius, RandomSource random) {
        int minX = Mth.floor(x - radius);
        int maxX = Mth.floor(x + radius);
        int minY = Mth.floor(y - radius);
        int maxY = Mth.floor(y + radius);
        int minZ = Mth.floor(z - radius);
        int maxZ = Mth.floor(z + radius);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int blockX = minX; blockX <= maxX; ++blockX) {
            for (int blockY = minY; blockY <= maxY; ++blockY) {
                for (int blockZ = minZ; blockZ <= maxZ; ++blockZ) {
                    double dx = blockX - x;
                    double dy = blockY - y;
                    double dz = blockZ - z;

                    if (dx * dx + dy * dy + dz * dz < radius * radius) {
                        mutablePos.set(blockX, blockY, blockZ);
                        if (config.target().test(level.getBlockState(mutablePos), random)) {
                            // Décide si on place un minerai ou de la pierre
                            if (random.nextFloat() < config.primaryOreDensity()) {
                                // Si c'est un minerai, on vérifie si c'est le bloc rare
                                if (random.nextFloat() < config.rareBlockChance()) {
                                    level.setBlock(mutablePos, config.rareBlock(), 2);
                                } else {
                                    level.setBlock(mutablePos, config.primaryOre(), 2);
                                }
                            } else {
                                level.setBlock(mutablePos, config.fillerStone(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
}

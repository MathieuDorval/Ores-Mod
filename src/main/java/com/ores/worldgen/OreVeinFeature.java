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

        double startX = origin.getX() + 8.0;
        double startZ = origin.getZ() + 8.0;
        double startY = random.nextInt(level.getMinY(), level.getMaxY());

        double maxDistanceSq = 40 * 40;

        int length = 300;
        float radius = config.veinSize();

        float yaw = random.nextFloat() * (float) Math.PI * 2.0F;
        float pitch = (random.nextFloat() - 0.5F) * 2.0F / 8.0F;

        double currentX = startX;
        double currentY = startY;
        double currentZ = startZ;

        for (int i = 0; i < length; ++i) {
            float pitchHorz = Mth.cos(pitch);
            float pitchVert = Mth.sin(pitch);
            currentX += Mth.cos(yaw) * pitchHorz;
            currentY += pitchVert;
            currentZ += Mth.sin(yaw) * pitchHorz;

            pitch *= 0.92F;
            if (random.nextInt(4) == 0) {
                yaw += (random.nextFloat() - 0.5F) * 0.65F;
                pitch += (random.nextFloat() - 0.5F) * 0.1F;
            }

            double distSq = (currentX - startX) * (currentX - startX) + (currentZ - startZ) * (currentZ - startZ);
            if (distSq > maxDistanceSq) {
                break;
            }

            if (currentY > level.getMaxY() || currentY < level.getMinY()) {
                continue;
            }

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
                            if (random.nextFloat() < config.primaryOreDensity()) {
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
package com.ores.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CustomBlocks {
    // FALLING BLOCK
    public static class CustomFallingBlock extends FallingBlock {
        private final boolean dropsOnBreak;
        public static final MapCodec<CustomFallingBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec(), Codec.BOOL.fieldOf("drops_on_break").forGetter(b -> b.dropsOnBreak)).apply(instance, CustomFallingBlock::new));
        public CustomFallingBlock(BlockBehaviour.Properties properties, boolean dropsOnBreak) { super(properties); this.dropsOnBreak = dropsOnBreak; }
        @Override protected @NotNull MapCodec<? extends FallingBlock> codec() { return CODEC; }
        @Override protected void falling(FallingBlockEntity entity) { entity.dropItem = this.dropsOnBreak; }
        @Override public int getDustColor(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) { return state.getMapColor(getter, pos).col; }
    }
    // ORE x FALLING BLOCK
    public static class CustomFallingOreBlock extends FallingBlock {
        private final UniformInt xpRange;
        public static final MapCodec<CustomFallingOreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec(), UniformInt.CODEC.fieldOf("xp_range").forGetter(b -> b.xpRange)).apply(instance, CustomFallingOreBlock::new));
        public CustomFallingOreBlock(Properties properties, UniformInt xpRange) { super(properties); this.xpRange = xpRange; }
        @Override protected @NotNull MapCodec<? extends FallingBlock> codec() { return CODEC; }
        @Override protected void falling(FallingBlockEntity entity) { entity.dropItem = false; }
        @Override public void spawnAfterBreak(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull ItemStack pTool, boolean pDropExperience) { super.spawnAfterBreak(pState, pLevel, pPos, pTool, pDropExperience); if (pDropExperience) { this.dropExperience(pLevel, pPos, pTool); } }
        public void dropExperience(ServerLevel pLevel, BlockPos pPos, ItemStack pTool) { Optional<Holder.Reference<Enchantment>> silkTouchHolder = pLevel.registryAccess().lookup(Registries.ENCHANTMENT).flatMap(lookup -> lookup.get(Enchantments.SILK_TOUCH)); int silkLevel = silkTouchHolder.map(pTool::getEnchantmentLevel).orElse(0); if (silkLevel == 0) { int i = this.xpRange.sample(pLevel.random); if (i > 0) { this.popExperience(pLevel, pPos, i); } } }
        @Override public int getDustColor(BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) { return state.getMapColor(getter, pos).col; }
    }
    // ORE x REDSTONE TYPE
    public static class CustomRedstoneOreBlock extends Block {
        public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
        private final UniformInt xpRange;
        private final int particleColor;
        public static final MapCodec<CustomRedstoneOreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec(), UniformInt.CODEC.fieldOf("xp_range").forGetter(b -> b.xpRange), Codec.INT.fieldOf("particle_color").forGetter(b -> b.particleColor)).apply(instance, CustomRedstoneOreBlock::new));
        public CustomRedstoneOreBlock(Properties properties, UniformInt xpRange, int particleColor) { super(properties); this.xpRange = xpRange; this.particleColor = particleColor; this.registerDefaultState(this.defaultBlockState().setValue(LIT, false)); }
        @Override protected @NotNull MapCodec<? extends Block> codec() { return CODEC; }
        @Override public void attack(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) { this.interact(state, level, pos); super.attack(state, level, pos, player); }
        @Override public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) { if (!entity.isSteppingCarefully()) { this.interact(state, level, pos); } super.stepOn(level, pos, state, entity); }
        @Override public @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) { if (level.isClientSide) { this.spawnParticles(level, pos); } else { this.interact(state, level, pos); } return (stack.getItem() instanceof BlockItem && (new BlockPlaceContext(player, hand, stack, hitResult)).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS); }
        private void interact(BlockState state, Level level, BlockPos pos) { this.spawnParticles(level, pos); if (!state.getValue(LIT)) { level.setBlock(pos, state.setValue(LIT, true), 3); } }
        @Override public boolean isRandomlyTicking(@NotNull BlockState state) { return state.getValue(LIT); }
        @Override public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) { if (state.getValue(LIT)) { level.setBlock(pos, state.setValue(LIT, false), 3); } }
        @Override public void spawnAfterBreak(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull ItemStack pTool, boolean pDropExperience) { super.spawnAfterBreak(pState, pLevel, pPos, pTool, pDropExperience); if (pDropExperience) { this.dropExperience(pLevel, pPos, pTool); } }
        public void dropExperience(ServerLevel pLevel, BlockPos pPos, ItemStack pTool) { Optional<Holder.Reference<Enchantment>> silkTouchHolder = pLevel.registryAccess().lookup(Registries.ENCHANTMENT).flatMap(lookup -> lookup.get(Enchantments.SILK_TOUCH)); int silkLevel = silkTouchHolder.map(pTool::getEnchantmentLevel).orElse(0); if (silkLevel == 0) { int i = this.xpRange.sample(pLevel.random); if (i > 0) { this.popExperience(pLevel, pPos, i); } } }
        @Override public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) { if (state.getValue(LIT)) { this.spawnParticles(level, pos); } }
        private void spawnParticles(Level level, BlockPos pos) { DustParticleOptions particleOptions = new DustParticleOptions(this.particleColor, 1.0F); for(Direction direction : Direction.values()) { BlockPos blockpos = pos.relative(direction); if (!level.getBlockState(blockpos).isSolidRender()) { Direction.Axis direction$axis = direction.getAxis(); double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getStepX() : (double)level.random.nextFloat(); double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double)direction.getStepY() : (double)level.random.nextFloat(); double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getStepZ() : (double)level.random.nextFloat(); level.addParticle(particleOptions, (double)pos.getX() + d1, (double)pos.getY() + d2, (double)pos.getZ() + d3, 0.0D, 0.0D, 0.0D); } } }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LIT); }
    }
    // ORE x REDST0NE TYPE x FALLING BLOCK
    public static class CustomFallingRedstoneOreBlock extends FallingBlock {
        public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
        private final UniformInt xpRange;
        private final int particleColor;
        public static final MapCodec<CustomFallingRedstoneOreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec(), UniformInt.CODEC.fieldOf("xp_range").forGetter(b -> b.xpRange), Codec.INT.fieldOf("particle_color").forGetter(b -> b.particleColor)).apply(instance, CustomFallingRedstoneOreBlock::new));
        public CustomFallingRedstoneOreBlock(Properties properties, UniformInt xpRange, int particleColor) { super(properties); this.xpRange = xpRange; this.particleColor = particleColor; this.registerDefaultState(this.defaultBlockState().setValue(LIT, false)); }
        @Override protected @NotNull MapCodec<? extends FallingBlock> codec() { return CODEC; }
        @Override public void attack(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) { this.interact(state, level, pos); super.attack(state, level, pos, player); }
        @Override public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) { if (!entity.isSteppingCarefully()) { this.interact(state, level, pos); } super.stepOn(level, pos, state, entity); }
        @Override public @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) { if (level.isClientSide) { this.spawnParticles(level, pos); } else { this.interact(state, level, pos); } return (stack.getItem() instanceof BlockItem && (new BlockPlaceContext(player, hand, stack, hitResult)).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS); }
        private void interact(BlockState state, Level level, BlockPos pos) { this.spawnParticles(level, pos); if (!state.getValue(LIT)) { level.setBlock(pos, state.setValue(LIT, true), 3); } }
        @Override public boolean isRandomlyTicking(@NotNull BlockState state) { return state.getValue(LIT); }
        @Override public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) { if (state.getValue(LIT)) { level.setBlock(pos, state.setValue(LIT, false), 3); } }
        @Override
        protected void falling(FallingBlockEntity entity) {
            entity.dropItem = false;
        }
        @Override public void spawnAfterBreak(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull ItemStack pTool, boolean pDropExperience) { super.spawnAfterBreak(pState, pLevel, pPos, pTool, pDropExperience); if (pDropExperience) { this.dropExperience(pLevel, pPos, pTool); } }
        public void dropExperience(ServerLevel pLevel, BlockPos pPos, ItemStack pTool) { Optional<Holder.Reference<Enchantment>> silkTouchHolder = pLevel.registryAccess().lookup(Registries.ENCHANTMENT).flatMap(lookup -> lookup.get(Enchantments.SILK_TOUCH)); int silkLevel = silkTouchHolder.map(pTool::getEnchantmentLevel).orElse(0); if (silkLevel == 0) { int i = this.xpRange.sample(pLevel.random); if (i > 0) { this.popExperience(pLevel, pPos, i); } } }
        @Override public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) { if (state.getValue(LIT)) { this.spawnParticles(level, pos); } }
        private void spawnParticles(Level level, BlockPos pos) { DustParticleOptions particleOptions = new DustParticleOptions(this.particleColor, 1.0F); for(Direction direction : Direction.values()) { BlockPos blockpos = pos.relative(direction); if (!level.getBlockState(blockpos).isSolidRender()) { Direction.Axis direction$axis = direction.getAxis(); double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getStepX() : (double)level.random.nextFloat(); double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double)direction.getStepY() : (double)level.random.nextFloat(); double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getStepZ() : (double)level.random.nextFloat(); level.addParticle(particleOptions, (double)pos.getX() + d1, (double)pos.getY() + d2, (double)pos.getZ() + d3, 0.0D, 0.0D, 0.0D); } } }
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LIT); }
        @Override public int getDustColor(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) { return this.particleColor; }
    }

}
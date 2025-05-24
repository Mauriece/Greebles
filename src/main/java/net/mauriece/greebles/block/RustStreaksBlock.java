package net.mauriece.greebles.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RustStreaksBlock extends Block {
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");

    protected static final VoxelShape SHAPE = Block.box(0, 0, 15, 16, 16, 16);

    public RustStreaksBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(0.2F)
        );
        this.registerDefaultState(this.defaultBlockState()
                .setValue(CONNECTED_UP, false)
                .setValue(CONNECTED_DOWN, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState above = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
        BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
        return this.defaultBlockState()
                .setValue(CONNECTED_UP, above.getBlock() == this)
                .setValue(CONNECTED_DOWN, below.getBlock() == this);
    }

    @Override
    public BlockState updateShape(BlockState state, net.minecraft.core.Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (dir == net.minecraft.core.Direction.UP) {
            return state.setValue(CONNECTED_UP, neighborState.getBlock() == this);
        }
        if (dir == net.minecraft.core.Direction.DOWN) {
            return state.setValue(CONNECTED_DOWN, neighborState.getBlock() == this);
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_UP, CONNECTED_DOWN);
    }
}
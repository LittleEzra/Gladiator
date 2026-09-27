package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public class ServerInputData {
    public static final Supplier<AttachmentType<ServerInputData>> TYPE = GladiusDataAttachments.SERVER_INPUT;

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public float forwardImpulse;
    public float leftImpulse;

    private static float calculateImpulse(boolean input, boolean otherInput) {
        if (input == otherInput) {
            return 0.0F;
        } else {
            return input ? 1.0F : -1.0F;
        }
    }

    public void set(boolean up, boolean down, boolean left, boolean right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.forwardImpulse = calculateImpulse(up, down);
        this.leftImpulse = calculateImpulse(left, right);
    }

    public boolean getUp() {
        return up;
    }

    public boolean getDown() {
        return down;
    }

    public boolean getLeft() {
        return left;
    }

    public boolean getRight() {
        return right;
    }

    public boolean getForDirection(float yRot, Direction direction){
        Direction lookDirection = Direction.fromYRot(yRot);
        if (lookDirection == direction){
            return up;
        } else if (lookDirection.getOpposite() == direction){
            return down;
        } else if (lookDirection.getClockWise() == direction){
            return right;
        } else if (lookDirection.getCounterClockWise() == direction){
            return left;
        }
        return false;
    }
}

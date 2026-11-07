package dev.powercyphe.stealthcraft.common.util;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public record Quad(Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4) implements Iterable<Vec3>  {

    @Override
    public @NotNull Iterator<Vec3> iterator() {
        return List.of(this.p1(), this.p2(), this.p3(), this.p4()).iterator();
    }
}

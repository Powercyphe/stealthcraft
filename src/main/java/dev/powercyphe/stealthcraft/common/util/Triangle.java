package dev.powercyphe.stealthcraft.common.util;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public record Triangle(Vec3 p1, Vec3 p2, Vec3 p3) implements Iterable<Vec3> {

    public Vec3 centerPos() {
        Vec3 w = this.p3.subtract(this.p2);
        Vec3 h = this.p2.subtract(this.p1);
        return this.p1.add(w).add(h);
    }

    public @NotNull List<Vec3> withCenter() {
        return List.of(this.p1, this.p2, this.p3, this.centerPos());
    }

    @Override
    public @NotNull Iterator<Vec3> iterator() {
        return List.of(this.p1(), this.p2(), this.p3()).iterator();
    }
}

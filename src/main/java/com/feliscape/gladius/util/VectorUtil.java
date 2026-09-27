package com.feliscape.gladius.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Quaterniondc;

public class VectorUtil {
    public static Vec3 rotateWithQuaternion(Quaterniondc quaternion, Vec3 vector){
        return rotateWithQuaternion(quaternion, vector.x, vector.y, vector.z);
    }

    public static Vec3 rotateWithQuaternion(Quaterniondc quaternion, double x, double y, double z){
        double xx = quaternion.x() * quaternion.x(), yy = quaternion.y() * quaternion.y(), zz = quaternion.z() * quaternion.z(), ww = quaternion.w() * quaternion.w();
        double xy = quaternion.x() * quaternion.y(), xz = quaternion.x() * quaternion.z(), yz = quaternion.y() * quaternion.z(), xw = quaternion.x() * quaternion.w();
        double zw = quaternion.z() * quaternion.w(), yw = quaternion.y() * quaternion.w(), k = 1 / (xx + yy + zz + ww);
        return new Vec3(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2 * (xy - zw) * k, y, (2 * (xz + yw) * k) * z)),
                Math.fma(2 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, (2 * (yz - xw) * k) * z)),
                Math.fma(2 * (xz - yw) * k, x, Math.fma(2 * (yz + xw) * k, y, ((zz - xx - yy + ww) * k) * z)));
    }

    public static Vec3 arbitraryPerpendicular(Vec3 v){
        boolean a = abs(v.x) <  abs(v.y) && abs(v.x) <  abs(v.z);
        boolean b = abs(v.y) <= abs(v.x) && abs(v.y) <  abs(v.z);
        boolean c = abs(v.z) <= abs(v.x) && abs(v.z) <= abs(v.y);
        return v.cross(new Vec3(a ? 1 : 0, b ? 1 : 0, c ? 1 : 0));
    }

    private static double abs(double d){
        return Math.abs(d);
    }
}

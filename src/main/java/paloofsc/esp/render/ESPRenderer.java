package paloofsc.esp.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import paloofsc.esp.ESPModClient;
import paloofsc.esp.config.ESPConfig;
import java.awt.Color;

public class ESPRenderer {

    public void render(MatrixStack matrices, Camera camera, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        ESPConfig cfg = ESPModClient.config;
        Vec3d cam = camera.getPos();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player || !(entity instanceof PlayerEntity player)) continue;
            if (player.isDead() || player.getHealth() <= 0f) continue;
            if (entity.squaredDistanceTo(client.player) > cfg.espDistance * cfg.espDistance) continue;

            double x = entity.prevX + (entity.getX() - entity.prevX) * tickDelta - cam.x;
            double y = entity.prevY + (entity.getY() - entity.prevY) * tickDelta - cam.y;
            double z = entity.prevZ + (entity.getZ() - entity.prevZ) * tickDelta - cam.z;

            Color color = getColor(player, cfg);

            if (cfg.boxESP) drawBox(matrices, x, y, z, entity.getWidth(), entity.getHeight(), color, cfg.boxWidth);
            if (cfg.tracerESP) drawTracer(matrices, x, y, z, color, cfg.tracerAlpha);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private Color getColor(PlayerEntity p, ESPConfig cfg) {
        if (p.isInvisible()) return Color.decode(cfg.colorInvis);
        if (p.isSneaking()) return Color.decode(cfg.colorSneak);
        return Color.decode(cfg.colorEnemy);
    }

    private void drawBox(MatrixStack m, double x, double y, double z, float w, float h, Color c, int lw) {
        double hw = w / 2;
        float r = c.getRed() / 255f, g = c.getGreen() / 255f, b = c.getBlue() / 255f;
        Matrix4f mat = m.peek().getPositionMatrix();
        BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(lw);
        line(buf, mat, x - hw, y, z - hw, x - hw, y + h, z - hw, r, g, b);
        line(buf, mat, x + hw, y, z - hw, x + hw, y + h, z - hw, r, g, b);
        line(buf, mat, x - hw, y, z + hw, x - hw, y + h, z + hw, r, g, b);
        line(buf, mat, x + hw, y, z + hw, x + hw, y + h, z + hw, r, g, b);
        BufferRenderer.drawWithGlobalProgram(buf.end());
    }

    private void drawTracer(MatrixStack m, double x, double y, double z, Color c, int alpha) {
        float r = c.getRed() / 255f, g = c.getGreen() / 255f, b = c.getBlue() / 255f, a = alpha / 255f;
        Matrix4f mat = m.peek().getPositionMatrix();
        BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        RenderSystem.lineWidth(1f);
        buf.vertex(mat, 0, 0, 0).color(r, g, b, a);
        buf.vertex(mat, (float) x, (float) (y + 1), (float) z).color(r, g, b, a);
        BufferRenderer.drawWithGlobalProgram(buf.end());
    }

    private void line(BufferBuilder buf, Matrix4f mat, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b) {
        buf.vertex(mat, (float) x1, (float) y1, (float) z1).color(r, g, b, 0.9f);
        buf.vertex(mat, (float) x2, (float) y2, (float) z2).color(r, g, b, 0.9f);
    }
}

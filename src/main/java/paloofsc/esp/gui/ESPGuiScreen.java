package paloofsc.esp.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import paloofsc.esp.ESPModClient;
import paloofsc.esp.config.ESPConfig;

public class ESPGuiScreen extends Screen {

    private int category = 0;
    private ESPConfig cfg = ESPModClient.config;

    public ESPGuiScreen() { super(Text.literal("Paloofsc ESP")); }

    @Override
    protected void init() {
        int cx = width / 2;

        addDrawableChild(ButtonWidget.builder(Text.literal("Основные"), b -> { category = 0; clearAndInit(); }).dimensions(10, 40, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Визуал"), b -> { category = 1; clearAndInit(); }).dimensions(10, 65, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Цвета"), b -> { category = 2; clearAndInit(); }).dimensions(10, 90, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Файл"), b -> { category = 3; clearAndInit(); }).dimensions(10, 115, 70, 20).build());

        int lx = cx - 120, rx = cx + 10;

        switch (category) {
            case 0 -> {
                addDrawableChild(ButtonWidget.builder(Text.literal("ESP: " + s(cfg.espEnabled)), b -> { cfg.espEnabled = !cfg.espEnabled; clearAndInit(); }).dimensions(lx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Трейсеры: " + s(cfg.tracerESP)), b -> { cfg.tracerESP = !cfg.tracerESP; clearAndInit(); }).dimensions(rx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Боксы: " + s(cfg.boxESP)), b -> { cfg.boxESP = !cfg.boxESP; clearAndInit(); }).dimensions(lx, 65, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Скелет: " + s(cfg.skeletonESP)), b -> { cfg.skeletonESP = !cfg.skeletonESP; clearAndInit(); }).dimensions(rx, 65, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("HP: " + s(cfg.healthESP)), b -> { cfg.healthESP = !cfg.healthESP; clearAndInit(); }).dimensions(lx, 90, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Дист: " + s(cfg.distanceESP)), b -> { cfg.distanceESP = !cfg.distanceESP; clearAndInit(); }).dimensions(rx, 90, 110, 20).build());
            }
            case 1 -> {
                addDrawableChild(ButtonWidget.builder(Text.literal("Скрытые: " + s(cfg.showSneak)), b -> { cfg.showSneak = !cfg.showSneak; clearAndInit(); }).dimensions(lx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Невидимки: " + s(cfg.showInvis)), b -> { cfg.showInvis = !cfg.showInvis; clearAndInit(); }).dimensions(rx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Дальность: " + cfg.espDistance), b -> { cfg.espDistance += 32; if (cfg.espDistance > 512) cfg.espDistance = 32; clearAndInit(); }).dimensions(lx, 65, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Толщина: " + cfg.boxWidth), b -> { cfg.boxWidth++; if (cfg.boxWidth > 10) cfg.boxWidth = 1; clearAndInit(); }).dimensions(rx, 65, 110, 20).build());
            }
            case 2 -> {
                addDrawableChild(ButtonWidget.builder(Text.literal("Враг: " + cfg.colorEnemy), b -> { cfg.colorEnemy = rnd(); clearAndInit(); }).dimensions(lx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Союзник: " + cfg.colorFriend), b -> { cfg.colorFriend = rnd(); clearAndInit(); }).dimensions(rx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Скрытый: " + cfg.colorSneak), b -> { cfg.colorSneak = rnd(); clearAndInit(); }).dimensions(lx, 65, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("Невидимка: " + cfg.colorInvis), b -> { cfg.colorInvis = rnd(); clearAndInit(); }).dimensions(rx, 65, 110, 20).build());
            }
            case 3 -> {
                addDrawableChild(ButtonWidget.builder(Text.literal("§aСохранить"), b -> { cfg.save(); if (client != null && client.player != null) client.player.sendMessage(Text.literal("§a[ESP] Сохранено!"), true); }).dimensions(lx, 40, 110, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("§eЗагрузить"), b -> { ESPModClient.config = ESPConfig.load(); cfg = ESPModClient.config; clearAndInit(); }).dimensions(rx, 40, 110, 20).build());
            }
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("§cЗакрыть"), b -> close()).dimensions(cx - 50, height - 30, 100, 20).build());
    }

    private void clearAndInit() { clearChildren(); init(); }
    private String s(boolean b) { return b ? "§aON" : "§cOFF"; }
    private String rnd() { return String.format("#%06x", (int)(Math.random() * 0xFFFFFF)); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, "§dPALOOFSC ESP", width / 2, 10, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, "§5THE PESS OVERLAY v1.0 | 1.21.11", width / 2, 22, 0xAA00AA);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}

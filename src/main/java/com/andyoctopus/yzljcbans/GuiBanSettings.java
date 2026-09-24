package com.andyoctopus.yzljcbans;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/** Lists every loaded template and previews the text used on the disconnect screen. */
final class GuiBanSettings extends GuiScreen {
    private static final int ROW_HEIGHT = 17;
    private final GuiScreen parent;
    private final String previewDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    private List<String> keys = Collections.emptyList();
    private String selectedKey;
    private int listOffset;
    private int previewOffset;

    GuiBanSettings(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, 10, height - 28, 110, 20, "New Template"));
        buttonList.add(new GuiButton(1, width - 80, height - 28, 70, 20, "Done"));
        keys = BanTemplates.getKeys();
        if (selectedKey == null || !keys.contains(selectedKey)) {
            selectedKey = keys.isEmpty() ? null : keys.get(0);
        }
        keepSelectionVisible();
    }

    void selectTemplate(String key) {
        keys = BanTemplates.getKeys();
        selectedKey = key;
        previewOffset = 0;
        keepSelectionVisible();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiBanTemplateEditor(this));
        } else if (button.id == 1) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0 && mouseX >= 10 && mouseX < listRight()
                && mouseY >= 40 && mouseY < height - 38) {
            int index = listOffset + (mouseY - 40) / ROW_HEIGHT;
            if (index >= 0 && index < keys.size()) {
                selectedKey = keys.get(index);
                previewOffset = 0;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel == 0) {
            return;
        }
        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int step = wheel > 0 ? -1 : 1;
        if (mouseX < listRight()) {
            listOffset = clamp(listOffset + step, 0, Math.max(0, keys.size() - visibleRows()));
        } else {
            previewOffset = Math.max(0, previewOffset + step * 2);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "YzljcBans Templates", width / 2, 12, 0xFFFFFF);
        drawString(fontRendererObj, "Loaded templates (" + keys.size() + ")", 10, 28, 0xAAAAAA);

        int right = listRight();
        drawRect(8, 38, right, height - 36, 0x90000000);
        int end = Math.min(keys.size(), listOffset + visibleRows());
        for (int i = listOffset; i < end; i++) {
            int y = 40 + (i - listOffset) * ROW_HEIGHT;
            if (keys.get(i).equals(selectedKey)) {
                drawRect(10, y, right - 2, y + ROW_HEIGHT - 1, 0x905080B0);
            }
            String label = keys.get(i) + (BanTemplates.isCustom(keys.get(i)) ? " *" : "");
            drawString(fontRendererObj, fontRendererObj.trimStringToWidth(label, right - 22), 13, y + 4, 0xFFFFFF);
        }

        int previewLeft = right + 10;
        int previewRight = width - 8;
        drawRect(previewLeft, 38, previewRight, height - 36, 0xB0000000);
        if (selectedKey != null) {
            BanTemplate template = BanTemplates.get(selectedKey);
            if (template != null) {
                drawCenteredString(fontRendererObj, "Connection Lost", (previewLeft + previewRight) / 2, 45, 0xFFFFFF);
                drawString(fontRendererObj, fontRendererObj.trimStringToWidth(template.getName(), previewRight - previewLeft - 12),
                        previewLeft + 6, 60, 0xAAAAAA);
                List<String> preview = wrappedPreview(template, Math.max(20, previewRight - previewLeft - 14));
                int maxRows = Math.max(1, (height - 122) / 10);
                previewOffset = clamp(previewOffset, 0, Math.max(0, preview.size() - maxRows));
                int y = 79;
                for (int i = previewOffset; i < Math.min(preview.size(), previewOffset + maxRows); i++) {
                    String line = preview.get(i);
                    drawCenteredString(fontRendererObj, line, (previewLeft + previewRight) / 2, y, 0xFFFFFF);
                    y += 10;
                }
                if (BanTemplates.getLoadError() != null) {
                    drawString(fontRendererObj, "Custom config could not load", previewLeft + 6, height - 49, 0xFF6666);
                } else if (preview.size() > maxRows) {
                    drawString(fontRendererObj, "Scroll for more", previewLeft + 6, height - 49, 0x888888);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private List<String> wrappedPreview(BanTemplate template, int maxWidth) {
        List<String> wrapped = new ArrayList<>();
        for (String line : template.render("29d 23h 59m 59s", "ExamplePlayer", "ABC12345", "123456", previewDate)) {
            if (line.replaceAll("§[0-9a-fk-or]", "").isEmpty()) {
                wrapped.add("");
            } else {
                wrapped.addAll(fontRendererObj.listFormattedStringToWidth(line, maxWidth));
            }
        }
        return wrapped;
    }

    private int listRight() {
        return Math.min(160, Math.max(110, width / 3));
    }

    private int visibleRows() {
        return Math.max(1, (height - 78) / ROW_HEIGHT);
    }

    private void keepSelectionVisible() {
        int index = keys.indexOf(selectedKey);
        if (index < listOffset) {
            listOffset = index;
        } else if (index >= listOffset + visibleRows()) {
            listOffset = index - visibleRows() + 1;
        }
        listOffset = clamp(listOffset, 0, Math.max(0, keys.size() - visibleRows()));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}

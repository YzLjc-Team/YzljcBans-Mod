package com.andyoctopus.yzljcbans;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Creates a persistent custom ban template from an in-game form. */
final class GuiBanTemplateEditor extends GuiScreen {
    private static final int ROW_HEIGHT = 13;
    private final GuiBanSettings parent;
    private final List<String> lines = new ArrayList<>();
    private GuiTextField keyField;
    private GuiTextField nameField;
    private GuiTextField lineField;
    private int selectedLine;
    private int lineOffset;
    private String draftKey = "";
    private String draftName = "";
    private String error = "";

    GuiBanTemplateEditor(GuiBanSettings parent) {
        this.parent = parent;
        lines.add("");
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        int fieldWidth = Math.max(100, width - 98);
        keyField = new GuiTextField(0, fontRendererObj, 86, 24, fieldWidth, 18);
        nameField = new GuiTextField(1, fontRendererObj, 86, 47, fieldWidth, 18);
        lineField = new GuiTextField(2, fontRendererObj, 12, height - 98, width - 24, 18);
        keyField.setMaxStringLength(32);
        nameField.setMaxStringLength(40);
        lineField.setMaxStringLength(256);
        keyField.setText(draftKey);
        nameField.setText(draftName);
        lineField.setText(lines.get(selectedLine));
        keyField.setFocused(true);

        int controlWidth = (width - 30) / 4;
        buttonList.add(new GuiButton(0, 12, height - 73, controlWidth, 20, "+ Line"));
        buttonList.add(new GuiButton(1, 14 + controlWidth, height - 73, controlWidth, 20, "Delete"));
        buttonList.add(new GuiButton(2, 16 + controlWidth * 2, height - 73, controlWidth, 20, "Up"));
        buttonList.add(new GuiButton(3, 18 + controlWidth * 3, height - 73, controlWidth, 20, "Down"));
        buttonList.add(new GuiButton(4, width / 2 - 105, height - 48, 100, 20, "Save Template"));
        buttonList.add(new GuiButton(5, width / 2 + 5, height - 48, 100, 20, "Cancel"));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        keyField.updateCursorCounter();
        nameField.updateCursorCounter();
        lineField.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                if (lines.size() >= 30) {
                    error = "Maximum 30 lines.";
                    return;
                }
                lines.add(selectedLine + 1, "");
                selectLine(selectedLine + 1);
                lineField.setFocused(true);
                break;
            case 1:
                lines.remove(selectedLine);
                if (lines.isEmpty()) {
                    lines.add("");
                }
                selectLine(Math.min(selectedLine, lines.size() - 1));
                break;
            case 2:
                if (selectedLine > 0) {
                    swapLines(selectedLine, selectedLine - 1);
                    selectLine(selectedLine - 1);
                }
                break;
            case 3:
                if (selectedLine + 1 < lines.size()) {
                    swapLines(selectedLine, selectedLine + 1);
                    selectLine(selectedLine + 1);
                }
                break;
            case 4:
                save();
                break;
            case 5:
                mc.displayGuiScreen(parent);
                break;
            default:
                break;
        }
    }

    private void save() {
        try {
            BanTemplates.addCustom(keyField.getText(), nameField.getText(), lines);
            String key = keyField.getText().trim().toLowerCase(java.util.Locale.ROOT);
            parent.selectTemplate(key);
            mc.displayGuiScreen(parent);
        } catch (IllegalArgumentException | IOException exception) {
            error = exception.getMessage();
        }
    }

    private void swapLines(int first, int second) {
        String line = lines.get(first);
        lines.set(first, lines.get(second));
        lines.set(second, line);
    }

    private void selectLine(int index) {
        selectedLine = index;
        lineField.setText(lines.get(index));
        if (index < lineOffset) {
            lineOffset = index;
        } else if (index >= lineOffset + visibleRows()) {
            lineOffset = index - visibleRows() + 1;
        }
        lineOffset = Math.max(0, Math.min(lineOffset, lines.size() - visibleRows()));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
            return;
        }
        keyField.textboxKeyTyped(typedChar, keyCode);
        nameField.textboxKeyTyped(typedChar, keyCode);
        draftKey = keyField.getText();
        draftName = nameField.getText();
        if (lineField.textboxKeyTyped(typedChar, keyCode)) {
            lines.set(selectedLine, lineField.getText());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        keyField.mouseClicked(mouseX, mouseY, mouseButton);
        nameField.mouseClicked(mouseX, mouseY, mouseButton);
        lineField.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= 12 && mouseX < width - 12
                && mouseY >= 84 && mouseY < listBottom()) {
            int index = lineOffset + (mouseY - 84) / ROW_HEIGHT;
            if (index < lines.size()) {
                selectLine(index);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            lineOffset = Math.max(0, Math.min(lines.size() - visibleRows(), lineOffset + (wheel > 0 ? -1 : 1)));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "New Ban Template", width / 2, 9, 0xFFFFFF);
        drawString(fontRendererObj, "Key:", 12, 29, 0xCCCCCC);
        drawString(fontRendererObj, "Name:", 12, 52, 0xCCCCCC);
        keyField.drawTextBox();
        nameField.drawTextBox();
        drawString(fontRendererObj, "Lines (" + lines.size() + "): & colors | %DURATION% %REASON% %banid%", 12, 71, 0xAAAAAA);

        drawRect(10, 82, width - 10, listBottom(), 0x90000000);
        int end = Math.min(lines.size(), lineOffset + visibleRows());
        for (int i = lineOffset; i < end; i++) {
            int y = 84 + (i - lineOffset) * ROW_HEIGHT;
            if (i == selectedLine) {
                drawRect(12, y - 1, width - 12, y + ROW_HEIGHT - 1, 0x905080B0);
            }
            String label = (i + 1) + ": " + lines.get(i);
            drawString(fontRendererObj, fontRendererObj.trimStringToWidth(label, width - 30), 15, y + 1, 0xFFFFFF);
        }
        drawString(fontRendererObj, "Selected line:", 12, height - 109, 0xCCCCCC);
        lineField.drawTextBox();
        if (!error.isEmpty()) {
            drawCenteredString(fontRendererObj, fontRendererObj.trimStringToWidth(error, width - 20), width / 2, height - 22, 0xFF6666);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private int listBottom() {
        return height - 115;
    }

    private int visibleRows() {
        return Math.max(1, (listBottom() - 84) / ROW_HEIGHT);
    }
}

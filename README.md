# YzljcBans 客户端模组

YzljcBans 是一个针对 Minecraft 1.8.9 Forge 客户端开发的模组，用于在本地模拟 Hypixel 等服务器的封禁流程和界面。通过客户端指令 `/gb`（别名 `/getban`），你可以在指定延时后自动执行 `/limbo`、断开连接，并显示自定义的封禁界面。

## 功能特性
- 客户端侧实现，无需服务器支持。
- 支持多种封禁模板，覆盖 Hypixel 原版提示以及若干第三方服务器格式。
- 可自定义封禁时长、延迟进入 Limbo 的时间，以及附加理由字段。
- 自动生成 8 位 Ban ID（字符集 A-F 与 0-9）并填充到界面中。
- 模拟真实流程：发送 `/limbo` → 等待 3 秒 → 断线 → 展示封禁界面。

## 指令说明

```
/gb <模板> <时长/永久> <进入limbo延迟> [附加理由...]
```

- `<模板>`：使用 `Tab` 可查看所有可用模板键，例如 `cheating`、`chatban`、`security` 等。
- `<时长/永久>`：支持 `30d12h5m30s` 这类复合写法；若模板不需要 `%DURATION%` 会原样显示。输入 `permanent`/`perm`/`forever` 表示永久封禁。
- `<进入limbo延迟>`：格式同上，表示从执行指令到发送 `/limbo` 的等待时间。
- `[附加理由...]`：可选参数，若模板包含 `%REASON%` 会使用提供的文本；未提供时默认填入 Ban ID。

### 示例

```
/gb cheating 30d 10s
```

含义：选择 `cheating` 模板，封禁时长 30 天（最终显示为 `29d 23h 59m 59s`），在 10 秒后发送 `/limbo`，再过 3 秒自动断线并显示封禁页面。

## 模板占位符

封禁模板会自动替换以下占位符：

- `%DURATION%`：根据输入的封禁时长格式化（例：`29d 23h 59m 59s`）。永久时显示 `permanent`。
- `%REASON%`：指令中附加的理由文本；未提供时回退到 Ban ID。
- `%banid%`：随机生成的 8 位 Ban ID。
- `%ID%` / `%DATE%`：部分模板使用的数值型 ID 和当前日期。

## 构建与测试

工程依赖 JDK 8。首次构建前请确保本地已安装合适的 Java 运行时。

```
./gradlew build
```

若缺少 JRE/JDK，Gradle 将提示 “Unable to locate a Java Runtime”。安装后即可正常编译。

## 注意事项

- 模组只在客户端生效，不会影响服务器端数据。
- 部分模板包含其他语言文本（中/日文等），显示时需使用支持对应字符集的字体。
- 若要扩展模板，可编辑 `src/main/java/com/andyoctopus/yzljcbans/BanTemplates.java`，按照现有格式追加新的 `register` 调用即可。
- 非 **Hypixel** 服务器的封禁界面有可能不准确！

祝你使用愉快！如需更多封禁样式，可继续补充模板或自定义界面逻辑。

### 作者:
- AndyOctopus
- YZ_Ljc_
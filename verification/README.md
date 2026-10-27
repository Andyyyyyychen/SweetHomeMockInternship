# Additional acceptance checks

Run `mvn clean test` with Java 8 first, then `./verification/run-checks.sh` with `JAVA_HOME` set. The second command needs a graphical desktop because it uses the actual Swing view. It regenerates `submissions/task4-all-levels.pdf` using the application's PDF printer.

- `PrintStateCheck`: normal printing, hidden-level page bounds, selected-level restoration, observer-camera elevation restoration, and restoration after a rendering exception.
- `AcceptanceCheck`: description save, undo, redo and file round trip; catalog volume; ascending/descending sorting and resizing; four-page sample PDF; restoration of screen columns, filter, selected level and camera.
- The assignment's tests remain unchanged.

The task 1 image is the running application. Tasks 2 and 3 show real Swing application views initialized by `AcceptanceCheck`; description changes and sorting are exercised through the application's controllers/model. The Volume menu preview uses Java's cross-platform look and feel, lightweight menus and adjusted split panes for readable macOS window captures. Production popup configuration is unchanged. Task 4 is actual application PDF output, visually checked as one table followed by three level plans.

To inspect the preview, compile the two Java files as in `run-checks.sh` and launch `AcceptanceCheck description` or `AcceptanceCheck volume` with the same classpath.

## 实现说明

- 描述编辑：让 `HomeFurnitureController` 报告 DESCRIPTION 可编辑；沿用已有保存和撤销逻辑。
- Volume：按当前宽×深×高计算，加入属性枚举、比较器、表格渲染和菜单；修改尺寸后会重新计算和排序。
- 打印：家具表临时显示所有可见楼层并补充 Level 列；平面图依照楼层顺序逐页输出。`finally` 恢复列、过滤器、选中楼层和相机高度，避免打印影响编辑状态。

数值显示按作业要求沿用现有长度格式。四个验收文件位于 `submissions/`。

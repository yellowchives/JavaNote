# 快捷键

## 1、软件通用的快捷键

* 保存：Ctrl + S
* 剪切：Ctrl + X
* 粘贴：Ctrl + V
* 复制：Ctrl + C
* 全选：Ctlr + A
* 撤销：Ctrl + Z
* 最近使用的文件：Ctrl + E

## 2、IDEA常用快捷键

### （1）提高代码编写速度

* 智能提示：Alt + Enter
* 自动代码
  * 自动补全函数括号、分号、当前行缩进：Ctrl + Shift + 回车
  * 提示代码模板：Ctrl + J
  * 使用xx块环绕：Ctrl + Alt + T
  * 自动生成构造器、get/set：Alt + Insert  （有的键盘需要同时按Fn键）
  * 由方法自动生成返回值变量：Ctrl + Alt + V
* 格式化代码：Ctrl + Alt + L
* 插入空行开始编辑
  * 在当前光标下一行插入空行开始编辑：Shift + 回车
  * 在当前光标上一行插入空行开始编辑：Ctrl+Alt+回车
* 移动代码
  * 向上移动代码：Ctrl+Shift + ↑  （整个方法移动）或  Alt + Shift + ↑（逐行移动）
  * 向下移动代码：Ctrl+Shift + ↓ （整个方法移动） 或  Alt + Shift + ↓（逐行移动）
* 复制行：Ctrl+D
* 删除行
  * Ctrl + Y
  * 不选中删除光标所在行：Ctrl + X
* 重命名类、变量、方法等：选择要重名的元素，按Shift + F6
* 抽取代码重构方法：Ctrl + Alt + M
* 查看所调用方法的形参列表提醒：Ctrl + P
* 重写或实现方法
  * 实现接口的方法：Ctrl + i
  * 重写父类的方法：Ctrl + O
* 注释
  * 单行注释：Ctrl + /
  * 多行注释：Ctrl + Shift + /

* 转大小写：Ctrl +Shift + U

### （2）移动光标定位

* 移动定位
  * 跳转到上一个/下一个位置编辑位置：Ctrl + Alt + 左/右键
  * 跳转到上一个/下一个方法位置： Alt + 上/下键
  * 打开的类文件之间切换： Alt + 左/右键
  * 定位某行：Ctrl + G
* 选择文本：Ctrl + W
  * 按一下选择单词，再按选择语句，再按选择行，再按选择方法

### （3）查找与查看源码

* 按住Ctrl键，单击某个类或方法就可以打开某个类或方法的源码
  * 但是当某个变量是通过多态引用调用父类或父接口的方法时，想要打开方法的实现类：Ctrl +Alt +B
* 查看方法文档：选中方法名后按Ctrl + Q
* 查看某个类的结构信息
  * 类的继承层次：Ctrl + H
  * 类的UML关系图：Ctrl + Alt + U
  * 当前文件结构：Ctrl + F12键
* 搜索
  * 搜索类：Ctrl + N
  * 全项目搜索文本：Ctrl + Shift + F
  * 全项目替换文本：Ctrl +Shift + R
  * 当前源文件查找文本：Ctrl + F
  * 当前源文件替换问题：Ctrl + R
  * 任何地方搜索：双击Shift

* 折叠与展开
  * 折叠方法实现：Ctrl + Shift +  -
  * 展开方法实现：Ctrl +Shift + +

# 代码模板

## 1、常用快捷模板

* 主方法
  * psvm：public static void main(String[] args){}
  * main：public static void main(String[] args){}
* 输出语句
  * sout：System.out.println();
  * soutp：System.out.println("xxx :" + 方法形参);
  * soutm：System.out.println("xx类.当前方法名");
  * soutv：System.out.println("变量名 = " + 变量);
  * 变量.sout：System.out.println("变量名 = " + 变量);
* 非空判断
  * ifn：if(xx  == null)
  * inn：if(xx  != null)
  * 变量.null：if(变量 == null)
  * 变量.nn：if(变量 != null)
* 遍历
  * fori：for循环
  * itar：遍历数组
  * iter：foreach循环
* 静态常量声明
  * psf：public static final
  * prsf：private static final
* 常量声明：
  * psfi：public static final int
  * psfs：public static final String
* List集合遍历
  * list集合.for：foreach循环
  * list集合.fori：用for循环遍历list集合，正向遍历
  * list集合.forr：用for循环遍历list集合，逆向遍历

# 插件

- maven helper
- mybatisX
- sequenceDiagram
- Transalation
- Rainbow Brackets
- ignore
- GsonFormat
- auto fill java call arguments


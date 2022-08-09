![](Java核心技术.assets/Snipaste_2022-08-03_11-23-43.png)

![Snipaste_2022-08-03_11-24-22](Java核心技术.assets/Snipaste_2022-08-03_11-24-22.png)

如果getter返回的是一个实例对象，虽然这个实例对象是private的，但是仍然可以在对象外部对其进行修改。所以最好返回一个clone()对象



![](Java核心技术.assets/Snipaste_2022-08-03_11-25-32.png)

**类的方法明确接收的参数是显式参数，在方法里除了直接使用显式参数，还可以使用所在实例的成员变量，这些变量构成方法的隐式参数（常通过this来访问）。**如果显式参数也是一个同类型的实例，那么在方法里可以直接访问入参对象的私有成员变量。

static 方法只能访问静态的成员，不能访问非静态的成员。可以认为静态方法没有this参数。this 不能引用类的静态成员，因为 this 代表的是类的一个实例，而静态成员不属于类的任何实例。



![](Java核心技术.assets/Snipaste_2022-08-03_11-47-04.png)



从编译器的角度看，嵌套的包之间毫无关系。例如，java.util 包和 java.util.jar包没有任何关系。每一个包都是独立的类的集合。



![](Java核心技术.assets/Snipaste_2022-08-03_15-05-14.png)



类的私有成员只能在本类内部访问，类的变量不能访问。子类会继承父类的私有成员，但是子类也不能直接访问父类的私有成员，而要使用super关键字调用父类的getter来访问，或者在子类的构造器中使用super调用父类的构造器来初始化从父类继承的私有成员变量。
```java
public class FirstName {
    private String firstName;

    public FirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

public class FullName extends FirstName{

    private String lastName;
    private String fullName;

    public FullName(String firstName, String lastName) {
        super(firstName);
        this.lastName = lastName;
        //这里使用super.getFirstName()或者this.getFirstName()都行，因为子类没有重写这个方法
        //这里不能使用this.firstName。因为私有成员只能在本类内部访问，如果是protected成员就能在子类之中访问
        this.fullName = super.getFirstName() + "." + this.lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String lastName) {
        super.setFirstName(firstName);
        this.lastName = lastName;
        this.fullName = super.getFirstName() + "." + this.lastName;
    }
}
```

![](Java核心技术.assets/Snipaste_2022-08-03_16-57-23.png)



![](Java核心技术.assets/Snipaste_2022-08-03_21-03-48.png)



![](Java核心技术.assets/Snipaste_2022-08-03_21-06-21.png)



![](Java核心技术.assets/Snipaste_2022-08-03_21-07-35.png)



![](Java核心技术.assets/Snipaste_2022-08-04_20-05-41.png)



![](Java核心技术.assets/Snipaste_2022-08-04_20-45-26.png)



拓展数组大小的方法：Array是java.lang.reflect包的一个工具类

![](Java核心技术.assets/Snipaste_2022-08-04_20-55-33.png)



![](Java核心技术.assets/Snipaste_2022-08-04_21-09-19.png)



![](Java核心技术.assets/Snipaste_2022-08-05_17-10-15.png)



![](Java核心技术.assets/Snipaste_2022-08-05_17-14-05.png)

![](Java核心技术.assets/Snipaste_2022-08-05_17-15-33.png)



![](Java核心技术.assets/Snipaste_2022-08-05_17-17-28.png)

![](Java核心技术.assets/Snipaste_2022-08-05_17-20-29.png)



![](Java核心技术.assets/Snipaste_2022-08-06_22-23-54.png)

![](Java核心技术.assets/Snipaste_2022-08-06_22-25-19.png)

![](Java核心技术.assets/Snipaste_2022-08-06_22-29-00.png)

lambda表达式高级：

![](Java核心技术.assets/Snipaste_2022-08-06_22-33-50.png)

![](Java核心技术.assets/Snipaste_2022-08-06_22-34-00.png)

![](Java核心技术.assets/Snipaste_2022-08-06_22-34-25.png)

![](Java核心技术.assets/Snipaste_2022-08-06_22-35-58.png)

lambda表示中使用代码块外部的变量时，该变量必须是final的。



内部类是定义在一个类作用域中的类。使用内部类的原因有两点：

1. 内部类可以对同一个包中的其他类隐藏。
2. 内部类的方法可以访问这个类作用域中的成员，包括私有的

现在已经很少使用内部类了，大多数情况下内部类都可以被lambda表达式取代。内部类可以访问其外部类的成员，是因为编译器为其隐式添加了外部类对象的引用。可以使用OuterClass.this引用外部类的实例。如果只是想把内部类隐藏，而不想使用任何外部类的成员，可以把内部类声明为静态的。



![](Java核心技术.assets/Snipaste_2022-08-06_22-54-36.png)



![](Java核心技术.assets/Snipaste_2022-08-06_22-54-44.png)



## 代理


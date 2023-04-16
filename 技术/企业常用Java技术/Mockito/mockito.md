## 单元测试

### 测试替身 

Test Doubles：代替真正的 服务/外部依赖 起作用的替身

1. Dummy：被测类中包括一些依赖，这些依赖对于被测方法没有任何用，我们可以提供 Dummy，防止编译错误
2. Fake：比如数据库接口还没实现，使用一个假的实现类，内部是 HashMap，来进行测试
3. Stub：调用外部依赖的方法时，提供硬编码的响应
4. Spy：和 stub 类似，但会记录调用历史。比如很多 void 方法，没法用断言，但可以通过spy来记录调用次数。
5. Mock：和spy类似，但spy还可以调用真实方法，mock对象则完全是模拟的方法

### 常用框架

- Junit
- Mock
  - Mockito
  - powermock
- behavior testing
  - concordion
  - cocumber

## 基本使用

```java
package com.learnable.front.homework.controller;

import com.learnable.user.domain.api.dto.schoolclass.ClassDTO;
import com.learnable.user.domain.api.dto.teacher.TeacherDTO;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DemoTest {

    @Test
    public void verifyTest() {
        ClassDTO mockClassDTO = Mockito.mock(ClassDTO.class);
        mockClassDTO.setClassName("一班");
        mockClassDTO.setId(1);
        mockClassDTO.setId(2);

        //验证mock对象调用了一次setClassName
        Mockito.verify(mockClassDTO).setClassName("一班");
        //验证mock对象调用setId方法两次，参数是任意的int
        Mockito.verify(mockClassDTO, Mockito.times(2)).setId(Mockito.anyInt());
    }

    @Test
    public void stubTest() {
        ClassDTO mockClassDTO = Mockito.mock(ClassDTO.class);
        //打桩
        Mockito.when(mockClassDTO.getClassName()).thenReturn("class 1");
        Mockito.when(mockClassDTO.getId()).thenReturn(1).thenReturn(2); //多次调用返回值不同
        Mockito.when(mockClassDTO.getStatus()).thenThrow(new RuntimeException("status not found"));

        System.out.println(mockClassDTO.getClassName());
        System.out.println(mockClassDTO.getId());
        System.out.println(mockClassDTO.getId());
        System.out.println(mockClassDTO.getStatus());
    }

    @Test
    public void spyTest() {
        ClassDTO classDTO = new ClassDTO(1, 1001, "北京一中", "1班", "七年级1班", 2022, 200, 1);

        //spy方法根据存在的实例创建一个mock对象
        ClassDTO spy = Mockito.spy(classDTO);
        Mockito.when(spy.getId()).thenReturn(2);

        //修改mock对象的返回值，没有修改的则返回原来的值
        System.out.println(spy.getId());
        System.out.println(spy.getClassName());

    }

    //使用@Mock注解标记mock对象
    //在具体使用此对象的方法中初始化
    @Mock
    private TeacherDTO teacherDTO;

    @Test
    public void mockAnnotationsTest() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(teacherDTO.getName()).thenReturn("tom");
        System.out.println(teacherDTO.getName());
    }
}
```

## api 测试

使用 MockMvc 发送 Controller 请求：


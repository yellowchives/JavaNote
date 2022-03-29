# SpringBoot表单输入校验

[Spring Boot配合Hibernate Validator参数校验 | MrBird](https://mrbird.cc/Spring-Boot-Hibernate-Validator-Params-Check.html)

Spring支持 Bean Validation API（JSR-303）。这样的话，我们能够更容易地声明检验规则，而不必在应用程序代码中显式编写声明逻辑。

使用表单校验的步骤：

1. 添加依赖

   ```xml
   <!-- 这是JSR标准提供的接口， 现在改成了jarkata.validation。其实不用导这个包，下面的hibernate里有-->
   <dependency>
       <groupId>javax.validation</groupId>
       <artifactId>validation-api</artifactId>
   </dependency>
   <!-- hibernate提供的实现类-->
   <dependency>
       <groupId>org.hibernate.validator</groupId>
       <artifactId>hibernate-validator</artifactId>
   </dependency>
   
   <!-- springboot项目引入下面这个-->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```

2. 实体类上加注解声明校验规则。

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class User {
       @NotNull(message = "姓名不能为空")
       private String name;
       @NotNull(message = "年龄不能为空")
       private Integer age;
   }
   ```

3. 在controller上声明要进行的校验。

   ```java
   @GetMapping("/test")
   public String sayHello(@Valid User user, Errors errors) {
       if(errors.hasErrors()) {
           List<ObjectError> allErrors = errors.getAllErrors();
           return allErrors.toString();
       } else {
           return "success";
       }
   }
   ```

4. 结果：

   ```
   localhost:8080/test 发送无参数请求
   
   [Field error in object 'user' on field 'age': rejected value [null]; codes [NotNull.user.age,NotNull.age,NotNull.java.lang.Integer,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.age,age]; arguments []; default message [age]]; default message [年龄不能为空], Field error in object 'user' on field 'name': rejected value [null]; codes [NotNull.user.name,NotNull.name,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.name,name]; arguments []; default message [name]]; default message [姓名不能为空]]
   ```

@Valid声明式校验只能校验一些基本的非空错误，更复杂的校验规则需要使用@Validated

## 注意事项

如果不加 Errors errors来接受参数，就会抛出MethodArgumentNotValidException。可以统一捕捉校验失败的MethodArgumentNotValidException异常：

```java
@RestControllerAdvice
public class NotValidArgumentsHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String exceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        List<ObjectError> allErrors = e.getAllErrors(); //获取所有的错误对象
        for (ObjectError error : allErrors) {
            sb.append(error.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }
}
```

如果不是接受POJO参数，需要将校验规则加到参数前，将@Validated加到类上。

@RequestParam("name") 也会判断参数里有没有 name, 如果没有会抛出 MissingServletRequestParameterException。可以统一异常处理：

```java
@ControllerAdvice
public class NotValidArgumentsHandler {
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String exceptionHandler(MissingServletRequestParameterException e) {
        return e.getMessage();
    }
}
```

## 分组校验

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //标记接口，新增组
    public interface Add{}
    public interface Delete{}

    @Null(message = "姓名必须为空", groups = {Delete.class})
    @NotNull(message = "姓名不能为空", groups = {Add.class}) //新增方法，不能为空
    private String name;
    @NotNull(message = "年龄不能为空", groups = {Add.class})
    private Integer age;
}

@RestController
public class TestController {
    @GetMapping("/test")
    public String sayHello(@Validated(value = {User.Delete.class}) User user, Errors errors) {
        //指定了什么组，就只对此组的规则进行校验。默认都是default组。
        if(errors.hasErrors()) {
            List<ObjectError> allErrors = errors.getAllErrors();
            return allErrors.toString();
        } else {
            return "success";
        }
    }
}
```

结果：

```
localhost:8080/test?name=lalala

[Field error in object 'user' on field 'name': rejected value [lalala]; codes [Null.user.name,Null.name,Null.java.lang.String,Null]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.name,name]; arguments []; default message [name]]; default message [姓名必须为空]]
```

添加@Validated就是提示Spring在将JSON入参转换为POJO后，对此对象进行校验。如果校验不通过Spring则会抛出MethodArgumentNotValidException异常。

## 常用规则

Car类：

```java
public class Car {
    //标记接口
    public interface CarCheck{}
    
    @NotBlank // 不能为null,不能为空字符串。@NotEmpty也行，还可用于集合
    private String manufacturer;
    @NotNull // 不能为null
    @Length(min = 2, max = 14) // 字符串长度位于2到14之间 @Size也行
    private String licensePlate;
    @Min(2)
    @Max(5) // 注意,未添加NotNull注解,所以seatCount可以为null,只有当seatCount不为null@Min @Max才会做校验
    private Integer seatCount;
    @AssertTrue // registered不为null时,则值需是true
    private Boolean registered;
    // groups用于指定所属的校验组
    @AssertTrue(message = "The car has to pass the vehicle inspection first", groups = CarChecks.class)
    private Boolean passedVehicleInspection;
    @Valid // 表明应对driver对象内字段继续做校验
    @NotNull
    private Driver driver;
    @Valid // 表明应对passengers里的Person对象内字段继续做校验
    @Size(max = 2) // 表明passengers最多只能有两个对象
    private List<Person> passengers = new ArrayList<>();
    private String brand;
    @Range(min = 2,max = 4) // 作用同@Min @Max
    private Integer doors;
    private CarTypeEnum carTypeEnum;
    // get set......
}
```

Driver类：

```java
public class Driver extends Person {
    @NotNull
    @Min(value = 18, message = "必须年满18岁", groups = DriverChecks.class)
    public Integer age;
    @NotNull
    @AssertTrue(message = "必须具有驾照", groups = DriverChecks.class)
    public Boolean hasDrivingLicense;
    // get set......
}
```

Person类：

```java
public class Person {
    private long personId = 0;
    @NotBlank
    @CheckCase(value = CaseMode.UPPER, message = "名字必须为大写")
    private String name;
    public Address address;
    public Date birthday;
    // get set......
}
```

RentalCar类：

```java
@GroupSequence({RentalChecks.class, CarChecks.class, RentalCar.class})
public class RentalCar extends Car {
    @AssertFalse(message = "The car is currently rented out", groups = RentalChecks.class)
    private boolean rentalStation = true;
    // get set......
}
```

非web项目校验：

```java
public class ValidationTest {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Test
    public void test() {
        // 可以试着给不同字段赋值查看校验效果
        Car car = new Car();
        car.setManufacturer("benz");
        car.setLicensePlate("234234");
        car.setSeatCount(5);
        car.setRegistered(true);
        car.setPassedVehicleInspection(true);
        Driver driver = new Driver();
        driver.setName("JACK");
        driver.setAge(11);
        driver.setHasDrivingLicense(false);
        car.setDriver(driver);
        car.setPassengers(new ArrayList<>());
        car.setBrand("");
        car.setDoors(4);
        car.setCarTypeEnum(CarTypeEnum.BENZ);
        // 不传递校验顺序,则只校验groups为Default的,没有显式在注解写明groups属性的,则默认为Default
        validateBean(car);
        // 校验顺序显式传递
        validateBean(car, OrederedChecks.class);

        car.getDriver().setAge(20);
        car.getDriver().setHasDrivingLicense(true);
        validateBean(car, OrederedChecks.class);

        car.setSeatCount(1);
        Set<ConstraintViolation<Car>> constraintViolations3 = validator.validateProperty(car, "seatCount");
        System.err.println(constraintViolations3.iterator().next().getMessage());

        Set<ConstraintViolation<Car>> constraintViolations4 = validator.validateValue(Car.class, "registered", false);
        System.err.println(constraintViolations4.iterator().next().getMessage());

        // 校验顺序写在bean的类注解上
        RentalCar rentalCar = new RentalCar();
        Set<ConstraintViolation<RentalCar>> constraintViolations5 = validator.validate(rentalCar);
        System.err.println(constraintViolations5.iterator().next().getMessage());

    }

    @Test
    public void test1() {
        // 测试自定义校验注解
        Person person = new Person("John");
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);
        ConstraintViolation<Person> constraintViolation = constraintViolations.iterator().next();
        System.out.println(constraintViolation.getMessage());
    }

    public static <T> void validateBean(T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean, groups);
        if (constraintViolations.isEmpty()) {
            System.out.println("校验通过");
            return;
        }
        List<String> errors = new ArrayList<>(10);
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            errors.add(constraintViolation.getPropertyPath() + constraintViolation.getMessage());
        }
        //throw new ValidationException(StringUtils.join(errors, ","));
        System.err.println(StringUtils.join(errors, ","));
    }
}
```

其中Person类@CheckCase是自定义的校验，也就是说我们可以自定义自己的校验逻辑，这里@CheckCase(value = CaseMode.UPPER, message ="名字必须为大写")就是说字段值必须为大写，那么要如何做呢？分三步：

第一步：定义校验注解

```java
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckCaseValidator.class) //CheckCaseValidator类在下面
@Documented
public @interface CheckCase {
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	CaseMode value();
}

public enum CaseMode {
       UPPER,
       LOWER;
}
```

第二部：编写校验逻辑实现

```java
public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
    private CaseMode caseMode;
    @Override
    public void initialize(CheckCase constraintAnnotation) {
        this.caseMode = constraintAnnotation.value();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (caseMode == CaseMode.UPPER) {
            return value.equals(value.toUpperCase());
        } else {
            return value.equals(value.toLowerCase());
        }
    }
}
```

第三步：在bean添加注解即可生效

```java
public class Person {
    private long personId = 0;
    @CheckCase(value = CaseMode.UPPER, message = "名字必须为大写")
    private String name;
    public Address address;
    public Date birthday;
   // ......
}
```


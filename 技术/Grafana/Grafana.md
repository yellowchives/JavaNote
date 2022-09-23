## 选择时间范围

时间范围有仪表台级别和面板级别。

时间单位分为： `s (seconds)`, `m (minutes)`, `h (hours)`, `d (days)`, `w (weeks)`, `M (months)`, and `y (years)`。

使用减号回退到过去，`/<time unit>`显示完整的时间周期，看下面的例子：

| 示例相对范围                  | 从：       | 至：       |
| :---------------------------- | :--------- | :--------- |
| 最后 5 分钟                   | `now-5m`   | `now`      |
| 今天0点到现在（Today so far） | `now/d`    | `now`      |
| 今天0点到24点 （Today）       | `now/d`    | `now/d`    |
| 本星期（周一到周日）          | `now/w`    | `now/w`    |
| 一周至今（周一到今天）        | `now/w`    | `now`      |
| 前一个月                      | `now-1M/M` | `now-1M/M` |

> Time range controls:
>
> https://grafana.com/docs/grafana/v7.0/dashboards/time-range-controls/

在 panel 的 Query options 里有两个选择：

1. Relative time：指定和仪表台不同的时间范围
2. Time shift：相对仪表台的时间范围进行平移

- **Relative time:**

| Example          | Relative time field |
| :--------------- | :------------------ |
| Last 5 minutes   | `now-5m`            |
| The day so far   | `now/d`             |
| Last 5 days      | `now-5d/d`          |
| This week so far | `now/w`             |
| Last 2 years     | `now-2y/y`          |

- **Time shift:**

| Example              | Time shift field |
| :------------------- | :--------------- |
| Last entire week     | `1w/w`           |
| Two entire weeks ago | `2w/w`           |
| Last entire month    | `1M/M`           |
| This entire year     | `1d/y`           |
| Last entire year     | `1y/y`           |

> Query options:
>
> https://grafana.com/docs/grafana/latest/panels/query-options/
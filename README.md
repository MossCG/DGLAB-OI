# 郊狼OI联动
| 更适合ACM宝宝体质的郊狼~ | <br>
| 杂鱼蒟蒻ACMer是要被电的哦~ |

## 进度报告
- 搓了个框架
- 具体连OI爬排名功能还没写
- 连郊狼部分也还没写（Socket只能3.0主机，蓝牙技术栈没玩过，WebAPI没文档）
- 也就仅仅有个框架

## 开发进度
- |√| 基础代码框架
- |√| 蓝牙框架
- |√| Http/Https请求框架
- |√| 强度计算框架
- |X| 蓝牙连郊狼功能（用蓝牙主要考虑到Socket不支持2.0主机）
- |X| Socket连郊狼（不支持2.0主机）
- |X| WebAPI连郊狼（没有现成API文档）

## 强度计算规则
- 基础强度：value=basicValue;
- WA增量：value+=WATimes*WAValue;
- AC增量：value+=ACTimes*ACValue;
- 排名增量：value+=(rank-1)*rankValue;
- 强度上限：if (value>limit) v=limit;

PS：控制好强度上限啊喂！别把自己电坏了啊杂鱼！
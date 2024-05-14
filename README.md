# 郊狼OI联动
| 更适合ACM宝宝体质的郊狼~ | <br>
| 杂鱼蒟蒻ACMer是要被电的哦~ |

本项目使用 [DGLAB-BT](https://github.com/MossCG/DGLAB-BT) 开发<br>
（也是咱写的23333）<br>
Tips：请在正式使用前一定先调好参数！别把自己电坏了啊杂鱼！

## 支持设备
| 郊狼版本 | 是否支持 |
|------|------|
| 2.0  | 是    |
| 3.0  | 否    |

## 适配OJ
| ID   | 名称         | 作者     | 进度  |
|------|------------|--------|-----|
| CF   | Codeforces | MossCG | 已完成 |
| ZUCC | 浙大城院OJ     | MossCG | 已完成 |

## 进度报告
- 蓝牙部分几乎搓完了/支持2.0主机
- 已开始适配OJ......

## 开发进度
- |√| 基础代码框架
- |√| 蓝牙框架
- |√| 强度计算框架
- |√| 蓝牙连郊狼功能（仅支持2.0主机）
- |√| 爬OI功能（请各位自行适配学校OJ哦~）

## 强度计算规则
- 基础强度：value=basicValue;
- WA增量：value+=WATimes*WAValue;
- AC增量：value+=ACTimes*ACValue;
- 排名增量：value+=(rank-1)*rankValue;
- 强度上限：if (value>limit) v=limit;

PS：控制好强度上限啊喂！别把自己电坏了啊杂鱼！
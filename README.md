# CrazyFrog
《旅行青蛙》修改三叶草和奖券
---
修改原理是游戏的存档以类似C存储struct的方式存在了本地, 路径是`/storage/emulated/0/Android/data/jp.co.hit_point.tabikaeru/files/Tabikaeru.sav`, 同一路径下的`.bak`文件是备份文件, 如果把文件删除的话, 游戏会重新开始(不要问我怎么知道的).

其中四叶草是`4个Byte`的`int`来存储的, 下标从第`16H(22)`到`19H(25)`, 奖券的下标是`1AH(25)`到`1DH(29)`

存储方式都是大端存储.

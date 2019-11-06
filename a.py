import re
import numpy as np

lines = open('data/todo.txt', 'r').readlines()
out = open('data/newdata.txt', 'w')
pattern = re.compile(r'.*?\((\d+).*?\d+/\d+/(\d+)\,.*?\)')

# ap编号和x,y之间的映射
ap_coordinate = {}
coordinate_ap = {}
#映射之后的信息
new_lines = {}
x = 0
for line in lines:
    find_all_value = pattern.findall(line)[0:6]
    first_col_key = line.split()[0]
    # 坐标映射编号
    coordinate_ap[x] = first_col_key
    # 编号映射坐标
    ap_coordinate[first_col_key] = x
    x += 1

    # ap 路损信息
    new_lines[first_col_key] = find_all_value
out.write(str(new_lines))
out.close()

# 用户间路损值倒数的二维矩阵
value = np.ones((159,159))
for i in range(159):
    # 坐标
    x = i
    val = new_lines[coordinate_ap[x]]
    for j in range(len(val)):
        # 坐标
        y = ap_coordinate[val[j][0]]
        loss = float(val[j][1])
        if loss != 0:
            value[x,y] = 1./loss
        else:
            value[x,y] = loss

weight = np.sum(np.where(value != 1, value, 0), axis=1)
# ap weight最大的7个ap序号，是簇头
top_weight = weight.argsort()[::-1][0:7]

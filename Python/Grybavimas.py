import random
import time
import sys
import copy

v = 0


def Rekursija(n, m, mis):
    global v
    v += 5
    kel = []
    gryb = []
    maxIndex = -1
    maxG = sys.maxsize * -1
    if n > 0:
        v += 3
        for i in range(m):
            v += 1
            Rekursija2(n, m, 0, i, 0, [], kel, gryb, mis)
        for i in range(len(gryb)):
            v += 1
            if maxG < gryb[i]:
                v += 2
                maxG = gryb[i]
                maxIndex = i
        return kel[maxIndex]
    else:
        v += 1
        return []


def Rekursija2(n, m, x, y, sum, k, kel, gryb, mis):
    global v
    v += 1
    if x >= n or x < 0:
        v += 4
        temp = copy.deepcopy(k)
        kel.append(temp)
        temp = copy.deepcopy(sum)
        gryb.append(temp)
    else:
        v += 1
        if y == 0:
            v += 1
            if m == 1:
                v += 3
                k.append(y)
                Rekursija2(n, m, x+1, y, sum + mis[x][y], k, kel, gryb, mis)
                k.pop()
            else:
                v += 6
                k.append(y)
                Rekursija2(n, m, x+1, y, sum + mis[x][y], k, kel, gryb, mis)
                k.pop()
                k.append(y+1)
                Rekursija2(n, m, x+1, y+1, sum + mis[x][y+1], k, kel, gryb, mis)
                k.pop()
        else:
            v += 1
            if y == m - 1:
                v += 6
                k.append(y-1)
                Rekursija2(n, m, x+1, y-1, sum + mis[x][y-1], k, kel, gryb, mis)
                k.pop()
                k.append(y)
                Rekursija2(n, m, x+1, y, sum + mis[x][y], k, kel, gryb, mis)
                k.pop()
            else:
                v += 9
                k.append(y-1)
                Rekursija2(n, m, x+1, y-1, sum + mis[x][y-1], k, kel, gryb, mis)
                k.pop()
                k.append(y)
                Rekursija2(n, m, x+1, y, sum + mis[x][y], k, kel, gryb, mis)
                k.pop()
                k.append(y+1)
                Rekursija2(n, m, x+1, y+1, sum + mis[x][y+1], k, kel, gryb, mis)
                k.pop()


def Dinamine(n, m, mis, kel):
    global v
    v += 8
    maxG = sys.maxsize * -1
    ind = -1
    gerK = []
    for i in range(1, len(mis)):
        v += 1
        for j in range(len(mis[i])):
            v += 5
            maxL = -1
            index = -1
            if j == 0:
                v += 1
                if m == 1:
                    v += 1
                    if maxL < mis[i-1][j]:
                        v += 2
                        maxL = mis[i-1][j]
                        index = j
                else:
                    v += 2
                    if maxL < mis[i - 1][j]:
                        v += 2
                        maxL = mis[i - 1][j]
                        index = j
                    if maxL < mis[i - 1][j+1]:
                        v += 2
                        maxL = mis[i - 1][j+1]
                        index = j+1
            else:
                v += 1
                if j == m-1:
                    v += 2
                    if maxL < mis[i - 1][j]:
                        v += 2
                        maxL = mis[i - 1][j]
                        index = j
                    if maxL < mis[i - 1][j - 1]:
                        v += 2
                        maxL = mis[i - 1][j - 1]
                        index = j-1
                else:
                    v += 3
                    if maxL < mis[i - 1][j - 1]:
                        v += 2
                        maxL = mis[i - 1][j - 1]
                        index = j-1
                    if maxL < mis[i - 1][j]:
                        v += 2
                        maxL = mis[i - 1][j]
                        index = j
                    if maxL < mis[i - 1][j + 1]:
                        v += 2
                        maxL = mis[i - 1][j + 1]
                        index = j+1
            mis[i][j] += maxL
            kel[i][j] = index
    for i in range(m):
        v += 1
        if maxG < mis[n-1][i]:
            v += 2
            maxG = mis[n-1][i]
            ind = i
    for i in range(n-1, -1, -1):
        v += 2
        if ind >= 0:
            gerK.append(ind)
            v += 1
        ind = kel[i][ind]
    gerK.reverse()
    return gerK


def Valdymas(n, m):
    mis = []
    for i in range(n):
        mis.append([-1]*m)
    kel = copy.deepcopy(mis)
    for i in range(n):
        for j in range(m):
            mis[i][j] = random.randint(1, 5)
    start = time.time()
    gerK = Dinamine(n, m, mis, kel)
    # gerK = Rekursija(n, m, mis)
    finish = time.time()

    print("{}x{} {} {} {}".format(n, m, gerK, (finish - start), v))


N = [3, 4, 5, 6, 5, 5, 5, 5]
M = [3, 3, 3, 3, 4, 5, 6, 7]

for i in range(len(N)):
    Valdymas(N[i], M[i])

import random
import copy
import time

v = 0

def Rekursija(S, k):
    global v
    v += 7
    averages = []
    s = len(S)
    skKN = Rekursija2(s, k-1, [], [])
    pus = []
    for i in skKN:
        v+= 1
        pus.append(Paskirstymas(i, S))
    for i in pus:
        v += 4
        average = max(i)
        average += min(i)
        average = average // 2
        averages.append(average)
    return pus[averages.index(min(averages))], skKN[averages.index(min(averages))]


def Paskirstymas(ind, S):
    global v
    v += 3
    skyriai = []
    for i in ind:
        v += 3
        pus = 0
        for j in range(i):
            v += 1
            pus += S[j]
        skyriai.append(pus)
    return skyriai


def Skyriai(s):
    S = [0] * s
    for i in range(s):
        S[i] = random.randint(1, 9) * 10
    return S


def Rekursija2(x, y, skKN, t):
    global v
    v += 1
    if (y == 0):
        v += 5
        t.append(x)
        temp = copy.deepcopy(t)
        skKN.append(temp)
        t.pop()
        return skKN
    else:
        v += 2
        for i in range(1, x-y+1):
            v += 3
            t.append(i)
            skKN = Rekursija2(x-i, y-1, skKN, t)
            t.pop()
        return skKN


sk = [6, 8, 10, 12, 10, 10, 10, 10]
kn = [3, 3, 3, 3, 2, 4, 6, 8]

for i in range(len(sk)):
    arr = Skyriai(sk[i])
    start = time.time()
    x, y = Rekursija(arr, kn[i])
    finish = time.time()

    print("{}sk-{}k {} {} {} {}".format(sk[i], kn[i], x, y, (finish - start), v))

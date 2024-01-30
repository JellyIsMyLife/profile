# Padaryt geriausią kelionės radimą.
import random
import time

v = 0

def algorithm(r, c):
    global v
    v = 7
    cities = randC(c)
    bridges, weights = randBW(c)
    bridges, weights = fixBridges(bridges, weights)
    bridges, weights = sort(bridges, weights)

    bTrip, bScore = shortRoad(cities, bridges, weights, r)
    answ(bTrip, bScore)
    return


def randC(c):
    global v
    v += 3
    cities = [0] * c
    for i in range(c):
        v += 2
        cities[i] = random.randint(-9, 9)
    return cities


def randBW(c):
    global v
    v += 3
    bridges = [0] * c
    weights = [0] * c
    for i in range(c):
        v += 2
        bridges[i], weights[i] = randBW1(i + 1, c)
    return bridges, weights


def randBW1(index, c):
    global v
    v += 7
    temp = random.randint(1, c // 2)
    B = [0] * temp
    W = [0] * temp
    indexes = [0] * (temp + 1)
    indexes[0] = index
    for i in range(temp):
        v += 6
        temp = notSN(indexes, 1, c)
        indexes[i + 1] += temp
        B[i] = temp
        temp = notSN([0], 1, 9)
        W[i] = temp
    return B, W


def notSN(indexes, a, b):
    global v
    v += 3
    number = random.randint(a, b)
    for i in indexes:
        v += 2
        if number == i:
            v += 1
            number = notSN(indexes, a, b)
    return number


def fixBridges(bridges, weights):
    global v
    v += 3
    x = 0
    for i in bridges:
        v += 4
        z = 0
        for j in i:
            v += 3
            if x + 1 in bridges[j - 1]:
                v += 2
                temp = bridges[j - 1].index(x + 1)
                weights[j - 1][temp] = weights[x][z]
            if x + 1 not in bridges[j - 1]:
                v += 2
                bridges[j - 1].append(x + 1)
                weights[j - 1].append(weights[x][z])
            z += 1
        x += 1
    return bridges, weights


def sort(bridges, weights):
    global v
    v += 2
    for i in range(len(bridges)):
        v += 2
        for j in range(len(bridges[i])):
            v += 3
            k = j
            while k < len(bridges[i]):
                v += 3
                if bridges[i][k] < bridges[i][j]:
                    v += 6
                    t = bridges[i][k]
                    bridges[i][k] = bridges[i][j]
                    bridges[i][j] = t
                    t = weights[i][k]
                    weights[i][k] = weights[i][j]
                    weights[i][j] = t
                k += 1
    return bridges, weights


def shortRoad(C, B, W, res):
    global v
    v += 4
    trip = []
    pScore = []
    if len(C) > 0:
        v += 7
        TC = C
        p = [1]
        pScore = [TC[0]]
        TC[0] = 0
        n = 1
        trip.append([1])
        trip, pScore = path(TC, B, W, 1, res, p, pScore, n, trip, pScore[0])
    return bestPath(trip, pScore)


def path(C, B, W, dest, res, p, pScore, current, trip, score):
    global v
    v += 4
    if res == 0:  # check the resources
        v += 1
        return trip, pScore
    index = 0
    for i in B[current - 1]:
        v += 4
        if W[current - 1][index] > res:  # checks if it's possible to go to the next city
            v += 1
            continue
        else:  # if it can go to the next city:
            v += 9
            res -= W[current - 1][index]
            p.append(i)  # path is written down
            temp = p.copy()
            tscore = score + C[i - 1]  # adds to current score
            tC = C.copy()
            tC[i - 1] = 0  # changes cities score to 0
            if i == dest:  # if it's the starting city it will add info to trip and pScore
                v += 2
                trip.append(temp)
                pScore.append(tscore)
            trip, pScore = path(tC, B, W, dest, res, p, pScore, i, trip, tscore)  # goes into the next city
            p.pop()
        index += 1
    return trip, pScore  # returns the paths that return to starting city with their score.


def bestPath(trips, scores):
    global v
    v += 6
    if len(trips) == 0:
        v += 1
        return [], []
    bTrip = trips[0]
    bScore = scores[0]
    index = 0
    for i in scores:
        v += 4
        if i > bScore:
            v += 2
            bTrip = trips[index]
            bScore = i
        if i == bScore and len(bTrip) > len(trips[index]):
            v += 2
            bTrip = trips[index]
            bScore = i
        index += 1
    return bTrip, bScore


def answ(k, g):
    global v
    if len(k) == 0:
        print("Nesugenaruoti miestai.")
    else:
        st = "Kelias: "
        st += ", ".join([str(i) for i in k])
        print(st)
    print("Kelionės gerumas:", g)
    print("Veiksmų skaičius:", v)


r = 30
c = 5

for _ in range(5):
    print("")
    start = time.time()
    algorithm(r, c)
    finish = time.time()

    print("{} - sek.".format((finish - start)))

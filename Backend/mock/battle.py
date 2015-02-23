import requests
import time
from random import randint
n = 3              #im wyzsza liczba tym dluzsza rozgrywka
it = 15			   #ilosc iteracji
s = requests.Session()

hp = 1000
ammo = 2000

#battle
for i in range(0,it,1):
    print "Iteracja numer: %s" %i
    rand_max = randint(4,9)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz1', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 1, czekam"
    time.sleep(rand*n)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz2', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 2, czekam"
    time.sleep(rand*n)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz3', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 3, czekam"
    time.sleep(rand*n)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz4', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 4, czekam"
    time.sleep(rand*n)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz5', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 5, czekam"
    time.sleep(rand*n)
    rand = randint(1,rand_max)
    hp -= rand
    rand = randint(1,rand_max)
    ammo -= rand
    s.put('http://158.75.2.62:8080',params={'player_name':'gracz6', 'hp':hp, 'ammo':ammo, 'loc':'123.125'})
    print "po modyfikacji cech gracza 6, czekam"







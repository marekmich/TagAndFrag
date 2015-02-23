import requests
s = requests.Session()

#setting hp and ammo
s.put('http://158.75.2.62:8080',params={'player_name':'gracz1', 'hp':'1000', 'ammo':'2000'})
s.put('http://158.75.2.62:8080',params={'player_name':'gracz2', 'hp':'1000', 'ammo':'2000'})
s.put('http://158.75.2.62:8080',params={'player_name':'gracz3', 'hp':'1000', 'ammo':'2000'})
s.put('http://158.75.2.62:8080',params={'player_name':'gracz4', 'hp':'1000', 'ammo':'2000'})
s.put('http://158.75.2.62:8080',params={'player_name':'gracz5', 'hp':'1000', 'ammo':'2000'})
s.put('http://158.75.2.62:8080',params={'player_name':'gracz6', 'hp':'1000', 'ammo':'2000'})
print "Ustawiono parametry poczatkowe dla 6 graczy"
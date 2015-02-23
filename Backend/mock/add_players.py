import requests
s = requests.Session()

#adding players
s.post('http://158.75.2.62:8080',params={'player_name':'gracz1'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz2'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz3'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz4'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz5'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz6'})
print "Dodano 6 graczy"

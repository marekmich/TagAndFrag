import requests
s = requests.Session()

#adding players
s.post('http://158.75.2.62:8080',params={'player_name':'gracz1','id':'0'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz2','id':'0'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz3','id':'0'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz4','id':'0'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz5','id':'0'})
s.post('http://158.75.2.62:8080',params={'player_name':'gracz6','id':'0'})
print "Dodano 6 graczy"

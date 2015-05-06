"""Serwer REST dla projektu na przedmiot Programowanie Zespolowe
Nazwa projektu: Tag & Frag
Autor: Wojciech Wozniak, zespol numer 14
"""
import cherrypy
import json
import random
from pymongo import MongoClient
from datetime import datetime
from bson import Binary, Code
from bson.json_util import dumps
from jinja2 import Environment, FileSystemLoader
env = Environment(loader=FileSystemLoader('templates'))

#lookup = TemplateLookup(directories=['./templates/'])



def getRandomId():
	"""Funkcja zwraca losowe id (haslo) dla gracza
	"""
	id = random.randint(1000000,9999999)
	return id

def getWeaponCode(team):
	"""Funkcja zwraca kod broni
	Jako parametr funkcja przyjmuje numer druzyny wg schematu:
	numer druzyny parzysty - kod broni parzysty
	numer druzyny nieparzysty - kod broni nieparzysty
	kody broni parzyste i nieparzyste sa potrzebne do zapalania odpowiednich diod na opaskach
	"""
	cwc = random.randint(1,255)
	if int(team) % 2 == 0:
		while cwc % 2 == 1:
			cwc = random.randint(1,255)
	else:
		while cwc % 2 == 0:
			cwc = random.randint(1,255)
	return cwc


class TagAndFragWebService(object):
	"""Glowna klasa serwera, obsluguje requesty HTTP
	GET, POST, PUT, DELETE
	"""
	client = MongoClient()
	exposed = True
	db = client.test_database
	collection = db.test_collection
	players = db.players
	player = {"id","name","team","health","ammunition","localization","cwc"}
	fraglog = db.fraglog
	cherrypy.config.update({'server.socket_host' : '158.75.2.62',
				'server.socket_port' : 8080,
				'log.error_file' : 'error_log',     
				'log.access_file' : 'access_log',
				'log.screen' : True})
	#log = open('fraglog','a')
	#rozgrywka = open('rozgrywka','r+')
	
	@cherrypy.tools.accept(media='text/plain')

	def GET(self, parameter):
		"""Metoda GET
		request GET jest uzywany do pobierania danych z serwera
		Metoda GET przyjmuje jeden parametr o nazwie parameter, ktory
		moze przyjmowac nastepujace wartosci:
		- LIST - zwraca liste druzyn, do ktorych moze dolaczyc sie gracz
		- ALL_PLAYERS - zwraca liste wszystkich graczy, ktorzy sa w bazie danych serwera
		- FRAGLOG - zwraca FRAGLOG calej rozgrywki, czyli zapis trafien
		- dowolny inny - zwraca informacje o danym graczu, o ile taki gracz istnieje
		"""  
		if parameter == 'LIST':
			cursor = self.db.players.find({}, { '_id' : 0, 'id' : 0, 'name' : 0, 'health' : 0, 'ammunition' : 0, 'localization' : 0, 'cwc' : 0})		
			cursor = list(cursor)
			count = 0
			team_dict = dict()
			team_list = list()
			team_set = set()
			#return json.dumps(cursor)
			for element in cursor:
				team = element['team']
				team_list.append(team)  
				team_set.add(team)

			#print team_set
			#print "\n"
			#print team_list
			ile_druzyn = 0
			for element in team_set:
				team_dict[element] = team_list.count(element)
				ile_druzyn = ile_druzyn + 1
			#print("ile druzyn\n")
			#print ile_druzyn
			#print "plik: " + TagAndFragWebService.rozgrywka.read()
			teams_in_game = [line.strip() for line in open('rozgrywka')]
			#print "team dict:\n"
			#print team_dict
			#print "\nteams_in_game\n"
			#print teams_in_game
			usunieto = 0
			for element in teams_in_game:
				if team_dict.pop(int(element), None):
					usunieto = usunieto + 1
			#print "usunieto\n"
			#print usunieto
			#print "team dict po usunieciu:\n"
			#print team_dict
			#print "teams in game\n"
			#print teams_in_game
			for i in range(1,100,2):
				#print str(i) + "\n"
				#print i
				if team_dict.has_key(i):
					#print "ma klucz" + str(i) + "\n"
					if not team_dict.has_key(i+1):
						#print "nie ma klucza" + str(i+1) + "\n"
						team_dict[i+1] = 0
			for i in range(0,100,2):
				if i is not 0:
					if team_dict.has_key(i):
						if not team_dict.has_key(i-1):
							team_dict[i-1] = 0
			max = 0
			for i in range(0,100,1):
				if team_dict.has_key(i):
					max = i
			if max % 2 == 0:
				team_dict[max+1] = 0
				team_dict[max+2] = 0
			if max % 2 == 1:
				team_dict[max+2] = 0
				team_dict[max+3] = 0
			
					#team_dict[i+1] = 0
			team_dict.pop(0, None)
			return json.dumps(team_dict)
			###################################################
			#if int(usunieto) is int(ile_druzyn):
			#	licznik = 0
			#	teams_to_send = list()
			#	for i in range(1,100,1):
			#		if str(i) not in teams_in_game:
			#			teams_to_send.append(i)
			#			licznik = licznik + 1
			#		if licznik is 2:
			#			break
			#	team_dict_to_send = dict()
			#	for element in teams_to_send:
			#		team_dict_to_send[int(element)] = 0
			#	print "team dict to send\n"
			#	print team_dict_to_send
			#	return json.dumps(team_dict_to_send)
		#	return json.dumps(team_dict)
			############################################## 
				#print("teams to send:\n")
				#print teams_to_send
				#licznik = 1
				#ile_dodano = 0
				#teams_to_send = list() 
				#for element in team_set:
				#	if licznik < int(element):
				#		teams_to_send.append(licznik)
				#		ile_dodano = ile_dodano + 1
				#	licznik = licznik + 1
				#if ile_dodano is 0:
				#	teams_to_send.append(licznik)
				#	teams_to_send.append(licznik+1)
				#	ile_dodano = 2
				#if ile_dodano is 1:
				#	teams_to_send.append(licznik)
				#print "teams to file\n"
				#print teams_to_send


					
			#print "team " + str(team) + " ma " + str(count) + " graczy"
			#teams = self.db.players.distinct("team")
			#return json.dumps(cursor)
			#teams = list(teams)
			#teams = teams.sort()
			#for line in cursor:
			#	teams2 = self.db.players.find({"team" : line})
			#	print line, teams2.count()	
			#print str(teams.count(0)) + "graczy w druzynie 0"
			#print str(teams.count(1)) + "graczy w druzynie 1"
			#return json.dumps(team)
			#for element in teams
				
			#return json.dumps(teams)	
			#for element in rsor
			#	team = element['team']
			#	teams['team'] = 		
		if parameter == 'ALL_PLAYERS':
				players_db = self.db.players.find({} , { '_id': 0})
				players_db = list(players_db) #to wyciagnie z bazy
				return json.dumps(players_db)
		if parameter == 'FRAGLOG':
			fraglog_db = self.db.fraglog.find({} , { '_id': 0})
			fraglog_db = list(fraglog_db)
			return json.dumps(fraglog_db)
		cursor = self.db.players.find({"name" : parameter})
		count = str(cursor.count())
		if count is not "0":
			player = self.db.players.find({'name' : parameter} , {'_id': 0, 'cwc': 0})
			player = list(player)
			return json.dumps(player)

		else:
			return "nie ma takiego gracza"
		#id = 1762279
		#name = "wojtek"
		#return str(self.db.players.find_one({"id" : id, "name" : name }))
		#return str(self.db.players.find_one({"id" : 1762278, "name" : "wojtek"}))

	def POST(self, id, player_name):
		"""Metoda POST
		request POST sluzy do przesylania danych na serwer
		przyjmuje 2 parametry:
		- player_name - nick gracza, ktorego chcemy dodac
		- id - id gracza, ktorego chcemy dodac
		Metoda zwraca id = 0, gdy id gracza nie zgadza sie z jego nickiem
		Metoda zwraca id gracza, jesli gracz jest juz w bazie danych
		Metoda zwraca id gracza, jesli gracz nie byl w bazie danych (zostanie dla niego wygenerowane id)
		"""
		#name = player_name
		#id = getRandomId()
		id = int(id)
		cursor_id_name = self.db.players.find({"id" : id ,"name" : player_name})
		count_id_name = str(cursor_id_name.count())
		cursor_name = self.db.players.find({"name" : player_name})
		count_name = str(cursor_name.count())
		#print "count_id_name "+count_id_name
		#print "count_name \n"+count_name 
		if count_id_name is "1":
			#print "gracz juz znaleziony w bazie"
			#new_id = getRandomId()
				#player = { "id" : new_id,
					#    	"name" : player_name,
			#    	"team" : 0,
					#    	"health" : 100,
					#    	"ammunition" : 100,
					#    	"localization" : 123}
				#self.players.insert(player)
				return str(id)
		#cursor = self.db.players.find({'id' : id, 'name' : player_name})
		#count = str(cursor.count())
		if count_id_name is "0" and count_name is "1":
		#if count_name is "1":
			#print "ID niezgodne z name"
			id = 0
			return str(id)
		#cursor = self.db.players.find({"name" : "player_name"})
		if count_id_name is "0":
			#print "utworzenie nowego gracza"
			new_id = getRandomId()
			cursor = self.db.players.find({"id" : id})
			count = cursor.count()
			while count is 1:
				new_id = getRandomId()
				cursor = self.db.players.find({"id" : id})
				count = cursor.count()
			player = { "id" : new_id,
				  "name" : player_name,
				  "team" : 0,
				  "cwc" : 0,
				  "health" : 100,
				  "ammunition" : 100,
				  "localization" : 123}
			self.players.insert(player)
			return str(new_id)

	def PUT(self, id, player_name, end='default', ready='default', team='default', attacker_name='default', hp='default', ammo='default', loc='default'):
		"""Metoda PUT
		request PUT sluzy do uaktualniania danych na serwerze
		Metoda PUT przyjmuje kilka parametrow:
		- id - id gracza
		- player_name - nick gracza
		- end - jesli ten parametr ma inna wartosc niz 'default' to oznacza to, ze gra sie zakonczyla i mozna przejsc do ekranu koncowego
		- ready - jesli ten parametr ma inna wartosc niz 'default' to oznacza to, ze druzyna zawarta w parametrze 'team' jest gotowa do gry
		- team - jesli ten parametr ma inna wartosc niz 'default' to jest to numer druzyny
		- attacker_name - jesli ten parametr ma inna wartosc niz 'default' to jest to nick gracza, ktory zaatakowal gracza podanego parametrem 'player_name'
		- hp - jesli ten parametr ma inna wartosc niz 'default' to jest to ilosc punktow wytrzymalosci, za ktora gracz otrzymal trafienie
		- ammo - jesli ten parametr ma inna wartosc niz 'default' to sluzy on do zmiany ilosci amunicji gracza podanego parametrem 'player_name'
		- loc - jesli ten parametr ma inna wartosc niz 'default' to jest to lokalizacja gracza 'player_name'
		"""
		#log = open('fraglog','w')
		id = int(id)
		cursor =  self.db.players.find({"id" : id, "name" : player_name })
		count = str(cursor.count())
		#print "gggggg" + count 
		if count is not "0":
			if attacker_name is not "default":
				attacker_hp = self.db.players.find({"name" : attacker_name})
				for field in attacker_hp:
						att_hp = field['health']
						att_team = field['team']
				player_hp = self.db.players.find({"name" : player_name})
				for field in player_hp:
						pla_hp = field['health']
						pla_team = field['team']
			#pla_hp = int(pla_hp)-1
			#TagAndFragWebService.log.write(str(datetime.now()) + " " + attacker_name + "(" + str(att_hp) + ")" +  " trafil " + player_name + "(" + str(pla_hp) + ")" + "\n")
				#time_to_fraglog = str(datetime.now())
				time_to_fraglog = str(datetime.now().strftime("%H:%M:%S"))
				attacker_hp_to_fraglog = str(att_hp)
				player_hp_to_fraglog = str(pla_hp)
				fraglog_line = { "time" : time_to_fraglog,
						"attacker_name" : attacker_name,
						"attacker_team" : att_team,
						"attacker_hp" : attacker_hp_to_fraglog,
						"player_name" : player_name,
						"player_team" : pla_team,
						"player_hp" : player_hp_to_fraglog}
				self.fraglog.insert(fraglog_line)

			if hp is not 'default':
				self.db.players.update({'name':player_name}, {"$set":{'health': hp}}, upsert=False)
			if ammo is not 'default':
				self.db.players.update({'name':player_name}, {"$set":{'ammunition': ammo}}, upsert=False)
			if loc is not 'default':
				self.db.players.update({'name':player_name}, {"$set":{'localization': loc}}, upsert=False)
			if team is not 'default' and end is 'default':
				self.db.players.update({'name':player_name}, {"$set":{'team' : int(team)}}, upsert=False)
				cwc = getWeaponCode(team)
				cursor = self.db.players.find({"cwc" : cwc})
				count = cursor.count()
				#print "cursor = " + str(count) + "\n"
				while count is 1:
					cwc = getWeaponCode(team)
					cursor = self.db.players.find({"cwc" : cwc})
					count = cursor.count()
				self.db.players.update({'name':player_name}, {"$set":{'cwc' : cwc}}, upsert=False)
				return str(cwc)
			if ready is not 'default':
				#print "modyfikuje plik"
				ready = int(ready)
				file_list = [line.strip() for line in open('rozgrywka')]
				teams_in_game = [int(i) for i in file_list]
				if ready not in teams_in_game:
					with open('rozgrywka','a') as f:
						line = str(ready) + "\n"
						f.write(line)
			if end is not 'default':
				self.db.players.update({'id':id}, {"$set":{'cwc' : 0}}, upsert=False)
				self.db.players.update({'id':id}, {"$set":{'team': 0}}, upsert=False)
				self.db.players.update({'id':id}, {"$set":{'health': 100}}, upsert=False)
				file_list = [line.strip() for line in open('rozgrywka')]
				with open('rozgrywka','w') as f:
					for line in file_list:
						if line != str(team):
							line = line + "\n"
							f.write(line)
				return "po endzie"
		else:
			return "id gracza niezgodne z name"
		
	def DELETE(self):
		"""Metoda DELETE
		request DELETE sluzy do usuwania danych z serwera
		ta metoda usuwa wszystkich graczy z bazy danych na serwerze
		"""
		self.db.players.remove({})
		self.fraglog.remove({})

class TagAndFragWebPage(object):
	"""Klasa reprezentujaca strone fragloga serwera
	"""
	client = MongoClient()
	db = client.test_database
	fraglog = db.fraglog
	
	@cherrypy.expose
	def index(self, team1, team2):
		"""Metoda index
		Metoda index wyswietla fragloga, czyli zapis rozgrywki dla dwoch druzyn podanych w parametrach
		zostana wyswietlone wszystkie zdarzenia dotyczace druzyny podanej parametrem team1 oraz
		druzyny podanej parametrem team2
		"""
		# return "HELLO"
		tmpl = env.get_template("index.html")
		cursor = self.db.fraglog.find({'player_team': int(team1)}, {'_id': 0})
		cursor2 = self.db.fraglog.find({'attacker_team': int(team1)}, {'_id': 0})
		cursor3 = self.db.fraglog.find({'player_team': team2}, {'_id': 0})
		cursor4 = self.db.fraglog.find({'attacker_team': team2}, {'_id': 0})
		cursor = list(cursor)
		cursor2 = list(cursor2)
		cursor3 = list(cursor3)
		cursor4 = list(cursor4)
		cursor = cursor + cursor2 + cursor3 + cursor4
		#print "fraglog\n"
		#print cursor
		return tmpl.render(fraglog = cursor)
	


if __name__ == '__main__':
	conf = {
		'/': {
			'request.dispatch': cherrypy.dispatch.MethodDispatcher(),
			'tools.sessions.on': True,
			'tools.response_headers.on': True,
			'tools.response_headers.headers': [('Content-Type', 'text/plain')],
		},
	}

	webpage_conf = {       
		# '/': {
		# 	'request.dispatch': cherrypy.dispatch.MethodDispatcher(),
		# 	'tools.sessions.on': True,
		# 	'tools.response_headers.on': True,
		# 	'tools.response_headers.headers': [('Content-Type', 'text/plain')],
		# },
		'/static': {
			'tools.staticdir.on' : True,
			'tools.staticdir.dir' : "/root/source/test/test/web"
		}
	}

	cherrypy.tree.mount(TagAndFragWebPage(), '/web', webpage_conf)	
	cherrypy.quickstart(TagAndFragWebService(), '/', conf)

	
